package com.example.book;

import android.content.Intent;
import android.os.Bundle;
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

    private RadioButton sRb1;
    private RadioButton sRb2;
    private RadioButton sRb3;
    private RadioButton sRb4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);
        return view;

    }

    private void initView() {
        sRg1 = (RadioGroup) getActivity().findViewById(R.id.srg1);
        sRg2 = (RadioGroup) getActivity().findViewById(R.id.srg2);

        sRb1 = (RadioButton) getActivity().findViewById(R.id.srb1);
        sRb2 = (RadioButton) getActivity().findViewById(R.id.srb2);
        sRb3 = (RadioButton) getActivity().findViewById(R.id.srb3);
        sRb4 = (RadioButton) getActivity().findViewById(R.id.srb4);

        sRb1.setOnCheckedChangeListener(this);
        sRb2.setOnCheckedChangeListener(this);
        sRb3.setOnCheckedChangeListener(this);
        sRb4.setOnCheckedChangeListener(this);
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
                String temp = searchname.getText().toString();
                if (!TextUtils.isEmpty(temp)){
                    Toast.makeText(getActivity(), "数据为:" + temp, Toast.LENGTH_SHORT).show();
                }else if(sRg1.getCheckedRadioButtonId()!=-1){
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(sRg1.getCheckedRadioButtonId());
                    String selectText = radioButton.getText().toString();
                    Toast.makeText(getActivity(), "数据为:" + selectText, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),BookActivity.class);
                    startActivity(intent);
                }else if(sRg2.getCheckedRadioButtonId()!=-1) {
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(sRg2.getCheckedRadioButtonId());
                    String selectText = radioButton.getText().toString();
                    Toast.makeText(getActivity(), "数据为:" + selectText, Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getActivity(), "没有数据输入", Toast.LENGTH_LONG).show();
            }
        });
    }
}
