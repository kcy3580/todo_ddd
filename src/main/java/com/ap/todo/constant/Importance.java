package com.ap.todo.constant;

import lombok.Getter;

@Getter
public enum Importance {

    HIGH("S", 1),
    MIDDLE_HIGH("A", 2),
    MIDDLE("B", 3),
    MIDDLE_LOW("C", 4),
    LOW("D", 5),
    ;

    private final String code;
    private final Integer priority;

    Importance(String code, Integer priority) {
        this.code = code;
        this.priority = priority;
    }

}