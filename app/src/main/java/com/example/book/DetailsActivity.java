package com.example.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DetailsActivity extends Activity{
    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();//获取传来的intent对象
        json = intent.getStringExtra("json");
        setContentView(R.layout.activity_details);
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
}
