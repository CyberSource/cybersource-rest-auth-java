package com.cybersource.apisdk.model;

public class OrderInformation {
	private BillTo billTo;
	private AmountDetails amountDetails;

	public OrderInformation(BillTo billTo, AmountDetails amountDetails) {
		this.billTo = billTo;
		this.amountDetails = amountDetails;
	}

	public BillTo getBillTo() {
		return billTo;
	}

	public void setBillTo(BillTo billTo) {
		this.billTo = billTo;
	}

	public AmountDetails getAmountDetails() {
		return amountDetails;
	}

	public void setAmountDetails(AmountDetails amountDetails) {
		this.amountDetails = amountDetails;
	}

}
