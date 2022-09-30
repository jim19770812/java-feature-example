package org.jim.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
public class JContext {
    public final static String INTERNAL_VAR_PREFIX="__"; //内部变量前缀
    private Map<String, JContextEntity> variables= Maps.newConcurrentMap();

    /**
     * 将变量放入上下文对象
     * @param name
     * @param value
     */
    public void put(@NonNull String name, @NonNull Object value){
        this.variables.put(name, new JContextEntity().k(name).v(value));
    }

    public void putAll(@NonNull Map<String, Object> map){
        this.putAll(map.entrySet().stream().map(m->{
            JContextEntity ret=new JContextEntity();
            ret.k(m.getKey()).v(m.getValue());
            return ret;
        }).collect(Collectors.toList()));
    }

    public void putAll(@NonNull List<JContextEntity> contextEntities){
        Map<String, JContextEntity> t=contextEntities.stream().collect(Collectors.toMap(JContextEntity::k, v->v));
        this.variables.putAll(t);
    }

    /**
     * 获取变量
     * @param name
     * @return
     */
    @NonNull
    @SuppressWarnings(value={"unchecked"})
    public <T>Optional<T> get(@NonNull String name){
        if (!this.variables.containsKey(name)){
            return Optional.empty();
        }
        JContextEntity entity=this.variables.get(name);
        return Optional.ofNullable(entity).filter(Objects::nonNull).map(t->(T)t.v());
    }

    /**
     * 获取制定类型的变量
     * @param name
     * @param clazz
     * @return
     */
    @NonNull
    public <T> Optional<T> get(@NonNull String name, @NonNull Class<T> clazz){
        if (!this.variables.containsKey(name)){
            return Optional.empty();
        }
        JContextEntity entity=this.variables.get(name);
        return Optional.ofNullable(entity).filter(o->Objects.nonNull(o) && Objects.nonNull(o.v()) && clazz.isAssignableFrom(o.v().getClass())).map(t->(T)t.v());
    }

    private <T> Optional<Collection<T>> getCollection(@NonNull String name, Predicate<Collection<T>> collectionPredicate, Predicate<T> elementPredicate){
        if (!this.variables.containsKey(name)){
            return Optional.empty();
        }
        JContextEntity entity=this.variables.get(name);
        Optional<Collection<T>> result=Optional.ofNullable(entity).filter(o->{
            boolean ret=Objects.nonNull(o) && Objects.nonNull(o.v()) && o.v() instanceof Collection && collectionPredicate.test((Collection)o.v());
            if (ret){
                Collection<T> collection=(Collection<T>)o.v();
                return collection.stream().findFirst().map(elementPredicate::test).isPresent();
            }
            return ret;

        }).map(t->(Collection<T>)t.v());
        return result;
    }

    /**
     * 获取Set类型的变量
     * @param name
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> Set<T> getSetInstance(@NonNull String name, @NonNull Class<T> clazz){
        Set<T> ret=this.getCollection(name,  c->Objects.nonNull(c) && c instanceof Set, ele->Objects.nonNull(ele) && clazz.isAssignableFrom(ele.getClass())).map(o->(Set<T>)o).orElse(Sets.newHashSet());
        return ret;
    }

    /**
     * 获取Set类型的变量
     * @param name
     * @param clazz
     * @param predicate
     * @return
     * @param <T>
     */
    public <T> Set<T> getSetInstance(@NonNull String name, @NonNull Class<T> clazz, @NonNull Predicate<T> predicate){
        Set<T> ret=this.getCollection(name,  c->Objects.nonNull(c) && c instanceof Set, ele->Objects.nonNull(ele) && clazz.isAssignableFrom(ele.getClass())).map(o->{
            return ((Set<T>)o).stream().filter(t->predicate.test(t)).collect(Collectors.toSet());
        }).orElse(Sets.newHashSet());
        return ret;
    }

    /**
     * 获取List类型的变量
     * @param name
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> List<T> getListInstance(@NonNull String name, @NonNull Class<T> clazz){
        List<T> ret=this.getCollection(name,  c->Objects.nonNull(c) && c instanceof List, ele->Objects.nonNull(ele) && clazz.isAssignableFrom(ele.getClass())).map(o->(List<T>)o).orElse(Lists.newArrayList());
        return ret;
    }

    /**
     * 获取List类型的变量
     * @param name
     * @param clazz
     * @param predicate
     * @return
     * @param <T>
     */
    public <T> List<T> getListInstance(@NonNull String name, @NonNull Class<T> clazz, @NonNull Predicate<T> predicate){
        List<T> ret=this.getCollection(name,  c->Objects.nonNull(c) && c instanceof List, ele->Objects.nonNull(ele) && clazz.isAssignableFrom(ele.getClass())).map(o->{
            return ((List<T>)o).stream().filter(t->predicate.test(t)).collect(Collectors.toList());
        }).orElse(Lists.newArrayList());
        return ret;
    }

    /**
     * 获取Map类型的变量
     * @param name
     * @param keyType
     * @param valueType
     * @return
     * @param <K>
     * @param <V>
     */
    public <K, V> Map<K, V> getMapInstance(@NonNull String name, @NonNull Class<K> keyType, @NonNull Class<V> valueType){
        if (!this.variables.containsKey(name)){
            return Maps.newHashMap();
        }
        JContextEntity entity=this.variables.get(name);
        Map<K, V> result=Optional.ofNullable(entity).filter(o->{
            boolean ret=Objects.nonNull(o) && Objects.nonNull(o.v()) && o.v() instanceof Map;
            if (ret){
                Map<K, V> map=(Map<K, V>)o.v();
                /**
                 * 如果没有数据也返回true
                 */
                if (map.isEmpty()) return true;
                return map.entrySet().stream().findFirst().filter(entry->{
                    boolean ret2=Objects.nonNull(entry) && entry instanceof Map.Entry;
                    if (!ret2) return false;
                    Map.Entry ent=(Map.Entry) entry;
                    ret2= Objects.nonNull(ent.getKey()) && keyType.isAssignableFrom(ent.getKey().getClass()) && Objects.nonNull(ent.getValue()) && valueType.isAssignableFrom(ent.getValue().getClass());
                    return ret2;
                }).isPresent();
            }
            return ret;
        }).map(t->(Map<K, V>)t.v()).orElse(Maps.newHashMap());
        return result;
    }
    /**
     * 判断是否包含某个变量
     * @param name
     * @return
     */
    @NonNull
    public Boolean contains(@NonNull String name){
        return this.variables.containsKey(name);
    }

    /**
     * 根据名字移除某个变量
     * @param name
     */
    public void remove(@NonNull String name){
        this.variables.remove(name);
    }

    @NonNull
    public Integer size(){
        return this.variables.size();
    }

    /**
     * 移除一组变量
     * @param names
     */
    public void remove(@NonNull  List<String> names){
        for(String k :names){
            this.remove(k);
        }
    }

    /**
     * 清除所有变量
     */
    public void clear(){
        this.variables.clear();
    }

    /**
     * 复制上下文，并返回一个新的实例
     * @return
     */
    @NonNull
    public JContext copyInstance(){
        JContext result=new JContext();
        Map<String, JContextEntity> vals=this.variables.values().stream().collect(Collectors.toMap(JContextEntity::k, JContextEntity::copyInstance));
        result.variables.putAll(vals);
        return result;
    }

    @NonNull
    public Stream<JContextEntity> stream(){
        return this.variables.values().stream();
    }

    /**
     * 是否包含内部变量
     * @return
     */
    @SuppressWarnings(value = { "unused" })
    private Boolean existsInternalVariables(){
        return this.variables.keySet().stream().anyMatch(k -> k.startsWith(INTERNAL_VAR_PREFIX));
    }

    /**
     * 合并上下文
     * @param context
     */
    public void merge(@NonNull JContext context){
        merge(context.stream().collect(Collectors.toList()));
    }

    /**
     * 合并上下文实体列表
     * @param contextEntities
     */
    public void merge(@NonNull List<JContextEntity> contextEntities){
        for(JContextEntity entry : contextEntities){
            this.put(entry.k(), entry.v());
        }
    }

    /**
     * 获取keys集合
     * @return
     */
    @NonNull
    public Set<String> keys(){
        return this.variables.keySet().stream().sorted().collect(Collectors.toSet());
    }

    /**
     * 获取一个只读的实体集合
     * @return
     */
    @NonNull
    public Set<Map.Entry<String, JContextEntity>> entrySet(){
        return this.variables.entrySet().stream().map(e-> new AbstractMap.SimpleEntry<String, JContextEntity>(e.getKey(), e.getValue())).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        StringBuilder result=new StringBuilder();
        for(JContextEntity entity : this.variables.values()){
            result.append(entity.k()).append("...").append("\n");
        }
        return result.toString();
    }
}