package com.tienda_back.model.dto.response;

import com.tienda_back.model.entity.Products.Product;

import java.util.List;

public record ResponseJsonProducts(List<ResponseJsonProduct> responseJsonProducts) {
}
