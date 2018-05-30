package com.example.book;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.book.adapter.inter.InterClick;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookMessageActivity extends Activity implements AdapterView.OnItemClickListener,InterClick{


        // 模拟listview中加载的数据
        private static final String[] CONTENTS = { "一条鱼","一只狗" };
        private List<String> contentList;
        private ListView mListView;
        private Set<SwipeListLayout> sets = new HashSet();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_book_message);
            init();
        }

        private void init() {
            mListView = (ListView) findViewById(R.id.listview);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (sets.size() > 0) {
                                for (SwipeListLayout s : sets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    sets.remove(s);
                                }
                            }
                            break;

                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });
            contentList = new ArrayList<String>();
            for (int i = 0; i < CONTENTS.length; i++) {
                contentList.add(CONTENTS[i]);
            }
            mListView.setAdapter(new ContentAdapter(this, contentList, this));
            mListView.setOnItemClickListener(this);
        }

        /**
         * 响应ListView的条目点击事件
         */
        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
            Toast.makeText(this, "点击的条目位置是-->" + position, Toast.LENGTH_SHORT)
                    .show();
        }

        /**
         * 接口方法，响应ListView按钮点击事件
         */

        @Override
        public void commentClick(View v) {
            Toast.makeText(
                    BookMessageActivity.this,
                    "listview的内部的同意按钮被点击了！，位置是-->" + (Integer) v.getTag()
                            + ",内容是-->" + contentList.get((Integer) v.getTag()),
                    Toast.LENGTH_SHORT).show();
            dialogEditMessage();
        }

    private void dialogEditMessage() {
        final EditText editText = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("发送消息(确定转让的时间地点)");
        editText.setHeight(240);
        builder.setView(editText);
        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BookMessageActivity.this, editText.getText().toString() + "  发送成功！", Toast.LENGTH_LONG).show();

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

        @Override
        public void reportClick(View v) {
            Toast.makeText(
                    BookMessageActivity.this,
                    "listview的内部的分享按钮被点击了！，位置是-->" + (Integer) v.getTag()
                            + ",内容是-->" + contentList.get((Integer) v.getTag()),
                    Toast.LENGTH_SHORT).show();
        }
    public  class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }
    class ContentAdapter extends BaseAdapter implements View.OnClickListener {

        private static final String TAG = "ContentAdapter";
        private List<String> mContentList;
        private LayoutInflater mInflater;
        private InterClick mCallback;

        public ContentAdapter(Context context, List<String> contentList,
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "getView");
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.book_message_item, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView
                        .findViewById(R.id.tv_name);
                holder.button1 = (Button) convertView.findViewById(R.id.request_yes);
                //holder.button2 = (Button) convertView.findViewById(R.id.button2);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final SwipeListLayout sll_main = (SwipeListLayout) convertView.findViewById(R.id.sll_main);
            holder.textView.setText(mContentList.get(position));
            holder.button1.setOnClickListener(this);
//		holder.button2.setOnClickListener(this);

            // 设置位置，获取点击的条目按钮
            holder.button1.setTag(position);
//		holder.button2.setTag(position);
            TextView tv_top = (TextView) convertView.findViewById(R.id.tv_top);
            TextView tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            sll_main.setOnSwipeStatusListener(new MyOnSlipStatusListener(
                    sll_main));
            tv_top.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    String str = contentList.get(position);
                    contentList.remove(position);
                    contentList.add(0, str);
                    notifyDataSetChanged();
                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    contentList.remove(position);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        public class ViewHolder {
            public TextView textView;
            public Button button1;
            public Button button2;

        }

        // 响应按钮点击事件,调用子定义接口，并传入View
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.request_yes:
                    mCallback.commentClick(v);
                    break;
//		case R.id.button2:
//			mCallback.shareClick(v);
//			break;
                default:
                    break;
            }
        }
    }
}
