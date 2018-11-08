package com.renta.renta_driver.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.renta.renta_driver.model.driver.Driver;
import com.renta.renta_driver.view.UsersView;

public class UsersPresenter extends BasePresenter {

    private Context mContext;
    private UsersView mUsersView;

    public UsersPresenter(Context context, UsersView usersView) {
        this.mContext = context;
        this.mUsersView = usersView;
    }

    public void getUserProfile(String id) {
        showProgressDialog(mContext);

        initFirebase();

        mFirebaseFirestore.collection("drivers").document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        hideProgressDialog();

                        Driver user = documentSnapshot.toObject(Driver.class);
                        Log.e("onGetUserSuccess", "onFailure: " + user);
                        mUsersView.onGetUserSuccess(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressDialog();
                        Log.e("onGetUserError", "onFailure: ");
                        mUsersView.onGetUserError();
                    }
                });
    }
}
