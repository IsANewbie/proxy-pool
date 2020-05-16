package com.vtech.newscrawler.entity.baidu;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Root {
    private int errno;
    private String request_id;
    private int timestamp;
    private Data data;
}