package com.design_phantom.iwlldotime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amb01 on 2018/09/16.
 */

public class TimerManager  {


    public List<Timer> getTimers(){
        List<Timer> list = new ArrayList<>();
        list = TimerCategorySample.getTimers();
        return list;
    }

}
