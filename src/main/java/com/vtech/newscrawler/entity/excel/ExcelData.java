package com.vtech.newscrawler.entity.excel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ExcelData {
   private String no;
   private String date;
   private String keyword;
   private String channel;
   private String media;
   private String title;
   private String url;
   private String author;
   private String remark;
   private String type;
   private String readnum;
   private String nature;
}
