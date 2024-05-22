package com.if2210.app.model;

import java.util.Map;
import java.util.HashMap;
import java.lang.Exception;

public class ShopModel {
    private Map<ProductCardModel, Integer> productList;
    private static ShopModel instance;

    // Constructor
    private ShopModel() {
        this.productList = new HashMap<ProductCardModel, Integer>();
        // for initiate, we must add product to product list
    }

    // GetInstance function
    public static synchronized ShopModel getInstance() {
        if (instance == null) {
            instance = new ShopModel();
        }
        return instance;
    }

    // setter for product list
    public void setProductList(Map<ProductCardModel, Integer> productList) {
        this.productList = productList;
    }

    // getter for productList
    public Map<ProductCardModel, Integer> getProductList() {
        return this.productList;
    }

    // method for adding product to productlist
    public void addProduct(ProductCardModel product) throws Exception {
        if (this.productList.containsKey(product)) {
            this.productList.put(product, this.productList.get(product) + 1);
        } else {
            this.productList.put(product, 0);
        }

    }

    // method for getting product from productlist and decrease the stock
    public void decreaseProduct(ProductCardModel product) throws Exception {
        if (productList.containsKey(product)) {
            int stock = productList.get(product);
            productList.put(product, stock - 1);
        } else {
            System.out.println("Product not found.");
        }
    }
}
