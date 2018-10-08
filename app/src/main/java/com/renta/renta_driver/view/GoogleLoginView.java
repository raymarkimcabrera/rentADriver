package com.renta.renta_driver.view;

import com.google.firebase.auth.FirebaseUser;

public interface GoogleLoginView {

    void onGmailLoginSuccess(FirebaseUser user);

    void onGmailLoginError(String message);
}
