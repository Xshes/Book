package com.example.book;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.book.Entity.SimResult;
import com.example.book.Entity.User;
import com.google.gson.Gson;

/**
 * Created by 72784 on 2018/5/8.
 */

public class InfoFragment extends Fragment{
    private ImageButton signature;
    private TextView nowsignature;
    private TextView nameText;
    private ImageButton name;
    private TextView nowname;
    private ImageButton transfer;
    private ImageButton read;
    private ImageButton sysmessage;
    private ImageButton bookmessage;
    private ImageButton exit;

    User user;
    String json;
    Gson gson =new Gson();

    private Handler handler  = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    showResult();
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3, container, false);
        return view;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nowsignature=(TextView) getActivity().findViewById(R.id.signaturetext);
        signature=(ImageButton)getActivity().findViewById(R.id.mysignature);
        nowname=(TextView) getActivity().findViewById(R.id.nickname);
        nameText=(TextView) getActivity().findViewById(R.id.nameText);
        name=(ImageButton) getActivity().findViewById(R.id.myname);
        transfer=(ImageButton) getActivity().findViewById(R.id.mytransfer);
        read=(ImageButton) getActivity().findViewById(R.id.myreading);
        bookmessage=(ImageButton)getActivity().findViewById(R.id.mybookmessage);
        exit=(ImageButton) getActivity().findViewById(R.id.myexit);
        user=MainActivity.loginUser;
        nameText.setText(user.UserName);
        nowsignature.setText(user.UserSignature);

        bookmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),BookMessageActivity.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                SharedPreferences.Editor editor = MainActivity.sp.edit();
                editor.clear();
                editor.commit();
                startActivity(intent);
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),TransferBookActivity.class);
                startActivity(intent);
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),BookCommentActivity.class);
                startActivity(intent);
            }
        });
        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditText();
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditname();
            }
        });
    }
    public void editUserName( String userName)
    {
        final String name=userName;
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    String loginURL=GetData.url+"User/EditName?account="+user.UserAccount+"&userName="+name;
                    json =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            };

        }.start();
    }

    public void editSignature(String userSignature)
    {
        final String signature=userSignature;
        new Thread() {
            public void run() {
                try {
                    //构造连接字符串，并查询
                    String loginURL=GetData.url+"User/EditSignature?account="+user.UserAccount+"&userSignature="+signature;
                    json =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x002);
            };

        }.start();
    }

    private void dialogEditText() {
        final EditText editText = new EditText(getActivity());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),3);
        builder.setTitle("更改签名");
        builder.setView(editText);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), editText.getText().toString() + "", Toast.LENGTH_LONG).show();
                nowsignature.setText(editText.getText());
                editSignature(editText.getText().toString());
                MainActivity.loginUser.UserSignature=editText.getText().toString();
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
    private void dialogEditname() {
        final EditText editText = new EditText(getActivity());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),3);
        builder.setTitle("更改昵称");
        builder.setView(editText);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), editText.getText().toString() + "", Toast.LENGTH_LONG).show();
                nameText.setText(editText.getText());
                editUserName(editText.getText().toString());
                nowname.setText(editText.getText());
                MainActivity.loginUser.UserName=editText.getText().toString();
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
    public void showResult()
    {
        SimResult result = gson.fromJson(json,SimResult.class);
        Toast.makeText(this.getActivity(), result.result, Toast.LENGTH_LONG).show();
    }
}
