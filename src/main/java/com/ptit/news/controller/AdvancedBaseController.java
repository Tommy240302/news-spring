package com.ptit.news.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.ptit.news.common.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AdvancedBaseController extends BaseController {

    /**
     * Execute command and return ResponseEntity with proper HTTP status
     * 
     * @param command The command to execute
     * @param <T>     The return type
     * @return ResponseEntity containing the result
     */
    protected <T> ResponseEntity<Response<T>> executeCommandWithResponse(Object command) {
        Response<T> result = executeCommand(command);
        HttpStatus status = result.getStatus().equals("Success") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(result);
    }

    /**
     * Execute query and return ResponseEntity with proper HTTP status
     * 
     * @param query The query to execute
     * @param <T>   The return type
     * @return ResponseEntity containing the result
     */
    protected <T> ResponseEntity<Response<T>> executeQueryWithResponse(Object query) {
        Response<T> result = executeQuery(query);
        HttpStatus status = result.getStatus().equals("Success") ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(result);
    }

    /**
     * Execute command and return ResponseEntity with custom success status
     * 
     * @param command       The command to execute
     * @param successStatus HTTP status for success case
     * @param <T>           The return type
     * @return ResponseEntity containing the result
     */
    protected <T> ResponseEntity<Response<T>> executeCommandWithCustomStatus(Object command, HttpStatus successStatus) {
        Response<T> result = executeCommand(command);
        HttpStatus status = "Success".equalsIgnoreCase(result.getStatus().toString()) ? successStatus : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(result);
    }

    /**
     * Execute query and return ResponseEntity with custom success status
     * 
     * @param query         The query to execute
     * @param successStatus HTTP status for success case
     * @param <T>           The return type
     * @return ResponseEntity containing the result
     */
    protected <T> ResponseEntity<Response<T>> executeQueryWithCustomStatus(Object query, HttpStatus successStatus) {
        Response<T> result = executeQuery(query);
        HttpStatus status = result.getStatus().equals("Success") ? successStatus : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(result);
    }
}