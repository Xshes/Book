package com.example.book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.book.Entity.Book;
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
        TextView bookName = (TextView) view.findViewById(R.id.book_name);//获取该布局内的视图
        TextView BookAuthor = (TextView) view.findViewById(R.id.book_author);//获取该布局内的视图
        bookName.setText(book.BookName);//为视图设置资源
        BookAuthor.setText(book.BookAuthor);
        return view;
    }
}
