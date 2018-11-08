package com.renta.renta_driver.view;


import com.renta.renta_driver.model.driver.Driver;
import com.renta.renta_driver.model.user.User;

public interface UsersView {

    void onGetUserSuccess(Driver driver);

    void onGetUserError();
}
