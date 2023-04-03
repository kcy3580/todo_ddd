package com.ap.todo.infrastructure.rest;

import com.ap.common.constants.ResultCode;
import com.ap.common.exception.ApiException;
import com.ap.todo.domain.repositories.ManagerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/***
 * 외부 통신 시 feign client 인터페이스를 사용해왔는데, Manager 도메인의 서버를 구성할 수 없으니
 * name만 client로 하고 외부 요청을 mocking 하여 반환하도록 합니다.
 */
@Repository
public class ManagerClient implements ManagerRepository {

    // 5명의 담당자 정보인데, 외부 Manger 도메인에서 조회된 Mock Data
    private final List<FindManagerInfoDto> managerInfoDtoList = List.of(
            new FindManagerInfoDto("id1", "manger1"),
            new FindManagerInfoDto("id2", "manger2"),
            new FindManagerInfoDto("id3", "manger3"),
            new FindManagerInfoDto("id4", "manger4"),
            new FindManagerInfoDto("id5", "manger5")
    );

    @Override
    public FindManagerInfoDto findByManagerId(String managerId) {
        return managerInfoDtoList.stream().filter(item -> item.getId().equals(managerId)).findFirst().orElseThrow(() -> new ApiException(ResultCode.NO_DATA));
    }

    @Override
    public List<FindManagerInfoDto> findManagerInfoList() {
        return managerInfoDtoList;
    }

}
