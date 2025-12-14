package io.github.ahumadamob.plangastos.dto.common;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponseDto {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<ErrorDetailDto> messages;
    private String errorCode;
    private String path;

    public ErrorResponseDto() {
    }

    public ErrorResponseDto(LocalDateTime timestamp, int status, String error, List<ErrorDetailDto> messages,
            String errorCode, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.messages = messages;
        this.errorCode = errorCode;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<ErrorDetailDto> getMessages() {
        return messages;
    }

    public void setMessages(List<ErrorDetailDto> messages) {
        this.messages = messages;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
