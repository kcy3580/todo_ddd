package com.ap.todo.domain.aggregates;

import com.ap.common.exception.ApiException;
import com.ap.todo.constant.Importance;
import com.ap.todo.constant.TodoStatus;
import com.ap.todo.domain.commands.UpdateTodoCommand;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.domain.valueobjects.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.ap.common.constants.ResultCode.UNABLE_TO_LEAVE_TODO;

@Getter
@Builder
@AllArgsConstructor
@Slf4j
@ToString
public class Todo implements Serializable {

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

    public boolean isSameImportance(Importance importance) {
        return this.priority.getImportance().equals(importance);
    }

    public boolean isSameManager(String managerId) {
        return this.getManager().getId().equals(managerId);
    }

    private String getExecutionDateToStringDate() {
        return this.executionDate.getYear() + String.valueOf(this.executionDate.getMonth().getValue()) + this.executionDate.getDayOfMonth();
    }

    // To-Do 정보를 변경한다.
    public void updateInfo(UpdateTodoCommand command) {
        this.task = command.getTask();
        this.description = command.getDescription();
        this.priority = command.getPriority();
    }

    // 위임 시 이미 위임된 To-Do 일 경우 오류를 던진다.
    public void updatePriorityAndStatusByDelegation(long todoId, Priority priority, Manager manager) {
        if(this.status != TodoStatus.LEAVED) {
            this.todoId = todoId;
            this.priority = priority;
            this.status = TodoStatus.LEAVED;
            this.manager = manager;
        } else {
            throw new ApiException(UNABLE_TO_LEAVE_TODO);
        }
    }

    public void updateDelegationInfo(String delegationInfo) {
        this.description = delegationInfo;
    }
}
