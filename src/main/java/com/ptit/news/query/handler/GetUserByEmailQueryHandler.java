package com.ptit.news.query.handler;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.axonframework.queryhandling.QueryHandler;
import com.ptit.news.query.dto.GetUserByEmailQuery;
import com.ptit.news.entity.User;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.dto.UserResponse;
import com.ptit.news.exception.ResourceNotFoundException;
import com.ptit.news.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GetUserByEmailQueryHandler {

    @Autowired
    private UserRepository userRepository;

    @QueryHandler
    public UserResponse handle(GetUserByEmailQuery query) {
        try {
            // Sửa lỗi: Thay findByEmail bằng findByEmailAndIsDeletedFalse
            User user = userRepository.findByEmailAndIsDeletedFalse(query.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", query.getEmail()));

            // Dùng constructor sẵn có để convert sang UserResponse
            return new UserResponse(user);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting user by email: {}", e.getMessage(), e);
            throw new InvalidRequestException("Failed to get user");
        }
    }
}
