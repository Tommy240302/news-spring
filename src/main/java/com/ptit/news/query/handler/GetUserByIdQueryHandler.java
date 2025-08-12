package com.ptit.news.query.handler;

import org.springframework.stereotype.Component;
import org.axonframework.queryhandling.QueryHandler;
import com.ptit.news.query.dto.GetUserByIdQuery;
import com.ptit.news.dto.UserResponse;
import com.ptit.news.entity.User;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.exception.ResourceNotFoundException;

@Component
public class GetUserByIdQueryHandler {

    private final UserRepository userRepository;

    public GetUserByIdQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @QueryHandler
public UserResponse handle(GetUserByIdQuery query) {
    User user = userRepository.findById(query.getId())
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", query.getId()));
    return new UserResponse(user);
}

}
