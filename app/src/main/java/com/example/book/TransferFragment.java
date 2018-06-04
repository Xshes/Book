package com.example.book;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.book.utils.PhotoUtils;

/**
 * Created by 72784 on 2018/5/8.
 */

public class TransferFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{
    private TextView bookname;
    private TextView bookauthor;

    private RadioGroup mRg1;
    private RadioGroup mRg2;

    private RadioButton mRb1;
    private RadioButton mRb2;
    private RadioButton mRb3;
    private RadioButton mRb4;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);
        return view;
    }
    private void initView() {
        mRg1 = (RadioGroup) getActivity().findViewById(R.id.rg1);
        mRg2 = (RadioGroup) getActivity().findViewById(R.id.rg2);

        mRb1 = (RadioButton) getActivity().findViewById(R.id.rb1);
        mRb2 = (RadioButton) getActivity().findViewById(R.id.rb2);
        mRb3 = (RadioButton) getActivity().findViewById(R.id.rb3);
        mRb4 = (RadioButton) getActivity().findViewById(R.id.rb4);

        mRb1.setOnCheckedChangeListener(this);
        mRb2.setOnCheckedChangeListener(this);
        mRb3.setOnCheckedChangeListener(this);
        mRb4.setOnCheckedChangeListener(this);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb1:
                mRg2.clearCheck();
                break;
            case R.id.rb2:
                mRg2.clearCheck();
                break;
            case R.id.rb3:
                mRg1.clearCheck();
                break;
            case R.id.rb4:
                mRg1.clearCheck();
                break;
            default:
                break;
        }
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        bookname = (TextView) getActivity().findViewById(R.id.book_name);
        bookauthor = (TextView) getActivity().findViewById(R.id.book_author);
        Button subutton = (Button) getActivity().findViewById(R.id.submit_button);
        Button bindbutton=(Button) getActivity().findViewById(R.id.book_binding);
        Bitmap bitmap = PhotoUtils.getBitmapFromUri(ChooseActivity.cropImageUri, getActivity());
        // TODO: 2018/6/4 bitmap 需要判空
        subutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp1 = bookname.getText().toString();
                String temp2 = bookauthor.getText().toString();
                if (TextUtils.isEmpty(temp1)||TextUtils.isEmpty(temp2)||mRg1.getCheckedRadioButtonId()==-1&&mRg2.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getActivity(), "请填写完整信息" , Toast.LENGTH_SHORT).show();
                }else if(mRg1.getCheckedRadioButtonId()!=-1){
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(mRg1.getCheckedRadioButtonId());
                    String selectText = radioButton.getText().toString();
                    Toast.makeText(getActivity(), "数据为:" + temp1+temp2+selectText, Toast.LENGTH_SHORT).show();
                }else if(mRg2.getCheckedRadioButtonId()!=-1) {
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(mRg2.getCheckedRadioButtonId());
                    String selectText = radioButton.getText().toString();
                    Toast.makeText(getActivity(), "数据为:" + temp1+temp2+selectText, Toast.LENGTH_SHORT).show();
                }
            }
        });
        bindbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity() , ChooseActivity.class);
                startActivity(i);
            }
        });
    }
}
