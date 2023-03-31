package com.ap.todo.constant;

import lombok.Getter;

@Getter
public enum TodoStatus {

    PROGRESS("P", "진행중"),
    COMPLETED("S", "완료"),
    CANCELED("C", "취소"),
    LEAVED("L", "다른 사람에게 위임"),
    ;

    private final String code;
    private final String description;

    TodoStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

}