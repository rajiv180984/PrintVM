package com.first.machin.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AESUtils {
 
	@Value("machinesecuriykey")
	private String machinekey;
	
	@Value("${machineId}")
	private String machineId;
	
    private static SecretKeySpec secretKey;
    private static byte[] key;
 
    public void setKey(String myKey) 
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
 
    public  String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public  String decrypt(String strToDecrypt, String secret) 
    {
        
        try
        {
        	System.out.println(" This is ABC :"+ strToDecrypt);
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            String orgtr = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt.getBytes())));
            System.out.println(" Decryption ======="+ orgtr);
            return orgtr;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;

    }
    
    public static void main(String[] args) 
    {
    	
    	System.out.println(Integer.parseInt("8782", 17));
    	final String secretKey = getPrivateKey();
         
        
        // MachineId It should save in DB machine
        // By Machine Name fetch the Machine ID
        String secreteKey = System.getProperty("machine_key");
        System.out.println("========"+ secreteKey);
        System.out.println( System.getenv("JAVA_HOME"));
        AESUtils eas = new AESUtils();
        String originalString = "SRTXVZ";   // 
        String encryptedString = "73lvaWsYRllhOR+aQHkC6SGM0LZIg8curSyjlc6QI1s5XeX1J86ieUgfD2mbsL9IlfhiKWaACtE+U068g01L9i491UpI5EohvOkSbqLcAM=";
        //eas.encrypt(originalString, eas.getMachineSecuritykey(eas.machinekey, eas.machineId)) ;
       // System.out.println(" ============== "+ eas.machinekey);
        
       //eas.setKey("8782Champ");
        
        String decryptedString = eas.decrypt(encryptedString, "8782") ;
        System.out.println("1: "+encryptedString);
        decryptedString =  eas.decrypt("73lvaWsYRllhOR+aQHkC6SGM0LZIg8curSyjlc6QI1s5XeX1J86ieUgfD2mbsL9IlfhiKWaACtE+U068g01L9i491UpI5EohvOkSbqLcAM=", "8782");
         
        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
        
        System.out.println( "password :" +eas.encrypt("zeroxm", secretKey));
        
        
    }
    
    public static String getPrivateKey() {
    	return "rajmank";
    	
    	
    }

    // this is for secrete key
	public String getMachineSecuritykey(String machineKey, String machineId) {
		   String passwordToHash = generatePassword(machineKey, machineId);
		     
		try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            return sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
     
		return null;
		
	}

	//password generator
	private String generatePassword(String machineKey, String machineID) {
		 return machineKey.concat(String.valueOf(Integer.parseInt(machineID, 17)));
		//return machinekey.concat(String.valueOf(Integer.parseInt(machineId, 17)));
	}

	public String getMachinekey() {
		return machinekey;
	}

	public void setMachinekey(String machinekey) {
		this.machinekey = machinekey;
	}
	
	
}