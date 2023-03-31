package com.ap.todo.interfaces.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import static com.ap.common.constants.ResultCode.SUCCESS;
import static com.ap.common.constants.StaticValues.RESULT_CODE;
import static com.ap.common.constants.StaticValues.RESULT_MESSAGE;

@Slf4j
public class BaseController {

    protected HttpHeaders getSuccessHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RESULT_CODE, SUCCESS.getCode());
        headers.set(RESULT_MESSAGE, SUCCESS.getUrlEncodingMessage());
        return headers;
    }

}