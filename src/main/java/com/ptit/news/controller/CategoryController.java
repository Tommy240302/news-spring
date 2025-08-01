package com.ptit.news.controller;

import com.ptit.news.common.Response;
import com.ptit.news.dto.CategoryResponse;
import com.ptit.news.query.dto.GetAllCategoryQuery;
import lombok.RequiredArgsConstructor;
// ...existing code...
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
public class CategoryController extends AdvancedBaseController {

    // ...existing code...

    @GetMapping
    public Response<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = queryGateway.query(
                new GetAllCategoryQuery(),
                org.axonframework.messaging.responsetypes.ResponseTypes.multipleInstancesOf(CategoryResponse.class)
        ).join();
        return Response.Success(categories, "Lấy danh sách chuyên mục thành công.");
    }

}
