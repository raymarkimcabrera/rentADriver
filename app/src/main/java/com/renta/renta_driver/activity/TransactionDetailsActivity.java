package com.renta.renta_driver.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.R;
import com.renta.renta_driver.model.payment.Payment;
import com.renta.renta_driver.model.transaction.Transaction;
import com.renta.renta_driver.presenter.PaymentPresenter;
import com.renta.renta_driver.presenter.TransactionPresenter;
import com.renta.renta_driver.utils.GeneralUtils;
import com.renta.renta_driver.view.PaymentView;
import com.renta.renta_driver.view.TransactionView;
import com.renta.renta_driver.view.UsersView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TransactionDetailsActivity extends BaseActivity implements TransactionView, PaymentView {
    private static final String TAG = TransactionDetailsActivity.class.getSimpleName();

    @BindView(R.id.pickUpTextView)
    TextView mPickUpTextView;

    @BindView(R.id.destinationTextView)
    TextView mDestinationTextView;

    @BindView(R.id.pickUpDateTextView)
    TextView mPickUpDateTextView;

    @BindView(R.id.destinationDateTextView)
    TextView mDestinationDateTextView;

    @BindView(R.id.typeOfVehicleTextView)
    TextView mTypeOfVehicleTextView;

    @BindView(R.id.costTextView)
    TextView mCostTextView;

    @BindView(R.id.statusTextView)
    TextView mStatusTextView;

    @BindView(R.id.passengersTextView)
    TextView mPassengersTextView;

    @BindView(R.id.serialCodeTextView)
    TextView mSerialCodeTextView;


    @BindView(R.id.carModelTextView)
    TextView mCarModelTextView;


    private Transaction mTransaction;
    private Payment mPayment;
    private TransactionPresenter mTransactionPresenter;
    private PaymentPresenter mPaymentPresenter;

    public static Intent newIntent(Context context, Transaction transaction) {
        Intent intent = new Intent(context, TransactionDetailsActivity.class);
        intent.putExtra("TRANSACTION", transaction);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        getArgs();
        initToolbar();

        initializePresenter();
        Log.e(TAG, "onCreate: " + mTransaction.getPaymentID() );
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTransactionPresenter.getTransactionById(mTransaction.getId());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(TransactionHistoryActivity.newIntent(mContext));
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_transaction_details;
    }

    @Override
    public void onGetTransactionViewSuccess(List<Transaction> transactionList) {
        //Ignore
    }

    @Override
    public void onNoTransaction() {
        //Ignore
    }

    @Override
    public void onGetTransactionViewError() {
        //Ignore
    }

    @Override
    public void onTransactionStatusUpdateSuccess() {
        startActivity(ScheduleActivity.newIntent(mContext));
        finish();
    }


    @Override
    public void onTransactionStatusUpdateError() {
        Toast.makeText(mContext, "There is problem connecting to the server. Please try again.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetTransactions(Transaction transaction) {

    }

    @Override
    public void onGetTransaction(Transaction transaction) {
        mPaymentPresenter.getPaymentByID(transaction.getPaymentID());
    }

    @Override
    public void onGetTransactionError() {

    }

    @Override
    public void onGetPaymentSuccess(Payment payment) {
        mPayment = payment;
        initialize();
    }

    @Override
    public void onGetPaymentError() {

    }

    private void getArgs() {
        mTransaction = (Transaction) getIntent().getSerializableExtra("TRANSACTION");
//        Toast.makeText(mContext, mTransaction.getId(), Toast.LENGTH_SHORT).show();
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Transaction details");
        }
    }

    private void initialize() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm aaa");
        mPickUpTextView.setSelected(true);
        mDestinationTextView.setSelected(true);
        mPickUpTextView.setText(mTransaction.getPickUpAddress());
        mDestinationTextView.setText(mTransaction.getDestinationAddress());
        mPassengersTextView.setText(String.valueOf(mTransaction.getPassengers()));
        mStatusTextView.setText(mTransaction.getStatus());
        mTypeOfVehicleTextView.setText(mTransaction.getTypeOfVehicle());
        mSerialCodeTextView.setText(mTransaction.getId());
        mPickUpDateTextView.setText(simpleDateFormat.format(mTransaction.getStartDate()));
        mDestinationDateTextView.setText(simpleDateFormat.format(mTransaction.getEndDate()));
        Log.e(TAG, "initialize: " + mPayment );
        mCostTextView.setText(GeneralUtils.toPesoFormat(Double.parseDouble(String.valueOf(mTransaction.getOfferAccepted().getPrice()))));


        mCarModelTextView.setText(mTransaction.getOfferAccepted().getCar().getCarModel());



    }

    private void initializePresenter() {
        mTransactionPresenter = new TransactionPresenter(mContext, this);
        mPaymentPresenter = new PaymentPresenter(mContext, this);

    }

    @OnClick({R.id.pickUpButton, R.id.destinationButon})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.pickUpButton:
                String uri = "waze://?ll=" + mTransaction.getPickupLocation().getLatitude() + ", " + mTransaction.getPickupLocation().getLongitude() + "&z=10";
                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                break;

            case R.id.destinationButon:
                String destinationUri = "waze://?ll=" + mTransaction.getDestinationLocation().getLatitude() + ", " + mTransaction.getDestinationLocation().getLongitude() + "&z=10";
                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(destinationUri)));
                break;

        }

    }
}
