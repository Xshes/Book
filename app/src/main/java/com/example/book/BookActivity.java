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
//        findViewById(R.id.bk_detail).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(BookActivity.this,DetailsActivity.class);
//                Book book=contentList.get(position);
//                intent.putExtra("bookNum",book.BookNumber);
//                intent.putExtra("bookName",book.BookName);
//                intent.putExtra("bookAuthor",book.BookAuthor);
//                startActivity(intent);
//            }
//        });
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
        Toast.makeText(
                this,
                "listview的内部的评论按钮被点击了！，位置是-->" + (Integer) v.getTag()
                        + ",内容是-->" + contentList.get((Integer) v.getTag()),
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,ReportDetailActivity.class);
        startActivity(intent);
    }

    private void dialogEditComment() {
        final EditText editText = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("举报内容");
        editText.setHeight(240);
        builder.setView(editText);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BookActivity.this, editText.getText().toString() + "  发送成功！", Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void dialogEditReport() {
        final EditText editText = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("封禁");
        editText.setHeight(240);
        builder.setView(editText);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BookActivity.this, editText.getText().toString() + "  发送成功！", Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void reportClick(View v) {
        Toast.makeText(
                BookActivity.this,
                "listview的内部的举报按钮被点击了！，位置是-->" + (Integer) v.getTag()
                        + ",内容是-->" + contentList.get((Integer) v.getTag()),
                Toast.LENGTH_SHORT).show();
        dialogEditReport();
    }
    @Override
    public void retransferClick(View v){
        Toast.makeText(this,  "发布成功！", Toast.LENGTH_LONG).show();
    }
}
