package com.renta.renta_driver.view;


import com.renta.renta_driver.model.conversation.MessageList;

import java.util.List;

public interface MessageListView {

    void onGetConversationSuccess(MessageList messageLists);
    void onGetConversationError();
    void onNoConversation();

    void onMessageSent();
    void onMessageNotSent();
}
