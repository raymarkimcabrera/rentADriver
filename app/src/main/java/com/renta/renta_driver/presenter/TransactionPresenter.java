package com.renta.renta_driver.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.renta.renta_driver.model.car.Car;
import com.renta.renta_driver.model.transaction.Transaction;
import com.renta.renta_driver.view.TransactionView;

import java.util.ArrayList;
import java.util.List;

public class TransactionPresenter extends BasePresenter {

    private Context mContext;
    private TransactionView mTransactionView;
    private List<Transaction> mTransactionList;

    public TransactionPresenter(Context mContext, TransactionView mTransactionView) {
        this.mContext = mContext;
        this.mTransactionView = mTransactionView;
    }

    public void getTransactions(String userID) {
        mTransactionList = new ArrayList<>();

        initFirebase();

        showProgressDialog(mContext);

        Query getTransactionsID = mFirebaseFirestore.collection("car")
                .whereEqualTo("driverID", userID);

        getTransactionsID.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() != 0) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Car car = documentSnapshot.toObject(Car.class);
                        Log.e("onSuccess:", "onSuccess: " + car.getTransactionID() );
                        if (!car.getTransactionID().isEmpty()){
                            mFirebaseFirestore.collection("transaction").document(car.getTransactionID())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Transaction transaction = documentSnapshot.toObject(Transaction.class);
                                            mTransactionView.onGetTransactions(transaction);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            hideProgressDialog();
                                            mTransactionView.onGetTransactionViewError();
                                        }
                                    });
                        }
                    }
                } else {
                    mTransactionView.onNoTransaction();
                }
                hideProgressDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();

                mTransactionView.onGetTransactionViewError();
            }
        });
    }


}
