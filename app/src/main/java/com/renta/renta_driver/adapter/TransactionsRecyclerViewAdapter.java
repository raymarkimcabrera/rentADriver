package com.renta.renta_driver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.renta.renta_driver.R;
import com.renta.renta_driver.model.transaction.Transaction;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionsRecyclerViewAdapter extends RecyclerView.Adapter<TransactionsRecyclerViewAdapter.TransactionsViewHolder> {
    private final static String PICK_UP = "PICK_UP";
    private final static String DESTINATION = "DESTINATION";


    private Context mContext;
    private List<Transaction> mTransactionList;
    private OnClickTransactionListener mOnClickTransactionListener;
    private Transaction mTransaction = null;

    public TransactionsRecyclerViewAdapter(Context mContext, List<Transaction> mTransactionList, OnClickTransactionListener mOnClickTransactionListener) {
        this.mContext = mContext;
        this.mTransactionList = mTransactionList;
        this.mOnClickTransactionListener = mOnClickTransactionListener;
    }

    public interface OnClickTransactionListener {
        void OnTransactionSelected(Transaction transaction);
    }

    @NonNull
    @Override
    public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transaction, parent, false);
        TransactionsViewHolder transactionsViewHolder = new TransactionsViewHolder(view);
        transactionsViewHolder.setIsRecyclable(true);
        return transactionsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsViewHolder holder, final int position) {
        final Transaction transaction = mTransactionList.get(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");

        holder.mPickUpDateTextView.setSelected(true);
        holder.mPickUpDateTextView.setText(transaction.getPickUpAddress());
        holder.mDestinationTextView.setSelected(true);
        holder.mDestinationTextView.setText(transaction.getDestinationAddress());


        holder.mTransactionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, ""+ position, Toast.LENGTH_SHORT).show();
                mOnClickTransactionListener.OnTransactionSelected(transaction);
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

    public class TransactionsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pickUpAddressDateTextView)
        TextView mPickUpDateTextView;

        @BindView(R.id.destinationDateTextView)
        TextView mDestinationDateTextView;

        @BindView(R.id.pickUpAddressTextView)
        TextView mPickUpAddressTextView;

        @BindView(R.id.destinationTextView)
        TextView mDestinationTextView;

        @BindView(R.id.transactionLinearLayout)
        LinearLayout mTransactionLinearLayout;

        public TransactionsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
