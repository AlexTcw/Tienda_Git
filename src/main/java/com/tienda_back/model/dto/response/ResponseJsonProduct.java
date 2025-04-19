package com.tienda_back.model.dto.response;

import java.util.List;

public record ResponseJsonProduct(long productID,
                                  String name,
                                  String sku,
                                  String description,
                                  double price,
                                  String brand,
                                  int stock,
                                  List<String> categories) {
}
