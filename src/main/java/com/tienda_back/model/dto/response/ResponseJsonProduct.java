package com.tienda_back.model.dto.response;

public record ResponseJsonProduct(long productID,
                                  String name, String sku,
                                  String description, Double price, String category,
                                  String brand, int stock) {
}
