package com.ap.common.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 앱연동 응답 규격
 * */
@Getter
public enum ResultCode {

    /** 공통 */
    SUCCESS("0000","정상", HttpStatus.OK),
    NO_DATA("0001","데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_PARAMETER("0002","파라미터가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    UNABLE_TO_LEAVE_TODO("0003","이미 위임된 TODO 이므로 위임할 수 없습니다.", HttpStatus.BAD_REQUEST),
    SERVER_ERROR("9999","서버에러가 발생했습니다. 잠시 후 다시 요청해주세요", HttpStatus.INTERNAL_SERVER_ERROR),
    ;
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ResultCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getUrlEncodingMessage(){
        return URLEncoder.encode(this.message, StandardCharsets.UTF_8);
    }

}