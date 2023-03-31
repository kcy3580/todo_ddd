package com.ap.todo.domain.aggregates;

import com.ap.todo.domain.entities.Manager;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Slf4j
@ToString
public class Todo {

    // Id
    private Long todoId;

    // 담당자
    private Manager manager;

    // 실행 날짜
    private LocalDateTime executionDate;

    // 우선 순위
    private Object priority;

    // 업무 제목
    private String task;

    // 업무 설명
    private String description;

    // 상태
    private Object status;

}
