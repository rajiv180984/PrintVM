package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class PaymentDetails  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8979827284257725809L;
	
	private Customer customerData;
	private WalletModel walletData;
	private OrderDetails orderData;
	private TransactionDetails transactionData;

	public Customer getCustomerData() {
		return customerData;
	}

	public void setCustomerData(Customer customerData) {
		this.customerData = customerData;
	}

	public WalletModel getWalletData() {
		return walletData;
	}

	public void setWalletData(WalletModel walletData) {
		this.walletData = walletData;
	}

	public OrderDetails getOrderData() {
		return orderData;
	}

	public void setOrderData(OrderDetails orderData) {
		this.orderData = orderData;
	}

	public TransactionDetails getTransactionData() {
		return transactionData;
	}

	public void setTransactionData(TransactionDetails transactionData) {
		this.transactionData = transactionData;
	}

}
