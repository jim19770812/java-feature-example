package com.examples.java8feature;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.jim.utils.SystemUtil;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * CompletableFuture是异步的执行机制
 * 1.每个链式代码片段都是在fork/join池里执行，并发性很好
 * 2.前一个步骤执行玩成才会执行后一个步骤
 * 3.整个设计规范和javascript的Promise相似
 *   applySupplyAsync() => new Promise((resolve, reject)=>{....}) 提供供给，发起异步执行
 *   thenApplyAsync() => Promise.then()
 *   handleAsync() => Promise.then()
 *   exceptionallyAsync() => Promise.catch()
 *   thenCompleteAsync() = > Promise.then()
 *   allOf() => Promise.all([])
 *   anyOf() => Promise.race([])
 *
 * 3.几乎所有方法都提供了一个同步方法和一个异步方法（*Async）提供了足够的灵活性
 *
 */
public class CompletableFutureDemo1 {
    /**
     * supplyAsync的最简单例子
     */
    @SneakyThrows
    public void supplyAsync_Test1() {
        CompletableFuture<String> cf=CompletableFuture.supplyAsync(()->"hello world");
        System.out.println(cf.get());
    }

    /**
     * supplyAsync执行，延迟并阻塞
     */
    @SneakyThrows
    public void supplyAsync_timeoutTest1(){
        CompletableFuture<String> cf1=CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(1000);
            return "延迟1秒，hello world";
        });
        System.out.println(cf1.get());

        CompletableFuture<String> cf2=CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(2000);
            return "延迟2秒，hello world，超时";
        });
        try{
            System.out.println(cf2.get(1000, TimeUnit.MILLISECONDS));
            Assert.isTrue(false, "这句不会执行");
        }catch (TimeoutException te){
            System.out.println("执行CompletableFuter.get() 超时了");
        }
    }

    /**
     * thenApplyAsync()的链式调用，和Javascript的Promise.then() 作用相似
     */
    @SneakyThrows
    public void supply_thenApplyTest1(){
        CompletableFuture<Integer> cf=CompletableFuture.supplyAsync(()->{
            return 10;
        }).thenApplyAsync((x)->{
            Assert.isTrue(x==10, "这个异常不会触发");
            return x*2;
        });
        System.out.println(cf.get());
    }

    /**
     * exceptionally()的用法，作用和javascript的Promise.catch()作用相似
     */
    @SneakyThrows
    public void supply_thenApply_exceptionallyTest1(){
        CompletableFuture<Integer> cf=CompletableFuture.supplyAsync(()->{
            throw new RuntimeException("有错误发生！");
        }).thenApplyAsync((x)->{
            System.out.println("这段代码不会执行！");
            return 0;
        }).exceptionallyAsync((e)->{
            System.out.println("exceptionallyAsync 有错误发生："+ e.getMessage());
            return -1;
        });
        System.out.println(cf.get());
        /**
         exceptionallyAsync 有错误发生：java.lang.RuntimeException: 有错误发生！
         -1

         * 返回-1表示执行了exceptionallyAsync
         */

    }

    /**
     * handle的例子，和whenComplete作用差不多，只是handle具有能接收上个步骤结果作为参数的能力
     */
    @SneakyThrows
    public void supplyAsync_handleTest1(){
        CompletableFuture<Integer> cf=CompletableFuture.supplyAsync(()->{
            return Lists.newArrayList(5,1,9,2,3);
        }).handleAsync((list, error)->{
            System.out.println("handle 方法收到的参数:" + list.stream().map(String::valueOf).collect(Collectors.joining(",")) + " , " + Optional.ofNullable(error).map(Throwable::getMessage).orElse("无异常"));
            Integer ret=list.stream().sorted().findFirst().orElse(-1);
            return ret;
        });
        System.out.println("结果："+ cf.get());
        /**
         handle 方法收到的参数:5,1,9,2,3 , 无异常
         结果：1
         从结果上看handle确实和
         */
    }

    /**
     * cfAll().handle() 的例子
     * 在thenApplyAsync中执行CompletableFuture，拿到结果后交由handleAsync步骤
     */
    @SneakyThrows
    public void allOf_handleTest2(){
        String param1="这是外部传入的一个参数";
        CompletableFuture<String> cf1=CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(500);
            System.out.println("cf1 done");
            return "hello cf1";
        });
        CompletableFuture<String> cf2=CompletableFuture.supplyAsync(()->{
            System.out.println("cf2 done");
            return "hello cf2";
        });
        CompletableFuture<String> cf3=CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(1000);
            System.out.println("cf3 done");
            return "hello cf3";
        });
        /**
         * 注意：allOf()中提供参数没有意义，因为在thenApplyAsync中始终是null
         */
        CompletableFuture<?> cfAll=CompletableFuture.allOf(cf3).thenApplyAsync(v->{
            System.out.println("thenApplyAsync: v参数："+v); //v参数的内容是null
            /**
             * 注意：因为allOf()执行后不会有值传入thenComplate，所以要在allOf()执行后增加一个thenApply()来搜集执行结果
             */
            List<CompletableFuture<String>> cfList=Lists.newArrayList(cf1, cf2, cf3);
            List<String> ret=cfList.stream().map(o->{
                try {
                    return o.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            return ret;
        }).handleAsync((param2, error)->{
            /**
             * 因为前一个步骤的thenApply()已经获取到了结果，其返回会作为whenComplate的入参
             */
            System.out.println("handle收到的参数："+ param2);
            System.out.println("handle:"+ Optional.ofNullable(error).map(Throwable::getMessage).orElse("无异常"));
            return param2;
        });
        Object ret=cfAll.get();
        System.out.println("cfAll return is "+ ret);
        /**
         cf2 done
         cf1 done
         cf3 done
         thenApplyAsync: v参数：null
         handle收到的参数：[hello cf1, hello cf2, hello cf3]
         handle:无异常
         cfAll return is [hello cf1, hello cf2, hello cf3]
       */
    }

    /**
     * accept()作用和whenComlete()/get()类似，但但accept()不会返回结果
     */
    @SneakyThrows
    public void supply_thenAcceptTest1(){
        CompletableFuture<String> cf=CompletableFuture.supplyAsync(()->{
            System.out.println("cf done");
            return "hello cf";
        });
        cf.thenAccept((p)->{
            System.out.println("thenAccept return is " + p);
        });
        /**
         cf done
         thenAccept return is hello cf

         执行结果
         *
         */
    }

    /**
     * supplyAsync().whenComplete()例子，验证不返回数据的场景
     */
    @SneakyThrows
    public void supply_whenCompleteTest1(){
        CompletableFuture<String> cf=CompletableFuture.supplyAsync(()->{
            System.out.println("cf1 done");
            return "hello cf1";
        }).whenComplete((param, error)->{
            System.out.println("whenComplete 参数1: " +param); //whenComplete是可以收到上一流程return的数据的
            System.out.println("whenComplete 参数2: " +error);
            //whenComplete里不能返回数据，这里加return会编译不过
        });
        System.out.println(cf.get());
        /**
         cf1 done
         whenComplete 参数1: hello cf1
         whenComplete 参数2: null
         hello cf1
         * 从结果上看，虽然whenComplete()如果不return数据，就默返回上一步骤的处理结果
         * 但是如果返回结果，就会接收thenComplete()中返回的结果
         */
    }

    /**
     * supplyAsync().whenComplete() 在whenComplete()中抛出异常的例子
     */
    public void supply_whenCompleteTest2(){
        CompletableFuture<String> cf=CompletableFuture.supplyAsync(()->{
            System.out.println("cf1 done");
            return "hello cf1";
        }).whenComplete((param, error)->{
            throw new RuntimeException("有错误发生！");
        });
        try{
            System.out.println(cf.get());
        }catch (Exception e){
            System.out.println("外部捕获到了来自whenComplete()的异常");
        }
        /**
         cf1 done
         外部捕获到了来自whenComplete()的异常
         * 外部可以正常捕获whenComplete()抛出的异常，只不过只能是UncheckedException
         */
    }

    /**
     * allOf().supplyAsync().whenComplete() 等待所有Future执行结束后触发whenComplete
     * 和Javascript的Promise.All([]).then()作用相同
     * 其中的applyAysnc()的作用是搜集执行的结果，没有这步whenComplete会收到的参数是null
     */
    @SneakyThrows
    public void allOf_supplies_whenCompleteTest1(){
        CompletableFuture<String> cf1=CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(500);
            System.out.println("cf1 done");
            return "hello cf1";
        });
        CompletableFuture<String> cf2=CompletableFuture.supplyAsync(()->{
            System.out.println("cf2 done");
            return "hello cf2";
        });
        CompletableFuture<String> cf3=CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(1000);
            System.out.println("cf3 done");
            return "hello cf3";
        });
        CompletableFuture<?> cfAll=CompletableFuture.allOf() //无论allOf()传不传参数都会执行thenApply()方法
                .thenApplyAsync(v->{
            System.out.println("v参数："+v); //这里接不到参数
            /**
             * 注意：因为allOf()执行后不会有值传入thenComplate，所以要在allOf()执行后增加一个thenApply()来搜集执行结果
             */
            List<CompletableFuture<String>> cfList=Lists.newArrayList(cf1, cf2, cf3);
            List<String> ret=cfList.stream().map(o->{
                try {
                    return o.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            return ret;
        }).whenComplete((ret, error)->{
            /**
             * 因为前一步骤的thenApply()已经获取到了结果，其返回会作为whenComplate的入参
             */
            System.out.println("whenComplete:"+ ret+ " ,"+ Optional.ofNullable(error).map(Throwable::getMessage).orElse("无异常"));
        });

        Object ret=cfAll.get();
        System.out.println("cfAll return is "+ ret);
        /**
         cf2 done
         cf1 done
         cf3 done
         v参数：null
         whenComplete:[hello cf1, hello cf2, hello cf3] ,无异常
         cfAll return is [hello cf1, hello cf2, hello cf3]
         * 从结果上看，每个CompletableFuture只执行了一次，并没有预想中的执行两次的情况出现
         */
    }

    /**
     * allOf().supplyAsync().whenComplete() 等待所有Future执行结束后触发whenComplete
     * 和Javascript的Promise.All([]).then()作用相同
     * 其中的applyAysnc()的作用是搜集执行的结果，没有这步whenComplete会收到的参数是null
     */
    @SneakyThrows
    public void supply_supplies_whenCompleteTest1(){
        CompletableFuture<String> cf1=CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(500);
            System.out.println("cf1 done");
            return "hello cf1";
        });
        CompletableFuture<String> cf2=CompletableFuture.supplyAsync(()->{
            System.out.println("cf2 done");
            return "hello cf2";
        });
        CompletableFuture<String> cf3=CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(1000);
            System.out.println("cf3 done");
            return "hello cf3";
        });
        CompletableFuture<?> cfAll=CompletableFuture.supplyAsync(()->{
            /**
             * 注意：因为allOf()执行后不会有值传入thenComplate，所以要在allOf()执行后增加一个thenApply()来搜集执行结果
             */
            List<CompletableFuture<String>> cfList=Lists.newArrayList(cf1, cf2, cf3);
            List<String> ret=cfList.parallelStream().map(o->{
                try {
                    return o.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            return ret;
        }).whenComplete((ret, error)->{
            /**
             * 因为前一步骤的thenApply()已经获取到了结果，其返回会作为whenComplate的入参
             */
            System.out.println("whenComplete:"+ ret+ " ,"+ Optional.ofNullable(error).map(Throwable::getMessage).orElse("无异常"));
        });

        Object ret=cfAll.get();
        System.out.println("cfAll return is "+ ret);
        /**
         cf2 done
         cf1 done
         cf3 done
         v参数：null
         whenComplete:[hello cf1, hello cf2, hello cf3] ,无异常
         cfAll return is [hello cf1, hello cf2, hello cf3]
         * 从结果上看，每个CompletableFuture只执行了一次，并没有预想中的执行两次的情况出现
         */
    }

    /**
     * anyOf().supplyAsync().whenComplete()的例子，多个Future只要有一个执行完成就触发whenComplete()
     * 作用和Javascript的Promise.race().then()作用相似
     * 其中的applyAysnc()的作用是搜集执行的结果，没有这步whenComplete会收到的参数是null
     */
    @SneakyThrows
    public void anyOf_supply_whenCompleteAsyncTest1(){
        CompletableFuture<String> cf1=CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(100);
            System.out.println("cf1 done");
            return "hello cf1";
        });
        CompletableFuture<String> cf2=CompletableFuture.supplyAsync(()->{
            System.out.println("cf2 done");
            return "hello cf2";
        });
        CompletableFuture<String> cf3=CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(400);
            System.out.println("cf3 done");
            return "hello cf3";
        });
        CompletableFuture<?> cfAny=CompletableFuture.anyOf(cf1, cf2, cf3).thenApplyAsync(v->{
            System.out.println("v参数的内容："+v);
            return v;
        }).whenCompleteAsync((ret, error)->{ //whenComplete和whenCompleteAsync从就结果上看没啥区别，一个同步执行一个异步执行，推荐异步
            /**
             * 因为前一步骤的thenApply()已经获取到了结果，其返回会作为whenComplate的入参
             */
            System.out.println("whenComplete:"+ ret+ " ,"+ Optional.ofNullable(error).map(Throwable::getMessage).orElse("无异常"));
        });

        Object ret=cfAny.get();
        System.out.println("cfAny return is "+ ret);
        SystemUtil.sleep(500); //等待cf1和cf3执行完并观察输出
        /**
         cf2 done
         v参数的内容：hello cf2
         whenComplete:hello cf2 ,无异常
         cfAny return is hello cf2
         cf1 done //如果主线程没有sleep(n)，这句输出是看不到的
         cf3 done //如果主线程没有sleep(n)，这句输出是看不到的
         * 从结果上看，虽然最终采用了执行最快的的cf2的结果，但cf1和cf3并没有立刻结束而是在独立的线程里执行完成了
         */
    }

    /**
     * thenCombine() 向一个Future组合另一个Future，然后等待两个Future都完成
     */
    @SneakyThrows
    public void supply_thenCombineTest1(){
        CompletableFuture<String> cf1 =CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(100);
            System.out.println("cf1 done");
            return "cf1";
        });
        CompletableFuture<String> cf2 =CompletableFuture.supplyAsync(()->{
            SystemUtil.sleep(1000);
            System.out.println("cf2 done");
            return "cf2";
        });
        CompletableFuture<String> cf1_2=cf1.thenCombineAsync(cf2, (f1, f2)->{
            return f1+","+f2;
        });
        System.out.println("两个future组合后的执行结果："+cf1_2.get());
        /**
         cf1 done
         cf2 done
         两个future组合后的执行结果：cf1,cf2
         */
    }

    @SneakyThrows
    public void runAsyncTest1(){
        CompletableFuture<Void> cf=CompletableFuture.runAsync(()->{
            System.out.println("thread running");
            SystemUtil.sleep(1000);
            System.out.println("thread done");
        });
        cf.get(); //阻塞等待异步线程结束
        System.out.println("线程执行结束后才会打印这句话");
    }

    public static void main(String[] args) {
        CompletableFutureDemo1 demo1=new CompletableFutureDemo1();
//        demo1.supplyAsync_Test1();
//        demo1.supplyAsync_timeoutTest1();
//        demo1.supply_thenApplyTest1();
//        demo1.supply_thenApply_exceptionallyTest1();
//        demo1.supplyAsync_handleTest1();
        demo1.allOf_handleTest2();
//        demo1.supply_thenAcceptTest1();
//        demo1.supply_whenCompleteTest1();
//        demo1.supply_whenCompleteTest2();
//        demo1.allOf_supplies_whenCompleteTest1();
//        demo1.supply_supplies_whenCompleteTest1();
//        demo1.anyOf_supply_whenCompleteAsyncTest1();
//        demo1.supply_thenCombineTest1();
//        demo1.runAsyncTest1();
    }
}
