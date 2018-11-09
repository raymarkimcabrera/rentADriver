package com.renta.renta_driver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.BuildConfig;
import com.renta.renta_driver.R;
import com.renta.renta_driver.adapter.OfferListRecyclerViewAdapter;
import com.renta.renta_driver.model.driver.Driver;
import com.renta.renta_driver.model.transaction.Transaction;
import com.renta.renta_driver.model.user.User;
import com.renta.renta_driver.presenter.OffersPresenter;
import com.renta.renta_driver.presenter.UsersPresenter;
import com.renta.renta_driver.utils.GeneralUtils;
import com.renta.renta_driver.utils.ImageUtil;
import com.renta.renta_driver.utils.Preferences;
import com.renta.renta_driver.view.OffersView;
import com.renta.renta_driver.view.UsersView;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends BaseActivity implements OffersView, UsersView {

    @BindView(R.id.offersListView)
    RecyclerView mOffersListView;

    @BindView(R.id.navigationView)
    NavigationView mNavigationView;

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    private Context mContext;
    private OffersPresenter mOffersPresenter;
    private OfferListRecyclerViewAdapter mOfferListRecyclerViewAdapter;

    private UsersPresenter mUserPresenter;
    private TextView mHeaderTitleTextView;
    private CircleImageView mHeaderImageView;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, DashboardActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        initPresenter();
        initToolBar();
        initNavigationMenu();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mOffersPresenter.getAllPendingTransactions();

        mUserPresenter.getUserProfile(Preferences.getString(mContext, Preferences.USER_ID));

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_dashboard;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                else if (!mDrawerLayout.isDrawerVisible(GravityCompat.START))
                    mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void initToolBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.img_menu);
            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initNavigationMenu() {
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.drawer_logged_in);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.menuItemHome:
                        startActivity(DashboardActivity.newIntent(mContext));
                        return true;
                    case R.id.menuMessages:
                        startActivity(MessageListActivity.newIntent(mContext));
                        return true;
                    case R.id.menuItemCars:
                        startActivity(CarListActivity.newIntent(mContext));
//                        startActivity(TransactionsHistoryActivity.newIntent(mContext));
                        return true;
                    case R.id.menuOffers:
                        startActivity(OffersListActivity.newIntent(mContext));
                        return true;
                    case R.id.menuHistory:
                        startActivity(TransactionHistoryActivity.newIntent(mContext));
                        return true;
                    case R.id.menuSchedule:
                        startActivity(ScheduleActivity.newIntent(mContext));
                        return true;
                }
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Menu menu = mNavigationView.getMenu();
        MenuItem appVersion = menu.findItem(R.id.menuAppVersion);
        appVersion.setTitle("Version " + BuildConfig.VERSION_NAME);
        View headerView = mNavigationView.getHeaderView(0);
        mHeaderTitleTextView = (TextView) headerView.findViewById(R.id.userTextView);
        mHeaderImageView = (CircleImageView) headerView.findViewById(R.id.userImageView);
        LinearLayout userLinearLayout = (LinearLayout) headerView.findViewById(R.id.userLinearLayout);
        userLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ProfileActivity.newIntent(mContext));
            }
        });
    }


    private void initPresenter() {
        mOffersPresenter = new OffersPresenter(mContext, this);
        mUserPresenter = new UsersPresenter(mContext, this);
    }

    @Override
    public void onGetUserSuccess(Driver driver) {
        mHeaderTitleTextView.setText(driver.getFirstName() + " " + driver.getLastName());
        if (!driver.getImageUrl().isEmpty())
            ImageUtil.loadImageFromUrl(mContext, mHeaderImageView, driver.getImageUrl());
    }

    @Override
    public void onGetUserError() {

    }

    @Override
    public void onUserUpdateSuccess() {

    }

    @Override
    public void onUserUpdateError() {

    }

    @Override
    public void onGetCustomerProfileSuccess(User user) {

    }

    @Override
    public void onGetCustomerProfileError() {

    }
}
