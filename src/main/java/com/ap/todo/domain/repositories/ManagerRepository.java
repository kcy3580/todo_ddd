package com.ap.todo.domain.repositories;

import com.ap.todo.infrastructure.rest.FindManagerInfoDto;

import java.util.List;

public interface ManagerRepository {

    FindManagerInfoDto findByManagerId(String managerId);
    List<FindManagerInfoDto> findManagerInfoList();

}
