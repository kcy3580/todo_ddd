package com.ap.todo.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateTodoRspDto {

    // Id
    private Long todoId;

    // 업무 제목
    private String task;

    // 업무 설명
    private String description;

}
