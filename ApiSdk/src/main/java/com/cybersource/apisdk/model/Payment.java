package com.cybersource.apisdk.model;

public class Payment {

	private ClientReferenceInformation clientReferenceInformation;
	private ProcessingInformation processingInformation;
	private AggregatorInformation aggregatorInformation;
	private OrderInformation orderInformation;
	private PaymentInformation paymentInformation;
	
	public ClientReferenceInformation getClientReferenceInformation() {
		return clientReferenceInformation;
	}

	public void setClientReferenceInformation(ClientReferenceInformation clientReferenceInformation) {
		this.clientReferenceInformation = clientReferenceInformation;
	}

	public ProcessingInformation getProcessingInformation() {
		return processingInformation;
	}

	public void setProcessingInformation(ProcessingInformation processingInformation) {
		this.processingInformation = processingInformation;
	}

	public AggregatorInformation getAggregatorInformation() {
		return aggregatorInformation;
	}

	public void setAggregatorInformation(AggregatorInformation aggregatorInformation) {
		this.aggregatorInformation = aggregatorInformation;
	}

	public OrderInformation getOrderInformation() {
		return orderInformation;
	}

	public void setOrderInformation(OrderInformation orderInformation) {
		this.orderInformation = orderInformation;
	}

	public PaymentInformation getPaymentInformation() {
		return paymentInformation;
	}

	public void setPaymentInformation(PaymentInformation paymentInformation) {
		this.paymentInformation = paymentInformation;
	}

	public Payment(ClientReferenceInformation clientReferenceInformation, ProcessingInformation processingInformation,
			AggregatorInformation aggregatorInformation, OrderInformation orderInformation,
			PaymentInformation paymentInformation) {

		this.clientReferenceInformation = clientReferenceInformation;
		this.processingInformation = processingInformation;
		this.aggregatorInformation = aggregatorInformation;
		this.orderInformation = orderInformation;
		this.paymentInformation = paymentInformation;
	}

}
