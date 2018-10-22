package com.renta.renta_driver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.renta.renta_driver.BaseActivity;
import com.renta.renta_driver.BuildConfig;
import com.renta.renta_driver.R;

import butterknife.BindView;

public class DashboardActivity extends BaseActivity {

    @BindView(R.id.navigationView)
    NavigationView mNavigationView;

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    private Context mContext;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, DashboardActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        initToolBar();
        initNavigationMenu();
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
                mDrawerLayout.closeDrawer(GravityCompat.START);

                switch (menuItem.getItemId()) {
                    case R.id.menuItemHome:
                        startActivity(DashboardActivity.newIntent(mContext));
                        return true;
                    case R.id.menuMessages:
//                        startActivity(MessageListActivity.newIntent(mContext));
                        return true;
                    case R.id.menuItemCars:
                        startActivity(CarListActivity.newIntent(mContext));
//                        startActivity(TransactionsHistoryActivity.newIntent(mContext));
                        return true;
                    case R.id.menuOffers:
                        startActivity(OffersListActivity.newIntent(mContext));
                        return true;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Menu menu = mNavigationView.getMenu();
        MenuItem appVersion = menu.findItem(R.id.menuAppVersion);
        appVersion.setTitle("Version " + BuildConfig.VERSION_NAME);

    }
}
