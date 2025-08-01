package com.ptit.news.controller;

import org.springframework.web.bind.annotation.*;
import com.ptit.news.query.dto.GetUserByIdQuery;
import com.ptit.news.dto.UserResponse;
import com.ptit.news.common.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
public class UserController extends BaseController {

    @GetMapping("/{id}")
    public Response<UserResponse> getUserById(@PathVariable Long id) {
        GetUserByIdQuery query = GetUserByIdQuery.builder().id(id).build();
        return executeQuery(query, UserResponse.class);
    }
}
