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

import com.renta.renta_driver.R;
import com.renta.renta_driver.model.car.Car;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarListRecyclerViewAdapter extends RecyclerView.Adapter<CarListRecyclerViewAdapter.CarListViewHolder> {
    private final static String PICK_UP = "PICK_UP";
    private final static String DESTINATION = "DESTINATION";


    private Context mContext;
    private List<Car> mCarList;
    private OnClickCarListener mOnClickCarListener;
//    private Transaction mTransaction = null;

    public CarListRecyclerViewAdapter(Context mContext, List<Car> carList, OnClickCarListener mOnClickTransactionListener) {
        this.mContext = mContext;
        this.mCarList = carList;
        this.mOnClickCarListener = mOnClickTransactionListener;
    }

    public interface OnClickCarListener {
        void OnCarSelected(Car car);
    }

    @NonNull
    @Override
    public CarListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_car, parent, false);
        CarListViewHolder transactionsViewHolder = new CarListViewHolder(view);
        transactionsViewHolder.setIsRecyclable(true);
        return transactionsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarListViewHolder holder, final int position) {
        final Car car = mCarList.get(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");

        holder.mCarModelTextView.setText(car.getCarModel());
//        holder.mPickUpTextView.setText(transaction.getPickUpAddress());
//        holder.mDestinationTextView.setSelected(true);
//        holder.mDestinationTextView.setText(transaction.getDestinationAddress());
//
//
//        holder.mTransactionCreatedTextView.setText(simpleDateFormat.format(transaction.getCreatedDate()));
//        holder.mVehicleTypeTextView.setText(transaction.getTypeOfVehicle());
//        holder.mTransactionRatingBar.setRating(transaction.getRating());
//
        holder.mCarLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, ""+ position, Toast.LENGTH_SHORT).show();
                mOnClickCarListener.OnCarSelected(car);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCarList != null ? mCarList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class CarListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.carModelTextView)
        TextView mCarModelTextView;
//
//        @BindView(R.id.destinationTextView)
//        TextView mDestinationTextView;
//
//        @BindView(R.id.pickUpTextView)
//        TextView mPickUpTextView;
//
//        @BindView(R.id.transactionRatingBar)
//        RatingBar mTransactionRatingBar;
//
//        @BindView(R.id.vehicleTypeTextView)
//        TextView mVehicleTypeTextView;
//
        @BindView(R.id.carLinearLayout)
        LinearLayout mCarLinearLayout;

        public CarListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
