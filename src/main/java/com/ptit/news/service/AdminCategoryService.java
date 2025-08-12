package com.ptit.news.service;

import com.ptit.news.dto.CategoryCreateUpdateDTO;
import com.ptit.news.dto.CategoryDTO;
import com.ptit.news.entity.Category;
import com.ptit.news.exception.ResourceNotFoundException;
import com.ptit.news.exception.ValidationException;
import com.ptit.news.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDTO createCategory(CategoryCreateUpdateDTO createDto) {
        // Kiểm tra xem content đã tồn tại chưa (không phân biệt hoa thường)
        // Sử dụng existsByContentAndIsDeletedFalse để kiểm tra sự tồn tại của category đang hoạt động
        if (categoryRepository.existsByContentAndIsDeletedFalse(createDto.getContent())) {
            throw new ValidationException("Category with content '" + createDto.getContent() + "' already exists and is active.");
        }

        Category category = new Category();
        category.setContent(createDto.getContent());
        category.setIsDeleted(false); // Đảm bảo trạng thái ban đầu là không xóa

        if (createDto.getParentId() != null) {
            // Đảm bảo category cha tồn tại và chưa bị xóa mềm
            Category parentCategory = categoryRepository.findByIdAndIsDeletedFalse(createDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found or is deleted with ID: " + createDto.getParentId()));
            category.setParent(parentCategory);
        }

        Category savedCategory = categoryRepository.save(category);
        return CategoryDTO.fromEntity(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        // Lấy tất cả các category chưa bị xóa, sắp xếp theo content
        List<Category> categories = categoryRepository.findAllByIsDeletedFalseOrderByContentAsc();

        // Xây dựng cây phân cấp từ danh sách phẳng
        // Lọc ra các category gốc (parent là null)
        List<Category> rootCategories = categories.stream()
                .filter(category -> category.getParent() == null)
                .collect(Collectors.toList());

        // Chuyển đổi thành DTO và bao gồm các category con
        return rootCategories.stream()
                .map(CategoryDTO::fromEntityWithChildren)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        return CategoryDTO.fromEntityWithChildren(category);
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryCreateUpdateDTO updateDto) {
        Category existingCategory = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // Kiểm tra xem content mới có bị trùng với category khác (ngoại trừ chính nó)
        // và category đó đang hoạt động
        if (!existingCategory.getContent().equalsIgnoreCase(updateDto.getContent()) &&
                categoryRepository.existsByContentAndIsDeletedFalse(updateDto.getContent())) {
            throw new ValidationException("Another active category with content '" + updateDto.getContent() + "' already exists.");
        }

        existingCategory.setContent(updateDto.getContent());

        // Cập nhật parent category
        if (updateDto.getParentId() != null) {
            // Không cho phép category tự làm parent của chính nó
            if (updateDto.getParentId().equals(id)) {
                throw new ValidationException("Category cannot be its own parent.");
            }
            // Đảm bảo category cha mới tồn tại và chưa bị xóa mềm
            Category newParent = categoryRepository.findByIdAndIsDeletedFalse(updateDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found or is deleted with ID: " + updateDto.getParentId()));
            existingCategory.setParent(newParent);
        } else {
            // Nếu parentId là null, đặt category này thành category gốc
            existingCategory.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        return CategoryDTO.fromEntity(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category categoryToDelete = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // Kiểm tra xem có category con ĐANG HOẠT ĐỘNG nào không
        // (Logic này đã được bao gồm trong `markCategoryAndChildrenAsDeleted` nếu nó chỉ xóa mềm các con đang hoạt động)
        // Tuy nhiên, nếu bạn muốn ngăn xóa mềm cha khi có con đang hoạt động, bạn cần kiểm tra trước khi gọi đệ quy.
        // Dựa trên `markCategoryAndChildrenAsDeleted` của bạn, nó sẽ xóa mềm tất cả các con.
        // Nếu bạn muốn ngăn xóa mềm cha khi có con đang hoạt động, hãy thêm logic sau:
        boolean hasActiveChildren = categoryToDelete.getChildren().stream()
                .anyMatch(child -> !child.getIsDeleted());
        if (hasActiveChildren) {
            throw new ValidationException("Cannot soft-delete category with active children categories. Please delete children first.");
        }
        // Thực hiện soft delete: đánh dấu category và tất cả các category con của nó là isDeleted = true
        markCategoryAndChildrenAsDeleted(categoryToDelete);
    }

    // Helper method để đánh dấu category và tất cả các category con là đã xóa
    private void markCategoryAndChildrenAsDeleted(Category category) {
        if (category == null || category.getIsDeleted()) {
            return;
        }
        category.setIsDeleted(true);
        categoryRepository.save(category);

        // Duyệt qua các category con để xóa mềm chúng
        if (category.getChildren() != null) {
            for (Category child : category.getChildren()) {
                markCategoryAndChildrenAsDeleted(child);
            }
        }
    }

    @Transactional
    public CategoryDTO restoreCategory(Long id) {
        // Tìm category đã bị xóa mềm
        Category categoryToRestore = categoryRepository.findByIdAndIsDeletedTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found or is not soft-deleted with ID: " + id));

        // Kiểm tra nếu category cha của nó đã bị xóa mềm, thì không cho khôi phục
        if (categoryToRestore.getParent() != null && categoryToRestore.getParent().getIsDeleted()) {
            throw new ValidationException("Cannot restore category because its parent category is deleted. Please restore the parent first.");
        }

        categoryToRestore.setIsDeleted(false); // Khôi phục
        Category restoredCategory = categoryRepository.save(categoryToRestore);
        return CategoryDTO.fromEntity(restoredCategory);
    }
}
