package com.examples.java8feature;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class StreamDemo1 {
    @Data
    @AllArgsConstructor
    static class Person implements Comparable<Person>{
        private String name;
        private Integer age;

        @Override
        public int compareTo(Person o) {
            if (this.getAge()==o.getAge()){
                return 0;
            }else if (this.getAge()>o.getAge()){
                return 1;
            }else{
                return -1;
            }
        }
    }

    private List<Integer> listData1 = Lists.newArrayList(13,25,9,43,998,868,500,394,null, 74,57,46);
    private Map<String, Integer> mapData1= Maps.newHashMap();
    private StreamDemo1(){
        mapData1.put("a", 1);
        mapData1.put("c", 53);
        mapData1.put("k", 992);
        mapData1.put("s", 72);
        mapData1.put("v", 31);
        mapData1.put("z", 32);
        mapData1.put("m", 18);
        mapData1.put("p", 45);
        mapData1.put("o", 77);
        mapData1.put("f", 21);
        mapData1.put("aa", 215);
    }

    private List<Person> personList=Lists.newArrayList(
            new Person("吕布", 20),
            new Person("张飞", 19),
            new Person("马超", 21)
    );

    private List<Person> personList2=Lists.newArrayList(
            new Person("张飞", 19),
            new Person("吕布", 20),
            new Person("马超", 21),
            new Person("张飞", 12)
    );

    /**
     * forEach中修改元素的内容
     */
    public void objectForEachModifyElementInLoopDemo1(){
        List<Person> pList=Lists.newArrayList(
                new Person("吕布", 20),
                new Person("马超", 18),
                new Person("典维", 21)
        );
        pList.forEach(p->{
            p.setAge(0);
        });
        for(Person p : pList){
            System.out.println(p.toString());
        }
        pList.parallelStream().forEach(p->{
            p.setAge(0);
        });
        for(Person p : pList){
            System.out.println(p.toString());
        }
    }

    /**
     * 过滤
     */
    public void filterDemo1(){
        Set<Integer> outList= listData1
                .stream()
//                .parallel()
                .filter(Objects::nonNull)
                .filter(i-> i%2==1)
                .collect(Collectors.toSet());
        System.out.println(outList);
    }

    /**
     * 过滤
     */
    public void filterDemo2(){
        Set<String> outList=personList.stream().parallel().filter(o->o.age<20).map(o->o.name).collect(Collectors.toSet());
        System.out.println(outList);
    }
    public void parallelStreamDemo1(){
        List<Integer> l=this.listData1.parallelStream().filter(Objects::nonNull).sorted()
                .filter(i->i%2==0)
                .collect(Collectors.toList());
        System.out.println(l);
    }

    /**
     * 并行流
     */
    public void parallelStreamDemo2(){
        this.listData1.parallelStream().forEach(o->{
            try {
                Thread.sleep(1000);
                System.out.println("->"+o);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("all done");
    }

    /**
     * 排序，把数据按照某个规则排序，默认正序
     */
    public void sortDemo1(){
        List<Integer> l=this.listData1.stream().filter(Objects::nonNull)//排序只有使用串行流才有效
                .sorted().collect(Collectors.toList());
        System.out.println(l);
    }

    /**
     * 排序，倒序
     */
    public void sortReserveDemo1(){
        List<Integer> l=this.listData1.stream().filter(Objects::nonNull)//排序只有使用串行流才有效
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        System.out.println(l);
    }

    /**
     * 使用列表自带的排序，倒序
     */
    public void sortReserveDemo2(){
        System.out.println("排序前");
        this.personList.stream().forEach(System.out::println);
        List<Person> l=new ArrayList<>(this.personList.stream().toList());////排序只有使用串行流才有效
        l.sort((Person v1, Person v2)->v1.getAge() - v2.getAge()>0?-1:1);
        System.out.println("排序后");
        l.forEach(System.out::println);
    }

    /**
     * 排序，倒序 结果未生效
     */
    public void sortReserveDemo3(){
        System.out.println("排序前");
        this.personList.stream().forEach(System.out::println);
        List<Person> l=this.personList.stream()
                .sorted(Comparator.comparing(Person::getAge).reversed()).collect(Collectors.toList());
        System.err.println("排序后 结果未生效");
        l.parallelStream().forEach(System.out::println);
    }

    /**
     * 排序，倒序 结果未生效
     */
    public void sortReserveDemo4(){
        System.out.println("排序前");
        this.personList.stream().forEach(System.out::println);
        List<Person> l=this.personList.stream()
                .sorted((o1, o2)->o1.getAge()-o2.getAge()>0?-1:1).collect(Collectors.toList());
        System.err.println("排序后 结果未生效");
        l.parallelStream().forEach(System.out::println);
    }

    /**
     * 排序，nullfirst，把nullu元素放在前面
     */
    public void sortNullsFirstDemo3(){
        List<Integer> l=this.listData1.stream().filter(Objects::nonNull)//排序只有使用串行流才有效
                .sorted(Comparator.nullsFirst(Comparator.comparing(Integer::intValue))).collect(Collectors.toList());
        System.out.println(l);
    }

    /**
     * 排序，nullfirst，把nullu元素放在前面
     */
    public void sortNullsLastDemo4(){
        List<Integer> l=this.listData1.stream().filter(Objects::nonNull)
                .sorted(Comparator.nullsLast(Comparator.comparing(Integer::intValue))).collect(Collectors.toList());
        System.out.println(l);
    }

    /**
     * 排序，自定义排序
     */
    public void sortCustomOrderDemo5(){
        System.out.println("排序前");
        this.personList2.stream().forEach(System.out::println);
        this.personList2.sort(
                Comparator.comparing(Person::getName)//先按照姓名排序
                        .thenComparing(o->o.getAge()) //再按照年龄排序
        );
        System.out.println("排序后，先按照名字排序，然后按照年龄排序");
        this.personList2.stream().forEach(System.out::println);
    }

    /**
     * 排序，自定义排序，排序逻辑完全自己写
     */
    public void CustomOrderDem6(){
        System.out.println("排序前");
        this.personList2.stream().forEach(System.out::println);
        this.personList2.sort(
                Comparator.comparing(Person::getName, (o1, o2)->{
                    if (o1==null || o2== null) return 1;
                    return o1.compareTo(o2);
                }).thenComparing(Person::getAge, (o1, o2)->{
                    if (o1==null || o2== null) return 1;
                    return o1.compareTo(o2);
                })//thenComparing可以继续些
        );
        System.out.println("排序后，先按照名字排序，然后按照年龄排序");
        this.personList2.stream().forEach(System.out::println);
    }

    /**
     * peek是一个中间过程，相当与在处理中的时候做点事情
     * 一般peek用于调试问题
     */
    public void peekDemo1(){
        List<Integer> vals=this.listData1.stream().peek(System.out::println).collect(Collectors.toList());
        System.out.println(vals);
    }

    /**
     * peek修改年龄
     */
    public void peekModifyDemo2(){
        List<Person> pList=this.personList2.stream().peek(p->p.setAge(p.getAge()+10)).collect(Collectors.toList());
        pList.stream().forEach(System.out::println);
    }

    /**
     * skip，跳过2个元素
     */
    public void skipDemo1(){
        List<Person> pList=this.personList2.stream().skip(2).collect(Collectors.toList());
        System.out.println(pList);
    }

    /**
     * allMatch，判断是否流里每个元素都满足条件
     */
    public void allMatchDemo1(){
        System.out.println(this.personList2);
        boolean ret=this.personList2.stream().allMatch(p->p.getName().startsWith("吕"));//等同于filter().count()==list.size()
        System.out.println("是否所有人都姓吕"+ret);
    }

    /**
     * anyMatch，判断流里是否有任一元素满足条件
     */
    public void anyMatchDemo1(){
        System.out.println(this.personList2);
        boolean ret=this.personList2.stream().anyMatch(p->p.getName().startsWith("吕"));//等同于filter().count()>0
        System.out.println("是否包含姓吕的"+ret);
    }

    /**
     * noneMatch, 判断流里是否没有元素满足条件
     */
    public void noneMatchDemo1(){
        boolean ret=this.personList2.stream().noneMatch(p->p.getName().startsWith("曹"));//等同于filter().count()==0
        System.out.println("是否未包含姓曹的"+ret);
    }

    /**
     * min，获取流里年龄最小的人
     */
    public void minDemo1(){
        Optional<Person> person=this.personList2.stream().min(Comparator.comparing(Person::getAge));
        person.ifPresent(System.out::println);
    }

    /**
     * minBy，获取流里的年龄最小的人
     */
    public void minByDemo1(){
        Optional<Person> person=this.personList2.stream().collect(Collectors.minBy(Comparator.comparing(Person::getAge)));
        person.ifPresent(System.out::println);
    }

    /**
     * max，获取流里年龄最大的人
     */
    public void maxDemo1(){
        Optional<Person> person=this.personList2.stream().max(Comparator.comparing(Person::getAge));
        person.ifPresent(System.out::println);
    }

    /**
     * maxBy，获取流里i年龄最大的人
     */
    public void maxByDemo1(){
        Optional<Person> person=this.personList2.stream().collect(Collectors.maxBy(Comparator.comparing(Person::getAge)));
        person.ifPresent(System.out::println);
    }

    /**
     * map的例子
     */
    public void mapDemo1(){
        listData1.stream().forEach(System.out::println);
        List<Integer> ret= listData1.stream().filter(Objects::nonNull).map(o->o*10).distinct().collect(Collectors.toList());
        System.out.println(ret);
    }

    /**
     * map的例子2,不同的类型，省去filter直接map
     */
    public void mapDemo2(){
        List<JsonElement> list=Lists.newArrayList(new JsonPrimitive("能看到这个信息说明执行结果正确"), new JsonObject(), JsonNull.INSTANCE, new JsonArray());
        try{
            List<String> tempList=list.stream().map(JsonElement::getAsString).collect(Collectors.toList());
            assert  false:"这句不会执行";
        }catch (Exception e){
            System.out.println("预期中的报错"+ e.getMessage()+"，错误原因是因为Stream操作时因为流中的对象类型是JsonElement，因为类型不符，无法执行getAsString方法导致异常");
        }
        list.stream().filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsString).findFirst().ifPresent(System.out::println);
    }

    /**
     * Collections.toMap，把对象转成map，key是名字，value是年龄
     */
    public void toMapDemo1(){
        Map<String, Integer> m=this.personList.stream().collect(Collectors.toMap(k->k.name, v->v.age));
        System.out.println(m);
    }

    /**
     * 转换成Map
     */
    public void toMapDemo2(){
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orange", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99"))
//                new Item("apple", 10, new BigDecimal("9.99")), //key不能重复
//                new Item("banana", 10, new BigDecimal("19.99")), //key不能重复
//                new Item("apple", 20, new BigDecimal("12.99")) //key不能重复
        );
        Map<String, Integer> m1= items.stream().collect(
                Collectors.toMap(k->k.name, v->v.age)
        );
        System.out.println(m1);
        //{banana=20, papaya=20, apple=10, orang=10, watermelon=10}
    }

    /**
     * 转换成Map
     */
    public void toMapDemo3(){
        List<Map<String, Object>> items=Arrays.asList(
                ImmutableMap.<String, Object>builder().put("name", "apple").put("amount", 2).build(),
//                ImmutableMap.<String, Object>builder().put("name", "apple").put("amount", 1).build(), //key不能重复
                ImmutableMap.<String, Object>builder().put("name", "banana").put("amount", 2).build(),
                ImmutableMap.<String, Object>builder().put("name", "orange").put("amount", 6).build(),
                ImmutableMap.<String, Object>builder().put("name", "watermelon").put("amount", 1).build(),
                ImmutableMap.<String, Object>builder().put("name", "papaya").put("amount", 1).build()
        );
        Map<String, Object> m1=items.stream().map(m->(Map<String, Object>)m).collect(
                Collectors.toMap(k->(String)k.get("name"), v->v.get("amount"))
        );
        System.out.println(m1);
        //{banana=2, orange=6, papaya=1, apple=2, watermelon=1}
    }

    /**
     * sort 最简单的排序
     */
    public void sortedDemo1(){
        this.listData1.stream().filter(Objects::nonNull).sorted().forEach(System.out::println);
    }

    /**
     * 把int[]转换成Integer[]
     */
    public void mapToIntDemo1(){
        int[] vals=new int[]{1,2,3,4,5,6};
        Integer[] integers = Arrays.stream(vals).boxed().toArray(Integer[]::new);
        Arrays.stream(integers).forEach(System.out::println);
    }

    /**
     * 把Integer[]转成int[]
     */
    public void mapToIntDemo2(){
        Integer[] vals=new Integer[]{1,2,3,4,5,6};
        int[] ints = Arrays.stream(vals).mapToInt(Integer::valueOf).toArray();
        Arrays.stream(ints).forEach(System.out::println);
    }

    /**
     * 装箱，就是把基本类型转换成包装类型
     */
    public void boxedDemo1(){
        int[] vals=new int[]{1,2,3,4,5};
        List<Integer> integers=Arrays.stream(vals).boxed().collect(Collectors.toList());
        System.out.println(integers);
    }

    /**
     * flatmap, 将数据拍平，例如把int[]{2,3,4}通过flatMap处理成 2, 3, 4
     */
    public void flatMapDemo1(){
        List<int[]> list1=Lists.newArrayList(new int[]{1,2,3}, new int[]{4,5,6}, new int[]{7,8,9});
        List<Integer>outputList=list1.stream().flatMap(o-> Arrays.stream(o)//这句是把每个数组成员都转成流对象，Stream机制会自动对流对象合并
                        .boxed())//把流里面的基本类型int转换成包装类型Integer
                .collect(Collectors.toList());
        System.out.println(outputList);
    }

    /**
     * flatmap, 重组数据
     */
    public void flatMapDemo2(){
        List<String> lists=Lists.newArrayList("a,b,c,d,e", "f,g,h,i,j,k", "l,m,n,o", "p,q,r,s,t", "u,v,w,x,y", "z");
        List<String> outList=lists.stream().flatMap(o->Stream.of(o.split(","))).collect(Collectors.toList());
        System.out.println(outList);
    }

    /**
     * reduce, 把流数据归约成一个值，count,min,max的后台就是用的reduce
     * 有三种实现方法，但有两种是最常用的
     */
    public void reduceDemo1(){
        System.out.println("第一种做法");
        int val=this.listData1.stream()
                .filter(Objects::nonNull) //必须滤除null，不然会报错
                .reduce(0, (o1, o2) -> o1 + o2);//对年龄求和，第一个参数0表示初始值
        System.out.println(val);

        System.out.println("第二种做法");
        int val2=this.listData1.stream().filter(o->!Objects.isNull(o))
                .reduce((o1, o2)->o1+o2).get();
        System.out.println(val2);
    }

    /**
     * limit，限流操作，限制元素的个数，和mysql的limit类似
     */
    public void limitDemo1(){
        List<Integer> list=this.listData1.stream().filter(Objects::nonNull)
                .limit(3).collect(Collectors.toList());
        System.out.println(list);
    }

    /**
     * mapToObj 把数据映射成对象
     */
    public void mapToObjDemo1(){
        //字符串到列表转换
        String str="abdefgh";
        List<Character> l=str.chars().mapToObj(o->(char)o).collect(Collectors.toList());
        System.out.println(l);
    }

    /**
     * summaryStatistics例子，对所有人的年龄求和
     */
    public void summaryStatisticsDemo1(){
        IntSummaryStatistics st=this.listData1.stream().filter(Objects::nonNull)
                .mapToInt(i->i).summaryStatistics();
        System.out.println("max:"+st.getMax());
        System.out.println("min:"+st.getMin());
        System.out.println("avg:"+st.getAverage());
        System.out.println("count:"+st.getCount());
        Long val=this.personList2.stream().mapToInt(Person::getAge).summaryStatistics().getSum();
        System.out.println(val);
    }

    /**
     * Collectors.summingInt 的两种做法
     */
    public void summingIntDemo1(){
        List<Integer> list1=Lists.newArrayList(1,2,3,4,5,6);
        int val=list1.parallelStream().collect(Collectors.summingInt(Integer::intValue)); //第一种做法
        int val2= list1.parallelStream().mapToInt(Integer::intValue).sum(); //第二种做法
        System.out.println(val);
        System.out.println(val2);
    }

    /**
     * averagingInt，求平均值
     */
    public void averagingIntDemo1(){
        double age=this.personList2.stream().collect(Collectors.averagingInt(Person::getAge));
        System.out.println("平均年龄="+age);
    }

    /**
     * toArray，整数列表转换成整数数组
     */
    public void toArrayDemo1(){
        System.out.println("第一种做法");
        Integer[] integers=this.listData1.stream().filter(Objects::nonNull).toArray(Integer[]::new);
        Arrays.stream(integers).forEach(System.out::println);
        System.out.println("第二种做法，注意里面的null要排除掉，不然会报错");
        int[] ints=this.listData1.stream().filter(Objects::nonNull)
                .mapToInt(Integer::intValue).toArray();
        Arrays.stream(ints).forEach(System.out::println);
    }

    /**
     * toArray，字符串列表转成字符串数组
     */
    public void toArrayDemo2(){
        List<String> lists=Lists.newArrayList("a", "b", "c", "d", "e", "f", "g");
        String[] ret=lists.stream().map(String::valueOf).toArray(String[]::new);
        Arrays.stream(ret).forEach(System.out::println);
    }

    /**
     * Stream.of
     */
    public void streamOfDemo1(){
        String strJoin= Stream.of(this.listData1).filter(Objects::nonNull)
                .map(o->o.toString())
                .collect(Collectors.joining(","));
        System.out.println(strJoin);

        strJoin= Stream.of(this.listData1.toArray()).filter(Objects::nonNull)
                .map(o->o.toString())
                .collect(Collectors.joining(","));
        System.out.println(strJoin);
//        Stream.of(1,2,3,4,5).
    }

    /**
     * Stream.concat， 合并两个流
     */
    public void streamConcatDemo1(){
        Stream<Integer> s1=Stream.of(1,2,3,4,5);
        Stream<Integer> s2=Stream.of(6,7,8,9,10);
        Stream<Integer> s3=Stream.concat(s1, s2);
        s3.forEach(System.out::println);
    }

    /**
     * Stream.builder() 连续添加多个值
     */
    public void streamBuilderDemo1(){
        Stream<Integer> s1=Stream.<Integer>builder().add(1).add(2).add(3).add(4).build();
        s1.forEach(System.out::println);
    }

    public void joinDemo1(){
        String s=this.listData1.stream().filter(Objects::nonNull)
                .map(o->o.toString())
                .distinct()
                //.collect(Collectors.joining(",", "[", "]"));
                .collect(Collectors.joining(","));
        System.out.println(s);
    }

    @Data
    @AllArgsConstructor
    public static class Item {
        private String name;
        private Integer age;
        private BigDecimal price;
    }

    @Data
    @AllArgsConstructor
    public static class _Channel{
        private String group;
        private String channel;
    }

    /**
     * gruupingBy，分组后是个Map，key和值都是属性
     */
    public void groupingByDemo1(){
        List<_Channel> channels=Lists.newArrayList(
                new _Channel("g1", "t1"),
                new _Channel("g1", "t2"),
                new _Channel("g2", "t3"),
                new _Channel("", "t4")
        );
        Map<String, Set<String>> maps=channels.stream()
                .collect(Collectors.groupingBy(_Channel::getGroup, //对应生成 Map<String, ...>
                        Collectors.mapping(_Channel::getChannel, Collectors.toSet()))); //对应生成 Map<...Set<String>>
        Map<String, Map<String, _Channel>> maps2=channels.stream()
                .collect(Collectors.groupingBy(_Channel::getGroup, Collectors.toMap(_Channel::getChannel, Function.identity()))); //Function.identify()在这里等价与 v->v
        System.out.println(maps);
        //{=[t4], g1=[t1, t2], g2=[t3]}
    }

    /**
     * gruupingBy，分组后是个Map，key和值都是属性
     */
    public void grouping_mappingDemo1(){
        List<_Channel> channels=Lists.newArrayList(
                new _Channel("g1", "t1"),
//                new _Channel("g1", "t1"),
                new _Channel("g1", "t2"),
                new _Channel("g2", "t3"),
                new _Channel("", "t4")
        );
        Map<String, Map<String, _Channel>> maps=channels.stream()
                .collect(Collectors.groupingBy(_Channel::getGroup, Collectors.toMap(_Channel::getChannel, Function.identity())));
        maps.entrySet().forEach(entry->{
            System.out.println(entry.toString());
        });
        //={t4=StreamDemo1._Channel(group=, channel=t4)}
        //  g1={t1=StreamDemo1._Channel(group=g1, channel=t1), t2=StreamDemo1._Channel(group=g1, channel=t2)}
        //  g2={t3=StreamDemo1._Channel(group=g2, channel=t3)
        // }
    }
    /**
     * groupingBy，将一个由Map组成的List分组，按照名称分组，并把薪资求和
     */
    public void groupingByDemo2(){
        List<Map<String, String>> list=Lists.newArrayList(
                ImmutableBiMap.<String, String>builder().put("name", "马超").put("salary", "32000").build(),
                ImmutableBiMap.<String, String>builder().put("name", "蒋干").put("salary", "15000").build(),
                ImmutableBiMap.<String, String>builder().put("name", "蒋干").put("salary", "500").build(),
                ImmutableBiMap.<String, String>builder().put("name", "蒋干").put("salary", "18000").build(),
                ImmutableBiMap.<String, String>builder().put("name", "董卓").put("salary", "21000").build()
        );
        Map<String, Integer> maps=list.stream().collect(Collectors.groupingBy(o->o.get("name"), Collectors.summingInt(o->Integer.parseInt(o.get("salary")))));
        System.out.println(maps);
        //{董卓=21000, 张辽=28000, 吕布=40000, 蒋干=33500, 马超=32000}
    }

    /**
     * groupingBy，将一个由Map组成的List分组，按照名称分组，并把薪资求和
     */
    public void groupingByDemo3(){
        List<Map<String, String>> list=Lists.newArrayList(
                ImmutableBiMap.<String, String>builder().put("name", "马超").put("salary", "32000").build(),
                ImmutableBiMap.<String, String>builder().put("name", "蒋干").put("salary", "15000").build(),
                ImmutableBiMap.<String, String>builder().put("name", "蒋干").put("salary", "500").build(),
                ImmutableBiMap.<String, String>builder().put("name", "蒋干").put("salary", "18000").build(),
                ImmutableBiMap.<String, String>builder().put("name", "董卓").put("salary", "21000").build()
        );
        Map<Object, List<Map<String, String>>> maps=list.stream().collect(Collectors.groupingBy(k->(String)k.get("name")));
        System.out.println(maps);
        //{董卓=[{name=董卓, salary=21000}], 蒋干=[{name=蒋干, salary=15000}, {name=蒋干, salary=500}, {name=蒋干, salary=18000}], 马超=[{name=马超, salary=32000}]}
    }

    /**
     * groupingBy，按照名称分组，值是价格列表
     */
    public void groupingByDemo4(){
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("12.99"))
        );
        Map<String, Set<BigDecimal>> groupByPriceMap = items.stream().collect(
                Collectors.groupingBy(Item::getName,Collectors.mapping(Item::getPrice, Collectors.toSet()))
        );
        System.out.println(groupByPriceMap);
        //{papaya=[9.99], banana=[19.99], apple=[9.99, 12.99], orang=[29.99], watermelon=[29.99]}
    }

    /**
     * groupingBy，按照薪资分组，把薪资相同的品牌组成一个列表
     */
    public void groupingByDemo5(){
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("12.99"))
        );
        Map<BigDecimal, List<Item>> groupByPriceMap = items.stream().collect(
                Collectors.groupingBy(Item::getPrice)
        );
        groupByPriceMap.entrySet().stream().forEach(System.out::println);
        //19.99=[StreamDemo1.Item(name=banana, age=20, price=19.99), StreamDemo1.Item(name=banana, age=10, price=19.99)]
        //29.99=[StreamDemo1.Item(name=orang, age=10, price=29.99), StreamDemo1.Item(name=watermelon, age=10, price=29.99)]
        //9.99=[StreamDemo1.Item(name=apple, age=10, price=9.99), StreamDemo1.Item(name=papaya, age=20, price=9.99), StreamDemo1.Item(name=apple, age=10, price=9.99)]
        //12.99=[StreamDemo1.Item(name=apple, age=20, price=12.99)]
    }

    /**
     * groupingBy，按照名字分组，组成一个key是名字，值是项目列表
     */
    public void groupingByDemo6(){
        //3 apple, 2 banana, others 1
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("12.99"))
        );

        Map<String, List<Item>> groupByNameMap = items.stream().collect(
                Collectors.groupingBy(Item::getName)
        );
        groupByNameMap.entrySet().stream().forEach(System.out::println);
        //papaya=[StreamDemo1.Item(name=papaya, age=20, price=9.99)]
        //banana=[StreamDemo1.Item(name=banana, age=20, price=19.99), StreamDemo1.Item(name=banana, age=10, price=19.99)]
        //apple=[StreamDemo1.Item(name=apple, age=10, price=9.99), StreamDemo1.Item(name=apple, age=10, price=9.99), StreamDemo1.Item(name=apple, age=20, price=12.99)]
        //orang=[StreamDemo1.Item(name=orang, age=10, price=29.99)]
        //watermelon=[StreamDemo1.Item(name=watermelon, age=10, price=29.99)]
    }

    /**
     * groupingBy，按照价格分组，值是一个品名的集合
     */
    public void groupingByDemo7(){
        //3 apple, 2 banana, others 1
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("12.99"))
        );
        // group by price, uses 'mapping' to convert List<Item> to Set<String>
        Map<BigDecimal, Set<String>> groupingMap = items.stream().collect(
                Collectors.groupingBy(Item::getPrice,
                        Collectors.mapping(Item::getName, Collectors.toSet())
                )
        );
        groupingMap.entrySet().stream().forEach(System.out::println);
        //19.99=[banana]
        //29.99=[orang, watermelon]
        //9.99=[papaya, apple]
        //12.99=[apple]
    }

    /**
     * groupingBy，分组
     */
    public void groupingByDemo8(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("type","aaa");
        map.put("name","苹果");
        list.add(map);
        map = new HashMap<>();
        map.put("type","bbb");
        map.put("name","稻谷");
        list.add(map);
        map = new HashMap<>();
        map.put("type","111");
        map.put("name","鸡肉");
        list.add(map);

        map = new HashMap<>();
        map.put("type","aaa");
        map.put("name","西瓜");
        list.add(map);
        Map<String, List<Map<String, Object>>> collect = list.stream().collect(Collectors.groupingBy(this::groupingByDemo8_getKey));
        collect.entrySet().stream().forEach(System.out::println);
//        aaa=[{name=苹果, type=aaa}, {name=西瓜, type=aaa}]
//        111=[{name=鸡肉, type=111}]
//        bbb=[{name=稻谷, type=bbb}]

        System.out.println();

        Map<String, List<Map<String, Object>>> collect2 = list.stream().collect(Collectors.groupingBy(k->k.get("type").toString()));
        collect2.entrySet().stream().forEach(System.out::println);
//        aaa=[{name=苹果, type=aaa}, {name=西瓜, type=aaa}]
//        111=[{name=鸡肉, type=111}]
//        bbb=[{name=稻谷, type=bbb}]
    }

    /**
     * groupingBy，分组
     */
    public void groupingByDemo9(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("type","aaa");
        map.put("name","苹果");
        list.add(map);
        map = new HashMap<>();
        map.put("type","bbb");
        map.put("name","稻谷");
        list.add(map);
        map = new HashMap<>();
        map.put("type","111");
        map.put("name","鸡肉");
        list.add(map);

        map = new HashMap<>();
        map.put("type","aaa");
        map.put("name","西瓜");
        list.add(map);
        Map<String, List<Map<String, Object>>> collect = list.stream().collect(Collectors.groupingBy(e -> e.get("type").toString()));
        collect.entrySet().stream().forEach(System.out::println);
//        aaa=[{name=苹果, type=aaa}, {name=西瓜, type=aaa}]
//        111=[{name=鸡肉, type=111}]
//        bbb=[{name=稻谷, type=bbb}]
    }

    /**
     * groupingBy，分组后只取首条
     */
    public void groupingByDemo10(){
        List<Item> items = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orang", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("12.99"))
        );
        Map<String, Item> groupByPriceMap = items.stream().collect(
                Collectors.groupingBy(Item::getName, Collectors.collectingAndThen(Collectors.toList(), t->t.get(0)))
        );
        groupByPriceMap.entrySet().forEach(System.out::println);
//        papaya=StreamDemo1.Item(name=papaya, age=20, price=9.99)
//        banana=StreamDemo1.Item(name=banana, age=20, price=19.99)
//        apple=StreamDemo1.Item(name=apple, age=10, price=9.99)
//        orang=StreamDemo1.Item(name=orang, age=10, price=29.99)
//        watermelon=StreamDemo1.Item(name=watermelon, age=10, price=29.99)
        //{papaya=[9.99], banana=[19.99], apple=[9.99, 12.99], orang=[29.99], watermelon=[29.99]}
    }

    /**
     * partitioningBy，分区，按照条件拆分成两组List
     */
    public void partitioningByDemo1(){
        Stream<Integer> stream=Stream.of(1,2,3,4,5,6,7);
        Map<Boolean, List<Integer>> list= stream.collect(Collectors.partitioningBy(o-> o>3));
        System.out.println(list);
        //{false=[1, 2, 3], true=[4, 5, 6, 7]}
    }

    /**
     * partitioningBy，分区，按照条件拆分成成两组List
     */
    public void partitioningByDemo2(){
        Map<Boolean, List<Person>> list=this.personList2.stream().collect(Collectors.partitioningBy(p->p.getAge()>18));
        //{false=[StreamDemo1.Person(name=张飞, age=12)], true=[StreamDemo1.Person(name=张飞, age=19), StreamDemo1.Person(name=吕布, age=20), StreamDemo1.Person(name=马超, age=21)]}
    }

    private String groupingByDemo8_getKey(Map<String, Object> map){
        return map.get("type").toString();
    }

    /**
     * updateAndGet，在流中更新数据
     */
    public void update_elements_in_stream_demo1(){
        AtomicReference<Integer> ageCount= new AtomicReference<>(0);
        this.personList2.parallelStream().forEach(e->{
            ageCount.updateAndGet(v -> v + e.age);
        });
        System.out.println(ageCount);
    }

    /**
     * 在流中更新数据
     */
    public void update_elements_in_stream_demo2(){
        Map<String, Integer> ageMap=new HashMap<>(10);
        this.personList2.parallelStream().forEach(o->{
            if (ageMap.containsKey(o.getName())){
                ageMap.put(o.getName(), ageMap.get(o.getName())+o.age);
                return;
            }
            ageMap.put(o.getName(), o.age);
        });
        System.out.println(ageMap);
        //{张飞=31, 吕布=20, 马超=21}
    }

    /**
     * Stream.Builder 通过Stream.Builder创建流
     */
    public void stream_addDeo1(){
        Stream.Builder builder= Stream.<String>builder();
        builder.add("apple");
        builder.add("banana");
        builder.add("orange");
        Stream<String> stream=builder.build();
        stream.forEach(System.out::println);
    }

    /**
     * Stream.Builder创建流
     */
    public void stream_acceptDemo1(){
        Stream.Builder builder= Stream.<String>builder();
        builder.accept("apple");
        builder.accept("banana");
        builder.accept("orange");
        Stream<String> stream=builder.build();
        stream.forEach(System.out::println);
    }

    /**
     * Stream.iterate 使用无限的序列流，相当于死循环
     */
    public void stream_iterateDemo1(){
        Stream.iterate(0,n->n+2).limit(5).map(o->o+" ").forEach(System.out::print);
        //0 2 4 6 8
    }

    /**
     * Stream.generate，留生成器，创建无限序列的流，相当于死循环
     */
    public void stream_aenerateDemo1(){
        Stream.generate(new Random()::nextInt).limit(5).map(o->o+" ").forEach(System.out::print);
        //-561337157 537527578 2080234559 1148475619 531475705
    }

    /**
     * Stream.of 创建流
     */
    public void stream_contactDemo1(){
        Stream<Number> stream1=Stream.of(1,2,3);
        Stream<Number> stream2=Stream.of(4,5,6);
        Stream.concat(stream1, stream2).map(o->o+" ").forEach(System.out::print);
        //1 2 3 4 5 6
    }

    /**
     * Collectors.partitioningBy 按照条件分区
     */
    public void collect_partitioningBy_demo1(){
        Map<Boolean, List<Integer>> collect1 = Stream.of(1, 2, 3, 4).collect(Collectors.partitioningBy(it -> it % 2 == 0));
        collect1.entrySet().parallelStream().forEach(System.out::println);
        //false=[1, 3]
        //true=[2, 4]
    }

    public static void main(String[] args){
        StreamDemo1 demo1=new StreamDemo1();
//        demo1.objectForEachModifyElementInLoopDemo1();
//        demo1.filterDemo1();
//        demo1.filterDemo2();
//        demo1.parallelStreamDemo1();
//        demo1.parallelStreamDemo2();
//        demo1.mapDemo1();
//        demo1.mapDemo2();
//        demo1.toMapDemo1();
//        demo1.toMapDemo2();
//        demo1.toMapDemo3();
//        demo1.joinDemo1();
//        demo1.sortDemo1();
//        demo1.sortedDemo1();
//        demo1.sortReserveDemo1();
//        demo1.sortReserveDemo2();
//        demo1.sortReserveDemo3();
//        demo1.sortReserveDemo4();
//        demo1.sortNullsFirstDemo3();
//        demo1.sortNullsLastDemo4();
//        demo1.sortCustomOrderDemo5();
//        demo1.CustomOrderDem6();
//        demo1.peekDemo1();
//        demo1.peekModifyDemo2();
//        demo1.skipDemo1();
//        demo1.allMatchDemo1();
//        demo1.anyMatchDemo1();
//        demo1.noneMatchDemo1();
//        demo1.minDemo1();
//        demo1.minByDemo1();
//        demo1.maxDemo1();
//        demo1.maxByDemo1();
//        demo1.reduceDemo2();
//        demo1.limitDemo1();
//        demo1.mapToIntDemo1();
//        demo1.mapToIntDemo2();
//        demo1.mapToObjDemo1();
//        demo1.boxedDemo1();
//        demo1.flatMapDemo1();
//        demo1.flatMapDemo2();
//        demo1.toArrayDemo1();
//        demo1.toArrayDemo2();
//        demo1.streamOfDemo1();
//        demo1.streamConcatDemo1();
//        demo1.streamBuilderDemo1();
//        demo1.summaryStatisticsDemo1();
//        demo1.summingIntDemo1();
//        demo1.averagingIntDemo1();
//        demo1.groupingByDemo1();
//        demo1.grouping_mappingDemo1();
//        demo1.groupingByDemo2();
//        demo1.groupingByDemo3();
//        demo1.groupingByDemo8();
//        demo1.partitioningByDemo1();
//        demo1.partitioningByDemo2();
//        demo1.groupingByDemo4();;
//        demo1.groupingByDemo6();
//        demo1.groupingByDemo7();
//        demo1.groupingByDemo8();
//        demo1.groupingByDemo9();
//        demo1.groupingByDemo10();
//        demo1.groupingByDemo10();
//        demo1.update_elements_in_stream_demo1();
//        demo1.update_elements_in_stream_demo2();
//        demo1.stream_addDeo1();
//        demo1.stream_acceptDemo1();
//        demo1.stream_iterateDemo1();
//        demo1.stream_aenerateDemo1();
//       demo1.stream_contactDemo1();
//        demo1.collect_demo1();
        demo1.collect_partitioningBy_demo1();
    }
}