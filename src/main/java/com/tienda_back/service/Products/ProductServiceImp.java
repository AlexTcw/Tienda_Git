package com.tienda_back.service.Products;

import com.tienda_back.model.dto.consume.ConsumeJsonLong;
import com.tienda_back.model.dto.consume.ConsumeJsonProduct;
import com.tienda_back.model.dto.generic.LongStringDto;
import com.tienda_back.model.dto.response.*;
import com.tienda_back.model.entity.Products.Category;
import com.tienda_back.model.entity.Products.CategoryProduct;
import com.tienda_back.model.entity.Products.Product;
import com.tienda_back.model.exception.ResourceNotFoundException;
import com.tienda_back.repository.Product.CategoryProductRepository;
import com.tienda_back.repository.Product.CategoryRepository;
import com.tienda_back.repository.Product.ProductRepository;
import jakarta.transaction.Transactional;
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

    public static String generateSku(String brand, List<Category> categories) {
        List<String> categoriesNames = categories.stream().map(Category::getName).toList();

        String brandPart = brand.substring(0, Math.min(3, brand.length())).toUpperCase();

        // Tomamos las 3 primeras letras de hasta 2 categorías
        String categoryPart = categoriesNames.stream()
                .limit(2)
                .map(cat -> cat.substring(0, Math.min(3, cat.length())).toUpperCase())
                .collect(Collectors.joining());

        String randomPart = String.valueOf((int) (Math.random() * 9000) + 1000);

        return brandPart + "-" + categoryPart + "-" + randomPart;
    }

    @Override
    public ResponseJsonProducts findAllProducts() {
        List<String[]> products = productRepository.findAllProducts();

        return getResponseJsonProducts(products);
    }

    @Override
    public ResponseJsonProduct findProductById(Long id) {
        List<String[]> productList = productRepository.findProductById(id);

        if (productList.isEmpty()) {
            throw new ResourceNotFoundException("Product with id:  " + id + " not found");
        }

        String[] product = productList.getFirst();

        return new ResponseJsonProduct(
                Integer.parseInt(product[0]),//product_id
                product[1],//product_name
                product[2],//product_sku
                product[3],//product_description
                Double.parseDouble(product[4]),//product_price
                product[5],//product_brand
                Integer.parseInt(product[6]),//stock
                Arrays.asList(product[7].split(", "))//categories
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
    public ResponseJsonProducts findProductsByCategoryId(Long categoryId) {
        List<String[]> products = productRepository.findProductsByCategoryId(categoryId);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product with id:  " + categoryId + " not found");
        }
        return getResponseJsonProducts(products);
    }

    @Override
    public ResponseJsonProducts findProductsBySku(String sku) {
        List<String[]> products = productRepository.findProductsByProductSku(sku);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product with sku:  " + sku + " not found");
        }
        return getResponseJsonProducts(products);
    }

    @Override
    public ResponseJsonSet findAllBrands() {
        return new ResponseJsonSet(new HashSet<>(productRepository.findAllProductBrand()));
    }

    @Override
    public ResponseJsonProducts findProductsByBrandName(String brandName) {
        List<String[]> products = productRepository.findProductsByBrandName(brandName);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product with brand:  " + brandName + " not found");
        }
        return getResponseJsonProducts(products);
    }

    @Override
    public ResponseJsonProducts findProductsOutOfStock() {
        List<String[]> products = productRepository.findProductsOutOfStock();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products out of stock");
        }
        return getResponseJsonProducts(products);
    }

    @Override
    public ResponseJsonGeneric findProductsByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<String[]> productsByKeyword = productRepository.findProductsByKeyWordPage(keyword, pageable);
        if (productsByKeyword.isEmpty()) {
            throw new ResourceNotFoundException("No products found with keyword:  " + keyword);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("products", productsByKeyword.getContent());
        data.put("page", page);
        data.put("size", size);
        data.put("total", productsByKeyword.getTotalElements());
        return new ResponseJsonGeneric(data);
    }

    @Override
    public ResponseJsonProducts findProductsByKeyWord(String keyword) {
        List<String []> products = productRepository.findProductsByKeyWord(keyword);

        return getResponseJsonProducts(products);
    }

    @Override
    public ResponseJsonProduct createProduct(ConsumeJsonProduct consumeJsonProduct) {
        List<Category> categories = consumeJsonProduct.categories().stream()
                .map(categoryId -> {
                    if (!categoryRepository.existsCategoryByCategoryId(categoryId)) {
                        throw new ResourceNotFoundException("Category with ID " + categoryId + " not found");
                    }
                    return categoryRepository.findCategoryByCategoryId(categoryId);
                })
                .collect(Collectors.toList());


        Product product = productRepository.save(Product.builder()
                .name(consumeJsonProduct.name())
                .brand(consumeJsonProduct.brand())
                .price(consumeJsonProduct.price())
                .sku(generateSku(consumeJsonProduct.brand(), categories))
                .description(consumeJsonProduct.description())
                .stock(consumeJsonProduct.stock())
                .build());

        categoryProductRepository.saveAll(getCategoryProducts(consumeJsonProduct, product));

        return findProductById(product.getProductId());
    }

    @Override
    public ResponseJsonProduct updateProduct(ConsumeJsonProduct consumeJsonProduct) {
        if (consumeJsonProduct.productID() == 0) {
            throw new ResourceNotFoundException("You must provide a product id");
        }

        if (!productRepository.existsProductByProductId(consumeJsonProduct.productID())) {
            throw new ResourceNotFoundException("Product with id: " + consumeJsonProduct.productID() + " not found");
        }

        // Obtener el producto existente
        Product product = productRepository.findProductByProductId(consumeJsonProduct.productID());
        System.out.println("Product to update: " + product.getName());

        // Actualizar los valores solo si no son nulos en consumeJsonProduct
        Optional.ofNullable(consumeJsonProduct.name()).ifPresent(product::setName);
        Optional.ofNullable(consumeJsonProduct.brand()).ifPresent(product::setBrand);
        Optional.ofNullable(consumeJsonProduct.description()).ifPresent(product::setDescription);
        Optional.of(consumeJsonProduct.price()).ifPresent(product::setPrice);
        Optional.of(consumeJsonProduct.stock()).ifPresent(product::setStock);

        // Guardar el producto actualizado en el repositorio
        productRepository.save(product);

        // Devolver el producto actualizado
        return findProductById(product.getProductId());
    }

    @Override
    @Transactional
    public ResponseJsonString deleteProductById(ConsumeJsonLong consume) {
        if (consume == null) {
            throw new ResourceNotFoundException("You must provide a product id");
        }
        if (consume.value() == null || consume.value() == 0) {
            throw new ResourceNotFoundException("You must provide a product id");
        }
        if (!productRepository.existsProductByProductId(consume.value())) {
            throw new ResourceNotFoundException("Product with id: " + consume.value() + " not found");
        }

        Product product = productRepository.findProductByProductId(consume.value());

        categoryProductRepository.deleteCategoryProductByProduct(product);
        productRepository.delete(product);

        return new ResponseJsonString("Product with id: " + consume.value() + " deleted successfully");
    }

    private List<CategoryProduct> getCategoryProducts(ConsumeJsonProduct consumeJsonProduct, Product product) {
        List<Category> categories = new ArrayList<>();
        List<Long> categoriesIds = consumeJsonProduct.categories();
        categoriesIds.forEach(id -> {
            Category cat = categoryRepository.findCategoryByCategoryId(id);
            categories.add(cat);
        });

        //update categories
        List<CategoryProduct> categoryProducts = new ArrayList<>();
        categories.forEach(category -> {
            CategoryProduct categoryProduct = new CategoryProduct();
            categoryProduct.setCategory(category);
            categoryProduct.setProduct(product);
            categoryProducts.add(categoryProduct);
        });
        return categoryProducts;
    }

    private ResponseJsonProducts getResponseJsonProducts(List<String[]> products) {

        List<ResponseJsonProduct> responseJsonProducts = products.stream()
                .map(product -> new ResponseJsonProduct(
                        Integer.parseInt(product[0]),//product_id
                        product[1],//product_name
                        product[2],//product_sku
                        product[3],//product_description
                        Double.parseDouble(product[4]),//product_price
                        product[5],//product_brand
                        Integer.parseInt(product[6]),//stock
                        Arrays.asList(product[7].split(", "))//categories
                ))
                .collect(Collectors.toList());
        return new ResponseJsonProducts(responseJsonProducts);
    }

}
