package com.ap.todo.interfaces.dto;

import com.ap.todo.constant.Importance;
import com.ap.todo.constant.TodoStatus;
import com.ap.todo.domain.valueobjects.Manager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
