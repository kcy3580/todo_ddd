package com.ap.todo.constant;

import com.ap.common.constants.ResultCode;
import com.ap.common.exception.ApiException;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Importance {

    HIGH("S", 1),
    MIDDLE_HIGH("A", 2),
    MIDDLE("B", 3),
    MIDDLE_LOW("C", 4),
    LOW("D", 5),
    ;

    private final String code;
    private final Integer importanceSeq;

    private static final Map<String, Importance> codes = Map.copyOf(
            Stream.of(values()).collect(Collectors.toMap(Importance::getCode, Function.identity())));

    Importance(String code, Integer importanceSeq) {
        this.code = code;
        this.importanceSeq = importanceSeq;
    }


    public static Importance findByCode(final String code) {
        if (!codes.containsKey(code)) {
            throw new ApiException(ResultCode.INVALID_PARAMETER);
        }
        return codes.get(code);
    }

}