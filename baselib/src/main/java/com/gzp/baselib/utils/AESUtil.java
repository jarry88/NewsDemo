package com.gzp.baselib.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class AESUtil {

    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * BASE64解密
     */
    public static byte[] decryptBASE64(String data) {
        return Base64.decode(data);
    }

    /**
     * BASE64加密
     */
    public static String encryptBASE64(byte[] data) {
        return Base64.encode(data);
    }

    /**
     * AES加密后使用BASE64做转码
     * 这里只能16字节密钥， 如果要使用24，32，需要配置local_policy.jar 和US_export_policy.jar
     *
     * @param data
     * @param key
     * @return
     */
    public static String encryptAESBASE64(String data, String key) {
        //ey为16，如果要使用24，32，需要配置local_policy.jar 和US_export_policy.jar
        if (key == null || key.length() != 16) {
            return null;
        }
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            String iv = key;
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
            byte[] bytes = cipher.doFinal(data.getBytes(DEFAULT_ENCODING));
            return encryptBASE64((bytes));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * AES解密
     *
     * @param data
     * @param key
     * @return
     */
    public static String decryptAESBASE64(String data, String key) {
        if (key == null || key.length() != 16) {
            return null;
        }
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            String iv = key;
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
            byte[] bytes = cipher.doFinal(decryptBASE64(data));
            return new String(bytes, DEFAULT_ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
