package com.liujiajun.po;

public class CheckUserId {

    private Integer userid;
    private boolean flag; //用户存在 返回false  用户不存在 返回true
    private String errorMsg;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "CheckUserId{" +
                "userid=" + userid +
                ", flag=" + flag +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
