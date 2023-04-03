package com.ap.common.exception;

import com.ap.common.constants.ResultCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    @Getter
    private final String resultCode;
    @Getter
    private final String resultMessage;
    @Getter
    private final HttpStatus httpStatus;

    public ApiException(ResultCode resultCode) {
        super("[" + resultCode + "] " + resultCode.getMessage());
        this.resultCode = resultCode.getCode();
        this.resultMessage = resultCode.getMessage();
        this.httpStatus = resultCode.getHttpStatus();
    }

}