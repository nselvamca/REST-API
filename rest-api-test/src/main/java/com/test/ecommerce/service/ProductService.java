package com.test.ecommerce.service;

import com.test.ecommerce.exception.ProductException;
import com.test.ecommerce.model.products.Products;

public interface ProductService {
	
	
	/**
	   * This method is used to get list of Products
	   * @param labelType parameter to get Products by Label Type
	   * @return Products This returns Product object contains list of Products
	   */
	Products getAllProducts(String labelType) throws ProductException;

}
