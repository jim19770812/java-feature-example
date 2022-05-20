package org.jim.utils;

import lombok.NonNull;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.Assert;

import java.util.*;

/**
 * ST构建器
 * 1.用于调试输出，格式化字符欻等场景
 * 2.支持多种基本类型，集合，数组，支持生成
 * 3.支持输出数据类型
 * 4.标题，类名，间隔符，kv前缀和后缀
 * 5.内置格式化字符串，不用再额外调用字符串格式化了
 * 6.内置System.out.print方法，方便向控制台输出
 * 例子
 * ToString.of().banner("这是一段测试， 当前日期是：{}", new Date()).target("TEST").name("姓名").type(String.class).val("张飞").name("性别").val("男").name("年龄").val(20).println();
 * ToString.of(log).banner("这是一段测试，当前日期是：{}", new Date()).target("TEST").name("k1").val(100).debugLog();
 * ToString.of().blockPrefix("[").blockSuffix("]").val(1).val(2).val(3).val(4).val(5).val(6).println();
 *
 */
@Accessors(chain = true, fluent = true)
public class ST {
    private StringBuilder sb=new StringBuilder();
    private Optional<String> banner=Optional.empty();
    private Optional<String> target=Optional.empty();
    /**
     * 键的名字
     */
    private Optional<String> name =Optional.empty();
    /**
     * 数据类型，默认是没有的，如果提供了，会输出 name:type=value的格式
     */
    private Optional<String> type =Optional.empty();

    /**
     * 键和值之间的等于字符，默认是=
     */
    private Optional<String> eq=Optional.of("=");
    /**
     * 每个名字-值对的前置字符串，一般用来在换行后增加制表符，这样输出会好看一些，一般配合 itemDelimiter(",\n") 使用
     */
    private Optional<String> itemPrefix=Optional.empty();
    /**
     * 每个名字-值对的分隔符
     */
    private Optional<String> itemDelimiter =Optional.of(", ");
    /**
     * 块的前缀字符串
     */
    private Optional<String> blockPrefix=Optional.empty();
    /**
     * 块的后缀字符字符串
     */
    private Optional<String> blockSuffix=Optional.empty();
    /**
     * 值部分超过设置的省略长度就显示成... ，0表示不启用
     */
    private Optional<Integer> ellipsis=Optional.empty();
    /**
     * 日志对象
     */
    private Optional<Logger> logger=Optional.empty();

    @NonNull
    public ST logger(@NonNull Logger log){
        this.logger= Optional.of(log);
        return this;
    }

    @NonNull
    public ST logger(@NonNull Class<?> clazz){
        this.logger= Optional.of(LoggerFactory.getLogger(clazz));
        return this;
    }

    @NonNull
    public ST logger(@NonNull String className){
        this.logger= Optional.of(LoggerFactory.getLogger(className));
        return this;
    }

    @NonNull
    public static ST of(@NonNull String banner, @NonNull String target){
        return new ST(target).banner(banner);
    }

    @NonNull
    public static ST of(@NonNull String target){
        return of("", target);
    }

    @NonNull
    public static ST of(@NonNull Class targetClass){
        return of(targetClass.getSimpleName());
    }

    @NonNull
    public static ST of(){
        return new ST();
    }

    @NonNull
    public static ST of(@NonNull Logger log){
        return of().logger(log);
    }

    @NonNull
    public static ST of(@NonNull Logger log, @NonNull String binner, @NonNull String target){
        return of(binner, target).logger(log);
    }

    @NonNull
    public static ST of(@NonNull Logger log, @NonNull String target){
        return of(target).logger(log);
    }

    @NonNull
    public static ST of(@NonNull Logger log, @NonNull Class<?> clazz){
        return of(clazz).logger(log);
    }

    @NonNull
    protected ST(){
    }

    @NonNull
    protected ST(@NonNull String target){
        this.target=Optional.of(target);
    }

    @NonNull
    public ST name(@NonNull String name){
        this.name =Optional.of(name);
        return this;
    }

    @NonNull
    public ST name(@NonNull String format, @NonNull Object... objects){
        this.name =Optional.of(MessageFormatter.arrayFormat(format, objects).getMessage());
        return this;
    }

    @NonNull
    public ST type(@NonNull String name){
        this.type =Optional.of(name);
        return this;
    }

    @NonNull
    public ST type(@NonNull Class<?> clazz){
        this.type =Optional.of(clazz.getSimpleName());
        return this;
    }

    @NonNull
    protected String toStr(CharSequence value, @NonNull Boolean ellipsisEnable){
        if (value == null || value.length()==0) return "";
        final String[] result=new String[]{String.valueOf(value)};
        if (!ellipsisEnable){
            return result[0];
        }

        this.ellipsis.ifPresent(len->{
            if (len>0 && result[0].length() > len) {
                result[0]=result[0].substring(0, len-3)+"...";
            }
        });
        return result[0];
    }

    @NonNull
    protected String toStr(CharSequence value){
        return this.toStr(value, true);
    }

    @NonNull
    protected String toStr(Object value){
        if (Objects.isNull(value)) return "";
        return toStr(value.toString());
    }

    @NonNull
    protected String toStr(Object value, @NonNull String elseValue){
        if (Objects.isNull(value)) return elseValue;
        return toStr(value);
    }

    @NonNull
    public ST val(CharSequence v){
        this.addHeader();
        this.itemContent(v, true);
        this.addFooter();
        return this;
    }

    @NonNull
    public ST val(@NonNull String format, Object... objects){
        this.valfmt(format, objects);
        return this;
    }

    @NonNull
    public ST valfmt(@NonNull String format, Object... objects){
        this.val(MessageFormatter.arrayFormat(format, objects).getMessage());
        return this;
    }

    @NonNull
    protected ST addHeader(){
        this.itemPrefix.ifPresent(this.sb::append);
        this.name.ifPresent(name->{
            this.sb.append(name);
            this.type.ifPresent(type->this.sb.append(":").append(type));
            this.type =Optional.empty();
            this.eq.ifPresent(this.sb::append);
        });
        this.name =Optional.empty();
        return this;
    }

    @NonNull
    protected ST addFooter(){
        this.addDelimiter();
        return this;
    }

    @NonNull
    protected ST itemContent(@NonNull CharSequence v, @NonNull Boolean ellipsisEnabled){
        this.sb.append(toStr(v, ellipsisEnabled));
        return this;
    }

    @NonNull
    protected ST addDelimiter(){
        this.itemDelimiter.ifPresent(this.sb::append);
        return this;
    }

    @NonNull
    public ST itemPrefix(@NonNull String itemPrefix){
        this.itemPrefix=Optional.of(itemPrefix);
        return this;
    }

    @NonNull
    protected ST addEllipsis(){
        this.sb.append("...");
        return this;
    }

    @NonNull
    public ST array(Object[] v, @NonNull int maxLength){
        if (Objects.isNull(v)){
            return this.val("[]");
        }
        int i=0;
        ST nameValue= ST.of().delimiter(", ").blockPrefix("[").blockSuffix("]");
        this.ellipsis.ifPresent(nameValue::ellipsis);
        for(Object o : v){
            nameValue.val(o);
            if (maxLength>0 && i>=maxLength-1){
                nameValue.addDelimiter().addEllipsis();
                break;
            }
            i++;
        }
        this.addHeader();
        this.itemContent(nameValue.toString(), false);
        this.addFooter();
        return this;
    }

    @NonNull
    public ST array2(Object... v){
        return this.array(v, 0);
    }

    @NonNull
    public ST val(Number v){
        return this.val(this.toStr(v));
    }

    @NonNull
    public ST val(Collection<?> v){
        return this.val(this.toStr(v));
    }

    @NonNull
    public ST val(Collection<?> v, @NonNull Integer maxLength){
        int i=0;
        ST nameValue= ST.of().delimiter(",").blockPrefix("[").blockSuffix("]");
        for(Object o : v){
            nameValue.val(o);
            if (maxLength>0 && i>=maxLength-1){
                nameValue.addDelimiter().addEllipsis();
                break;
            }
            i++;
        }
        this.addHeader();
        this.itemContent(nameValue.toString(), false);
        this.addFooter();
        return this;
    }

    @NonNull
    public ST val(@NonNull Map<?, ?> v, @NonNull Integer maxLength){
        int i=0;
        ST nameValue= ST.of().delimiter(",").blockPrefix("[").blockSuffix("]");
        for(Map.Entry<?, ?> entry : v.entrySet()){
            nameValue.name(this.toStr(entry.getKey())).val(entry.getValue());
            if (maxLength>0 && i>=maxLength-1){
                nameValue.addDelimiter().addEllipsis();
                break;
            }
            i++;
        }
        this.addHeader();
        this.itemContent(nameValue.toString(), false);
        this.addFooter();
        return this;
    }

    @NonNull
    public ST val(@NonNull Map<?, ?> v){
        return this.val(v, 0);
    }

    @NonNull
    public ST val(Class<?> v){
        return this.val(this.toStr(v, Objects.isNull(v)?"":v.getSimpleName()));
    }

    @NonNull
    public ST val(Object v){
        return this.val(this.toStr(v));
    }

    @NonNull
    public ST nullV(){
        return this.val("null");
    }

    @NonNull
    public ST banner(@NonNull String banner){
        this.banner=Optional.of(banner);
        return this;
    }

    /**
     * 基于Sl4j风格的格式化功能的banner方法，可以用 banner("{}, {}", "hello", "world") 相当于执行 banner("hello, world")的效果
     * @param bannerFmt
     * @param objects
     * @return
     */
    @NonNull
    public ST banner(@NonNull String bannerFmt, @NonNull Object... objects){
        this.banner=Optional.of(MessageFormatter.arrayFormat(bannerFmt, objects).getMessage());
        return this;
    }

    @NonNull
    public ST target(@NonNull String target){
        this.target=Optional.of(target);
        return this;
    }

    @NonNull
    public ST target(@NonNull Object instance){
        return this.target(instance.getClass());
    }

    /**
     * 基于Sl4j风格的格式化功能的target方法，可以用 banner("{}, {}", "hello", "world") 相当于执行 target("hello, world")
     * @param format
     * @param objects
     * @return
     */
    @NonNull
    public ST target(@NonNull String format, @NonNull Object... objects){
        this.target=Optional.of(MessageFormatter.arrayFormat(format, objects).getMessage());
        return this;
    }

    @NonNull
    public ST target(@NonNull Class<?> clazz){
        this.target=Optional.of(clazz.getSimpleName());
        return this;
    }

    @NonNull
    public ST eq(@NonNull String eq){
        this.eq=Optional.of(eq);
        return this;
    }

    @NonNull
    public ST delimiter(@NonNull String kvDelimiter){
        if (!Strings.isEmpty(kvDelimiter)){
            this.itemDelimiter = Optional.of(kvDelimiter);
        }
        return this;
    }

    @NonNull
    public ST blockPrefix(@NonNull String blockPrefix) {
        if (!Strings.isEmpty(blockPrefix)){
            this.blockPrefix = Optional.of(blockPrefix);
        }
        return this;
    }

    @NonNull
    public ST blockSuffix(@NonNull String blockSuffix) {
        if (!Strings.isEmpty(blockSuffix)){
            this.blockSuffix = Optional.of(blockSuffix);
        }
        return this;
    }

    /**
     * 设置val的最大长度，超出限制会自动截断并用...替代
     * @param ellipsis
     * @return
     */
    @NonNull
    public ST ellipsis(@NonNull int ellipsis){
        Assert.isTrue(ellipsis>0, "传入的省略长度必须要>0，不然无法输出结果");
        this.ellipsis=Optional.of(ellipsis);
        return this;
    }

    /**
     * 打印输出
     * @return
     */
    @NonNull
    public ST println(){
        System.out.println(this.toString());
        return this;
    }

    /**
     * 输出debug日志
     */
    public void debugLog(){
        this.logger.ifPresent(log->log.debug(this.toString()));
    }

    /**
     * 输出error日志
     */
    public void errorLog(){
        this.logger.ifPresent(log->log.error(this.toString()));
    }

    /**
     * 输出error日志
     */
    public void errorLog(@NonNull Throwable e){
        this.logger.ifPresent(log->log.error(this.toString(), e));
    }

    /**
     * 输出info日志
     */
    public void infoLog(){
        this.logger.ifPresent(log->log.info(this.toString()));
    }

    /**
     * 输出warn日志
     */
    public void warnLog(){
        this.logger.ifPresent(log->log.warn(this.toString()));
    }

    /**
     * 输出trace日志
     */
    public void traceLog(){
        this.logger.ifPresent(log->log.trace(this.toString()));
    }

    @NonNull
    public String build(){
        StringBuffer result=new StringBuffer();
        this.blockPrefix.ifPresent(result::append);
        this.banner.ifPresent(result::append);
        this.target.ifPresent(t->{
            result.append("[").append(t).append("]");
        });
        if (this.banner.isPresent() || this.target.isPresent()){
            this.itemDelimiter.ifPresent(result::append);
        }
        result.append(sb);
        this.blockSuffix.ifPresent(result::append);
        this.itemDelimiter.ifPresent(d->{
            if (result.length()>0 && !Strings.isEmpty(d)){
                int idx=result.lastIndexOf(d);
                if (idx>=0){
                    result.deleteCharAt(idx);
                }
            }
        });
        return result.toString();
    }

    @NonNull
    public String toStr(){
        return this.build();
    }

    @NonNull
    public String string(){
        return this.build();
    }

    @NonNull
    @Override
    public String toString(){
        return this.build();
    }

    @NonNull
    public static String format(@NonNull String fmt, @NonNull Object... objects){
        return ST.of().valfmt(fmt, objects).build();
    }

    @NonNull
    public static boolean isBlank(String val){
        return Strings.isBlank(val);
    }

    @NonNull
    public static boolean isEmpty(String val){
        return Strings.isEmpty(val);
    }

    @NonNull
    public static boolean isNotBlank(String val){
        return Strings.isNotBlank(val);
    }

    @NonNull
    public static boolean isNotEmpty(CharSequence val){
        return Strings.isNotEmpty(val);
    }

    /**
     * 强制约束字符串非空白字符串（非null,非0长度，trim后非0长度）
     * @param val
     * @return
     */
    @NonNull
    public static String requiredNonBlank(String val){
        Assert.isTrue(Strings.isNotBlank(val), "不允许传入空白字符串！");
        return val;
    }

    /**
     * 强制邀请传入的字符序列非空（非null，非0长度）
     * @param val
     * @return
     */
    public CharSequence requiredNonEmpty(CharSequence val){
        Assert.isTrue(Strings.isNotEmpty(val), "不允许传入空字符串");
        return val;
    }

    public static void main(String[] args){
        Logger log= LoggerFactory.getLogger(ST.class);
        //this.sb.append(banner).append(" [").append(target).append("]").append(!Strings.isEmpty(this.kvPrefix)?this.kvPrefix:"");
        ST.of("这是一个测试")
                .target(Object.class)
                .eq("=").ellipsis(7)
                .delimiter(",\n")
                .itemPrefix("\t")
                .blockPrefix("[").blockSuffix("]")
                .name("hello {}", "world").type("string").val("123")
                .name("class").val(100)
                .name("num").type(Integer.class).val(100)
                .val("1111111111")
                .val("0123456789")
                .array(new Object[]{"array1", "array01234567890", null, "array3"}, 2)
                .array2("array1", "array2", "array3", "array4", "array5")
//                .val(ImmutableMap.builder().put("k1", "v1").build())
//                .val(ImmutableMap.builder().put("k1", "v1").put("k2", "v2").put("k3", "v3").build(), 2)
//                .val(ImmutableSet.builder().add("set1").add("set2").add("set3").add("set4").build(), 2)
//                .val(ImmutableList.builder().add("list1").add("list2").add("list3").add("list4").build(), 2)
                .banner("这是一段测试")
                .nullV()
                .println();
        ST.of().banner("这是一段测试， 当前日期是：{}", new Date()).target("TEST").name("姓名").type(String.class).val("张飞").name("性别").val("男").name("年龄").val(20).println();
        ST.of(log).banner("这是一段测试，当前日期是：{}", new Date()).target("TEST").name("k1").val(100).debugLog();
        ST.of().blockPrefix("[").blockSuffix("]").val(1).val(2).val(3).val(4).val(5).val(6).println();
    }
}
