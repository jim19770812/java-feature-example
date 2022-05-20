package org.jim.utils;

import lombok.NonNull;

import java.util.Base64;

public class Base64Util {
    @NonNull
    public static byte[] decode(@NonNull String str) throws Exception {
        return encodeBytes(str.getBytes("utf-8"));
    }

    @NonNull
    public static String decodeStr(@NonNull String str) throws Exception {
        if (str == null)
            return null;
        return new String(decode(str));
    }

    @NonNull
    public static byte[] encodeBytes(@NonNull byte[] bytes) throws Exception {
        return Base64.getEncoder().encode(bytes);
    }

    @NonNull
    public static byte[] decodeBytes(@NonNull byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    @NonNull
    public static String encodeStr(@NonNull String str) throws Exception {
        return new String(encodeBytes(str.getBytes("utf-8")));
    }

}
