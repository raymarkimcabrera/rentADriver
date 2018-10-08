package com.renta.renta_driver.view;

import com.google.firebase.firestore.DocumentReference;

public interface RegisterView {

    void onRegisterSuccess(DocumentReference documentReference);

    void onRegisterFailed(String message);
}
