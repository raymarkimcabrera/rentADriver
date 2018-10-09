package com.renta.renta_driver.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.R;
import com.renta.renta_driver.model.car.Car;
import com.renta.renta_driver.presenter.CarPresenter;
import com.renta.renta_driver.utils.Preferences;
import com.renta.renta_driver.view.CarListView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddCarActivity extends BaseActivity implements CarListView {

    @BindView(R.id.carModelEditText)
    EditText mCarModelEditText;

    @BindView(R.id.plateNumberEditText)
    EditText mPlateNumberEditText;

    private CarPresenter mCarPresenter;
    private Car mCar;

    public static Intent newIntent(Context context){
        Intent intent =  new Intent(context, AddCarActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initPresenter();
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_add_car;
    }

    @OnClick(R.id.addCarButton)
    void onClick(){
        prepareData();
        mCarPresenter.addCar(mCar);
    }

    private void prepareData() {
        mCar = new Car();
        mCar.setCarModel(mCarModelEditText.getText().toString());
        mCar.setPlateNumber(mPlateNumberEditText.getText().toString());
        mCar.setDriverID(Preferences.getString(mContext, Preferences.USER_ID));
    }

    public void initPresenter(){
        mCarPresenter = new CarPresenter(mContext, this);
    }

    @Override
    public void onGetCarListSuccess(List<Car> carList) {
        //Ignore
    }

    @Override
    public void onNoCarList() {
        //Ignore
    }

    @Override
    public void onGetCarListError() {
        //Ignore
    }

    @Override
    public void onPostCarSuccess() {
        startActivity(CarListActivity.newIntent(mContext));
        finish();
    }

    @Override
    public void onPostCarError() {
        Toast.makeText(mContext, "Error adding Car", Toast.LENGTH_SHORT).show();
    }
}
