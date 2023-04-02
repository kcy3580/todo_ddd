package com.ap.todo.domain.repositories;

import com.ap.todo.domain.aggregates.Todo;

import java.util.List;

public interface TodoRepository {

    List<Todo> findByManagerId(String managerId);
    void save(Todo todo);

}
