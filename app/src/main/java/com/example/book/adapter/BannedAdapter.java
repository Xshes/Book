package com.example.book.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.book.R;
import com.example.book.adapter.inter.InterClick;

import java.util.List;

public class BannedAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = "ContentAdapter";
    private List<String> mContentList;
    private LayoutInflater mInflater;
    private InterClick mCallback;

    public BannedAdapter(Context context, List<String> contentList,
                              InterClick callback) {
        mContentList = contentList;
        mInflater = LayoutInflater.from(context);
        mCallback = callback;
    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount");
        return mContentList.size();
    }

    @Override
    public Object getItem(int position) {
        Log.i(TAG, "getItem");
        return mContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, "getItemId");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView");
        BannedAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.banned_item, null);
            holder = new BannedAdapter.ViewHolder();
            holder.textView = (TextView) convertView
                    .findViewById(R.id.unban_name);
            holder.button1 = (Button) convertView.findViewById(R.id.unban);
//            holder.button2 = (Button) convertView.findViewById(R.id.report);

            convertView.setTag(holder);
        } else {
            holder = (BannedAdapter.ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mContentList.get(position));
        holder.button1.setOnClickListener(this);
//        holder.button2.setOnClickListener(this);

        // 设置位置，获取点击的条目按钮
        holder.button1.setTag(position);
//        holder.button2.setTag(position);
        return convertView;
    }

    public class ViewHolder {
        public TextView textView;
        public Button button1;
//        public Button button2;

    }

    // 响应按钮点击事件,调用子定义接口，并传入View
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unban:
                mCallback.commentClick(v);
                break;
//            case R.id.report:
//                mCallback.reportClick(v);
//                break;
               default:
                break;
        }
    }
}
