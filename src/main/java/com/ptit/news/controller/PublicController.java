package com.ptit.news.controller;

import com.ptit.news.common.Response;
import com.ptit.news.dto.CategoryResponse;
import com.ptit.news.query.dto.GetAllCategoryQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PublicController extends BaseController {

    @GetMapping("/categories")
    public Response<List<CategoryResponse>> getAllCategory() {
        return executeQuery(new GetAllCategoryQuery());
    }

}
