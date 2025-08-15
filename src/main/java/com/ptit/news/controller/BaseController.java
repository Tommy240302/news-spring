package com.ptit.news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import com.ptit.news.common.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseController {

    @Autowired
    protected CommandGateway commandGateway;

    @Autowired
    protected QueryGateway queryGateway;

    /**
     * Execute a command and return the result
     * 
     * @param command The command to execute
     * @param <T>     The return type
     * @return Response containing the result
     */
    @SuppressWarnings("unchecked")
    protected <T> Response<T> executeCommand(Object command) {
        try {
            return (Response<T>) commandGateway.sendAndWait(command);
        } catch (Exception e) {
            log.error("Error executing command: {}", command.getClass().getSimpleName(), e);
            throw e;
        }
    }

    /**
     * Execute a query and return the result
     * 
     * @param query The query to execute
     * @param <T>   The return type
     * @return Response containing the result
     */
    @SuppressWarnings("unchecked")
    protected <T> Response<T> executeQuery(Object query) {
        try {
            return (Response<T>) queryGateway.query(query, Object.class).get();
        } catch (Exception e) {
            log.error("Error executing query: {}", query.getClass().getSimpleName(), e);
            return Response.Error("Lỗi khi thực hiện truy vấn");
        }
    }
}