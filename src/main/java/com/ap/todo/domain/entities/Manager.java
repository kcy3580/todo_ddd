package com.ap.todo.domain.entities;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString
public class Manager {

    // 담당자 Id
    private long id;

    // 이름
    private String name;

}
