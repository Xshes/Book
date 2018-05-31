package com.example.book;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;
import com.example.book.Entity.SimResult;
import com.google.gson.*;

import java.util.List;

public class MainActivity extends Activity {

    private EditText id_login;
    private EditText password_login;
    private ImageView avatar_login;
    private CheckBox rememberpassword_login;
    private CheckBox auto_login;
    private Button button_login;
    private Button button_reg;
    protected static SharedPreferences sp;
    private String idvalue;
    private String passwordvalue;
    private static final int PASSWORD_MIWEN = 0x81;

    Gson gson = new Gson();
    String userLogin="用户登录";
    String adminLogin="管理员登陆";
    String isBan="登陆失败，用户已被封禁";
    String json;
    MyApplication application = (MyApplication)this.getApplication();

    private Handler handler  = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    SwitchAct();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //找到相应的布局及控件
        setContentView(R.layout.activity_main);
        id_login=(EditText) findViewById(R.id.login_id);
        password_login=(EditText) findViewById(R.id.login_password);
        avatar_login=(ImageView) findViewById(R.id.login_avatar);
        rememberpassword_login=(CheckBox) findViewById(R.id.login_rememberpassword);
        auto_login=(CheckBox) findViewById(R.id.login_autologin);
        button_login=(Button) findViewById(R.id.login_button);
        button_reg=(Button)findViewById(R.id.reg_button);

        if (sp.getBoolean("ischeck",false)){
            rememberpassword_login.setChecked(true);
            id_login.setText(sp.getString("PHONEEDIT",""));
            password_login.setText(sp.getString("PASSWORD",""));
            //密文密码
            password_login.setInputType(PASSWORD_MIWEN);
            if (sp.getBoolean("auto_ischeck",false)){
                auto_login.setChecked(true);
                if (sp.getString("LOGINTYPE","").equals(userLogin)){
                    Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this,AdminActivity.class);
                    startActivity(intent);
                }
            }
        }

        button_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_login.getPaint().setFlags(0);
                idvalue=id_login.getText().toString();
                password_login.getPaint().setFlags(0);
                passwordvalue=password_login.getText().toString();
                Login();
            }
        });

        rememberpassword_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rememberpassword_login.isChecked()){
                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ischeck",true).commit();
                }
                else {
                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ischeck",false).commit();
                }
            }
        });

        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login.isChecked()){
                    System.out.println("自动登录已选中");
                    sp.edit().putBoolean("auto_ischeck",true).commit();
                }else {
                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("auto_ischeck",false).commit();
                }
            }
        });
    }

    public void Login(){
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    String loginURL=GetData.url+"User/Login?account="+idvalue+"&password="+passwordvalue;
                    json =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            };

        }.start();
    }

    public void SwitchAct(){
        SimResult result = gson.fromJson(json,SimResult.class);
        String res=result.result;
        if (res.equals(userLogin)){
            if (rememberpassword_login.isChecked()){
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("LOGINTYPE",userLogin);
                editor.putString("PHONEEDIT",idvalue);
                editor.putString("PASSWORD",passwordvalue);
                editor.commit();
            }
            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
            startActivity(intent);
            finish();
        }else if(res.equals(adminLogin))
        {
            if (rememberpassword_login.isChecked()){
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("LOGINTYPE",adminLogin);
                editor.putString("PHONEEDIT",idvalue);
                editor.putString("PASSWORD",passwordvalue);
                editor.commit();
            }
            Intent intent = new Intent(MainActivity.this,AdminActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "管理员登陆", Toast.LENGTH_SHORT).show();
        }
        else if(res.equals(isBan))
        {
            Toast.makeText(MainActivity.this, isBan, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MainActivity.this, "用户名或密码错误，请重新登录", Toast.LENGTH_SHORT).show();
        }
    }


}
