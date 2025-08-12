package com.ptit.news.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ptit.news.command.dto.CreateUserCommand; // Có thể bỏ import này nếu không dùng nữa
import com.ptit.news.command.dto.UpdateUserCommand; // Có thể bỏ import này nếu không dùng nữa
import com.ptit.news.query.dto.GetUserByIdQuery; // Có thể bỏ import này nếu không dùng nữa
import com.ptit.news.common.Response;
import com.ptit.news.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminController extends AdvancedBaseController {

    // Các phương thức khác của AdminController (nếu có)
    // Ví dụ: các endpoint Dashboard, Statistics, Category Management nếu bạn đặt ở đây
    // ...
}