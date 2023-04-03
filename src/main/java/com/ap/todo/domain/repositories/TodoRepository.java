package com.ap.todo.domain.repositories;

import com.ap.todo.domain.aggregates.Todo;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository {

    List<Todo> findByManagerId(String managerId);
    List<Todo> findTodoListByManagerIdAndExecutionDate(String managerId, LocalDateTime executionDate);
    Todo findById(String todoId);
    void save(Todo todo);
    void saveAll(List<Todo> todoList);
    void deleteById(String todoId);

}
