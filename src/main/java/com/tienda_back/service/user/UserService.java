package com.tienda_back.service.user;


import com.tienda_back.model.dto.consume.ConsumeJsonLogin;
import com.tienda_back.model.dto.consume.ConsumeJsonString;
import com.tienda_back.model.dto.response.ResponseJsonLogin;
import com.tienda_back.model.dto.response.ResponseJsonString;

public interface UserService {
    ResponseJsonString bcrypt(ConsumeJsonString consume);

    String bcrypt(String password);

    ResponseJsonLogin login(ConsumeJsonLogin consume);
}
