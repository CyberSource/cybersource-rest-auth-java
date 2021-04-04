package com.cybersource.apisdk.model;

public class PaymentInformation {
	private Card card;

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public PaymentInformation(Card card) {
		this.card = card;
	}

}
