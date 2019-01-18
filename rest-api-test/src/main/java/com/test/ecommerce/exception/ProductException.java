package com.test.ecommerce.exception;

public class ProductException extends Exception{
	
	private String errorMessage;
	 
	public String getErrorMessage() {
		return errorMessage;
	}
	public ProductException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	public ProductException() {
		super();
	}

}
