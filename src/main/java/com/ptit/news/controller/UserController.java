package com.ptit.news.controller;

import com.ptit.news.command.dto.CreateRequestAuthorCommand;
import com.ptit.news.dto.RequestAuthorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ptit.news.query.dto.GetUserByIdQuery;
import com.ptit.news.command.dto.UpdateUserCommand;
import com.ptit.news.common.Response;
import com.ptit.news.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController extends AdvancedBaseController {

    @GetMapping("/{id}")
    public Response<User> getUserById(@PathVariable Long id) {
        GetUserByIdQuery query = GetUserByIdQuery.builder()
                .id(id)
                .build();
        return executeQuery(query);
    }

    @PutMapping("/{id}")
    public Response<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserCommand command) {
        // Set the ID from path variable
        command.setId(id);
        return executeCommand(command);
    }

    @PostMapping("/request-author")
    public ResponseEntity<Response<RequestAuthorResponse>> requestAuthor(@RequestBody CreateRequestAuthorCommand command) {
        return executeCommandWithCustomStatus(command, HttpStatus.CREATED);
    }
}