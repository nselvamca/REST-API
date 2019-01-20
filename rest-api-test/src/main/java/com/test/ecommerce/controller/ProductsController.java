package com.test.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.ecommerce.constant.ProductsEnumConstants;
import com.test.ecommerce.exception.ErrorResponse;
import com.test.ecommerce.exception.ProductException;
import com.test.ecommerce.model.products.Products;
import com.test.ecommerce.service.ProductService;



@RestController
@RequestMapping(value = "/v1/products")
public class ProductsController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductsController.class);
	
	@Autowired
	ProductService productService;
	
	@GetMapping(value = "/allproducts")
	 public  ResponseEntity<Products> getAllProducts(@RequestParam(value = "labelType", required = false) 
	 						String labelType) throws ProductException{
		
		
       if ((labelType != null) && !(labelType.equals(ProductsEnumConstants.SHOWWASNOW.vlaue()) || labelType.equals(ProductsEnumConstants.SHOWWASTHENNOW.vlaue())
    		   || labelType.equals(ProductsEnumConstants.SHOWPERDSCOUNT.vlaue()))) {
    	   throw new ProductException("Invalid Input");
       }
       
		return new ResponseEntity<>(productService.getAllProducts(labelType) , HttpStatus.OK);
         
	 }
	
	@ExceptionHandler(ProductException.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.OK);
	}
	

}
