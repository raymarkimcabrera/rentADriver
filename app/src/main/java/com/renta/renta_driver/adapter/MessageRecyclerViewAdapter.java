package com.renta.renta_driver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renta.renta_driver.R;
import com.renta.renta_driver.model.conversation.Message;
import com.renta.renta_driver.model.conversation.MessageList;
import com.renta.renta_driver.model.driver.Driver;
import com.renta.renta_driver.model.user.User;
import com.renta.renta_driver.presenter.UsersPresenter;
import com.renta.renta_driver.utils.ImageUtil;
import com.renta.renta_driver.utils.Preferences;
import com.renta.renta_driver.view.UsersView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder> implements UsersView {

    private List<MessageList> mMessageLists;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private UsersPresenter mUserPresenter;
    private MessageViewHolder mMessageViewHolder;

    public MessageRecyclerViewAdapter(Context context, List<MessageList> messageLists, OnItemClickListener onItemClickListener) {
        this.mMessageLists = messageLists;
        this.mContext = context;
        this.mOnItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClicked(MessageList messageList, String title);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        mMessageViewHolder = holder;
        final MessageList messageList = mMessageLists.get(position);

        mUserPresenter = new UsersPresenter(mContext, this);

        int latestMessageIndex = messageList.getThread().size() - 1;
        String driverName = "";
        String userId = "";

        for (Message message : messageList.getThread()) {
            if (!message.getSenderID().equals(Preferences.getString(mContext, Preferences.USER_ID))) {
                driverName = message.getSenderName();
                userId = message.getSenderID();
            }
        }

        mUserPresenter.getCustomerProfile(userId);

        mMessageViewHolder.mDriverTextView.setText(driverName);
        final String title = driverName;
        PrettyTime prettyTime = new PrettyTime();
        mMessageViewHolder.mLastUpdateTextView.setText(prettyTime.format(messageList.getThread().get(latestMessageIndex).getCreatedAt()));
        mMessageViewHolder.mMessageSummaryTextView.setText(messageList.getThread().get(latestMessageIndex).getContent());

        mMessageViewHolder.mMessageLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClicked(messageList, title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessageLists.size();
    }

    public void updateItems(List<MessageList> messageList){
        this.mMessageLists = messageList;
        notifyDataSetChanged();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.driverTextView)
        TextView mDriverTextView;

        @BindView(R.id.messageSummaryTextView)
        TextView mMessageSummaryTextView;

        @BindView(R.id.lastUpdateTextView)
        TextView mLastUpdateTextView;

        @BindView(R.id.messageLinearLayout)
        LinearLayout mMessageLinearLayout;

        @BindView(R.id.messageImageView)
        CircleImageView messageImageView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public void onGetUserSuccess(Driver driver) {

    }

    @Override
    public void onGetUserError() {

    }

    @Override
    public void onUserUpdateSuccess() {

    }

    @Override
    public void onUserUpdateError() {

    }

    @Override
    public void onGetCustomerProfileSuccess(User user) {
        if (!user.getImageUrl().isEmpty())
            ImageUtil.loadImageFromUrl(mContext, mMessageViewHolder.messageImageView, user.getImageUrl());
    }

    @Override
    public void onGetCustomerProfileError() {

    }
}
