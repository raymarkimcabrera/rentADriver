package com.renta.renta_driver.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.R;
import com.renta.renta_driver.adapter.TransactionsRecyclerViewAdapter;
import com.renta.renta_driver.model.transaction.Transaction;
import com.renta.renta_driver.presenter.TransactionPresenter;
import com.renta.renta_driver.utils.Preferences;
import com.renta.renta_driver.view.TransactionView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TransactionHistoryActivity extends BaseActivity implements TransactionView {
    private final static String TAG = TransactionHistoryActivity.class.getSimpleName();

    @BindView(R.id.transactionHistoryRecyclerView)
    RecyclerView mTransactionHistoryRecyclerView;

    @BindView(R.id.noTransactionTextView)
    TextView mNoTransactionsTextView;

    private TransactionPresenter mTransactionPresenter;
    private List<Transaction> mTransactionList;
    private TransactionsRecyclerViewAdapter mTransactionsRecyclerViewAdapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, TransactionHistoryActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTransactionList = new ArrayList<>();
        mTransactionPresenter.getTransactions(Preferences.getString(mContext, Preferences.USER_ID));

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_transaction_history;
    }

    private void initPresenter() {
        mTransactionPresenter = new TransactionPresenter(mContext, this);
    }

    @Override
    public void onGetTransactionViewSuccess(List<Transaction> transactionList) {

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
        mTransactionList.add(transaction);
        mTransactionHistoryRecyclerView.setVisibility(View.VISIBLE);
        mNoTransactionsTextView.setVisibility(View.GONE);
        mTransactionsRecyclerViewAdapter = new TransactionsRecyclerViewAdapter(mContext, mTransactionList, new TransactionsRecyclerViewAdapter.OnClickTransactionListener() {
            @Override
            public void OnTransactionSelected(Transaction transaction) {
                String uri = "waze://?ll=" + transaction.getDestinationLocation().getLatitude() + ", " + transaction.getDestinationLocation().getLongitude() + "&z=10";
                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
            }
        });

        mTransactionHistoryRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mTransactionHistoryRecyclerView.setLayoutManager(llm);
        mTransactionHistoryRecyclerView.setAdapter(mTransactionsRecyclerViewAdapter);
        mTransactionsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetTransaction(Transaction transaction) {

    }

    @Override
    public void onGetTransactionError() {

    }
}
