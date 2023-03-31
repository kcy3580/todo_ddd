package com.ap.todo.interfaces.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class CreateTodoReqDto {

    @NotBlank
    private String task;

    @NotNull
    private String managerId;

    private String description;

}
