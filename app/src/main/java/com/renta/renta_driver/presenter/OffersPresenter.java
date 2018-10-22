package com.renta.renta_driver.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.renta.renta_driver.model.transaction.Transaction;
import com.renta.renta_driver.view.OffersView;

import java.util.ArrayList;
import java.util.List;

public class OffersPresenter extends BasePresenter {

    private Context mContext;
    private OffersView mOffersView;

    public OffersPresenter(Context mContext, OffersView mOffersView) {
        this.mContext = mContext;
        this.mOffersView = mOffersView;
    }

    public void getAllPendingTransactions() {
        showProgressDialog(mContext);

        initFirebase();
        final List<Transaction> transactionList = new ArrayList<>();
        Query getPendingTransaction = mFirebaseFirestore.collection("transaction")
                .whereEqualTo("status", "PENDING");

        getPendingTransaction.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                transactionList.add(documentSnapshot.toObject(Transaction.class));
                            }

                            hideProgressDialog();
                            mOffersView.onGetAllAvailableTransactionSuccess(transactionList);
                        } else {
                            hideProgressDialog();
                            mOffersView.onNoTransactionsPending();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressDialog();
                        mOffersView.onGetAllAvailableTransactionError();
                    }
                });
    }

    public void updateTransaction(Transaction transaction) {
        showProgressDialog(mContext);

        initFirebase();

        mFirebaseFirestore.collection("transaction").document(transaction.getId())
                .set(transaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgressDialog();
                        mOffersView.onUpdateTransactionSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressDialog();
                        mOffersView.onUpdateTransactionError();

                    }
                });
    }
}
