package com.renta.renta_driver.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.R;

public class DashboardActivity extends BaseActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, DashboardActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_main;
    }
}
