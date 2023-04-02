package com.ap.todo.domain.repositories;

import com.ap.todo.infrastructure.rest.FindManagerInfoDto;

public interface ManagerRepository {

    FindManagerInfoDto findByManagerId(String managerId);

}
