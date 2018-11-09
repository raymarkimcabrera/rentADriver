package com.renta.renta_driver.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.R;
import com.renta.renta_driver.model.transaction.Transaction;
import com.renta.renta_driver.presenter.TransactionPresenter;
import com.renta.renta_driver.utils.DateUtil;
import com.renta.renta_driver.utils.Preferences;
import com.renta.renta_driver.view.TransactionView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class ScheduleActivity extends BaseActivity implements TransactionView {

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;

    private List<EventDay> mEventDayList;
    private TransactionPresenter mTransactionPresenter;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, ScheduleActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initializePresenter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mEventDayList = new ArrayList<>();
        mTransactionPresenter.getPaidTransactions(Preferences.getString(mContext, Preferences.USER_ID));
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_schedule;
    }

    @Override
    public void onGetTransactionViewSuccess(List<Transaction> transactionList){
    }

    @Override
    public void onNoTransaction() {

    }

    @Override
    public void onGetTransactionViewError() {

    }

    @Override
    public void onTransactionStatusUpdateSuccess() {

    }

    @Override
    public void onTransactionStatusUpdateError() {

    }

    @Override
    public void onGetTransactions(Transaction transaction) {
        Calendar eventDate = DateUtil.convertDateToCalendar(transaction.getStartDate());
        EventDay eventDay = new EventDay(eventDate, R.drawable.event_circle);
        mEventDayList.add(eventDay);

        mCalendarView.setEvents(mEventDayList);
        try {
            mCalendarView.setDate(Calendar.getInstance());
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetTransaction(Transaction transaction) {

    }

    @Override
    public void onGetTransactionError() {

    }

    private void initializePresenter(){
        mTransactionPresenter = new TransactionPresenter(mContext, this);
    }
}
