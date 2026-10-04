package com.first.test.controller;

import java.util.UUID;

public class Demo {
 public static void main(String[] args) {
	float test = -1.35f;
	System.out.println(" The Value :"+ test);
	test = Math.abs(test);
	System.out.println(" The 2nd Value :"+ test);
	
	
	String token = UUID.randomUUID().toString();
	token = token.replaceAll("-", "");
	System.out.println(" Secured "+ token);
	
	String mobileRegEx =  ".*[^0-9].*";
	
	
	String t = "BgnyMg5TuiFLttmDtJjp5Ny1wH8UdYVXXpKiWRjDR3VA8xaG+dybOIm8Cb4f3co1qrrxC03ephpT28+C9+gGEqioJK+/Hxb7zfxQ9ZXrcR1+6SYRTJyMly0eJxPWrwYm";
	//System.out.println(AESUtils.decrypt(t, "56343"));

}
}
