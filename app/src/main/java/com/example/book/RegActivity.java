package com.example.book;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegActivity extends Activity {
    private Button button_submit;
    private TextView id;
    private TextView name;
    private TextView password;
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
                    Toast.makeText(RegActivity.this, "数据为:" + temp1+temp2+temp3, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
