package com.ptit.news.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.ptit.news.common.Response;
import com.ptit.news.common.enums.StatusResponse; // Import enum StatusResponse của bạn
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
        // Sửa lỗi: Kiểm tra enum StatusResponse.Success
        // Nếu thành công thì là HttpStatus.OK, nếu không thì là BAD_REQUEST
        HttpStatus status = result.getStatus() == StatusResponse.Success ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
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
        // Sửa lỗi: Kiểm tra enum StatusResponse.Success
        // Nếu thành công thì là HttpStatus.OK (hoặc NOT_FOUND nếu query thất bại do không tìm thấy)
        // Dựa vào cách BaseController.executeQuery trả về Response.Error, nếu lỗi thì status là Fail
        HttpStatus status = result.getStatus() == StatusResponse.Success ? HttpStatus.OK : HttpStatus.NOT_FOUND; // Hoặc HttpStatus.INTERNAL_SERVER_ERROR
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
        // Sửa lỗi: Kiểm tra enum StatusResponse.Success
        HttpStatus status = result.getStatus() == StatusResponse.Success ? successStatus : HttpStatus.BAD_REQUEST;
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
        // Sửa lỗi: Kiểm tra enum StatusResponse.Success
        HttpStatus status = result.getStatus() == StatusResponse.Success ? successStatus : HttpStatus.NOT_FOUND; // Hoặc HttpStatus.INTERNAL_SERVER_ERROR
        return ResponseEntity.status(status).body(result);
    }


    // --- Các phương thức 'success' và 'error' tùy chỉnh cho AdminCategoryController và các controller khác ---
    // (Nếu bạn muốn AdminCategoryController gọi trực tiếp các phương thức này thay vì executeCommandWithResponse/executeQueryWithResponse)

    protected <T> ResponseEntity<Response<T>> success(T data, String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(
                Response.<T>Success(data, message), // Sử dụng static factory method Success của bạn
                httpStatus
        );
    }

    protected <T> ResponseEntity<Response<T>> success(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    protected <T> ResponseEntity<Response<T>> error(String errorMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>(
                Response.<T>Error(errorMessage), // Sử dụng static factory method Error của bạn
                httpStatus
        );
    }
}