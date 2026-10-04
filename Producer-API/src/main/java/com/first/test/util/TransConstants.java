package com.first.test.util;

public class TransConstants {

	public enum TransactionStatus{
		created,
		authorized,
		captured,
		refunded,
		FAILED,
		SUCCESS
	}
}
