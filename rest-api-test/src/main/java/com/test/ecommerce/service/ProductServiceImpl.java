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
import com.test.ecommerce.exception.ProductException;
import com.test.ecommerce.products.model.ColorSwatches;
import com.test.ecommerce.products.model.Product;
import com.test.ecommerce.products.model.Products;
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
		JsonNode root;		
		
		try {			
			
			
			root = mapper.readTree(EcommerceUtil.readDataFromFile()).get("products");
			
			//root = mapper.readTree(EcommerceUtil.getDataByRestTempate().getBody()).get("products");
			
			if (root.isArray()) {
				
				root.forEach((JsonNode objNode) ->{
					
					Product product = new Product();
					
					JsonNode wasNode = objNode.get("price").findValue("was");
					
					if(wasNode.textValue() != null && wasNode.toString().length() > 2) {
					
					
					String priceLabelNowPrice = null;
					String priceLabelWasPrice = objNode.get("price").get("was").textValue();
					priceLabelWasPrice = convertPriceDecToInt(priceLabelWasPrice);
					
					Integer wasValue = Integer.parseInt(priceLabelWasPrice);
					product.setPriceReduction(wasValue);
					
					if(objNode.get("price").get("now").hasNonNull("to")) {
						
						String nowPrice = objNode.get("price").get("now").get("to").textValue();
						nowPrice = convertPriceDecToInt(nowPrice);
						product.setNowPrice("£"+nowPrice);
						
						priceLabelNowPrice = nowPrice;

					    } else {
					    	
					    	String nowPrice = objNode.get("price").get("now").textValue();
							nowPrice = convertPriceDecToInt(nowPrice);
						    product.setNowPrice("£"+nowPrice);
						    priceLabelNowPrice = nowPrice;

					    }
					
					
					product.setPriceReduction(Integer.parseInt(priceLabelWasPrice) - Integer.parseInt(priceLabelNowPrice));
					
					String wasNowPrice = "";
					
					 wasNowPrice = getLabelByType(labelType, objNode, priceLabelNowPrice, priceLabelWasPrice);
					product.setPriceLabel(wasNowPrice);
					
					
					}else {
						
						return;
					}
					

					if(objNode.get("colorSwatches").isArray()) {
						
						
						List<ColorSwatches> colorSwatchesList = new ArrayList<ColorSwatches>();
						
						objNode.get("colorSwatches").forEach((JsonNode colorNode) ->{
							
							ColorSwatches colorSwatches = new ColorSwatches();
							colorSwatches.setColor(colorNode.get("color").textValue());
							colorSwatches.setRgbColor(rgbColorProperties.get(colorNode.get("basicColor").textValue()));
							colorSwatches.setSkuid(colorNode.get("skuId").textValue());
							
							
							colorSwatchesList.add(colorSwatches);
						
						});
						
						product.setColorSwatches(colorSwatchesList);
						
					}
			    	
					
			    	product.setProductId(objNode.get("productId").textValue());
			    	product.setTitle(objNode.get("title").textValue());
			    	productsList.add(product); 
					
				});
				
			}
			
			
			products = new Products();
			
			products.setProducts(productsList.stream().sorted((o1, o2)->o2.getPriceReduction().
	                compareTo(o1.getPriceReduction())).
	                collect(Collectors.toList()));
			
		} catch (Exception e) {
			log.error("Error : ", e);
			throw new ProductException("Internal Server Error");
		}
		
		
		return products;
	}

	private String getLabelByType(String labelType, JsonNode objNode, String priceLabelNowPrice,
			String priceLabelWasPrice) throws NumberFormatException {
		String thenPrice;
		String wasNowPrice = "";
		if(labelType != null && labelType.equals("ShowWasThenNow")) {
		
		
		 thenPrice	= (objNode.get("price").get("then2").textValue().length() > 0) ? ", Then £" + convertPriceDecToInt(objNode.get("price").get("then2").textValue())  
							
				: (objNode.get("price").get("then1").textValue().length() > 0) ? ", Then £" + convertPriceDecToInt(objNode.get("price").get("then1").textValue()) : "";
		
			wasNowPrice =	"Was £" + priceLabelWasPrice + thenPrice + ", Now £" + priceLabelNowPrice;
		 
		 } else if(labelType != null && labelType.equals("ShowPercDscount")) {
			
			wasNowPrice = "30% Off - Now £"+priceLabelNowPrice;
					
		} else {
			
			wasNowPrice = "Was £" + priceLabelWasPrice + ", Now £" + priceLabelNowPrice;
		}
		return wasNowPrice;
	}

	private String convertPriceDecToInt(String price) throws NumberFormatException {
		int nowPriceInInt = (int) Double.parseDouble(price);
		price	= (nowPriceInInt > 10) ? ""+nowPriceInInt : price;
		return price;
	}

}

