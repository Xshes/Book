package com.example.book;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.book.adapter.AdminBanAdapter;
import com.example.book.adapter.BookAdapter;
import java.util.ArrayList;
import java.util.List;

import com.example.book.adapter.inter.InterClick;
import com.google.gson.*;

import com.example.book.Entity.*;

public class BookActivity extends Activity implements AdapterView.OnItemClickListener,
        InterClick {
    private List<Book> contentList;
    private ListView mListView;

    Gson gson = new Gson();
    String json;
    String tag;
    String param;


    private Handler handler  = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 0x001:
                    SetVal();
                    break;
                default:
                    break;
            }
        }
    };

    public void GetBookList()
    {
        new Thread() {
            public void run() {
                try {
                    String loginURL;
                    //构造连接字符串，并查询
                    if(tag!=null)
                        loginURL=GetData.url+"Book/search?param="+param+"&tag="+tag;
                    else
                        loginURL=GetData.url+"Book/search?param="+param;
                    json =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();//获取传来的intent对象
        param = intent.getStringExtra("param");
        tag = intent.getStringExtra("tag");
        GetBookList();
    }

    private void SetVal()
    {
        BookResult result=gson.fromJson(json,BookResult.class);
        contentList=result.list;
        setContentView(R.layout.book_list);
        init();
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.book_listview);
        mListView.setAdapter(new BookAdapter(this, contentList, this));
        mListView.setOnItemClickListener(this);
    }

    /**
     * 响应ListView的条目点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Intent intent = new Intent(this,DetailsActivity.class);
        Book book=contentList.get(position);
        intent.putExtra("bookNum",book.BookNumber);
        intent.putExtra("bookName",book.BookName);
        intent.putExtra("bookAuthor",book.BookAuthor);
        startActivity(intent);
    }

    /**
     * 接口方法，响应ListView按钮点击事件
     */

    @Override
    public void commentClick(View v) {
    }


    @Override
    public void reportClick(View v) {

    }
    @Override
    public void retransferClick(View v){

    }
}
