package com.renta.renta_driver.presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

    public void addCar(final Car car, byte[] image) {

        initFirebase();
        initFireStorage();
        showProgressDialog(mContext);

        final DocumentReference newCar = mFirebaseFirestore.collection("car").document();
        car.setId(newCar.getId());
        final String carImageFileUrl = car.getId() + ".jpg";

        final StorageReference carImageReference = mFirebaseStorage.getReference().child(carImageFileUrl);

        UploadTask uploadTask = carImageReference.putBytes(image);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }


                // Continue with the task to get the download URL
                return carImageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    car.setImageUrl(downloadUri.toString());
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
                } else {
                    hideProgressDialog();
                    mCarListView.onPostCarError();
                }
            }
        });




    }
}
