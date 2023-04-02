package com.ap.todo.application.outboundservices;

import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.domain.repositories.ManagerRepository;
import com.ap.todo.infrastructure.rest.FindManagerInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerOutboundService {

    private final ManagerRepository managerRepository;

    /**
     * 담당자 ID로 To-Do 목록을 가져온다.
     * */
    public Manager findInfo(String managerId) {
        FindManagerInfoDto findManagerInfoDto = managerRepository.findByManagerId(managerId);
        return new Manager(findManagerInfoDto.getId(), findManagerInfoDto.getName());
    }

}
