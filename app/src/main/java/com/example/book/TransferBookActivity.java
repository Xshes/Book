package com.example.book;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.book.Entity.BookResult;
import com.example.book.Entity.Transfer;
import com.example.book.Entity.TransferResult;
import com.example.book.Entity.User;
import com.example.book.adapter.AdminBanAdapter;
import com.example.book.adapter.BookAdapter;

import java.util.ArrayList;
import java.util.List;

import com.example.book.Entity.Book;
import com.example.book.adapter.inter.InterClick;
import com.google.gson.Gson;

public class TransferBookActivity extends Activity implements AdapterView.OnItemClickListener,
        InterClick {
    // static final String[] CONTENTS = { "一条鱼", "一只狗", "一个壮汉" };
    private List<Book> contentList=new ArrayList<Book>();
    private ListView mListView;
    User user;
    String json;
    String bookJson;
    Gson gson=new Gson();
    private Handler handler  = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    SearchBookList();
                    break;
                case 0x002:
                    SetContentList();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        user=MainActivity.loginUser;
        GetTransfer();
    }

    public void GetTransfer()    {
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    String loginURL=GetData.url+"Transfer/GetTransList?account="+user.UserAccount;
                    json =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            };

        }.start();
    }

    public void GetBook(final String bookNums)    {
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    String loginURL=GetData.url+"Book/GetBookByNumList?bookNums="+bookNums;
                    bookJson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x002);
            };

        }.start();
    }

    public void SearchBookList(){
        TransferResult result = gson.fromJson(json,TransferResult.class);
        String res=result.result;
        if(res.equals("查询成功")) {
            List<Transfer> transfers = result.list;
            List<String> bookNums=new ArrayList<String>();
            //通过Transfer列表获取book列表
            for (Transfer transfer : transfers) {
                bookNums.add(transfer.BookNumber);
            }
            String bookBumStr=gson.toJson(bookNums,List.class);
            GetBook(bookBumStr);

        }
        else
        {
            Toast.makeText(this, res+"请重试！！！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,SecondActivity.class);
            startActivity(intent);
        }

    }

    public void SetContentList()
    {
        BookResult result = gson.fromJson(bookJson,BookResult.class);
        String res=result.result;
        if(res.equals("查询成功")) {
            List<Book> books = result.list;
            for(Book book:books) {
                int flag=0;
                for(Book content:contentList)
                {
                    if(content.BookNumber.equals(book.BookNumber))
                        flag=1;
                }
                if(flag==0)
                    contentList.add(book);
            }
            init();
        }
        else
        {
            Toast.makeText(this, res+"请重试！！！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,SecondActivity.class);
            startActivity(intent);
        }
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


    private void dialogEditReport() {
        final EditText editText = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("封禁");
        editText.setHeight(240);
        builder.setView(editText);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(TransferBookActivity.this, editText.getText().toString() + "  发送成功！", Toast.LENGTH_LONG).show();

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
                TransferBookActivity.this,
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