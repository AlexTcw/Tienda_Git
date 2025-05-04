package com.tienda_back.Controller;

import com.tienda_back.model.dto.consume.ConsumeJsonLogin;
import com.tienda_back.model.dto.consume.ConsumeJsonString;
import com.tienda_back.model.dto.response.ResponseJsonLogin;
import com.tienda_back.model.dto.response.ResponseJsonString;
import com.tienda_back.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/auth"})
public class AuthController {

    private final UserService userService;

    @PostMapping(value = {"/login"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonLogin> login(@RequestBody ConsumeJsonLogin consume) {
        return ResponseEntity.ok(userService.login(consume));
    }

    @PostMapping(value = {"/bcrypt"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonString> bcrypt(@RequestBody ConsumeJsonString consume) {
        return ResponseEntity.ok(userService.bcrypt(consume));
    }
}
