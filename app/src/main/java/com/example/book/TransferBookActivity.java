package com.example.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.book.adapter.BookAdapter;

import java.util.ArrayList;
import java.util.List;

import com.example.book.Entity.Book;

public class TransferBookActivity extends Activity{
    private List<Book> bookList = new ArrayList<Book>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        initBooks(); // 初始化数据
        BookAdapter adapter = new BookAdapter(TransferBookActivity.this, R.layout.book_item, bookList);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(TransferBookActivity.this,DetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void initBooks() {
        Book apple = new Book("Apple");
        bookList.add(apple);
        Book banana = new Book("Banana");
        bookList.add(banana);
        Book orange = new Book("Orange");
        bookList.add(orange);

    }
}

