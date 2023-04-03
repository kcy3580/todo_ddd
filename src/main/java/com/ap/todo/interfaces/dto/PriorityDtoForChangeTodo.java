package com.ap.todo.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PriorityDtoForChangeTodo {

    // 중요도
    private String importance;

    // 순서
    private int sequence;

}
