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

public class myAdapter extends ArrayAdapter<Category> {

    myAdapter(Context context, List<Category> list){
        super(context, R.layout.list_view_01, list);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.list_view_01, parent, false);

        String categoryName = getItem(position).getCategory_name();
        String str = String.valueOf(getItem(position).getCategory_id());

        TextView categor = view.findViewById(R.id.category_name);
        categor.setText(categoryName);

        TimerCategoryManager manager = new TimerCategoryManager(getContext());
        List<JoinedMarix> list = manager.getJoinedMatrixListByCategoryId(getItem(position).getCategory_id());

        StringBuilder builder = new StringBuilder();
        for (JoinedMarix matrix : list){
            builder.append(matrix.getTimer().getTimer_title() + "/");
        }

        TextView info = view.findViewById(R.id.info);
        info.setText(builder.toString());

        return view;


    }

}
