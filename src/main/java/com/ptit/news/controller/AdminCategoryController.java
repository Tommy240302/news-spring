package com.ptit.news.controller;

import com.ptit.news.common.Response;
import com.ptit.news.dto.CategoryCreateUpdateDTO;
import com.ptit.news.dto.CategoryDTO;
import com.ptit.news.service.AdminCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminCategoryController extends AdvancedBaseController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public ResponseEntity<Response<CategoryDTO>> createCategory(@Valid @RequestBody CategoryCreateUpdateDTO createDto) {
        log.info("Creating new category with content: {}", createDto.getContent());
        CategoryDTO createdCategory = adminCategoryService.createCategory(createDto);
        return success(createdCategory, "Category created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Response<List<CategoryDTO>>> getAllCategories() {
        log.info("Fetching all categories.");
        List<CategoryDTO> categories = adminCategoryService.getAllCategories();
        return success(categories, "Categories fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        log.info("Fetching category with ID: {}", id);
        CategoryDTO category = adminCategoryService.getCategoryById(id);
        return success(category, "Category fetched successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<CategoryDTO>> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryCreateUpdateDTO updateDto) {
        log.info("Updating category with ID: {} and new content: {}", id, updateDto.getContent());
        CategoryDTO updatedCategory = adminCategoryService.updateCategory(id, updateDto);
        return success(updatedCategory, "Category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteCategory(@PathVariable Long id) {
        log.info("Deleting category with ID: {}", id);
        adminCategoryService.deleteCategory(id);
        // Trả về 204 No Content cho thao tác xóa thành công không cần body
        return success(null, "Category soft-deleted successfully", HttpStatus.NO_CONTENT);
    }

    // Endpoint MỚI để khôi phục thể loại đã xóa mềm
    @PutMapping("/{id}/restore")
    public ResponseEntity<Response<CategoryDTO>> restoreCategory(@PathVariable Long id) {
        log.info("Restoring category with ID: {}", id);
        CategoryDTO restoredCategory = adminCategoryService.restoreCategory(id);
        return success(restoredCategory, "Category restored successfully", HttpStatus.OK);
    }
}
