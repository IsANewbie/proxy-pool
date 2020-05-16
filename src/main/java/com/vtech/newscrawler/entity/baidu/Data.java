package com.vtech.newscrawler.entity.baidu;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Data {
    private String query;
    private String usm;
    private List<News> news;
    private boolean hasmore;

}
