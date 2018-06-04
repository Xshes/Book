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

import com.example.book.Entity.Book;
import com.example.book.Entity.BookResult;
import com.example.book.Entity.Evaluate;
import com.example.book.Entity.EvaluateResult;
import com.example.book.Entity.SimResult;
import com.example.book.Entity.Transfer;
import com.example.book.Entity.TransferResult;
import com.example.book.Entity.User;
import com.example.book.adapter.BookCommentAdapter;
import com.example.book.adapter.inter.InterClick;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class BookCommentActivity extends Activity implements AdapterView.OnItemClickListener,
        InterClick {
    //private static final String[] CONTENTS = { "时间简史", "绝地求生", "暴走漫画" };
    private List<Book> contentList=new ArrayList<>();
    private ListView mListView;
    List<Evaluate> evaluates= new ArrayList<>();
    String EvaBookNum;
    String PubBookNum;
    User user;
    String json;
    String bookJson;
    String evaJson;
    String evaCrateJson;
    String repCrateJson;
    String pubResJson;
    String pubJson;
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
                case 0x003:
                    SaveEva();
                    break;
                case 0x004:
                    ShowCrateEva();
                    break;
                case 0x005:
                    ShowCreateRep();
                    break;
                case 0x006:
                    SetPublish();
                    break;
                case 0x007:
                    ShowPublish();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_comment);
        user=MainActivity.loginUser;
        GetBooks();
    }

    //获取书籍列表
    private void GetBooks()    {
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    String loginURL=GetData.url+"Transfer/GetRecList?account="+user.UserAccount;
                    json =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            };

        }.start();
    }

    //获取书籍信息
    private void GetBook(final String bookNums)    {
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

    //获取评论
    private void GetEva(final String userAcc){
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    String loginURL=GetData.url+"Evaluate/GetByBookandUser?bookNum="+EvaBookNum+"&userAcc="+userAcc;
                    evaJson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x003);
            }

        }.start();
    }

    //新建评论
    private void CrateEva(final String bookNum,final String content,final String account){
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    String loginURL=GetData.url+"Evaluate/Create?bookNum="+bookNum+"&content="+content+"&account="+account;
                    evaCrateJson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x004);
            }

        }.start();
    }

    //新建举报
    private void CreateReport(final String reason,final String bookNum,final String reportObj){
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    String loginURL=GetData.url+"Report/Create?type="+reason+"&bookNum="+bookNum+"&reportObj="+reportObj;
                    repCrateJson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x005);
            }

        }.start();
    }

    //获取发布结果
    private void GetPublishResearch(final String bookNum,final String account){
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    String loginURL=GetData.url+"Publish/GetResult?bookNum="+bookNum+"&account="+account;
                    pubResJson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x006);
            }

        }.start();
    }

    //再发布书籍
    private void PublishBook(){
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    String loginURL=GetData.url+"Publish/Create?bookNum="+PubBookNum+"&account="+user.UserAccount;
                    pubJson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x007);
            }

        }.start();
    }


    //查询列表的内容
    private void SearchBookList(){
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

    //设置列表的内容
    private void SetContentList(){
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

    //解析评论详情
    private void SaveEva(){
        EvaluateResult result = gson.fromJson(evaJson,EvaluateResult.class);
        String res=result.result;
        if(res.equals("查询成功")) {
            evaluates=result.list;
            if(evaluates.size()==0)
                dialogEditComment(EvaBookNum,user.UserAccount);
            else {
                Toast.makeText(this, "本书已评论，请勿重复评论。", Toast.LENGTH_SHORT).show();
                dialogShowComment(evaluates.get(0).CommentContent);
            }
        }
        else
        {
            Toast.makeText(this, res+"请重试！！！", Toast.LENGTH_SHORT).show();
        }
    }

    //解析新建评论结果
    private void ShowCrateEva(){
        SimResult result = gson.fromJson(evaCrateJson,SimResult.class);
        String res=result.result;
        if(res.equals("创建成功")) {
            Toast.makeText(this, "评论成功！", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, res+"请重试！！！", Toast.LENGTH_SHORT).show();
        }
    }

    //解析举报结果
    private void ShowCreateRep(){
        SimResult result = gson.fromJson(repCrateJson,SimResult.class);
        Toast.makeText(this, result.result, Toast.LENGTH_SHORT).show();
    }

    //解析查询的发布结果
    private void SetPublish(){
        SimResult result = gson.fromJson(pubResJson,SimResult.class);
        String res=result.result;
        if(res.equals("已发布"))
            Toast.makeText(this, "本书已经再发布，请勿重复发布", Toast.LENGTH_SHORT).show();
        else
            PublishBook();
    }

    //解析发布结果
    private void ShowPublish()
    {
        SimResult result = gson.fromJson(pubJson,SimResult.class);
        Toast.makeText(this, result.result, Toast.LENGTH_SHORT).show();
    }

    // TODO: 2018/6/3 再发布和举报按钮的功能未实现 
    private void init() {
        mListView = (ListView) findViewById(R.id.comment_listview);
        mListView.setAdapter(new BookCommentAdapter(this, contentList, this));
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
        Book book = contentList.get((Integer) v.getTag());
        EvaBookNum=book.BookNumber;
        GetEva(user.UserAccount);
    }

    private void dialogShowComment(String comment) {
        final EditText editText = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("评论内容");
        editText.setHeight(240);
        editText.setText(comment);
        builder.setView(editText);
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void dialogEditComment(final String bookNumber, final String userAcc) {
        final EditText editText = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("评论内容");
        editText.setHeight(240);
        builder.setView(editText);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BookCommentActivity.this, editText.getText().toString() + "  发送成功！", Toast.LENGTH_LONG).show();
                CrateEva(bookNumber,editText.getText().toString(),userAcc);
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
        Book book = contentList.get((Integer) v.getTag());
        String bookNumber=book.BookNumber;
        dialogEditReport(bookNumber);
    }
    private void dialogEditReport(final String bookNum) {
        final EditText editText = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("举报详情");
        editText.setHeight(240);
        builder.setView(editText);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BookCommentActivity.this, editText.getText().toString() + "  发送成功！", Toast.LENGTH_LONG).show();
                CreateReport(editText.getText().toString(),bookNum,user.UserAccount);
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
    public void retransferClick(View v){
        Book book = contentList.get((Integer) v.getTag());
        PubBookNum=book.BookNumber;
        GetPublishResearch(PubBookNum,user.UserAccount);
        //Toast.makeText(BookCommentActivity.this,  "发布成功！", Toast.LENGTH_LONG).show();
    }
}
