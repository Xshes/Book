package com.example.book;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SystemMessageActivity extends Activity{
    private List<SystemMessage> system_messageList = new ArrayList<SystemMessage>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        initSystemMessage();
        SystemMessageAdapter adapter = new SystemMessageAdapter(SystemMessageActivity.this, R.layout.system_message_item,system_messageList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

    }
    private void initSystemMessage() {
        SystemMessage wel = new SystemMessage("欢迎");
        system_messageList.add(wel);
        SystemMessage ban = new SystemMessage("封禁");
        system_messageList.add(ban);

    }
}
