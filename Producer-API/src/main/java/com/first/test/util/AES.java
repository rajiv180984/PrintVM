package com.first.test.util;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    
    public static String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            String encryptedData =  Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            System.out.println( " Encrypted Key :"+encryptedData);
            return encryptedData;
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            System.out.println(" Decryption ======="+ new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt))));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(" ============== Testing ============");
        // AES.setKey("8782Champ");
        System.out.println(" ========    Secrete key ========="+ AES.secretKey);

        String encryptedData = AES.encrypt("printJOBID@@@count@@@pexpn@@@timestamp", "ShriGanesh1");
        AES.decrypt("yrmKqQJ8AQ87XhYvrt0CeZdFx92Y40mRVPqI9tDvVHRTs+lA7RcGJ2ETlpdvG76fix3+35dJJul7bbIdv7wsrAphxIi5XlQ/vvuR/fBrj7M=", "8782");
    }
}