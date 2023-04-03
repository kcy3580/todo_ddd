package com.ap.todo.domain.valueobjects;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString
@Builder
public class Manager {

    // 담당자 Id
    private String id;

    // 이름
    private String name;

    public Manager(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
