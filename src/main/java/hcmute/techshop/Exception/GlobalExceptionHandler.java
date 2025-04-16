package hcmute.techshop.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import hcmute.techshop.Model.ResponseModel;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // lỗi không tìm thấy tài nguyên
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseModel> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // lỗi request không hợp lệ
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseModel> handleBadRequest(BadRequestException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // lỗi chung (code: 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel> handleGeneralException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong in server: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseModel> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "You provided not enough argument: "+ex.getMessage());
    }

    // hàm hỗ trợ format response
    private ResponseEntity<ResponseModel> buildErrorResponse(HttpStatus status, String message) {
        ResponseModel response = new ResponseModel(false, message, null);
        return new ResponseEntity<>(response, status);
    }
}
