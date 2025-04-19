package com.tienda_back.service.Products;

import com.tienda_back.model.dto.generic.LongStringDto;
import com.tienda_back.model.dto.response.ResponseJsonGeneric;
import com.tienda_back.model.dto.response.ResponseJsonProduct;
import com.tienda_back.model.dto.response.ResponseJsonProducts;
import com.tienda_back.model.dto.response.ResponseJsonSet;
import com.tienda_back.model.entity.Products.Category;
import com.tienda_back.model.exception.ResourceNotFoundException;
import com.tienda_back.repository.Product.CategoryProductRepository;
import com.tienda_back.repository.Product.CategoryRepository;
import com.tienda_back.repository.Product.InventoryRepository;
import com.tienda_back.repository.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public ResponseJsonProducts findAllProducts() {
        List<String[]> products = productRepository.findAllProducts();

        return getResponseJsonProducts(products);
    }

    @Override
    public ResponseJsonProduct findProductById(Long id) {
        List<String[]> productList = productRepository.findProductById(id);

        if (productList.isEmpty()) {
            throw new ResourceNotFoundException("Product with id:  "+id+" not found");
        }

        String[] product = productList.getFirst();

        return new ResponseJsonProduct(
                Integer.parseInt(product[0]),    // product_id
                product[1],                      // product_name
                product[2],                      // product_sku
                product[3],                      // product_description
                Double.parseDouble(product[4]),  // product_price
                product[5],                      // category_name
                product[6],                      // product_brand
                Integer.parseInt(product[7])     // inventory_stock
        );
    }

    @Override
    public ResponseJsonSet findAllCategories() {
        List<Category> categories = categoryRepository.findAllCategories();
        Set<LongStringDto> result = categories.stream()
                .map(c -> new LongStringDto(c.getCategoryId(), c.getName()))
                .collect(Collectors.toSet());
        return new ResponseJsonSet(Collections.singleton(result));
    }

    @Override
    public ResponseJsonProducts findProductsByCategoryId(Long categoryId){
        List<String[]> products = productRepository.findProductsByCategoryId(categoryId);
        if (products.isEmpty()) {throw new ResourceNotFoundException("Product with id:  "+categoryId+" not found");}
        return getResponseJsonProducts(products);
    }

    @Override
    public ResponseJsonProducts findProductsBySku(String sku){
        List<String[]> products = productRepository.findProductsByProductSku(sku);
        if (products.isEmpty()) {throw new ResourceNotFoundException("Product with sku:  "+sku+" not found");}
        return getResponseJsonProducts(products);
    }

    @Override
    public ResponseJsonSet findAllBrands() {
        return new ResponseJsonSet(new HashSet<>(productRepository.findAllProductBrand()));
    }

    @Override
    public ResponseJsonProducts findProductsByBrandName(String brandName) {
        List<String[]> products = productRepository.findProductsByBrandName(brandName);
        if (products.isEmpty()) {throw new ResourceNotFoundException("Product with brand:  "+brandName+" not found");}
        return getResponseJsonProducts(products);
    }

    @Override
    public ResponseJsonProducts findProductsOutOfStock() {
        List<String[]> products = productRepository.findProductsOutOfStock();
        if (products.isEmpty()) {throw new ResourceNotFoundException("No products out of stock");}
        return getResponseJsonProducts(products);
    }

    @Override
    public ResponseJsonGeneric findProductsByKeyword(String keyword, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<String[]> productsByKeyword = productRepository.findProductsByKeyWord(keyword, pageable);
        if (productsByKeyword.isEmpty()) {throw new ResourceNotFoundException("No products found with keyword:  "+keyword);}
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("products", productsByKeyword.getContent());
        data.put("page", page);
        data.put("size", size);
        data.put("total", productsByKeyword.getTotalElements());
        return new ResponseJsonGeneric(data);
    }


    private ResponseJsonProducts getResponseJsonProducts(List<String[]> products) {
        List<ResponseJsonProduct> responseJsonProducts = products.stream()
                .map(product -> new ResponseJsonProduct(
                        Integer.parseInt(product[0]),//product_id
                        product[1],//product_name
                        product[2],//product_sku
                        product[3],//product_description
                        Double.parseDouble(product[4]),//product_price
                        product[5],//category_name
                        product[6],//product_brand
                        Integer.parseInt(product[7])//inventory_stock
                ))
                .collect(Collectors.toList());
        return new ResponseJsonProducts(responseJsonProducts);
    }

}
