package com.liujiajun.po;

import java.io.Serializable;

public class College implements Serializable {
    private Integer collegeid;

    private String collegename;

    public Integer getCollegeid() {
        return collegeid;
    }

    public void setCollegeid(Integer collegeid) {
        this.collegeid = collegeid;
    }

    public String getCollegename() {
        return collegename;
    }

    public void setCollegename(String collegename) {
        this.collegename = collegename == null ? null : collegename.trim();
    }

    @Override
    public String toString() {
        return "College{" +
                "collegeid=" + collegeid +
                ", collegename='" + collegename + '\'' +
                '}';
    }
}