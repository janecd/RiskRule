package com.hello.utils;

public class GwDataStructExtend extends GwDataStruct {

    private Long startTime;

    private Long updateTime;

    private Long periodTime; //时间间隔

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }


    public Long getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(Long periodTime) {
        this.periodTime = periodTime;
    }
}
