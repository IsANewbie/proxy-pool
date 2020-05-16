package com.vtech.newscrawler.entity.baidu;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class News {
    private String title;
    private String display_url;
    private String url;
    private String nid;
    private String site;
    private long sourcets;
    private long ts;
    private String type;
    private List<String> imageurls;
    private List<String> content;
    private String abs;
    private String layout;
    private int display_type;
}