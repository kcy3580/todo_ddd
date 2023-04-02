package com.ap.todo.application.outboundservices;

import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.domain.repositories.ManagerRepository;
import com.ap.todo.infrastructure.rest.FindManagerInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerOutboundService {

    private final ManagerRepository managerRepository;

    /**
     * 담당자 ID로 담당자 정보를 조회한다.
     * @param managerId     담당자Id
     * */
    public Manager findManagerInfo(String managerId) {
        FindManagerInfoDto findManagerInfoDto = managerRepository.findByManagerId(managerId);
        return new Manager(findManagerInfoDto.getId(), findManagerInfoDto.getName());
    }

    /**
     * 담당자 목록을 조회한다.
     * */
    public List<Manager> findManagerInfoList() {
        List<Manager> managerInfoList = new ArrayList<>();
        List<FindManagerInfoDto> findManagerInfoListDto = managerRepository.findManagerInfoList();
        findManagerInfoListDto.forEach(managerInfoDto ->
            managerInfoList.add(new Manager(managerInfoDto.getId(), managerInfoDto.getName()))
        );
        return managerInfoList;
    }

}
