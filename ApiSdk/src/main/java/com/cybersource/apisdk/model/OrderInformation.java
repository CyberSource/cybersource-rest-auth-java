package com.cybersource.apisdk.model;

public class OrderInformation {

	private BillTo billTo;
	private AmountDetails amountDetails;

	public OrderInformation(BillTo billTo, AmountDetails amountDetails) {
		this.billTo = billTo;
		this.amountDetails = amountDetails;
	}

}
