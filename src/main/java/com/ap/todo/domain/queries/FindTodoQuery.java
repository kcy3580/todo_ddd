package com.ap.todo.domain.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class FindTodoQuery {

    private String managerId;

    private LocalDateTime executionDate;

}
