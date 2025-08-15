package com.ptit.news.query.handler;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.axonframework.queryhandling.QueryHandler;
import com.ptit.news.query.dto.GetUserByIdQuery;
import com.ptit.news.entity.User;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.common.Response;
import com.ptit.news.exception.ResourceNotFoundException;
import com.ptit.news.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GetUserByIdQueryHandler {

    @Autowired
    private UserRepository userRepository;

    @QueryHandler
    public Response<User> handle(GetUserByIdQuery query) {
        try {
            User user = userRepository.findById(query.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", query.getId()));

            return Response.Success(user, "Lấy thông tin user thành công");

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting user by id: {}", e.getMessage(), e);
            throw new InvalidRequestException("Failed to get user");
        }
    }
}