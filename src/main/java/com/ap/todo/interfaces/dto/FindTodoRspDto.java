package com.ap.todo.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FindTodoRspDto {

    // Id
    private Long todoId;

    // 담당자
    private ManagerDtoForFindTodo manager;

    // 실행 날짜
    private String executionDate;

    // 우선순위
    private PriorityDtoForFindTodo priority;

    // 업무 제목
    private String task;

    // 업무 설명
    private String description;

    // 상태
    private String status;

}
