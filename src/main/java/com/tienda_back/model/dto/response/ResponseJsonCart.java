package com.tienda_back.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ResponseJsonCart(long cartId,
                               List<ResponseJsonCartProduct> products,
                               LocalDateTime cartDate,
                               BigDecimal amount,
                               int totalItems,
                               String status
                               ) {
}
