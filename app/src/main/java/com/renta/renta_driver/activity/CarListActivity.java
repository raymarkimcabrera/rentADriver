package com.renta.renta_driver.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.R;
import com.renta.renta_driver.adapter.CarListRecyclerViewAdapter;
import com.renta.renta_driver.model.car.Car;
import com.renta.renta_driver.presenter.CarPresenter;
import com.renta.renta_driver.utils.GeneralUtils;
import com.renta.renta_driver.utils.Preferences;
import com.renta.renta_driver.view.CarListView;

import java.util.List;

import butterknife.BindView;

public class CarListActivity extends BaseActivity implements CarListView, CarListRecyclerViewAdapter.OnClickCarListener {
    private static final String TAG = CarListActivity.class.getSimpleName();

    @BindView(R.id.carsRecyclerView)
    RecyclerView mCarsRecyclerView;

    @BindView(R.id.noCarLinearLayout)
    LinearLayout mNoCarLinearLayout;

    private CarPresenter mCarPresenter;
    private CarListRecyclerViewAdapter mCarListRecyclerViewAdapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CarListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initPresenter();

    }

    @Override
    protected void onResume() {
        mCarPresenter.getCars(Preferences.getString(mContext, Preferences.USER_ID));
        super.onResume();
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_car_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_car_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addCarItem:
                startActivity(AddCarActivity.newIntent(mContext));
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onGetCarListSuccess(List<Car> carList) {
        Log.e(TAG, "onGetCarListSuccess: " + carList.size() );
        mCarsRecyclerView.setVisibility(View.VISIBLE);
        mNoCarLinearLayout.setVisibility(View.GONE);
        mCarListRecyclerViewAdapter = new CarListRecyclerViewAdapter(mContext, carList, this);

        mCarsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mCarsRecyclerView.setLayoutManager(llm);
        mCarsRecyclerView.setAdapter(mCarListRecyclerViewAdapter);
        mCarListRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoCarList() {
        mCarsRecyclerView.setVisibility(View.GONE);
        mNoCarLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetCarListError() {
        mCarsRecyclerView.setVisibility(View.GONE);
        mNoCarLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void onPostCarSuccess() {
        //Ignore
    }

    @Override
    public void onPostCarError() {
        //Ignore
    }

    private void initPresenter() {
        mCarPresenter = new CarPresenter(mContext, this);
    }

    @Override
    public void OnCarSelected(Car car) {

    }
}
