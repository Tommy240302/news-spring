package com.ptit.news.controller;

import com.ptit.news.command.dto.CreateNewsCommand;
import com.ptit.news.common.Response;
import com.ptit.news.entity.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/author")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthorController extends AdvancedBaseController {

    @PostMapping("/create")
    public ResponseEntity<Response<News>> createNews(@RequestBody CreateNewsCommand command) {

        return executeCommandWithCustomStatus(command, HttpStatus.CREATED);
    }
}
