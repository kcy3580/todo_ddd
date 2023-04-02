package com.ap.todo.domain.aggregates;

import com.ap.todo.constant.TodoStatus;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.domain.valueobjects.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Slf4j
@ToString
public class Todo {

    // Id
    private Long todoId;

    // 담당자
    private Manager manager;

    // 실행 날짜
    private LocalDateTime executionDate;

    // 우선순위
    private Priority priority;

    // 업무 제목
    private String task;

    // 업무 설명
    private String description;

    // 상태
    private TodoStatus status;

    public Todo(Long todoId, CreateTodoCommand command, Manager manager, Priority priority) {
        this.todoId = todoId;
        this.manager = manager;
        this.executionDate = command.getExecutionDate();
        this.priority = priority;
        this.task = command.getTask();
        this.description = command.getDescription();
        this.status = TodoStatus.PROGRESS;
    }

    public boolean isSameExecutionDate(LocalDateTime executionDate) {
        String stringDate = executionDate.getYear() + String.valueOf(executionDate.getMonth().getValue()) + executionDate.getDayOfMonth();
        return this.getExecutionDateToStringDate().equals(stringDate);
    }

    private String getExecutionDateToStringDate() {
        return this.executionDate.getYear() + String.valueOf(this.executionDate.getMonth().getValue()) + this.executionDate.getDayOfMonth();
    }

}
