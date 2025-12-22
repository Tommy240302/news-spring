package com.ptit.news.command.handler;

import com.google.type.Date;
import com.ptit.news.command.dto.CountViewCommand;
import com.ptit.news.common.Response;
import com.ptit.news.entity.News;
import com.ptit.news.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Slf4j
@Component
public class CountViewCommandHandler {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private final String IPV4_PATTERN =
            "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private final Pattern pattern = Pattern.compile(IPV4_PATTERN);

    @Transactional
    @CommandHandler
    public Response<String> handle(CountViewCommand command) {
        try {

            News news = newsRepository.findById(command.getNewsId()).orElse(null);
            if (news==null) {
                return Response.Error("News invalid");
            }
            if (!pattern.matcher(command.getIpV4()).matches()){
                return Response.Error("IP invalid");
            }

            String checkValid = (String) redisTemplate.opsForValue().get(command.getIpV4()+"-"+command.getNewsId());
            if (checkValid!=null) {
                return Response.Error("View invalid");
            }
            String now = LocalDateTime.now().toString();
            redisTemplate.opsForValue().set(command.getIpV4()+"-"+command.getNewsId(),"Valid at"+now, Duration.ofMinutes(1));
            news.setViews(news.getViews()+1);
            newsRepository.save(news);
            return Response.Success("View Valid","View đã được tăng");
        } catch (Exception e) {
            log.error("Error View count: {}", e.getMessage(), e);
            return Response.Error("SERVER ERROR" );
        }
    }
}
