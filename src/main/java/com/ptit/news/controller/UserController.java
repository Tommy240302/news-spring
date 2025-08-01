package com.ptit.news.controller;

import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
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

    private final QueryGateway queryGateway;

    @GetMapping("/{id}")
    public Response<UserResponse> getUserById(@PathVariable Long id) {
        GetUserByIdQuery query = GetUserByIdQuery.builder()
                .id(id)
                .build();
        return executeQuery(query, UserResponse.class); // Dùng hàm type-safe
    }

}
