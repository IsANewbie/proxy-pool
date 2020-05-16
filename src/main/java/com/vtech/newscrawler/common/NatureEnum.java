package com.vtech.newscrawler.common;

public enum NatureEnum {
    /**
     * 原创
     */
    ORIGNINAL("原创"),
    /**
     * 转载
     */
    REPRINT("转载");
    private String desc;

    private NatureEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
