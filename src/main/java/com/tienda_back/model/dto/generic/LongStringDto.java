package com.tienda_back.model.dto.generic;

import java.io.Serializable;

public record LongStringDto(Long id, String name) implements Serializable {
}
