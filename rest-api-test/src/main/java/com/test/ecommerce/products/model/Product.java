package com.test.ecommerce.products.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Product {
	
	
	
	private String productId;
	private String title;
	private String nowPrice;
	@JsonIgnore
	private Integer priceReduction;
	private String priceLabel;
	private List<ColorSwatches> colorSwatches;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}
	public String getPriceLabel() {
		return priceLabel;
	}
	public void setPriceLabel(String priceLabel) {
		this.priceLabel = priceLabel;
	}
	public Integer getPriceReduction() {
		return priceReduction;
	}
	public void setPriceReduction(Integer priceReduction) {
		this.priceReduction = priceReduction;
	}
	public List<ColorSwatches> getColorSwatches() {
		return colorSwatches;
	}
	public void setColorSwatches(List<ColorSwatches> colorSwatches) {
		this.colorSwatches = colorSwatches;
	}
	
	
	
	

}
