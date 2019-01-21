package com.test.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.ecommerce.constant.ProductsEnumConstants;
import com.test.ecommerce.exception.ProductException;
import com.test.ecommerce.model.products.ColorSwatches;
import com.test.ecommerce.model.products.Product;
import com.test.ecommerce.model.products.Products;
import com.test.ecommerce.util.EcommerceUtil;


@Service("productService")
public class ProductServiceImpl implements ProductService{
	
	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Resource(name = "rgbColor")
	private Map<String, String> rgbColorProperties;

	
	@Override
	public Products getAllProducts(String labelType) throws ProductException {
		
		Products products = null;
		List<Product> productsList = new ArrayList<Product>();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode productsrootNode;		
		
		try {			
			
			/* This code will read Products JSON data from the file*/
			//productsrootNode = mapper.readTree(EcommerceUtil.readDataFromFile()).get(ProductsEnumConstants.PRODUCTS.vlaue());
			
			/* This below commented code - get Real Time Data from given URL using RestTemplate */
			productsrootNode = mapper.readTree(EcommerceUtil.getDataByRestTempate().getBody()).get("products"); 
			
			if (productsrootNode.isArray()) {
				
				productsrootNode.forEach((JsonNode productsNode) ->{
					
					Product product = new Product();
					JsonNode wasNode = productsNode.get(ProductsEnumConstants.PRICE.vlaue()).get(ProductsEnumConstants.WAS.vlaue());
					
					/* This if condition used to filter products with a price reduction 
					 * (Using Was Price)                                 
					 */
					if(wasNode.textValue() != null && wasNode.toString().length() > 2) {
					
					String priceLabelNowPrice = null;
					String priceLabelWasPrice = getNodeTextValByTwoFieldParam(productsNode, ProductsEnumConstants.PRICE.vlaue(), ProductsEnumConstants.WAS.vlaue());
					priceLabelWasPrice = convertPriceDecToInt(priceLabelWasPrice); /* This line will calculate Price Reduction */
					
					/* This if condition will assign 'Now' Price from 'To' Price element (if the Now Element has From & To Price)*/				
					if(productsNode.get(ProductsEnumConstants.PRICE.vlaue()).get(ProductsEnumConstants.NOW.vlaue()).hasNonNull(ProductsEnumConstants.TO.vlaue())) {
						
						String nowPrice = getNodeTextValByThreeFieldParam(productsNode, ProductsEnumConstants.PRICE.vlaue(), ProductsEnumConstants.NOW.vlaue(), 
										ProductsEnumConstants.TO.vlaue());
						nowPrice = convertPriceDecToInt(nowPrice);
						product.setNowPrice("£"+nowPrice);
						priceLabelNowPrice = nowPrice;

					    } else {
					    	String nowPrice = getNodeTextValByTwoFieldParam(productsNode, ProductsEnumConstants.PRICE.vlaue(), ProductsEnumConstants.NOW.vlaue());
							nowPrice = convertPriceDecToInt(nowPrice);
						    product.setNowPrice("£"+nowPrice);
						    priceLabelNowPrice = nowPrice;
					    }
					
					/* This line will calculate Price Reduction */
					product.setPriceReduction(Integer.parseInt(priceLabelWasPrice) - Integer.parseInt(priceLabelNowPrice));
					String wasNowPrice = "";
					
					/* This line will use to get Then Now Price */
					 wasNowPrice = getLabelByType(labelType, productsNode, priceLabelNowPrice, priceLabelWasPrice);
					product.setPriceLabel(wasNowPrice);
					
					}else {
						
						return;
					}
					
					/* This condition will check whether ColotSwatces element is an array of JSON Node */
					if(productsNode.get(ProductsEnumConstants.COLORSWATCHES.vlaue()).isArray()) {
						
						List<ColorSwatches> colorSwatchesList = new ArrayList<ColorSwatches>();
						
						productsNode.get(ProductsEnumConstants.COLORSWATCHES.vlaue()).forEach((JsonNode colorNode) ->{

							ColorSwatches colorSwatches = new ColorSwatches();
							colorSwatches.setColor(getNodeTextValByOneFieldParam(colorNode, ProductsEnumConstants.COLOR.vlaue()));
							/* This condition get RGB Color from Property file */
							colorSwatches.setRgbColor(rgbColorProperties.get(getNodeTextValByOneFieldParam(colorNode, ProductsEnumConstants.BASICCOLOR.vlaue())));
							colorSwatches.setSkuid(getNodeTextValByOneFieldParam(colorNode, ProductsEnumConstants.SKUID.vlaue()));
							colorSwatchesList.add(colorSwatches);
						});
						
						product.setColorSwatches(colorSwatchesList);						
					}
			    	
			    	product.setProductId(getNodeTextValByOneFieldParam( productsNode, ProductsEnumConstants.PRODUCTID.vlaue()));
			    	product.setTitle(getNodeTextValByOneFieldParam(productsNode, ProductsEnumConstants.TITLE.vlaue()));
			    	productsList.add(product); 
					
				});
				
			}
			
			
			products = new Products();
			
			/* This Line of code used to sorted to show the highest price reduction first 
			 * 
			 */
			products.setProducts(productsList.stream().sorted((o1, o2)->o2.getPriceReduction().
	                compareTo(o1.getPriceReduction())).
	                collect(Collectors.toList()));
			
		} catch (Exception e) {
			log.error("Error : ", e);
			throw new ProductException("Internal Server Error");
		}
		
		return products;
	}
	
	
	
	/**
	   * This method is used to filter LableType element by given condition
	   * @param labelType parameter to filter Label Type
	   * @param productsNode is a JsonNode of Products
	   * @param priceLabelNowPrice to pass Now Price element value
	   * @param priceLabelWasPrice to pass Was Price element Value
	   * @return Sting of LableType price details
	   */
	private String getLabelByType(String labelType, JsonNode productsNode, String priceLabelNowPrice,
			String priceLabelWasPrice) throws NumberFormatException {
		
		String thenPrice;
		String wasNowPrice = "";
		String wasConcatCurrency = ProductsEnumConstants.WAS.vlaue() + " " + ProductsEnumConstants.POUND.vlaue();
		String nowConcatCurrency = ProductsEnumConstants.NOW.vlaue() + " " + ProductsEnumConstants.POUND.vlaue();
				
		if (labelType != null && labelType.equals(ProductsEnumConstants.SHOWWASTHENNOW.vlaue())) {
			
			String thenConcatCurrency = ProductsEnumConstants.THEN.vlaue() + " " + ProductsEnumConstants.POUND.vlaue();

			thenPrice = (getNodeTextValByTwoFieldParam(productsNode, ProductsEnumConstants.PRICE.vlaue(), ProductsEnumConstants.THEN2.vlaue()).length() > 0)
					?  ", "  + thenConcatCurrency + convertPriceDecToInt(getNodeTextValByTwoFieldParam(productsNode, ProductsEnumConstants.PRICE.vlaue(), ProductsEnumConstants.THEN2.vlaue()))

					: (getNodeTextValByTwoFieldParam(productsNode, ProductsEnumConstants.PRICE.vlaue(), ProductsEnumConstants.THEN1.vlaue()).length() > 0) ? ", "  + thenConcatCurrency 
							+ convertPriceDecToInt(getNodeTextValByTwoFieldParam(productsNode, ProductsEnumConstants.PRICE.vlaue(), ProductsEnumConstants.THEN1.vlaue())) : "";

			wasNowPrice = wasConcatCurrency + priceLabelWasPrice + thenPrice + ", "+ nowConcatCurrency + priceLabelNowPrice;

		} else if (labelType != null && labelType.equals(ProductsEnumConstants.SHOWPERDSCOUNT.vlaue())) {

			wasNowPrice = "30% Off - "+ nowConcatCurrency + priceLabelNowPrice;

		} else {

			wasNowPrice = wasConcatCurrency + priceLabelWasPrice + ", "+ nowConcatCurrency + priceLabelNowPrice;
		}
		return wasNowPrice;
	}
	
	/**
	   * This method is used to Convert Decimal Value to Integer (Eg : 150.00 to 150)
	   * @param Price - Decimal Price
	   * @return Sting as Integer Format
	   */
	private String convertPriceDecToInt(String price) throws NumberFormatException {
		int nowPriceInInt = (int) Double.parseDouble(price);
		price	= (nowPriceInInt > 10) ? ""+nowPriceInInt : price;
		return price;
	}
	
	private String getNodeTextValByThreeFieldParam(JsonNode productsNode, String fieldNameOne, String filedNameTwo, String filedNameThree) {
		return productsNode.get(fieldNameOne).get(filedNameTwo).get(filedNameThree).textValue();
	}

	private String getNodeTextValByTwoFieldParam(JsonNode productsNode, String fieldNameOne, String filedNameTwo) {
		return productsNode.get(fieldNameOne).get(filedNameTwo).textValue();
	}
	
	private String getNodeTextValByOneFieldParam(JsonNode productsNode, String fieldNameOne) {
		return productsNode.get(fieldNameOne).textValue();
	}

}

