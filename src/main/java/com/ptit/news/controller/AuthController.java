
package com.ptit.news.controller;

import org.springframework.web.bind.annotation.*;
import com.ptit.news.command.dto.CreateUserCommand;
import com.ptit.news.command.dto.SignInCommand;
import com.ptit.news.command.dto.UpdateUserCommand;
import com.ptit.news.command.dto.AuthResponse;
import com.ptit.news.query.dto.GetUserByEmailQuery;
import com.ptit.news.common.Response;
import com.ptit.news.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController extends BaseController {

    @PostMapping("api/auth/register")
    public Object register(@RequestBody CreateUserCommand command) {
        return executeCommand(command);
    }

    @PostMapping("api/auth/sign-in")
    public Response<AuthResponse> signIn(@RequestBody SignInCommand command) {
        return executeCommand(command);
    }

    @GetMapping("api/auth/user/{email}")
    public Response<User> getUserByEmail(@PathVariable String email) {
        GetUserByEmailQuery query = GetUserByEmailQuery.builder()
                .email(email)
                .build();
        return executeQuery(query);
    }

    @PutMapping("api/auth/user")
    public Response<User> updateUser(@RequestBody UpdateUserCommand command) {
        return executeCommand(command);
    }
}
