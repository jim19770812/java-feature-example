package org.jim.context;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.Synchronized;

/**
 * 上下文管理者
 * 1.在threadlocal中管理变量，全局有效
 *
 * 注意：如果写入和读取分处不同的线程，会读不到数据
 */
public class JContextHolder {
    public static ThreadLocal<ConcurrentMap<String, Object>> threadLocal = new ThreadLocal<>();

    /**
     * 写入值
     *
     * @param key key
     * @param value 值
     */
    public static void set(String key, Object value) {
        ConcurrentMap<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new ConcurrentHashMap<>(100);
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    /**
     * 全局对象计数器：累加step，如果key没有，返回step，如果key有值，就+=step
     * @param key key
     * @param step 步长
     * @return 增加后的值
     */
    @Synchronized
    public static Integer incr(final String key, Integer step){
        Integer result=step;
        if (containsKey(key)){
            Optional<Integer> t= JContextHolder.get(key, Integer.class);
            result= t.map(o -> o + step).orElse(step);
            JContextHolder.set(key, result);
        }else{
            JContextHolder.set(key, result);
        }
        return result;
    }

    /**
     * 类似redis的incr，实现累加
     * @param key key
     * @return 增加后的值
     */
    @SuppressWarnings("unused")
    public static Integer incr(final String key){
        return incr(key, 1);
    }

    /**
     * 判断threadlocal里是否包含某个key
     * @param key key
     * @return 是否包含
     */
    @SuppressWarnings("all")
    public static boolean containsKey(final String key){
        return Optional.ofNullable(threadLocal.get()).filter(Objects::nonNull).map(o->((Map<String,Object>)o).containsKey(key)).orElse(false);
    }

    /**
     * 从threadlocal里移除某个key
     * @param key key
     * @return 是否移除
     */
    @SuppressWarnings("all")
    public static boolean removeKey(final String key){
        return Optional.ofNullable(threadLocal.get()).filter(Objects::nonNull).map(o->{
            Map<String,Object> map=(Map<String,Object>)o;
            if (!map.containsKey(key)) return false;
            map.remove(key);
            return true;
        }).orElse(false);
    }
    /**
     * 获取值
     * @param key key
     * @return 值
     */
    public static <T>Optional<T> get(String key){
        ConcurrentMap<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new ConcurrentHashMap<>(100);
            threadLocal.set(map);
        }
        if (!map.containsKey(key)){
            return Optional.empty();
        }
        @SuppressWarnings("unchecked")
        T ret=(T)map.get(key);
        return Optional.of(ret);
    }

    /**
     * 获取制定类型的值
     * @param key
     * @param clazz
     * @param <T>
     * @return  值
     */
    @SuppressWarnings("all")
    public static <T> Optional<T> get(String key, Class<T> clazz){
        Optional<T> result = get(key);
        return result;
    }

    /**
     * get threadLocal val by class
     *
     * @param key key
     * @return 值
     */
    @SuppressWarnings("unused")
    public static <T> Optional<T> getAsDefault(String key, Class<T> clazz, T defaultVal) {
        Optional<T> obj = get(key, clazz);
        return obj.isPresent()?obj:Optional.of(defaultVal);
    }

    /**
     * 清除所有变量
     */
    public static void remove() {
        threadLocal.remove();
    }
}
