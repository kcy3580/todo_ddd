package com.ap.todo.application.outboundservices;

import com.ap.common.constants.ResultCode;
import com.ap.common.exception.ApiException;
import com.ap.todo.domain.repositories.ManagerRepository;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.infrastructure.rest.FindManagerInfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.ap.common.constants.StaticValues.RESULT_CODE;
import static com.ap.common.constants.StaticValues.RESULT_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ManagerOutboundServiceTest {

    @InjectMocks
    private ManagerOutboundService managerOutboundService;

    @Mock
    private ManagerRepository managerRepository;

    @Test
    @DisplayName("담당자 ID로 담당자 정보를 조회하는데 성공했다.")
    void findManagerInfo_success() {
        // given
        String testerId = "testerId";
        String testerName = "testerName";
        FindManagerInfoDto findManagerInfoDto = new FindManagerInfoDto(testerId, testerName);
        given(managerRepository.findByManagerId(anyString())).willReturn(findManagerInfoDto);

        // when
        Manager actual = managerOutboundService.findManagerInfo(testerId);

        // then
        assertThat(actual.getId()).isEqualTo(testerId);
        assertThat(actual.getName()).isEqualTo(testerName);
    }

    @Test
    @DisplayName("담당자 ID로 담당자 정보를 조회하는데 일치하는 정보가 없어 오류가 발생했다.")
    void findManagerInfo_no_data_fail() {
        // given
        String testerId = "testerId";
        given(managerRepository.findByManagerId(anyString())).willThrow(new ApiException(ResultCode.NO_DATA));

        // when
        Throwable thrown = catchThrowable(() -> managerOutboundService.findManagerInfo(testerId));

        //Then
        assertThat(thrown)
                .isInstanceOf(ApiException.class)
                .hasFieldOrProperty(RESULT_CODE)
                .hasFieldOrProperty(RESULT_MESSAGE);

        assertThat(((ApiException)thrown).getResultCode())
                .isEqualTo(ResultCode.NO_DATA.getCode());
        assertThat(((ApiException)thrown).getResultMessage())
                .isEqualTo(ResultCode.NO_DATA.getMessage());
    }

    @Test
    @DisplayName("담당자 정보 목록을 조회하는데 성공했다.")
    void findManagerInfoList_success() {
        //given
        List<FindManagerInfoDto> managerInfoList = List.of(
                new FindManagerInfoDto("id1", "manger1"),
                new FindManagerInfoDto("id2", "manger2"),
                new FindManagerInfoDto("id3", "manger3"),
                new FindManagerInfoDto("id4", "manger4"),
                new FindManagerInfoDto("id5", "manger5")
        );

        given(managerRepository.findManagerInfoList()).willReturn(managerInfoList);

        // when
        List<Manager> actual = managerOutboundService.findManagerInfoList();

        // then
        assertThat(actual.size()).isEqualTo(managerInfoList.size());
    }

    @Test
    @DisplayName("담당자 정보 목록을 조회하는데 정보가 없어 빈 리스트가 반환됐다.")
    void findManagerInfoList_no_data() {
        //given
        given(managerRepository.findManagerInfoList()).willReturn(new ArrayList<>());

        // when
        List<Manager> actual = managerOutboundService.findManagerInfoList();

        // then
        assertThat(actual.size()).isEqualTo(0);
    }

}