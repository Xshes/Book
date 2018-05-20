package com.example.book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.book.Book;
import com.example.book.R;

import java.util.List;

public class BookAdapter extends ArrayAdapter {
    private final int resourceId;

    public BookAdapter(Context context, int textViewResourceId, List<Book> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = (Book) getItem(position); // 获取当前项的Book实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView bookName = (TextView) view.findViewById(R.id.book_name);//获取该布局内的图片视图

        bookName.setText(book.getName());//为图片视图设置图片资源
        return view;
    }
}
