package com.ap.todo.domain.commands;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class CreateTodoCommand {

    // 업무 제목
    private String task;

    // 담당자 Id
    private String managerId;

    // 업무 실행날짜
    private LocalDateTime executionDate;

    // 업무 설명
    private String description;

}
