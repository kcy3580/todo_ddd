package com.ap.todo.domain.commands;

import com.ap.todo.constant.TodoStatus;
import com.ap.todo.domain.valueobjects.Priority;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateTodoCommand {

    // To-Do Id
    private String todoId;

    // 위임할 담당자 Id
    private String managerIdForDelegation;

    // 업무 내용
    private String task;

    // 업무 설명
    private String description;

    // 실행 날짜(yyyyMMddHHmmss)
    private LocalDateTime executionDate;

    // 우선 순위
    private Priority priority;

    // 상태
    private TodoStatus status;

}
