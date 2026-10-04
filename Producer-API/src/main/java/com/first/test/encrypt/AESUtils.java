package com.first.test.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.first.test.controller.MachineController;
import com.first.test.model.WIFIMachine;
import com.first.test.service.MachineService;

@Component
public class AESUtils {
	
	private static Logger logger = LoggerFactory.getLogger(AESUtils.class);
    
	@Autowired
	MachineService machineService;
	// FIXME persist in Redis
	private Map<String, String> machineSecurityKeyMap = new HashMap<>();
	//FIXME persist in Redis 
	private Map<String, WIFIMachine> machineKeyMap = new HashMap<>();
	private  SecretKeySpec secretKey;
	private  byte[] key;

	public  void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public  String encrypt(String strToEncrypt, String machineId) {
		try {
			String password = getMachineSecuritykey( machineId);
			setKey(password);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			logger.error("Failed Machine encrypt :", e);
		}
		return null;
	}

	public  String decrypt(String strToDecrypt, String machineId) {
		try {
			
			String password = getMachineSecuritykey( machineId);
			setKey(password);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			logger.error("Failed Machine Descrypt :", e);
		}
		return null;
	}

	public  String decryptMachine(String strToDecrypt, String machineKey) {
		try {
			
			System.out.println(strToDecrypt+ " ====================== "+ machineKey);
			setKey(machineKey);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			logger.error("Failed Machine decryptMachine :", e);
		}
		return null;
	}

	public  String encryptMachine(String strToEncrypt, String machineKey) {
		try {
			setKey(machineKey);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			String wifiCode = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
			return wifiCode;
		} catch (Exception e) {
			
			logger.error("Failed Machine encryptMachine :", e);
		}
		return null;
	}
	
	public static void main(String[] args) {
	  

		// MachineId It should save in DB machine
		// By Machine Name fetch the Machine ID
		AESUtils aes = new AESUtils();
		 final String secretKey =  aes.getMachineSecuritykey("8782");
		String originalString = "SRTXVZ"; //
		String encryptedString = aes.encrypt(originalString, secretKey);
		String decryptedString = aes.decrypt(encryptedString, secretKey);

		System.out.println(originalString);
		System.out.println(encryptedString);
		System.out.println(decryptedString);

		//System.out.println("password :" + AESUtils.encrypt("zeroxm", secretKey));

	}

	public String getPrivateKey() {
		return "shri7ganeSHAWIFIz";
	}

	public  String getMachineSecuritykey(String machineId) {
		// replace with redis
		if (machineSecurityKeyMap.get(machineId) != null) {
			return machineSecurityKeyMap.get(machineId);
		} else {

			 WIFIMachine wifiMachine= getMachineKeyforGenerateSecret(machineId);
			String passwordToHash = generatePassword(wifiMachine.getMachineKey(), machineId);

			try {
				// Create MessageDigest instance for MD5
				MessageDigest md = MessageDigest.getInstance("MD5");
				// Add password bytes to digest
				md.update(passwordToHash.getBytes());
				// Get the hash's bytes
				byte[] bytes = md.digest();
				// This bytes[] has bytes in decimal format;
				// Convert it to hexadecimal format
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < bytes.length; i++) {
					sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
				}
				// Get complete hashed password in hex format
				machineSecurityKeyMap.put(machineId, sb.toString());
				return sb.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	private WIFIMachine getMachineKeyforGenerateSecret(String machineId) {
		// TODO Auto-generated method stub
	     if (machineKeyMap.get(machineId) != null) {
			return machineKeyMap.get(machineId);
		} else {
			//make call to DB
			// add to redis
			WIFIMachine wifiMachine = machineService.getMachineSecurityInfo(machineId);
			machineKeyMap.put(machineId, wifiMachine);
			return wifiMachine;
		}

	}

	// password generator
	private String generatePassword(String machineKey, String machineID) {
		return machineKey.concat(String.valueOf(Integer.parseInt(machineID, 17)));
		// return machinekey.concat(String.valueOf(Integer.parseInt(machineId, 17)));
	}
}