package com.ptit.news.query.handler;

import com.ptit.news.common.Response;
import com.ptit.news.dto.CategoryResponse;
import com.ptit.news.exception.InvalidRequestException;
import com.ptit.news.exception.ResourceNotFoundException;
import com.ptit.news.query.dto.GetAllCategoryQuery;
import com.ptit.news.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllCategoryQueryHandler {

    @Autowired
    private CategoryRepository categoryRepository;

    @QueryHandler
    public Response<List<CategoryResponse>> handler(GetAllCategoryQuery query) {
        try {
            List<CategoryResponse> categoryResponses = categoryRepository.findAll().stream()
                    .map(CategoryResponse::new)
                    .toList();
            return Response.Success(categoryResponses, "Lấy dữ liệu thành công");
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting category: {}", e.getMessage(), e);
            throw new InvalidRequestException("Failed to get category");
        }
    }
}
