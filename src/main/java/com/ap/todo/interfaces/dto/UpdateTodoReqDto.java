package com.ap.todo.interfaces.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class UpdateTodoReqDto {

    // To-Do Id
    @NotBlank
    private String todoId;

    // 위임할 담당자 Id
    private String managerIdForDelegation;

    // 업무 내용
    private String task;

    // 업무 설명
    private String description;

    // 실행 날짜(yyyyMMddHHmmss)
    private String executionDate;

    // 우선 순위
    private PriorityDtoForChangeTodo priority;

    // 상태
    private String status;

}
