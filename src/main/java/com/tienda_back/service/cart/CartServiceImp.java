package com.tienda_back.service.cart;

import com.tienda_back.model.dto.consume.ConsumeJsonCart;
import com.tienda_back.model.dto.consume.ConsumeJsonProductCart;
import com.tienda_back.model.dto.consume.ConsumeJsonProducts;
import com.tienda_back.model.dto.generic.LongIntDto;
import com.tienda_back.model.dto.response.ResponseJsonCart;
import com.tienda_back.model.dto.response.ResponseJsonCartProduct;
import com.tienda_back.model.dto.response.ResponseJsonCarts;
import com.tienda_back.model.dto.response.ResponseJsonString;
import com.tienda_back.model.entity.Products.Product;
import com.tienda_back.model.entity.Users.User;
import com.tienda_back.model.entity.cart.Cart;
import com.tienda_back.model.entity.cart.CartProduct;
import com.tienda_back.model.enums.CartStatusEnum;
import com.tienda_back.repository.Product.ProductRepository;
import com.tienda_back.repository.Users.UserRepository;
import com.tienda_back.repository.shop.CartProductRepository;
import com.tienda_back.repository.shop.CartRepository;
import com.tienda_back.service.Products.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ResponseJsonString sellProducts(ConsumeJsonProducts consume) {
        consume.products().forEach(product -> {
            reduceStock(product.value1(),product.value2());
        });
        return new ResponseJsonString("success");
    }

    private void registerPurchase(Long id, int quantity) {

    }

    private void reduceStock(Long id, int quantity) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product does not exist");
        }

        Product product = productRepository.findProductByProductId(id);

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Sold out of stock for product " + product.getProductId());
        }
        if (product.getStock() == 0) {
            throw new IllegalArgumentException("product with id " + product.getProductId() +" need a refill");
        }

        product.setStock(product.getStock() - quantity);
    }

    @Override
    public ResponseJsonCarts findCartsByCartStatus(String status){
        try {
            CartStatusEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status is not valid");
        }

        List<ResponseJsonCart> response = new ArrayList<>();
        cartRepository.getCartByCartStatus(status).forEach(cart -> {
            response.add(getResponseJsonCart(cart));
        });
        return new ResponseJsonCarts(response);
    }

    @Override
    public ResponseJsonCarts findCartsByUserId(long id){
        List<ResponseJsonCart> response = new ArrayList<>();
        cartRepository.getCartByUserId(id).forEach(cart -> {
            response.add(getResponseJsonCart(cart));
        });
        return new ResponseJsonCarts(response);
    }

    @Override
    public ResponseJsonCart findCartById(Long id) {
        if (!cartRepository.existsCartByCartId(id)){
            throw new IllegalArgumentException("Cart does not exist");
        }

        List<Object []> carts = cartRepository.getCartByCartId(id);
        Optional<Object[]> optionalCart = carts.stream().findFirst();
        if (optionalCart.isPresent()){
            return getResponseJsonCart(optionalCart.get());
        }


        throw new RuntimeException("something went wrong");
    }

    @Override
    @Transactional
    public ResponseJsonCart createCart(ConsumeJsonCart consume){

        if (userRepository.countUserByUserId(consume.userId()) <= 0){
            throw new IllegalArgumentException("User does not exist");
        }
        User user = userRepository.findUserByUserId(consume.userId());

        //primero creamos el carrito
        Cart cart = new Cart();
        List<LongIntDto> products = consume.products();
        cart.setUser(user);
        cart.setCartDate(LocalDateTime.now());
        cart.setCartStatus(CartStatusEnum.PENDIENTE);
        cart.setPayment(null);
        var savedCart = cartRepository.save(cart);

        // guardamos la relacion entre los 2
        saveCartProduct(products, savedCart);

        return findCartById(savedCart.getCartId());
    }

    @Override
    public ResponseJsonString cancelCartByCartId(Long id) {
        Cart cart = cartRepository.findCartByCartId(id);
        cart.setCartStatus(CartStatusEnum.CANCELADO);
        cartRepository.save(cart);
        return new ResponseJsonString("success");
    }

    @Override
    @Transactional
    public ResponseJsonCart updateCart(ConsumeJsonProductCart consume){
        if (!cartRepository.existsCartByCartId(consume.cartId())){
            throw new IllegalArgumentException("cart does not exist");
        }
        Cart cart = cartRepository.findCartByCartId(consume.cartId());
        cartProductRepository.deleteCartProductsByCart(cart);
        saveCartProduct(consume.products(), cart);
        return findCartById(consume.cartId());
    }

    @Override
    public ResponseJsonCarts findCartsByKeyword(String keyword, LocalDateTime startDate, LocalDateTime endDate) {
        // Convertir LocalDateTime a String (asegurarte de usar el formato adecuado)
        String startDateStr = (startDate != null) ? startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        String endDateStr = (endDate != null) ? endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;

        // Llamar al repositorio con los parámetros correctos
        List<ResponseJsonCart> response = new ArrayList<>();
        cartRepository.findCartsByKeyword(keyword, startDateStr, endDateStr).forEach(cart -> {
            response.add(getResponseJsonCart(cart));
        });
        return new ResponseJsonCarts(response);
    }


    private void saveCartProduct(List<LongIntDto> products,Cart cart){
        products.forEach(product -> {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setCart(cart);
            cartProduct.setProduct(productRepository.findProductByProductId(product.value1()));
            cartProduct.setQuantity(product.value2());
            cartProductRepository.save(cartProduct);
        });
    }


    private ResponseJsonCart getResponseJsonCart(Object [] cart) {
        long cartId = ((Number) cart[0]).longValue();
        String idsString = (String) cart[1];
        idsString = idsString.replaceAll("[\\[\\]]", ""); // Elimina los corchetes

        List<Long> productIds = Arrays.stream(idsString.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();
        Timestamp timestamp = (Timestamp) cart[2];
        LocalDateTime cartTime = timestamp.toLocalDateTime();
        int totalItems = productIds.size();
        List<ResponseJsonCartProduct> cartProducts = getCartProductsByProductId(cartId);
        BigDecimal amount = calculateAmount(cartId);
        String status = cart[3].toString();
        return new ResponseJsonCart(cartId,cartProducts,cartTime,amount,totalItems,status);
    }

    private BigDecimal calculateAmount(Long id) {
        final double[] amountCount = {0};

        productRepository.findProductsByCartId(id).forEach(product -> {
            double price = (double) product[3];
            int quantity = (int)product[5];
            double totalPrice = price * quantity;
            amountCount[0] += totalPrice;
        });

        return BigDecimal.valueOf(amountCount[0]);
    }

    private BigDecimal calculateAmount(List<LongIntDto> products) {
        final double[] amountCount = {0};
        products.forEach(product -> {
            double price = productRepository.findProductByProductId(product.value1()).getPrice();
            int quantity = product.value2();
            double totalPrice = price * quantity;
            amountCount[0] += totalPrice;
        });
        return BigDecimal.valueOf(amountCount[0]);
    }

    public List<ResponseJsonCartProduct> getCartProductsByProductId(Long id) {
        List<ResponseJsonCartProduct> cartProducts = new ArrayList<>();
        productRepository.findProductsByCartId(id).forEach(product -> {
            cartProducts.add(
                    new ResponseJsonCartProduct(
                            ((Long) product[0]).intValue(),
                            product[1].toString(),
                            product[2].toString(),
                            (double)product[3],
                            product[4].toString(),
                            (Integer)product[5]
                    )
            );
        });
        return cartProducts;
    }
}
