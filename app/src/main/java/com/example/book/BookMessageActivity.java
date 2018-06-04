package com.example.book;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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


import com.example.book.Entity.MessageResult;
import com.example.book.Entity.SimResult;
import com.example.book.Entity.User;
import com.example.book.Entity.UserMessage;
import com.example.book.adapter.inter.InterClick;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookMessageActivity extends Activity implements AdapterView.OnItemClickListener,InterClick{
    // 模拟listview中加载的数
    private List<UserMessage> contentList;
    private ListView mListView;
    private Set<SwipeListLayout> sets = new HashSet();

    Gson gson = new Gson();
    String messageJson;
    String SendJson;
    User user;

    private Handler handler  = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 0x001:
                    SetMessage();
                    break;
                case 0x002:
                    ShowSendMessageResult();
                    break;
                case 0x003:
                    ShowChangeMessageResult();
                    break;
                default:
                    break;
            }
        }
    };

    private void SetMessage(){
        MessageResult strResult=gson.fromJson(messageJson,MessageResult.class);
        contentList=strResult.list;
        init();
    }
    private void ShowSendMessageResult(){
        SimResult simResult=gson.fromJson(SendJson,SimResult.class);
        Toast.makeText(BookMessageActivity.this, simResult.result , Toast.LENGTH_LONG).show();
    }
    private void ShowChangeMessageResult(){

    }

    private void GetMessage(){
        new Thread() {
            public void run() {
                try {
                    String loginURL;
                    //构造连接字符串，并查询
                    loginURL=GetData.url+"Message/GetListByReceivePer?receiveAcc="+user.UserAccount;
                    messageJson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x001);
            }
        }.start();
    }

    private void SendMessage(final UserMessage message,final String mess){
        new Thread() {
            public void run() {
                try {
                    String loginURL;
                    //构造连接字符串，并查询
                    loginURL=GetData.url+"Message/Create?publishAcc="+user.UserAccount+"&receiveAcc="+message.PublishPer+"&message="+mess+"&publishName="+user.UserName;
                    String Json =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x002);
            }
        }.start();
    }

    private void ChangeMessageState(final UserMessage message ){
        new Thread() {
            public void run() {
                try {
                    String loginURL;
                    //构造连接字符串，并查询
                    loginURL=GetData.url+"Message/ChangeState?messageId="+message.MessageId+"&state=已读";
                    SendJson =GetData.getJson(loginURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x003);
            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user=MainActivity.loginUser;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_message);
        GetMessage();

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
        UserMessage message=contentList.get((Integer) v.getTag());
        dialogEditMessage(message);
    }

    private void dialogEditMessage(final UserMessage message) {
        final EditText editText = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("发送消息(确定转让的时间地点)");
        editText.setHeight(240);
        builder.setView(editText);
        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SendMessage(message,editText.getText().toString());
                ChangeMessageState(message);
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
    @Override
    public void retransferClick(View v){
        Toast.makeText(this,  "发布成功！", Toast.LENGTH_LONG).show();
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
        private List<UserMessage> mContentList;
        private LayoutInflater mInflater;
        private InterClick mCallback;

        public ContentAdapter(Context context, List<UserMessage> contentList,
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
                holder.textView2 = (TextView) convertView
                        .findViewById(R.id.tv1);
                holder.button1 = (Button) convertView.findViewById(R.id.request_yes);
                //holder.button2 = (Button) convertView.findViewById(R.id.button2);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final SwipeListLayout sll_main = (SwipeListLayout) convertView.findViewById(R.id.sll_main);
            holder.textView.setText(mContentList.get(position).PublishName);
            holder.textView2.setText(mContentList.get(position).MessageContext);
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
                    UserMessage str = contentList.get(position);
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
                    // TODO: 2018/6/4 position 有问题 
                    UserMessage str = contentList.get(position);
                    ChangeMessageState(str);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        public class ViewHolder {
            public TextView textView;
            public TextView textView2;
            public Button button1;
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
