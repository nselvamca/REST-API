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
    SHOWPERDSCOUNT("ShowPercDscount"),
    FOLDERNFILENAME("data/products.json"),
    INPUTJSONURL("https://jl-nonprod-syst.apigee.net/v1/categories/600001506/products?key=2ALHCAAs6ikGRBoy6eTHA58RaG097Fma");
	
	
	private String vlaue;

	ProductsEnumConstants(String vlaue) {
        this.vlaue = vlaue;
    }

    public String vlaue() {
        return vlaue;
    }

}
