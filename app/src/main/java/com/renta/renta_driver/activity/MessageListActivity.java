package com.renta.renta_driver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.R;
import com.renta.renta_driver.adapter.MessageRecyclerViewAdapter;
import com.renta.renta_driver.model.conversation.MessageList;
import com.renta.renta_driver.presenter.MessagePresenter;
import com.renta.renta_driver.utils.Preferences;
import com.renta.renta_driver.view.MessageListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MessageListActivity extends BaseActivity implements MessageListView {

    @BindView(R.id.messageRecyclerView)
    RecyclerView mMessageRecyclerView;

    private MessagePresenter mMessagePresenter;
    private List<MessageList> mMessageLists;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MessageListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initPresenter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initUi();
        mMessagePresenter.getConversationList(Preferences.getString(mContext, Preferences.USER_ID));
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_message_list;
    }

    @Override
    public void onGetConversationSuccess(MessageList messageList) {

        mMessageLists.add(messageList);

        MessageRecyclerViewAdapter messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(mContext, mMessageLists,
                new MessageRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(MessageList messageList) {
                        startActivity(MessagesActivity.newIntent(mContext, messageList));
                    }
                });

        mMessageRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageRecyclerView.setLayoutManager(llm);
        mMessageRecyclerView.setAdapter(messageRecyclerViewAdapter);
        messageRecyclerViewAdapter.notifyDataSetChanged();

        Log.e("ITEM_COUNT", "onGetConversationSuccess: " + messageRecyclerViewAdapter.getItemCount() );
    }

    @Override
    public void onGetConversationError() {

    }

    @Override
    public void onNoConversation() {
        Toast.makeText(mContext, "There are no messages.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMessageSent() {

    }

    @Override
    public void onMessageNotSent() {

    }

    private void initPresenter() {
        mMessagePresenter = new MessagePresenter(mContext, this);
    }

    private void initUi() {
        mMessageLists = new ArrayList<>();
    }

}
