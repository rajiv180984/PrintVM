package com.first.test.util;

import java.util.Random;

public class RandomStringGenerator {

	private static char CAP_CHAR[][] = {
			{ 'A', 'B', 'D', 'E', 'C', 'F', 'G', 'J', 'K', 'H', 'I', 'L', 'M', 'O', 'N', 'P', 'S', 'R', 'T', 'Y', 'U',
					'V', 'Z', 'X', 'W', 'Q' },
			{ 'a', 'b', 'c', 'e', 'd', 'f', 'g', 'j', 'k', 'h', 'i', 'L', 'm', 'o', 'n', 'p', 's', 'r', 't', 'y', 'u',
					'v', 'z', 'x', 'w', 'q' } };
	private static char ALFANUMERIC[] = { 'R', '1', 'A', 'Z', '$', '@', '#', '0', '2', '7', '8', '5', '6', '4', '3',
			'9', 'U', '7' };

	public static String randomStringGenerator(int length) {
		Random randomRow = new Random(0);
		Random randomCol = new Random();
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < length / 3; i++) {
			builder.append(CAP_CHAR[randomRow.nextInt(2)][randomCol.nextInt(26)])
					.append(CAP_CHAR[randomRow.nextInt(2)][randomCol.nextInt(26)])
					.append(ALFANUMERIC[randomCol.nextInt(17)]);
		}

		return builder.toString();
	}

	public static void main(String[] args) {
		System.out.println(randomStringGenerator(6));
	}
}
