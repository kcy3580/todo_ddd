package com.ap.todo.infrastructure.repositories;

import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.repositories.ManagerRepository;
import com.ap.todo.domain.repositories.TodoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    List<Todo> todoList = new ArrayList<>();

    @Override
    public List<Todo> findByManagerId(String managerId) {
        return todoList.stream().filter(todo -> todo.getManager().getId().equals(managerId)).collect(Collectors.toList());
    }

    @Override
    public void save(Todo todo) {
        todoList.add(todo);
    }

}
