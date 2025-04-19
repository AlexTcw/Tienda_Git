package com.tienda_back.model.dto.consume;

import java.util.List;

public record ConsumeJsonProduct(long productID,
                                 String name,
                                 String sku,
                                 String description,
                                 double price,
                                 List<Long> categories,
                                 String brand,
                                 int stock) {
}
