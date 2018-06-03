package com.example.book.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.book.Entity.Book;
import com.example.book.R;
import com.example.book.adapter.inter.InterClick;

import java.util.List;

public class BookAdapter extends BaseAdapter {
    private static final String TAG = "ContentAdapter";
    private List<Book> mContentList;
    private LayoutInflater mInflater;
    private InterClick mCallback;

    public BookAdapter(Context context, List<Book> contentList,
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
        BookAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.book_item, null);
            holder = new BookAdapter.ViewHolder();
            holder.textView = (TextView) convertView
                    .findViewById(R.id.book_name);
            holder.uptextView= (TextView) convertView
                    .findViewById(R.id.up_name);

            convertView.setTag(holder);
        } else {
            holder = (BookAdapter.ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mContentList.get(position).BookName);
        holder.uptextView.setText(mContentList.get(position).BookAuthor);
        return convertView;
    }

    public class ViewHolder {
        public TextView textView;
        public TextView uptextView;

    }

}
