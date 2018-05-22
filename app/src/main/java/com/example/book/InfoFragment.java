package com.example.book;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by 72784 on 2018/5/8.
 */

public class InfoFragment extends Fragment{
    private ImageButton signature;
    private TextView nowsignature;
    private ImageButton name;
    private TextView nowname;
    private ImageButton transfer;
    private ImageButton read;
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
        name=(ImageButton) getActivity().findViewById(R.id.myname);
        transfer=(ImageButton) getActivity().findViewById(R.id.mytransfer);
        read=(ImageButton) getActivity().findViewById(R.id.myreading);
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
                Intent intent=new Intent(getActivity(),ReadBookActivity.class);
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
        builder.setTitle("更改签名");
        builder.setView(editText);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), editText.getText().toString() + "", Toast.LENGTH_LONG).show();
                nowname.setText(editText.getText());
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
