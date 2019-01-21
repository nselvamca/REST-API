package com.test.ecommerce.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.test.ecommerce.constant.ProductsEnumConstants;
import com.test.ecommerce.controller.ProductsController;
import com.test.ecommerce.exception.ProductException;

public class EcommerceUtil {
	
	private static final Logger log = LoggerFactory.getLogger(EcommerceUtil.class);
	
	public static ResponseEntity<String> getDataByRestTempate() throws ProductException {
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = null;
		String resourceUrl
		  = ProductsEnumConstants.INPUTJSONURL.vlaue();
		
		try {
		 responseEntity = restTemplate.getForEntity(resourceUrl, String.class);
		
		} catch (Exception e) {
			log.error("Error : ", e);
			throw new ProductException("Internal Server Error");
		}
		
		return responseEntity;
	}
	
	
	public static String readDataFromFile() throws ProductException {
		
		
		String content = null;
		String fileName = ProductsEnumConstants.FOLDERNFILENAME.vlaue();
		
        ClassLoader classLoader = new ProductsController().getClass().getClassLoader();
 
        File file = new File(classLoader.getResource(fileName).getFile());
         
        
		try {
			content = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			log.error("Error : ", e);
			throw new ProductException("Internal Server Error");
		}
		
		
		return content;
	}

}
