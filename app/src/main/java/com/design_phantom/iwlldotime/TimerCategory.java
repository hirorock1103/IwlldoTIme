package com.design_phantom.iwlldotime;

/**
 * Created by amb01 on 2018/09/15.
 */

public class TimerCategory {

    private int matrix_id;
    private int category_id;
    private int timer_id;
    private int show_order;
    private String createdate;



    TimerCategory(){

    }

    public int getMatrix_id() {
        return matrix_id;
    }

    public void setMatrix_id(int matrix_id) {
        this.matrix_id = matrix_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getTimer_id() {
        return timer_id;
    }

    public void setTimer_id(int timer_id) {
        this.timer_id = timer_id;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public int getShowOrder() {
        return show_order;
    }

    public void setShowOrder(int order) {
        this.show_order = order;
    }
}
