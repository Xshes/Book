package com.example.book;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.book.adapter.AdminBanAdapter;
import com.example.book.adapter.BookAdapter;

import java.util.ArrayList;
import java.util.List;

import com.example.book.Entity.Book;
import com.example.book.adapter.inter.InterClick;

public class TransferBookActivity extends Activity implements AdapterView.OnItemClickListener,
        InterClick {
    private static final String[] CONTENTS = { "一条鱼", "一只狗", "一个壮汉" };
    private List<String> contentList;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        init();
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.book_listview);
        contentList = new ArrayList<String>();
        for (int i = 0; i < CONTENTS.length; i++) {
            contentList.add(CONTENTS[i]);
        }
        mListView.setAdapter(new BookAdapter(this, contentList, this));
        mListView.setOnItemClickListener(this);
    }

    /**
     * 响应ListView的条目点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Toast.makeText(this, "点击的条目位置是-->" + position, Toast.LENGTH_SHORT)
                .show();
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