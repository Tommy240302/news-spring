
package com.ptit.news.controller;

import com.ptit.news.common.Response;
import com.ptit.news.dto.CategoryResponse;
import com.ptit.news.dto.NewsResponse;
import com.ptit.news.query.dto.GetAllCategoryQuery;
import com.ptit.news.query.dto.GetAllPostQuery;
import lombok.extern.slf4j.Slf4j;
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
public class PublicController extends AdvancedBaseController {

    @GetMapping("/categories")
    public Response<List<CategoryResponse>> getAllCategory() {
        List<CategoryResponse> categories = queryGateway.query(
                new GetAllCategoryQuery(),
                org.axonframework.messaging.responsetypes.ResponseTypes.multipleInstancesOf(CategoryResponse.class)
        ).join();
        return Response.Success(categories, "Lấy danh sách chuyên mục thành công.");
    }

}
