package com.renta.renta_driver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renta.renta_driver.R;
import com.renta.renta_driver.model.car.Car;
import com.renta.renta_driver.model.transaction.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfferListRecyclerViewAdapter extends RecyclerView.Adapter<OfferListRecyclerViewAdapter.OfferListViewHolder> {
    private final static String PICK_UP = "PICK_UP";
    private final static String DESTINATION = "DESTINATION";


    private Context mContext;
    private List<Transaction> mTransactionList;
    private OfferListRecyclerViewAdapter.OnClickTransactionListener mOnCliCkTransactionListener;
//    private Transaction mTransaction = null;

    public OfferListRecyclerViewAdapter(Context mContext, List<Transaction> transactionList, OfferListRecyclerViewAdapter.OnClickTransactionListener mOnClickTransactionListener) {
        this.mContext = mContext;
        this.mTransactionList = transactionList;
        this.mOnCliCkTransactionListener = mOnClickTransactionListener;
    }

    public interface OnClickTransactionListener {
        void OnTransactionSelected(Transaction transaction);
    }

    @NonNull
    @Override
    public OfferListRecyclerViewAdapter.OfferListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transaction, parent, false);
        OfferListRecyclerViewAdapter.OfferListViewHolder transactionsViewHolder = new OfferListRecyclerViewAdapter.OfferListViewHolder(view);
        transactionsViewHolder.setIsRecyclable(true);
        return transactionsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferListRecyclerViewAdapter.OfferListViewHolder holder, final int position) {
        final Transaction transaction = mTransactionList.get(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");
//
//        holder.mCarModelTextView.setText(car.getCarModel());
        holder.mPickUpAddressTextView.setText(transaction.getPickUpAddress());
        holder.mPickUpAddressTextView.setSelected(true);
        holder.mDestinationTextView.setSelected(true);
        holder.mDestinationTextView.setText(transaction.getDestinationAddress());
        holder.mPickUpAddressDateTextView.setText(simpleDateFormat.format(transaction.getStartDate()));
        holder.mDestinationDateTextView.setText(simpleDateFormat.format(transaction.getEndDate()));

        holder.mTransactionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCliCkTransactionListener.OnTransactionSelected(transaction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTransactionList != null ? mTransactionList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class OfferListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pickUpAddressDateTextView)
        TextView mPickUpAddressDateTextView;

        @BindView(R.id.destinationDateTextView)
        TextView mDestinationDateTextView;

        @BindView(R.id.pickUpAddressTextView)
        TextView mPickUpAddressTextView;

        @BindView(R.id.destinationTextView)
        TextView mDestinationTextView;

        @BindView(R.id.transactionLinearLayout)
        LinearLayout mTransactionLinearLayout;

        public OfferListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
