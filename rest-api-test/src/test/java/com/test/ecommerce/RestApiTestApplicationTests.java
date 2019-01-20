package com.test.ecommerce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.test.ecommerce.model.products.Product;
import com.test.ecommerce.model.products.Products;
import com.test.ecommerce.service.ProductService;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class RestApiTestApplicationTests extends AbstractTest{
	
	@Autowired
	MockMvc mockmvc;
	
	@Autowired
	ProductService productService;

	@Test
	public void contextLoads() {
		
		
	try {
		
		
		MvcResult mvcResult = mockmvc.perform(
					MockMvcRequestBuilders.get("/v1/products/allproducts")
						.accept(MediaType.APPLICATION_JSON)
					).andReturn();
		
		System.out.println(mvcResult.getResponse().getContentAsString());
		
		int status = mvcResult.getResponse().getStatus();
		   assertEquals(200, status);
		 String content = mvcResult.getResponse().getContentAsString();
		   Products productlist = super.mapFromJson(content, Products.class);
		  assertTrue(productlist.getProducts().size() > 0);
		  System.out.println("Content Size "+productlist.getProducts().size());
		  
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	}

}

