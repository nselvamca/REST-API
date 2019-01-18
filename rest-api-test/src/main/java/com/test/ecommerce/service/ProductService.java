package com.test.ecommerce.service;

import com.test.ecommerce.exception.ProductException;
import com.test.ecommerce.products.model.Products;

public interface ProductService {
	
	Products getAllProducts(String labelType) throws ProductException;

}
