package com.tienda_back.model.dto.response;

import java.util.List;

public record ResponseJsonCartProduct(long productID,
                                      String name,
                                      String sku,
                                      double price,
                                      String brand,
                                      int quantity) {
}
