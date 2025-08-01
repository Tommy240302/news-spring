package com.ptit.news.service;

import jakarta.annotation.PostConstruct;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TestQuerySender {

    @Autowired
    QueryGateway queryGateway;

    @PostConstruct
    public void testQuery() {
        System.out.println("Hello");
//        queryGateway.query("Hello", ResponseTypes.instanceOf(String.class))
//                .thenAccept(System.out::println);
    }
}
