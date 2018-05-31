package com.example.book;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 72784 on 2018/5/8.
 */

public class SearchFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{
    private TextView searchname;
    private RadioGroup sRg1;
    private RadioGroup sRg2;

    private RadioButton JC;
    private RadioButton MZ;
    private RadioButton BJ;
    private RadioButton QT;
    String json;
    String param="";
    String selectText;

    private Handler handler  = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    ChangeAct();
                    break;
                default:
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);
        return view;

    }

    private void initView() {
        sRg1 = (RadioGroup) getActivity().findViewById(R.id.srg1);
        sRg2 = (RadioGroup) getActivity().findViewById(R.id.srg2);

        JC = (RadioButton) getActivity().findViewById(R.id.srb1);
        MZ = (RadioButton) getActivity().findViewById(R.id.srb2);
        BJ = (RadioButton) getActivity().findViewById(R.id.srb3);
        QT = (RadioButton) getActivity().findViewById(R.id.srb4);

        JC.setOnCheckedChangeListener(this);
        MZ.setOnCheckedChangeListener(this);
        BJ.setOnCheckedChangeListener(this);
        QT.setOnCheckedChangeListener(this);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.srb1:
                sRg2.clearCheck();
                break;
            case R.id.srb2:
                sRg2.clearCheck();
                break;
            case R.id.srb3:
                sRg1.clearCheck();
                break;
            case R.id.srb4:
                sRg1.clearCheck();
                break;
            default:
                break;
        }
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        searchname = (TextView) getActivity().findViewById(R.id.search_bar);
        Button search_button = (Button) getActivity().findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                param = searchname.getText().toString();
                // TODO: 2018/5/31 注释本段的Toast
                Toast.makeText(getActivity(), "数据1为:" + param, Toast.LENGTH_SHORT).show();
                if (sRg1.getCheckedRadioButtonId() != -1) {
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(sRg1.getCheckedRadioButtonId());
                    selectText = radioButton.getText().toString();
                    GetBookList();
                    Toast.makeText(getActivity(), "数据2为:" + selectText, Toast.LENGTH_SHORT).show();

                } else if (sRg2.getCheckedRadioButtonId() != -1) {
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(sRg2.getCheckedRadioButtonId());
                    selectText = radioButton.getText().toString();
                    GetBookList();
                    Toast.makeText(getActivity(), "数据2为:" + selectText, Toast.LENGTH_SHORT).show();
                } else {
                    GetBookList();
                }

            }
        });
    }
    public void GetBookList()
    {
        new Thread() {
            public void run() {
                try {
                    String loginURL;
                    //构造连接字符串，并查询
                    if(selectText!=null)
                        loginURL=GetData.url+"Book/search?param="+param+"&tag="+selectText;
                    else
                        loginURL=GetData.url+"Book/search?param="+param;
                    json =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            }
        }.start();
    }

    public void ChangeAct()
    {
        Intent intent = new Intent(getActivity(), BookActivity.class);
        intent.putExtra("json",json);
        startActivity(intent);
    }



}
