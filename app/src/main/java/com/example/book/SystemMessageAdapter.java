package com.example.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SystemMessageAdapter extends ArrayAdapter {
    private final int resourceId;

    public SystemMessageAdapter(Context context, int textViewResourceId, List<SystemMessage> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SystemMessage fruit = (SystemMessage) getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象

        TextView fruitName = (TextView) view.findViewById(R.id.tweetText);//获取该布局内的文本视图

        fruitName.setText(fruit.getName());//为文本视图设置文本内容
        return view;
    }
}
