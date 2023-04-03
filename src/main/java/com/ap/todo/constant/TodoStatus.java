package com.ap.todo.constant;

import com.ap.common.constants.ResultCode;
import com.ap.common.exception.ApiException;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum TodoStatus {

    PROGRESS("P", "진행중"),
    COMPLETED("S", "완료"),
    CANCELED("C", "취소"),
    LEAVED("L", "다른 사람에게 위임"),
    ;

    private final String code;
    private final String description;

    private static final Map<String, TodoStatus> codes = Map.copyOf(
            Stream.of(values()).collect(Collectors.toMap(TodoStatus::getCode, Function.identity())));

    TodoStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static TodoStatus findByCode(final String code) {
        if (!codes.containsKey(code)) {
            throw new ApiException(ResultCode.INVALID_PARAMETER);
        }
        return codes.get(code);
    }

}