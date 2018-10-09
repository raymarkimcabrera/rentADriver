package com.renta.renta_driver.presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.renta.renta_driver.model.car.Car;
import com.renta.renta_driver.view.CarListView;

import java.util.ArrayList;
import java.util.List;

public class CarPresenter extends BasePresenter {

    private Context mContext;
    private CarListView mCarListView;

    public CarPresenter(Context mContext, CarListView mCarListView) {
        this.mContext = mContext;
        this.mCarListView = mCarListView;
    }

    public void getCars(String userID) {

        final List<Car> tempCarList = new ArrayList<>();
        showProgressDialog(mContext);

        initFirebase();

        Query getCar = mFirebaseFirestore.collection("car")
                .whereEqualTo("driverID", userID);

        getCar.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        tempCarList.add(documentSnapshot.toObject(Car.class));
                        Log.e("onSuccess", "onSuccess: " + documentSnapshot.toObject(Car.class));
                    }

                    hideProgressDialog();
                    mCarListView.onGetCarListSuccess(tempCarList);
                } else {
                    hideProgressDialog();
                    mCarListView.onNoCarList();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                mCarListView.onGetCarListError();
            }
        });
    }

    public void addCar(Car car) {

        initFirebase();

        DocumentReference newCar = mFirebaseFirestore.collection("car").document();
        car.setId(newCar.getId());

        showProgressDialog(mContext);

        mFirebaseFirestore.collection("car").document(newCar.getId())
                .set(car)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgressDialog();
                        mCarListView.onPostCarSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressDialog();
                        mCarListView.onPostCarError();
                    }
                });

    }
}
