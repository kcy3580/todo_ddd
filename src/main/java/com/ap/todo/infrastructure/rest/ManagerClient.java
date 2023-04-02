package com.ap.todo.infrastructure.rest;

import com.ap.todo.domain.repositories.ManagerRepository;
import org.springframework.stereotype.Repository;


/***
 * 외부 통신 시 feign client 인터페이스를 사용해왔는데, Manager 도메인의 서버를 구성할 수 없으니
 * name만 client로 하고 외부 요청을 mocking 하여 반환하도록 합니다.
 */
@Repository
public class ManagerClient implements ManagerRepository {

    @Override
    public FindManagerInfoDto findByManagerId(String managerId) {
        return null;
    }

}
