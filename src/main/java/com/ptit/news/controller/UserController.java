package com.ptit.news.controller;

import com.ptit.news.command.dto.CreateRequestAuthorCommand;
import com.ptit.news.dto.RequestAuthorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.ptit.news.command.dto.AddCommentCommand;
import com.ptit.news.dto.CommentResponse;
import org.springframework.web.bind.annotation.*;
import com.ptit.news.query.dto.GetUserByIdQuery;
import com.ptit.news.dto.UserResponse;
import com.ptit.news.common.Response;
import com.ptit.news.query.dto.GetUserByEmailQuery;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping("/addComment")
    public Response<CommentResponse> addComment(@RequestBody AddCommentCommand command) {
        return executeCommand(command);
    }

    @PostMapping("/request-author")
    public ResponseEntity<Response<RequestAuthorResponse>> requestAuthor(@RequestBody CreateRequestAuthorCommand command) {
        return executeCommandWithCustomStatus(command, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public Response<UserResponse> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        GetUserByEmailQuery query = GetUserByEmailQuery.builder().email(email).build();
        return executeQuery(query, UserResponse.class);
    }
}