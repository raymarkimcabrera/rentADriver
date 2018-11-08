package com.renta.renta_driver.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.renta.renta_driver.model.conversation.Message;
import com.renta.renta_driver.model.conversation.MessageList;
import com.renta.renta_driver.model.transaction.Transaction;
import com.renta.renta_driver.view.MessageListView;

import java.util.ArrayList;
import java.util.List;

public class MessagePresenter extends BasePresenter {

    private Context mContext;
    private List<MessageList> messageList;
    private MessageListView messageListView;

    public MessagePresenter(Context mContext, MessageListView messageListView) {
        this.mContext = mContext;
        this.messageListView = messageListView;
    }

    public void getConversationList(String userID) {
        messageList = new ArrayList<>();
        showProgressDialog(mContext);

        initFirebase();
        Log.e("", "getConversationList: USER_ID" + userID);
        Query getAllTransaction = mFirebaseFirestore.collection("transaction").whereEqualTo("offerAccepted.car.driverID", userID);

        getAllTransaction.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    for (final DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Transaction transaction = documentSnapshot.toObject(Transaction.class);

                        initFirebase();
                        mFirebaseFirestore.collection("messages").document(transaction.getConversationID()).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        MessageList message = documentSnapshot.toObject(MessageList.class);

                                        messageListView.onGetConversationSuccess(message);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        hideProgressDialog();
                                        Log.e("onFailure", "onFailure: " + e.getMessage());
                                        messageListView.onGetConversationError();
                                    }
                                });
                        if (messageList.size() > 0) {
                            Log.e("Message_List", messageList.size() + "");
                        }
                    }

                    hideProgressDialog();
                    Log.i("Message_List_AFTER", messageList.size() + "");
//                    for (MessageList thread : messageList) {
//                        Log.i("Message_List", thread.getId());
//                    }
                } else {
                    hideProgressDialog();
                    messageListView.onNoConversation();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Log.e("onFailure", "onFailure: " + e.getMessage());
                messageListView.onGetConversationError();
            }
        });
    }

    public void getConversation(String userID) {

        initFirebase();

        mFirebaseFirestore.collection("messages").document(userID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        MessageList message = documentSnapshot.toObject(MessageList.class);
                        messageListView.onGetConversationSuccess(message);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("onFailure", "onFailure: " + e.getMessage());
                        messageListView.onGetConversationError();
                    }
                });

    }

    public void sendMessage(MessageList messageList, Message message) {
        showProgressDialog(mContext);

        messageList.getThread().add(message);
        initFirebase();

        mFirebaseFirestore.collection("messages").document(messageList.getId())
                .set(messageList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgressDialog();
                        messageListView.onMessageSent();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressDialog();
                        messageListView.onMessageNotSent();
                    }
                });
    }
}
