package com.ptit.news.service;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

@Service
public class TestQueryHandler {

    @QueryHandler
    public String handle(String query) {
        System.out.println(">> TestQueryHandler called");
        return "Response for: " + query;
    }
}