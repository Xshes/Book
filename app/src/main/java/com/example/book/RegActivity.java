package com.example.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.*;

import com.example.book.Entity.SimResult;

public class RegActivity extends Activity {
    private Button button_submit;
    private TextView id;
    private TextView name;
    private TextView password;
    Gson gson = new Gson();
    String json;

    private Handler handler  = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    RegResult();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        button_submit=(Button)findViewById(R.id.suj_button);
        id=(TextView)findViewById(R.id.reg_id);
        name=(TextView)findViewById(R.id.reg_name);
        password=(TextView)findViewById(R.id.reg_password);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp1=id.getText().toString();
                String temp2=name.getText().toString();
                String temp3=password.getText().toString();
                if (TextUtils.isEmpty(temp1)||TextUtils.isEmpty(temp2)||TextUtils.isEmpty(temp3)){
                    Toast.makeText(RegActivity.this, "请填写完整信息" , Toast.LENGTH_SHORT).show();
                }else {
                    reg(temp1,temp2,temp3);
                }

            }
        });
    }

    public void reg(String account,String name,String password)
    {
        //构造连接字符串，并查询
        final String loginURL=GetData.url+"User/register?account="+account+"&password="+password+"&userName="+name;
        new Thread() {
            public void run() {
                try {
                    json =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            };

        }.start();
    }

    public void RegResult()
    {
        SimResult result = gson.fromJson(json,SimResult.class);
        String res=result.result;
        Toast.makeText(RegActivity.this, res , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegActivity.this,MainActivity.class);
        startActivity(intent);

    }
}
