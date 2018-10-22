package com.renta.renta_driver.dialog;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.renta.renta_driver.R;
import com.renta.renta_driver.adapter.CarListRecyclerViewAdapter;
import com.renta.renta_driver.adapter.OfferListRecyclerViewAdapter;
import com.renta.renta_driver.model.car.Car;
import com.renta.renta_driver.utils.GeneralUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.facebook.internal.FacebookDialogFragment.TAG;

public class ChooseCarDialog extends DialogFragment {

    private final static String EMPTY_DATE = "Click to select Date";
    private final static String EMPTY_TIME = "Click to select Time";

    RecyclerView mChooseCarRecyclerView;

    private Unbinder mUnbinder;
    private static Context mContext;
    private static List<Car> mCarList;
    private CarListRecyclerViewAdapter mCarListRecyclerViewAdapter;
    private static OnClickListener mOnClickListener;

    public static ChooseCarDialog build(Context context, List<Car> carList, OnClickListener onClickListener) {
        Bundle args = new Bundle();
        ChooseCarDialog fragment = new ChooseCarDialog();
        fragment.setArguments(args);
        mContext = context;
        mCarList = carList;
        mOnClickListener = onClickListener;
        return fragment;
    }

    public interface OnClickListener {
        void onSubmit(Car car);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_STYLE_Transaction);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_choose_car, container, false);

        mChooseCarRecyclerView = (RecyclerView) view.findViewById(R.id.chooseCarRecyclerView);

        initUi();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


    private void initUi() {

        mCarListRecyclerViewAdapter = new CarListRecyclerViewAdapter(mContext, mCarList, new CarListRecyclerViewAdapter.OnClickCarListener() {
            @Override
            public void OnCarSelected(Car car) {
                mOnClickListener.onSubmit(car);
                ChooseCarDialog.this.dismiss();
            }
        });

        GeneralUtils.setDefaultRecyclerView(mContext, mChooseCarRecyclerView, mCarListRecyclerViewAdapter);

    }

}
