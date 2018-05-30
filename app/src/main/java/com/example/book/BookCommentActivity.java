package com.example.book;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.book.adapter.BookCommentAdapter;
import com.example.book.adapter.inter.InterClick;

import java.util.ArrayList;
import java.util.List;

public class BookCommentActivity extends Activity implements AdapterView.OnItemClickListener,
        InterClick {
    private static final String[] CONTENTS = { "时间简史", "绝地求生", "暴走漫画" };
    private List<String> contentList;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_comment);

        init();
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.comment_listview);
        contentList = new ArrayList<String>();
        for (int i = 0; i < CONTENTS.length; i++) {
            contentList.add(CONTENTS[i]);
        }
        mListView.setAdapter(new BookCommentAdapter(this, contentList, this));
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
                BookCommentActivity.this,
                "listview的内部的评论按钮被点击了！，位置是-->" + (Integer) v.getTag()
                        + ",内容是-->" + contentList.get((Integer) v.getTag()),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void shareClick(View v) {
        Toast.makeText(
                BookCommentActivity.this,
                "listview的内部的分享按钮被点击了！，位置是-->" + (Integer) v.getTag()
                        + ",内容是-->" + contentList.get((Integer) v.getTag()),
                Toast.LENGTH_SHORT).show();
    }
}
