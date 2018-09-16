package com.design_phantom.iwlldotime;

/**
 * Created by amb01 on 2018/09/15.
 */

public class Timer {
    private int timer_id;
    private int timer_minutes;
    private int timer_second;
    private String timer_title;
    private String createdate;

    Timer(){

    }

    Timer(int timer_minutes, int timer_second){
        this.timer_minutes = timer_minutes;
        this.timer_second = timer_second;
    }

    public int getTimer_id() {
        return timer_id;
    }

    public void setTimer_id(int timer_id) {
        this.timer_id = timer_id;
    }

    public int getTimer_minutes() {
        return timer_minutes;
    }

    public void setTimer_minutes(int timer_minutes) {
        this.timer_minutes = timer_minutes;
    }

    public int getTimer_second() {
        return timer_second;
    }

    public void setTimer_second(int timer_second) {
        this.timer_second = timer_second;
    }

    public String getTimer_title() {
        return timer_title;
    }

    public void setTimer_title(String timer_title) {
        this.timer_title = timer_title;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
