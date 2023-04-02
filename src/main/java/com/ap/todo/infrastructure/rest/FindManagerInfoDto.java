package com.ap.todo.infrastructure.rest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindManagerInfoDto {

    // 담당자 Id
    private String id;

    // 이름
    private String name;

}
