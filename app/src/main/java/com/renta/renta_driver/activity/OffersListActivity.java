package com.renta.renta_driver.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.R;
import com.renta.renta_driver.adapter.OfferListRecyclerViewAdapter;
import com.renta.renta_driver.model.car.Car;
import com.renta.renta_driver.model.transaction.Transaction;
import com.renta.renta_driver.presenter.OffersPresenter;
import com.renta.renta_driver.utils.GeneralUtils;
import com.renta.renta_driver.view.OffersView;

import java.util.List;

import butterknife.BindView;

public class OffersListActivity extends BaseActivity implements OffersView {

    @BindView(R.id.offersListView)
    RecyclerView mOffersListView;

    private OffersPresenter mOffersPresenter;
    private OfferListRecyclerViewAdapter mOfferListRecyclerViewAdapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, OffersListActivity.class);
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
        super.onResume();
        mOffersPresenter.getAllPendingTransactions();
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_offers_list;
    }

    @Override
    public void onGetAllAvailableTransactionSuccess(List<Transaction> transactionList) {
        Log.e("asd", "onGetAllAvailableTransactionSuccess: " + transactionList.size() );
        mOfferListRecyclerViewAdapter = new OfferListRecyclerViewAdapter(mContext, transactionList, new OfferListRecyclerViewAdapter.OnClickTransactionListener() {
            @Override
            public void OnTransactionSelected(Transaction transaction) {
                startActivity(OfferActivity.newIntent(mContext, transaction));
                finish();
            }
        });


        GeneralUtils.setDefaultRecyclerView(mContext, mOffersListView, mOfferListRecyclerViewAdapter);
    }

    @Override
    public void onGetAllAvailableTransactionError() {
        Toast.makeText(mContext, "Error connecting to server", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoTransactionsPending() {
        Toast.makeText(mContext, "No transactions available.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateTransactionSuccess() {
        //Ignore
    }

    @Override
    public void onUpdateTransactionError() {
        //Ignore
    }

    private void initPresenter() {
        mOffersPresenter = new OffersPresenter(mContext, this);
    }

}
