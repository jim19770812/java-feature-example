package org.jim.utils;import lombok.NonNull;import java.io.ByteArrayInputStream;import java.io.ByteArrayOutputStream;import java.io.ObjectInputStream;import java.io.ObjectOutputStream;/** * 字节数组序列化工具类 * * @author 韩晓峰 */public class ByteArraySerializeUtil {    /**     * 序列化成字节数组     *     * @param object     * @return     */    @NonNull    public static byte[] serialize(@NonNull Object object) {        ObjectOutputStream oos = null;        ByteArrayOutputStream baos = null;        try {            baos = new ByteArrayOutputStream();            oos = new ObjectOutputStream(baos);            oos.writeObject(object);            byte[] bytes = baos.toByteArray();            return bytes;        } catch (Exception e) {        }        return null;    }    /**     * 反序列化字节数组     *     * @param bytes     * @return     */    @NonNull    public static Object unserialize(@NonNull byte[] bytes) {        ByteArrayInputStream bais = null;        try {            bais = new ByteArrayInputStream(bytes);            ObjectInputStream ois = new ObjectInputStream(bais);            return ois.readObject();        } catch (Exception e) {        }        return null;    }}