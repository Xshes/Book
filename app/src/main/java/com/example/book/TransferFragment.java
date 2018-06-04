package com.example.book;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.book.Entity.Book;
import com.example.book.Entity.CreateBook;
import com.example.book.Entity.SimResult;
import com.example.book.Entity.User;
import com.example.book.utils.PhotoUtils;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

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
    Gson gson = new Gson();
    String createJson;
    User user;
    private Handler handler  = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    ShowCreate();
                    break;
                default:
                    break;
            }
        }
    };

    //创建新的书籍
    private void CreateBook(final String acc,final String author,final String bookName,final String tag,final byte[] cover){
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    CreateBook book= new CreateBook(tag,author,cover,bookName,acc);
                    String coverjson =gson.toJson(cover,byte[].class);
                    OkHttpClient okHttpClient=new OkHttpClient();
                    FormBody form = new FormBody.Builder()
                            .add("tag",tag)
                            .add("author",author)
                            .add("cover",coverjson)
                            .add("bookName",bookName)
                            .add("account",acc)
                            .build();

                    Request request=new Request.Builder()
                            .url(GetData.url+"Book/Create")
                            .post(form)
                            .build();
                    try {
                        Response response=okHttpClient.newCall(request).execute();
                        createJson=response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);

            }

        }.start();
    }

    //展示创建结果
    private void ShowCreate()
    {
        SimResult result = gson.fromJson(createJson,SimResult.class);
        Toast.makeText(getActivity(), result.result, Toast.LENGTH_SHORT).show();
    }
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
        subutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取bitmap并转化
                Bitmap bitmap = PhotoUtils.getBitmapFromUri(ChooseActivity.cropImageUri, getActivity());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] cover=baos.toByteArray();

                user=MainActivity.loginUser;
                String temp1 = bookname.getText().toString();
                String temp2 = bookauthor.getText().toString();
                if (TextUtils.isEmpty(temp1)||TextUtils.isEmpty(temp2)||mRg1.getCheckedRadioButtonId()==-1&&mRg2.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getActivity(), "请填写完整信息！" , Toast.LENGTH_SHORT).show();
                }else if(bitmap==null){
                    Toast.makeText(getActivity(), "请上传封面！" , Toast.LENGTH_SHORT).show();
                }
                else if(mRg1.getCheckedRadioButtonId()!=-1){
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(mRg1.getCheckedRadioButtonId());
                    String selectText = radioButton.getText().toString();
                    CreateBook(user.UserAccount,temp2,temp2,selectText,cover);
                }else if(mRg2.getCheckedRadioButtonId()!=-1) {
                    RadioButton radioButton = (RadioButton) getActivity().findViewById(mRg2.getCheckedRadioButtonId());
                    String selectText = radioButton.getText().toString();
                    CreateBook(user.UserAccount,temp2,temp2,selectText,cover);
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
