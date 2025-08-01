package com.ptit.news.controller;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;

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
     * Deprecated: Không còn dùng nữa — không rõ kiểu trả về sẽ gây lỗi Axon.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    protected <T> Response<T> executeQuery(Object query) {
        try {
            return (Response<T>) queryGateway.query(query, ResponseTypes.instanceOf(Object.class)).join();
        } catch (Exception e) {
            log.error("Error executing query (deprecated): {}", query.getClass().getSimpleName(), e);
            return Response.Error("Lỗi khi thực hiện truy vấn");
        }
    }

    /**
     * Execute a query with type-safe response
     */
    protected <T> Response<T> executeQuery(Object query, Class<T> responseType) {
        try {
            T result = queryGateway.query(query, ResponseTypes.instanceOf(responseType)).join();
            return Response.Success(result, "Truy vấn thành công");
        } catch (Exception e) {
            log.error("Error executing query: {}", query.getClass().getSimpleName(), e);
            return Response.Error("Lỗi khi thực hiện truy vấn");
        }
    }
}
