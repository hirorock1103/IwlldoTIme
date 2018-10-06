package com.design_phantom.iwlldotime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class myAdapter2 extends ArrayAdapter<Timer> {

    myAdapter2(Context context, List<Timer> list){
        super(context, R.layout.list_view_01, list);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.list_view_01, parent, false);

        String timerTitle = getItem(position).getTimer_title();
        String str = String.valueOf(getItem(position).getTimer_second());

        TextView timer = view.findViewById(R.id.category_name);
        timer.setText(timerTitle + "(" +str+ ")");


        return view;


    }

}
