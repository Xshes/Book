package com.example.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.book.adapter.BookAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends Activity{
    private List<Book> bookList = new ArrayList<Book>();
    private Button button_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        initBooks(); // 初始化数据
        BookAdapter adapter = new BookAdapter(BookActivity.this, R.layout.book_item, bookList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        button_details=(Button)findViewById(R.id.book_details);
        button_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BookActivity.this,DetailsActivity.class);
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
