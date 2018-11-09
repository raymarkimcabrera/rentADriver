package com.renta.renta_driver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.R;
import com.renta.renta_driver.model.conversation.Message;
import com.renta.renta_driver.model.conversation.MessageList;
import com.renta.renta_driver.model.driver.Driver;
import com.renta.renta_driver.model.user.User;
import com.renta.renta_driver.presenter.MessagePresenter;
import com.renta.renta_driver.presenter.UsersPresenter;
import com.renta.renta_driver.utils.ImageUtil;
import com.renta.renta_driver.utils.Preferences;
import com.renta.renta_driver.view.MessageListView;
import com.renta.renta_driver.view.UsersView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesActivity extends BaseActivity implements MessageListView, UsersView {

    @BindView(R.id.messagesLinearLayout)
    LinearLayout mMessagesLinearLayout;

    @BindView(R.id.textMessageEditText)
    EditText mTextMessageEditText;

    private MessageList mMessageList;
    private MessagePresenter mMessagePresenter;
    private UsersPresenter mUsersPresenter;
    private CircleImageView mAvatarImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initPresenter();

        getArgs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessagePresenter.getConversation(mMessageList.getId());
                Log.e("MESSAGES_THREAD", "run: ");
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void initPresenter() {
        mMessagePresenter = new MessagePresenter(mContext, this);
        mUsersPresenter = new UsersPresenter(mContext, this);
    }

    public static Intent newIntent(Context context, MessageList messageList, String title) {
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("THREAD", messageList);
        intent.putExtra("TITLE", title);
        return intent;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_messages;
    }

    private void getArgs() {
        mMessageList = (MessageList) getIntent().getSerializableExtra("THREAD");
//        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("TITLE"));
    }

    private void initUi() {
        mMessagesLinearLayout.removeAllViews();
        PrettyTime prettyTime = new PrettyTime();
        for (Message message : mMessageList.getThread()) {
            View driverMessageView = LayoutInflater.from(mContext).inflate(R.layout.list_item_driver_message, null);
            View userMessageView = LayoutInflater.from(mContext).inflate(R.layout.list_item_user_message, null);

            if (!message.getContent().isEmpty()) {

                View messageView;
                if (message.getSenderID().equals(Preferences.getString(mContext, Preferences.USER_ID))){
                    messageView = userMessageView;

                }
                else{
                    messageView = driverMessageView;
                }

                TextView contentTextView = (TextView) messageView.findViewById(R.id.contentTextView);
                TextView createdAtTextView = (TextView) messageView.findViewById(R.id.createdAtTextView);
                mAvatarImageView = (CircleImageView) messageView.findViewById(R.id.avatarImageView);

                contentTextView.setText(message.getContent());
                createdAtTextView.setText(prettyTime.format(message.getCreatedAt()));

                mMessagesLinearLayout.addView(messageView);
                mMessagesLinearLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMessagesLinearLayout.getChildAt(mMessagesLinearLayout.getChildCount() -1 ).requestFocus();
                    }
                }, 1000);
            }

        }

    }

    @Override
    public void onGetConversationSuccess(MessageList messageLists) {
        mMessageList = messageLists;
        initUi();
    }

    @Override
    public void onGetConversationError() {

    }

    @Override
    public void onNoConversation() {

    }

    @Override
    public void onMessageSent() {
        initUi();
        mTextMessageEditText.setText("");
    }

    @Override
    public void onMessageNotSent() {

    }

    @OnClick(R.id.sendImageView)
    void onClick() {
        mUsersPresenter.getUserProfile(Preferences.getString(mContext, Preferences.USER_ID));
    }

    @Override
    public void onGetUserSuccess(Driver user) {

            String message = mTextMessageEditText.getText().toString();
            Date currentDate = new Date(System.currentTimeMillis());
            String userId = Preferences.getString(mContext, Preferences.USER_ID);

            Message messageBody = new Message();

            messageBody.setContent(message);
            messageBody.setSenderID(userId);
            messageBody.setCreatedAt(currentDate);
            messageBody.setSenderName(user.getFirstName() +" "+ user.getLastName());

            mMessagePresenter.sendMessage(mMessageList, messageBody);
    }

    @Override
    public void onGetUserError() {
        Toast.makeText(mContext, "Failed to connect to server. Please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserUpdateSuccess() {

    }

    @Override
    public void onUserUpdateError() {

    }

    @Override
    public void onGetCustomerProfileSuccess(User user) {

    }

    @Override
    public void onGetCustomerProfileError() {

    }
}
