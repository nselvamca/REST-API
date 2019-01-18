package com.test.ecommerce.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.test.ecommerce.controller.ProductsController;

public class EcommerceUtil {
	
	public static ResponseEntity<String> getDataByRestTempate() {
		
		RestTemplate restTemplate = new RestTemplate();
        
		String resourceUrl
		  = "";
		ResponseEntity<String> responseEntity
		  = restTemplate.getForEntity(resourceUrl, String.class);
		
		
		return responseEntity;
	}
	
	
	public static String readDataFromFile() {
		
		String content = null;
		String fileName = "data/products.json";
        ClassLoader classLoader = new ProductsController().getClass().getClassLoader();
 
        File file = new File(classLoader.getResource(fileName).getFile());
         
        
		try {
			content = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return content;
	}

}
