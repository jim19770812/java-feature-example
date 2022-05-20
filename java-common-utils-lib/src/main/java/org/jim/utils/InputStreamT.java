package org.jim.utils;

import lombok.NonNull;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 输入流操作工具类
 * 1.支持读取本地文件
 * 2.支持读取jar包里的文件
 * 3.支持打包成jar包后读取相对路径下的文件
 * 4.支持获取文件的字符串内容
 * 5.支持获取文件的字节数组内容
 */
public class InputStreamT {
    private ClassLoader classLoader;
    private String relativeFileName;
    public static InputStreamT of(@NonNull ClassLoader classLoader){
        InputStreamT result=new InputStreamT();
        result.classLoader=classLoader;
        return result;
    }

    public static InputStreamT of (ClassLoader classLoader, String relativeFileName){
        InputStreamT result= of(classLoader);
        result.relativeFileName = relativeFileName;
        return result;
    }

    @NonNull
    public InputStreamT relativeFileName(@NonNull String fileName){
        this.relativeFileName=relativeFileName;
        return this;
    }

    @NonNull
    public InputStreamT resource(@NonNull Resource resource){
        this.relativeFileName=resource.getFilename();
        return this;
    }

    /**
     * 输入流转字符串
     * @param inputStream
     * @return
     */
    @NonNull
    public static String toStr(@NonNull InputStream inputStream){
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        try{
            return br.lines().collect(Collectors.joining("\n"));
        }finally{
            try { br.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public Optional<InputStream> asInputStream(){
        InputStream result=this.classLoader.getResourceAsStream(this.relativeFileName);
        return Optional.ofNullable(result);
    }

    /**
     * 获取文本内容
     * @return
     */
    public Optional<String> asString(){
        return this.asInputStream().map(InputStreamT::toStr);
    }

    /**
     * 获取文件中的全部字节数组
     * @return
     */
    public Optional<byte[]> asByteArray(){
        return this.asInputStream().map(t->{
            try{
                byte[] bytes=new byte[t.available()];
                t.read(bytes);
                return bytes;
            }catch (Exception e){
                return null;
            }
        });
    }

    /**
     * 获取资源对象
     * @return
     */
    public Optional<Resource> asResource(){
        Resource result=new InputStreamResource(asInputStream().orElse(null));
        return Optional.ofNullable(result);
    }
}
