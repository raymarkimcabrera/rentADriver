package com.renta.renta_driver.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.R;
import com.renta.renta_driver.dialog.ChooseCarDialog;
import com.renta.renta_driver.model.car.Car;
import com.renta.renta_driver.model.offer.Offer;
import com.renta.renta_driver.model.transaction.Transaction;
import com.renta.renta_driver.presenter.CarPresenter;
import com.renta.renta_driver.presenter.OffersPresenter;
import com.renta.renta_driver.utils.GeneralUtils;
import com.renta.renta_driver.utils.Preferences;
import com.renta.renta_driver.view.CarListView;
import com.renta.renta_driver.view.OffersView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OfferActivity extends BaseActivity implements CarListView, OffersView {

    private static final String TAG = OfferActivity.class.getSimpleName();

    @BindView(R.id.pickUpAddressDateTextView)
    TextView mPickUpAddressDateTextView;

    @BindView(R.id.destinationDateTextView)
    TextView mDestinationDateTextView;

    @BindView(R.id.pickUpAddressTextView)
    TextView mPickUpAddressTextView;

    @BindView(R.id.destinationTextView)
    TextView mDestinationTextView;

    @BindView(R.id.passengersTextView)
    TextView mPassengersTextView;

    @BindView(R.id.driverSpecificationsTextView)
    TextView mDriverSpecificationsTextView;

    @BindView(R.id.typeOfVehicleTextView)
    TextView mTypeOfVehicleTextView;

    @BindView(R.id.typeOfServiceTextView)
    TextView mTypeOfServiceTextView;

    @BindView(R.id.typeOfPaymentTextView)
    TextView mTypeOfPaymentTextView;


    private Transaction mTransaction;
    private Offer mOffer;
    private OffersPresenter mOffersPresenter;
    private CarPresenter mCarPresenter;


    public static Intent newIntent(Context context, Transaction transaction) {
        Intent intent = new Intent(context, OfferActivity.class);
        intent.putExtra("TRANSACTION", transaction);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        getArgs();

        initPresenter();

        initUi();
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_offer;
    }

    @OnClick(R.id.offerButton)
    void onClick() {
        mCarPresenter.getCars(Preferences.getString(mContext, Preferences.USER_ID));
    }

    private void initPresenter() {
        mOffersPresenter = new OffersPresenter(mContext, this);
        mCarPresenter = new CarPresenter(mContext, this);
    }

    private void getArgs() {
        mTransaction = (Transaction) getIntent().getSerializableExtra("TRANSACTION");
    }

    private void initUi() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");
        mDestinationTextView.setSelected(true);
        mPickUpAddressTextView.setSelected(true);

        mDestinationTextView.setText(mTransaction.getDestinationAddress());
        mPickUpAddressTextView.setText(mTransaction.getPickUpAddress());

        mPickUpAddressDateTextView.setText(simpleDateFormat.format(mTransaction.getStartDate()));
        mDestinationDateTextView.setText(simpleDateFormat.format(mTransaction.getEndDate()));

        mPassengersTextView.setText(String.valueOf(mTransaction.getPassengers()));
        mDriverSpecificationsTextView.setText(mTransaction.getDriverSpecifications().equals(WITH_DRIVER) ? "With Driver" : "Without Driver");
        mTypeOfVehicleTextView.setText(mTransaction.getTypeOfVehicle());
        mTypeOfPaymentTextView.setText(mTransaction.getTypeOfPayment().equals(ALL_OUT) ? "All out" : "Separate Payments");
        mTypeOfServiceTextView.setText(mTransaction.getTypeOfService().equals(ONE_WAY) ? "One Way" : "Round-trip");
    }

    @Override
    public void onGetCarListSuccess(List<Car> carList) {
        mOffer = new Offer();
        ChooseCarDialog chooseCarDialog = ChooseCarDialog.build(mContext, carList, new ChooseCarDialog.OnClickListener() {
            @Override
            public void onSubmit(Car car) {
                mOffer.setDriverID(Preferences.getString(mContext, Preferences.USER_ID));
                mOffer.setCar(car);
                mOffer.setCreatedAt(GeneralUtils.getCurrentDate());
                showAmountDialog();
            }
        });

        chooseCarDialog.show(((Activity) mContext).getFragmentManager(), TAG);
    }

    @Override
    public void onNoCarList() {
        Toast.makeText(mContext, "You have no car currently registered.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetCarListError() {
        Toast.makeText(mContext, "Error connecting to server", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostCarSuccess() {
        //Ignore
    }

    @Override
    public void onPostCarError() {
        //Ignore
    }

    @Override
    public void onGetAllAvailableTransactionSuccess(List<Transaction> transactionList) {
        //Ignore
    }

    @Override
    public void onGetAllAvailableTransactionError() {
        //Ignore
    }

    @Override
    public void onNoTransactionsPending() {
        //Ignore
    }

    @Override
    public void onUpdateTransactionSuccess() {
        startActivity(DashboardActivity.newIntent(mContext));
        finish();
    }

    @Override
    public void onUpdateTransactionError() {
        Toast.makeText(mContext, "Error connecting to server", Toast.LENGTH_SHORT).show();
    }

    private void showAmountDialog(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_enter_bid_amount, null);

        final EditText bidAmountTextView = (EditText) view.findViewById(R.id.bidAmountEditText);

        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("Enter Bid Amount")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int amount = Integer.parseInt(bidAmountTextView.getText().toString());
                        if (amount > 0){
                            mOffer.setPrice(amount);
                            dialog.dismiss();
                            mTransaction.getOfferList().add(mOffer);
                            mOffersPresenter.updateTransaction(mTransaction);
                        } else {
                            Toast.makeText(mContext, "Please enter a amount.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create();
        alertDialog.show();
    }
}
