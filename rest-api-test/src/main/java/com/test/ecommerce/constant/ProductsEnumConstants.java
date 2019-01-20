package com.test.ecommerce.constant;

public enum ProductsEnumConstants {
	
	POUND("£"),
	PRODUCTS("products"),
	PRODUCTID("productId"),
	TITLE("title"),
    PRICE("price"),
    WAS("was"),
    NOW("now"),
    TO("to"),
    THEN("then"),
    THEN1("then1"),
    THEN2("then2"),
    COLORSWATCHES("colorSwatches"),
    COLOR("color"),
    BASICCOLOR("basicColor"),
    SKUID("skuId"),
    SHOWWASNOW("ShowWasNow"),
    SHOWWASTHENNOW("ShowWasThenNow"),
    SHOWPERDSCOUNT("ShowPercDscount");
	
	
	private String vlaue;

	ProductsEnumConstants(String vlaue) {
        this.vlaue = vlaue;
    }

    public String vlaue() {
        return vlaue;
    }

}
