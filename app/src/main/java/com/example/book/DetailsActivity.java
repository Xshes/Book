package com.example.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends Activity{
    private TextView nameOfBook;
    private TextView authorOfBook;

    String bookNum;
    String bookName;
    String bookAuthor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();//获取传来的intent对象
        // TODO: 2018/5/31 图片传输过来
        bookNum = intent.getStringExtra("bookNum");
        bookName = intent.getStringExtra("bookName");
        bookAuthor = intent.getStringExtra("bookAuthor");
        setContentView(R.layout.activity_details);

        nameOfBook=findViewById(R.id.book_Name);
        authorOfBook=findViewById(R.id.book_author);

        bookName=nameOfBook.getText()+bookName;
        bookAuthor=authorOfBook.getText()+bookAuthor;
        nameOfBook.setText(bookName);
        authorOfBook.setText(bookAuthor);

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
    // TODO: 2018/5/31 书籍评论用列表显示
}
