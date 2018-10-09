package com.renta.renta_driver.view;

import com.renta.renta_driver.model.car.Car;

import java.util.List;

public interface CarListView {

    void onGetCarListSuccess(List<Car> carList);
    void onNoCarList();
    void onGetCarListError();

    void onPostCarSuccess();
    void onPostCarError();
}
