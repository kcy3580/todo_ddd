package com.ap.todo.infrastructure.repositories;

import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.repositories.ManagerRepository;
import com.ap.todo.domain.repositories.TodoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    @Override
    public List<Todo> findByManagerId(String managerId) {
        return null;
    }

    @Override
    public void save(Todo todo) {

    }

}
