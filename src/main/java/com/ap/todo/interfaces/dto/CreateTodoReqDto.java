package com.ap.todo.interfaces.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class CreateTodoReqDto {

    // 업무 제목
    @NotBlank
    private String task;

    // 담당자 Id
    @NotNull
    private String managerId;

    // 실행 날짜(yyyyMMddHHmmss)
    @NotNull
    private String executionDate;

    // 업무 설명
    private String description;

}
