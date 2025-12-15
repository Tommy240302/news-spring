package com.ptit.news.controller;

import com.ptit.news.common.Response;
import com.ptit.news.common.enums.RequestAuthorStatus;
import com.ptit.news.entity.RequestAuthor;
import com.ptit.news.entity.User;
import com.ptit.news.entity.Role;
import com.ptit.news.repository.RequestAuthorRepository;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.repository.RoleRepository;
import com.ptit.news.dto.RequestAuthorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional; // Thêm import này

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/author-requests")
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminAuthorRequestController { // Đã bỏ extends AdvancedBaseController

    @Autowired
    private RequestAuthorRepository requestAuthorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<Response<List<RequestAuthorResponse>>> getAllAuthorRequests() {
        try {
            List<RequestAuthor> requests = requestAuthorRepository.findAll();
            log.info("Found {} author requests in database.", requests.size());

            List<RequestAuthorResponse> responses = requests.stream()
                    .map(RequestAuthorResponse::new) // Sử dụng method reference
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Response.Success(responses, "Danh sách yêu cầu đã được tải."));

        } catch (Exception e) {
            log.error("Error fetching author requests: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Response.Error("Không thể tải danh sách yêu cầu."));
        }
    }

    @Transactional // Đảm bảo toàn bộ logic phê duyệt chạy trong một transaction
    @PutMapping("/{requestId}/approve")
    public ResponseEntity<Response<String>> approveAuthorRequest(@PathVariable Long requestId) {
        log.info("Processing approve request for ID: {}", requestId);
        try {
            Optional<RequestAuthor> optionalRequest = requestAuthorRepository.findById(requestId);
            if (optionalRequest.isEmpty()) {
                log.warn("Approve failed: Request with ID {} not found.", requestId);
                return ResponseEntity.badRequest().body(Response.Error("Không tìm thấy yêu cầu với ID: " + requestId));
            }

            RequestAuthor request = optionalRequest.get();
            if (!request.getStatus().equals(RequestAuthorStatus.PENDING)) {
                log.warn("Approve failed: Request ID {} is not in PENDING status. Current status: {}", requestId, request.getStatus());
                return ResponseEntity.badRequest().body(Response.Error("Yêu cầu đã được xử lý hoặc không ở trạng thái chờ duyệt."));
            }

            request.setStatus(RequestAuthorStatus.ACCEPTED);
            requestAuthorRepository.save(request);

            User user = request.getUser();
            if (user != null) {
                log.info("User associated with request {} found. Email: {}", requestId, user.getEmail());
                Optional<Role> authorRoleOptional = roleRepository.findByName("AUTHOR");
                if (authorRoleOptional.isPresent()) {
                    Role authorRole = authorRoleOptional.get();
                    user.getRoles().add(authorRole);
                    user.setPaymentNumber(request.getPaymentNumber());
                    userRepository.save(user);
                    log.info("User {} role updated to AUTHOR.", user.getEmail());
                } else {
                    log.error("Role 'AUTHOR' not found. Cannot assign role to user {}.", user.getEmail());
                    // Vẫn trả về thành công vì yêu cầu đã được phê duyệt, nhưng có thể cần xử lý khác
                    return ResponseEntity.internalServerError().body(Response.Error("Lỗi hệ thống: Không tìm thấy vai trò AUTHOR."));
                }
            } else {
                log.error("User associated with request ID {} not found. This indicates a data integrity issue.", requestId);
                // Bạn có thể trả về lỗi hoặc tiếp tục tùy thuộc vào business logic
                return ResponseEntity.badRequest().body(Response.Error("Không tìm thấy người dùng liên kết với yêu cầu."));
            }

            log.info("Request ID {} approved successfully.", requestId);
            return ResponseEntity.ok(Response.Success(null, "Yêu cầu đã được phê duyệt thành công."));
        } catch (Exception e) {
            log.error("Unexpected error approving request {}: {}", requestId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Response.Error("Không thể phê duyệt yêu cầu do lỗi không xác định."));
        }
    }

    @Transactional
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<Response<String>> rejectAuthorRequest(@PathVariable Long requestId) {
        log.info("Processing reject request for ID: {}", requestId);
        try {
            Optional<RequestAuthor> optionalRequest = requestAuthorRepository.findById(requestId);
            if (optionalRequest.isEmpty()) {
                log.warn("Reject failed: Request with ID {} not found.", requestId);
                return ResponseEntity.badRequest().body(Response.Error("Không tìm thấy yêu cầu với ID: " + requestId));
            }

            RequestAuthor request = optionalRequest.get();
            if (!request.getStatus().equals(RequestAuthorStatus.PENDING)) {
                log.warn("Reject failed: Request ID {} is not in PENDING status. Current status: {}", requestId, request.getStatus());
                return ResponseEntity.badRequest().body(Response.Error("Yêu cầu đã được xử lý hoặc không ở trạng thái chờ duyệt."));
            }

            request.setStatus(RequestAuthorStatus.DENIED);
            requestAuthorRepository.save(request);

            log.info("Request ID {} rejected successfully.", requestId);
            return ResponseEntity.ok(Response.Success(null, "Yêu cầu đã bị từ chối."));
        } catch (Exception e) {
            log.error("Unexpected error rejecting request {}: {}", requestId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Response.Error("Không thể từ chối yêu cầu do lỗi không xác định."));
        }
    }
}