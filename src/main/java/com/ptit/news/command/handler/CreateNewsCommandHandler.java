package com.ptit.news.command.handler;

import com.ptit.news.command.dto.CreateNewsCommand;
import com.ptit.news.command.dto.CreateUserCommand;
import com.ptit.news.common.Response;
import com.ptit.news.common.enums.UserRole;
import com.ptit.news.dto.NewsResponse;
import com.ptit.news.entity.Category;
import com.ptit.news.entity.News;
import com.ptit.news.entity.Role;
import com.ptit.news.entity.User;
import com.ptit.news.repository.CategoryRepository;
import com.ptit.news.repository.NewsRepository;
import com.ptit.news.repository.RoleRepository;
import com.ptit.news.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateNewsCommandHandler {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RoleRepository roleRepository;

    @CommandHandler
    public Response<NewsResponse> handle(CreateNewsCommand command) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByEmail(authentication.getName()).orElse(null);

            // test
//            User user = userRepository.findByEmail("writer@gmail.com").orElse(null);

            if (user == null) {
                return Response.Error("Không tìm thấy authencation");
            }
//            Role writerRole = roleRepository.findByName("WRITER").orElse(null);
//            if (!user.getRoles().contains(writerRole)){
//                throw new AccessDeniedException("không có quyền đăng bài");
//            }
            Category category = categoryRepository.findById(command.getCategoryId()).orElse(null);
            if (category == null) {
                return Response.Error("Chủ đề không tồn tại");
            }
            if (command.getTitle() == null) {
                return Response.Error("Bài viết không có tiêu đề");
            }
//            if (command.getSummary() == null) {
//                return Response.Error("Bài viết không có tóm tắt");
//            }
            if (command.getContent() == null) {
                return Response.Error("Bài viết không có nội dung");
            }
            if (command.getImage() == null) {
                command.setImage("https://ik.imagekit.io/dx1lgwjws/News/NewsDefault.png");
            }
            News news = News.builder()
                    .summary(command.getSummary())
                    .author(user)
                    .category(category)
                    .title(command.getTitle())
                    .status(false)
                    .image(command.getImage())
                    .views(0)
                    .content(command.getContent())
                    .build();
            newsRepository.save(news);
            return Response.Success(new NewsResponse(news), "Tạo bài đăng thành công");

        } catch (Exception e) {
            log.error("Error create news: {}", e.getMessage(), e);
            return Response.Error("Không thể tạo bài đăng");
        }
    }
}
