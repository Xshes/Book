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
import com.google.gson.*;

import com.example.book.Entity.*;

public class BookActivity extends Activity{
    private List<Book> bookList = new ArrayList<Book>();
    Gson gson=new Gson();
    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();//获取传来的intent对象
        json = intent.getStringExtra("json");
        Result<Book> result=gson.fromJson(json,Result.class);
        bookList=result.list;
        // TODO: 2018/5/31 处理数据并显示
        setContentView(R.layout.book_list);
        BookAdapter adapter = new BookAdapter(BookActivity.this, R.layout.book_item, bookList);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(BookActivity.this,DetailsActivity.class);
                intent.putExtra("json",json);
                startActivity(intent);
                finish();
            }
        });

    }
}
