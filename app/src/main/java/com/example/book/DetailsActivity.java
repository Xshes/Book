package com.example.book;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.Entity.Book;
import com.example.book.Entity.BookResult;
import com.example.book.Entity.StrResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends Activity{

    private List<String> mComment = new ArrayList<String>();
    private ListView mListView;
    private TextView nameOfBook;
    private TextView authorOfBook;
    private ImageView cover;

    String bookjson;
    String evajson;
    String bookNum;
    String bookName;
    String bookAuthor;
    Gson gson=new Gson();

    private Handler handler  = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 0x001:
                    SetImage();
                    break;
                case 0x002:
                    SetEva();
                    break;
                default:
                    break;
            }
        }
    };

    //设置图片封面
    private void SetImage(){
        BookResult bookResult = gson.fromJson(bookjson,BookResult.class);
        Book book=bookResult.list.get(0);
        setContentView(R.layout.activity_details);
        nameOfBook=findViewById(R.id.book_Name);
        authorOfBook=findViewById(R.id.book_author);
        cover=findViewById(R.id.book_bind);
        Bitmap coverBit= BitmapFactory.decodeByteArray(book.BookCover, 0, book.BookCover.length);
        bookAuthor=authorOfBook.getText()+bookAuthor;
        nameOfBook.setText(bookName);
        authorOfBook.setText(bookAuthor);
        cover.setImageBitmap(coverBit);
    }


    private void SetEva(){
        StrResult strResult=gson.fromJson(evajson,StrResult.class);
        mComment=strResult.list;
        if(mComment.size()==0)
            mComment.add("暂无评论");
        mListView = (ListView) findViewById(R.id.Comment_list);
        mListView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, mComment));
        mListView.setTextFilterEnabled(true);

        final PullUpToLoadMore ptlm= (PullUpToLoadMore) findViewById(R.id.ptlm);
        findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsActivity.this, "成功发送申请！", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptlm.scrollToTop();
            }
        });
    }
    //获取封面
    private void GetImage(){
        new Thread() {
            public void run() {
                try {
                    String loginURL;
                    //构造连接字符串，并查询
                    loginURL=GetData.url+"Book/GetBookByNum?bookNum="+bookNum;
                    bookjson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            }
        }.start();
    }

    private void GetEva(){
        new Thread() {
            public void run() {
                try {
                    String loginURL;
                    //构造连接字符串，并查询
                    loginURL=GetData.url+"Evaluate/GetByBook?bookNum="+bookNum;
                    evajson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x002);
            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();//获取传来的intent对象
        bookNum = intent.getStringExtra("bookNum");
        bookName = intent.getStringExtra("bookName");
        bookAuthor = intent.getStringExtra("bookAuthor");
        GetImage();
        GetEva();





    }
}
