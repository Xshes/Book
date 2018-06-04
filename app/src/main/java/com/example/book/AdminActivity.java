package com.example.book;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.NoCopySpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.book.Entity.SimResult;
import com.example.book.Entity.StrResult;
import com.example.book.adapter.AdminBanAdapter;
import com.example.book.adapter.inter.InterClick;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends Activity implements AdapterView.OnItemClickListener,
        InterClick {
    public static List<String> contentList;
    private ListView mListView;

    String listJson;
    String banJson;
    int index=0;
    Gson gson = new Gson();
    private Handler handler  = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 0x001:
                    SetList();
                    break;
                case 0x002:
                    ShowBanResult();
                    break;
                default:
                    break;
            }
        }
    };
    public void GetBanedList()    {
        new Thread() {
            public void run() {
                try {
                    String loginURL;
                    //构造连接字符串，并查询
                    loginURL=GetData.url+"Report/GetList";
                    listJson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            }
        }.start();
    }
    private void SetList(){
        StrResult result=gson.fromJson(listJson,StrResult.class);
        contentList=result.list;
        init();
    }

    private void Ban(final String banDuration,final String banInitiator,final String banObject){
        new Thread() {
            public void run() {
                try {
                    String loginURL;
                    //构造连接字符串，并查询
                    loginURL=GetData.url+"Ban/Create?banDuration="+banDuration+"&banInitiator="+banInitiator+"&banObject="+banObject;
                    banJson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x002);
            }
        }.start();
    }

    private void ShowBanResult(){
        SimResult result=gson.fromJson(banJson,SimResult.class);
        Toast.makeText(this, result.result, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        GetBanedList();
        findViewById(R.id.jumpto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,UnbanActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.admin_listview);
        mListView.setAdapter(new AdminBanAdapter(this, contentList, this));
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
        String account=contentList.get((Integer) v.getTag());
        Intent intent = new Intent(this,ReportDetailActivity.class);
        intent.putExtra("account",account);
        startActivity(intent);
    }

    private void dialogEditReport(final String account) {
        final String items[] = {"一天", "一周", "一个月", "半年", "永久封禁"};
        AlertDialog dialog = new AlertDialog.Builder(this)

                .setTitle("封禁时长选择")//设置对话框的标题
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AdminActivity.this, items[which], Toast.LENGTH_SHORT).show();
                        index=which;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String during=items[index];
                        Ban(during,"admin",account);
                        AdminBanAdapter.DeleteItem(account);
                        dialog.dismiss();
                        onCreate(null);
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void reportClick(View v) {
        dialogEditReport(contentList.get((Integer) v.getTag()));
    }
    @Override
    public void retransferClick(View v){
        Toast.makeText(this,  "发布成功！", Toast.LENGTH_LONG).show();

    }
}