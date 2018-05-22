package com.example.book;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class DetailsActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final PullUpToLoadMore ptlm= (PullUpToLoadMore) findViewById(R.id.ptlm);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptlm.scrollToTop();
            }
        });
    }
}
