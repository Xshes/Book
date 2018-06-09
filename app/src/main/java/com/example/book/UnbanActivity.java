package com.example.book;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.book.Entity.SimResult;
import com.example.book.Entity.StrResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UnbanActivity extends AppCompatActivity {

    private List<String> mStrs =new ArrayList<String>();
    private SearchView mSearchView;
    private ListView mListView;

    Gson gson =new Gson();
    String listJson;
    String unBanJson;
    private Handler handler  = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 0x001:
                    SetList();
                    break;
                case 0x002:
                    ShowUnBanResult();
                    break;
                default:
                    break;
            }
        }
    };

    private void SetList(){
        StrResult result=gson.fromJson(listJson,StrResult.class);
        mStrs=result.list;
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));
        mListView.setTextFilterEnabled(true);
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UnbanActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                SharedPreferences.Editor editor = MainActivity.sp.edit();
                editor.clear();
                editor.commit();
                startActivity(intent);
            }
        });

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogunban(mListView.getItemAtPosition(position).toString());
            }
        });

    }
    private void ShowUnBanResult(){
        SimResult result=gson.fromJson(unBanJson,SimResult.class);
        Toast.makeText(this, result.result, Toast.LENGTH_SHORT).show();
    }

    private void GetList(){
        new Thread() {
            public void run() {
                try {
                    String loginURL;
                    //构造连接字符串，并查询
                    loginURL=GetData.url+"Ban/GetList";
                    listJson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            }
        }.start();
    }

    private void UnBan(final String banObj){
        new Thread() {
            public void run() {
                try {
                    String loginURL;
                    //构造连接字符串，并查询
                    loginURL=GetData.url+"Ban/Delete?banObj="+banObj;
                    unBanJson =GetData.getJson(loginURL);
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
        setContentView(R.layout.activity_unban);
        GetList();


    }
    private void dialogunban(final String banObj){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("解除封禁");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UnBan(banObj);
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
}
