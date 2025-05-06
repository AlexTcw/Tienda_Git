package com.tienda_back.model.dto.response;

public record ResponseJsonLogin(long userId,String username, String jwtToken) {
}
