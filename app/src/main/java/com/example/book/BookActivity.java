package com.example.book;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.book.adapter.BookAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends Activity{
    private List<Book> bookList = new ArrayList<Book>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        initFruits(); // 初始化水果数据
        BookAdapter adapter = new BookAdapter(BookActivity.this, R.layout.book_item, bookList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }
    private void initFruits() {
        Book apple = new Book("Apple");
        bookList.add(apple);
        Book banana = new Book("Banana");
        bookList.add(banana);
        Book orange = new Book("Orange");
        bookList.add(orange);

    }
}
