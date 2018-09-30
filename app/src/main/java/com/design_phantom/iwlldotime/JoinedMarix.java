package com.design_phantom.iwlldotime;

public class JoinedMarix {

    private TimerCategory matrix;
    private Timer timer;
    private Category category;

    public void setTimer(Timer timer){
        this.timer = timer;
    }

    public void setCategory(Category category){
        this.category = category;
    }

    public void setMatrix(TimerCategory matrix){
        this.matrix = matrix;
    }

    public TimerCategory getMatrix() {
        return matrix;
    }

    public Timer getTimer() {
        return timer;
    }

    public Category getCategory() {
        return category;
    }
}
