package com.example.swipeviewex01;


import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

public class AES256Chiper {
    private final String encryptionKeyBase64 = "DWIzFkO22qfVMgx2fIsxOXnwz10pRuZfFJBvf4RS3eY=";
    private final String ivBase64 = "AcynMwikMkW4c7+mHtwtfw==";

    protected AES256Chiper()
    {}

    protected String encrypt(String plainText) throws Exception
    {
        byte[] plainTextArray = new byte[0];
        plainTextArray = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] keyArray = DatatypeConverter.parseBase64Binary(encryptionKeyBase64);
        byte[] iv = DatatypeConverter.parseBase64Binary(ivBase64);

        SecretKeySpec secretKey = new SecretKeySpec(keyArray, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return DatatypeConverter.printBase64Binary(cipher.doFinal(plainTextArray));
    }

    protected String decrypt(String messageBase64) throws Exception {

        byte[] messageArray = DatatypeConverter.parseBase64Binary(messageBase64);
        byte[] keyArray = DatatypeConverter.parseBase64Binary(encryptionKeyBase64);
        byte[] iv = DatatypeConverter.parseBase64Binary(ivBase64);

        SecretKey secretKey = new SecretKeySpec(keyArray, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return new String(cipher.doFinal(messageArray));
    }



//    public static byte[] ivBytes = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
////    public static String ivBytes = "0000000000000000";
//
//    public static String secretKey = "qwertyuiopasdfghqwertyuiopasdfgh";
//
//    public static String AES_Encode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//        byte[] textBytes = str.getBytes("UTF-8");
//        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
//        SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes("UTF-8"),"AES");
//        Cipher cipher = null;
//        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE,newKey,ivSpec);
//        return Base64.encodeToString(cipher.doFinal(textBytes),0);
//    }
//
//    public static String AES_Dncode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//        byte[] textBytes = Base64.decode(str,0);
//        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
//        SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes("UTF-8"),"AES");
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE,newKey,ivSpec);
//
//
//        return new String(cipher.doFinal(textBytes),"UTF-8");
//    }




//public static String Alg = "AES/CBC/PKCS5Padding";
//    public static String PK = "01234567890123456789012345678901"; // 32byte
//    public static String IV = PK.substring(0,16); // 16byte
//
//    public static String encrypt(String plainText) throws Exception {
//        Cipher cipher = Cipher.getInstance(Alg);
//        SecretKeySpec keySpec = new SecretKeySpec(IV.getBytes(), "AES");
//        IvParameterSpec ivParamSpec = new IvParameterSpec(IV.getBytes());
//        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
//
//        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
//        return Base64.getEncoder().encodeToString(encrypted);
//    }
//
//    public static String decrypt(String cipherText) throws Exception {
//        Cipher cipher = Cipher.getInstance(Alg);
//        SecretKeySpec keySpec = new SecretKeySpec(IV.getBytes(), "AES");
//        IvParameterSpec ivParamSpec = new IvParameterSpec(IV.getBytes());
//        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
//
//        byte[] decodedBytes =  Base64.getDecoder().decode(cipherText);
//        byte[] decrypted = cipher.doFinal(decodedBytes);
//        return new String(decrypted, "UTF-8");
//    }


}
