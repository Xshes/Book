package com.example.book.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.book.Entity.Book;
import com.example.book.R;
import com.example.book.adapter.inter.InterClick;

import java.util.List;

public class BookCommentAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = "ContentAdapter";
    private List<Book> mContentList;
    private LayoutInflater mInflater;
    private InterClick mCallback;

    public BookCommentAdapter(Context context, List<Book> contentList,
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_comment_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView
                    .findViewById(R.id.bk_name);
            holder.author = (TextView) convertView
                    .findViewById(R.id.bk_author);
            holder.button1 = (Button) convertView.findViewById(R.id.comment);
            holder.button2 = (Button) convertView.findViewById(R.id.report);
            holder.button3=(Button)convertView.findViewById(R.id.retransfer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mContentList.get(position).BookName);
        holder.author.setText("作者："+mContentList.get(position).BookAuthor);
        holder.button1.setOnClickListener(this);
		holder.button2.setOnClickListener(this);
		holder.button3.setOnClickListener(this);

        // 设置位置，获取点击的条目按钮
        holder.button1.setTag(position);
		holder.button2.setTag(position);
		holder.button3.setTag(position);
        return convertView;
    }

    public class ViewHolder {
        public TextView textView;
        public TextView author;
        public Button button1;
        public Button button2;
        public Button button3;

    }

    // 响应按钮点击事件,调用子定义接口，并传入View
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment:
                mCallback.commentClick(v);
                break;
		    case R.id.report:
			mCallback.reportClick(v);
			break;
            case R.id.retransfer:
               mCallback.retransferClick(v);
               break;
            default:
                break;
        }
    }
}
