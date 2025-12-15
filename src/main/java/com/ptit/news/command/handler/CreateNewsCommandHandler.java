package com.ptit.news.command.handler;

import com.ptit.news.command.dto.CreateNewsCommand;
import com.ptit.news.common.Response;
import com.ptit.news.common.enums.DataType;
import com.ptit.news.dto.NewsResponse;
import com.ptit.news.entity.Category;
import com.ptit.news.entity.News;
import com.ptit.news.entity.NewsCategory;
import com.ptit.news.entity.User;
import com.ptit.news.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @CommandHandler
    public Response<NewsResponse> handle(CreateNewsCommand command) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                return Response.Error("Không tìm thấy thông tin người dùng đang đăng nhập");
            }

            Category category = categoryRepository.findById(command.getCategoryId()).orElse(null);
            if (category == null) {
                return Response.Error("Chủ đề không tồn tại");
            }
            if (command.getTitle() == null) {
                return Response.Error("Bài viết không có tiêu đề");
            }
            if (command.getContent() == null) {
                return Response.Error("Bài viết không có nội dung");
            }
            if (command.getImage() == null) {
                command.setImage("https://ik.imagekit.io/dx1lgwjws/News/NewsDefault.png");
            }

            News news = News.builder()
                    .summary(command.getSummary())
                    .author(user)
                    .title(command.getTitle())
                    .status(false)
                    .isDeleted(false)
                    .image(command.getImage())
                    .views(0)
                    .content(command.getContent())
                    .build();

            String categoryDetectUrl = "http://localhost:8000/category_detect";
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("content", command.getContent());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(categoryDetectUrl, entity, Map.class);
            Map<String, Object> body = response.getBody();
            String categoriLabel = (String) body.get("detect_label");
            System.out.println(categoriLabel);
            Category categoryDetect = categoryRepository.findByContent(categoriLabel).orElse(null);
            //Human
            NewsCategory newsCategoryHM = NewsCategory.builder()
                    .dataType(DataType.HUMAN)
                    .category(category)
                    .selected(false)
                    .news(news)
                    .build();
            news.addCategory(newsCategoryHM);

            //AI
            NewsCategory newsCategoryAI = NewsCategory.builder()
                    .dataType(DataType.AI)
                    .category(categoryDetect)
                    .selected(false)
                    .news(news)
                    .build();
            news.addCategory(newsCategoryAI);
            news = newsRepository.save(news);

            return Response.Success(new NewsResponse(news), "Tạo bài đăng thành công");

        } catch (Exception e) {
            log.error("Error create news: {}", e.getMessage(), e);
            return Response.Error("Không thể tạo bài đăng");
        }
    }

}
