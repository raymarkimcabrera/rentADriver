package com.renta.renta_driver.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.BuildConfig;
import com.renta.renta_driver.R;
import com.renta.renta_driver.model.car.Car;
import com.renta.renta_driver.presenter.CarPresenter;
import com.renta.renta_driver.utils.FileUtil;
import com.renta.renta_driver.utils.ImageUtil;
import com.renta.renta_driver.utils.NavigationUtils;
import com.renta.renta_driver.utils.Preferences;
import com.renta.renta_driver.view.CarListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddCarActivity extends BaseActivity implements CarListView {

    public static final int REQUEST_CODE_PERMISSION_CAMERA_AND_STORAGE = 103;
    public static final int REQUEST_IMAGE_CAPTURE = 100;

    @BindView(R.id.carModelEditText)
    EditText mCarModelEditText;

    @BindView(R.id.plateNumberEditText)
    EditText mPlateNumberEditText;

    @BindView(R.id.carPhotoImageView)
    ImageView mCarPhotoImageView;

    @BindView(R.id.carPhotoLabel)
    TextView mCarPhotoLabel;

    private CarPresenter mCarPresenter;
    private Car mCar;
    private Uri mFileUri;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AddCarActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initPresenter();
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_add_car;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_IMAGE_CAPTURE:
                        String imagePath = mFileUri.toString();
                        Log.i("loadImage", "loadImage Not Cropped: String: " + imagePath);
                        ImageUtil.loadImageFromUrl(mContext, mCarPhotoImageView, imagePath);
                        break;
                    default:
                        Toast.makeText(mContext, "There was a problem with your request", Toast.LENGTH_SHORT).show();
                        break;
                }
            } else {
                // User Cancelled
            }
        }
    }

    @Override
    protected void permissionGranted(int requestCode) {
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA_AND_STORAGE) {
            FileUtil mFileUtil = new FileUtil(mContext);
//            Uri fileUri = mFileUtil.generateImageFileUri();
            Uri fileUri = FileProvider.getUriForFile(mContext,
                    mContext.getApplicationContext().getPackageName()
                            + ".my.package.name.provider", mFileUtil.generateImageFile());
            if (fileUri != null) {
                mFileUri = fileUri;
                NavigationUtils.startCameraActivity(this, mFileUri, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void permissionDenied(String message) {
        if (message.equals("NEVERASKAGAIN")) {
            Toast.makeText(mContext, "Cannot open camera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetCarListSuccess(List<Car> carList) {
        //Ignore
    }

    @Override
    public void onNoCarList() {
        //Ignore
    }

    @Override
    public void onGetCarListError() {
        //Ignore
    }

    @Override
    public void onPostCarSuccess() {
        startActivity(CarListActivity.newIntent(mContext));
        finish();
    }

    @Override
    public void onPostCarError() {
        Toast.makeText(mContext, "Error adding Car", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.addCarButton, R.id.carPhotoImageView})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCarButton:
                prepareData();
                break;
            case R.id.carPhotoImageView:
                checkForPermissions(REQUEST_CODE_PERMISSION_CAMERA_AND_STORAGE, android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
        }
    }

    private void prepareData() {
        mCar = new Car();
        mCar.setCarModel(mCarModelEditText.getText().toString());
        mCar.setPlateNumber(mPlateNumberEditText.getText().toString());
        mCar.setDriverID(Preferences.getString(mContext, Preferences.USER_ID));

        mCarPhotoImageView.setDrawingCacheEnabled(true);
        mCarPhotoImageView.buildDrawingCache();

        Bitmap bitmap = ((BitmapDrawable) mCarPhotoImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        mCarPresenter.addCar(mCar, data);
    }

    public void initPresenter() {
        mCarPresenter = new CarPresenter(mContext, this);
    }


}
