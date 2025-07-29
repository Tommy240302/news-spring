package com.ptit.news.command.handler;

import com.ptit.news.command.dto.RequestAuthorCommand;
import com.ptit.news.common.Response;
import com.ptit.news.common.enums.RequestAuthorStatus;
import com.ptit.news.dto.RequestAuthorResponse;
import com.ptit.news.entity.RequestAuthor;
import com.ptit.news.entity.User;
import com.ptit.news.repository.RequestAuthorRepository;
import com.ptit.news.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RequestAuthorCommandHandler {

    @Autowired
    private RequestAuthorRepository requestAuthorRepository;

    @Autowired
    private UserRepository userRepository;

    @CommandHandler
    public Response<RequestAuthorResponse> handler(RequestAuthorCommand command) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByEmail(authentication.getName()).orElse(null);

            if (user == null) {
                return Response.Error("Không tìm thấy authencation");
            }
            Optional<RequestAuthor> requestAuthorEx = requestAuthorRepository.findByUserAndStatusIn(user, List.of(RequestAuthorStatus.PENDING, RequestAuthorStatus.ACCEPTED));
            if (requestAuthorEx.isPresent()) {
                if (requestAuthorEx.get().getStatus().equals(RequestAuthorStatus.PENDING))
                {
                    return Response.Error("Yêu cầu của bạn đang chờ duyệt vui lòng không tạo thêm yêu cầu mới");
                }
                return Response.Error("Bạn đã là tác giả");
            }
            RequestAuthor requestAuthor = RequestAuthor
                    .builder()
                    .user(user)
                    .reason(command.getReason())
                    .sampleArticles(command.getSampleArticles())
                    .profileUrl(command.getProfileUrl())
                    .status(RequestAuthorStatus.PENDING)
                    .paymentNumber(command.getPaymentNumber())
                    .build();

            requestAuthorRepository.save(requestAuthor);
            return Response.Success(new RequestAuthorResponse(requestAuthor), "Yêu cầu thành công hãy đợi phê duyệt");
        } catch (Exception e) {
            log.error("Error create news: {}", e.getMessage(), e);
            return Response.Error("không thể tạo yêu cầu");
        }
    }
}
