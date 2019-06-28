package com.automobile.service.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.automobile.service.AutumobileAplication;
import com.automobile.service.R;
import com.automobile.service.adapter.MenuAdapter;
import com.automobile.service.fragment.ProductListFragment;
import com.automobile.service.fragment.ProfileFragment;
import com.automobile.service.model.SlidingMenuModel;
import com.automobile.service.util.Constans;
import com.automobile.service.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.ArrayList;


public class MenuBarActivity extends BaseActivity {
    //Defining Variables
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private SlidingMenuModel model;

    private ListView lvMenuList;
    private LinearLayout llheaderMain;

    private FragmentManager fragmentManager;
    private MenuAdapter mMenuAdapter;
    private Fragment mFragment = null;


    private TextView tvName;
    private ImageView ivProfileRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menubar);

        initComponents();
        initToolbar();


    }

    @Override
    public void initComponents() {


        lvMenuList = (ListView) findViewById(R.id.activity_menubar_lvMenuList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.activity_menubar_toolbar);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };


        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        setUpMenu();

    }


    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onResume() {
        super.onResume();


    }


    private void initToolbar() {


        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

    }


    private void openFragment(final Fragment mFragment) {


        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_menubar_containers, mFragment, mFragment.getClass().getSimpleName());
        transaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main, menu);
        //this.menu = menu;
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == android.R.id.home) {
            Utils.hideKeyboard(MenuBarActivity.this);
            fragmentManager = getFragmentManager();

            if (fragmentManager.getBackStackEntryCount() > 0) {


                if (Constans.IS_HOME) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Constans.IS_HOME = false;

                } else {
                    getFragmentManager().popBackStack();
                }

                return true;

            } else {

                if (Constans.PROFILE_REFRESS) {
                    Constans.PROFILE_REFRESS = false;
                    final String username = AutumobileAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_userName)), "");
                    final String profilePic = AutumobileAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_userProfilePic)), "");


                    if (profilePic != null && !profilePic.isEmpty()) {

                        Glide.with(MenuBarActivity.this).load(Uri.parse(profilePic)).asBitmap().placeholder(R.drawable.thumb_personel).centerCrop().into(new BitmapImageViewTarget(ivProfileRound) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(MenuBarActivity.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivProfileRound.setImageDrawable(circularBitmapDrawable);
                            }
                        });

                    }

                    tvName.setText(username);
                    tvName.setSelected(true);
                    drawerLayout.openDrawer(GravityCompat.START);

                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

            }


        }
        return super.onOptionsItemSelected(item);
    }


    public Toolbar getToolbar() {
        return toolbar;
    }


    private void setUpMenu() {
        ArrayList<SlidingMenuModel> slidingMenu = new ArrayList<SlidingMenuModel>();
        model = new SlidingMenuModel();
        //model.setTitle(getString(R.string.menu_category));
        model.setTitle(getString(R.string.menu_product));
        model.setIcon(R.drawable.menu_category_selector);
        slidingMenu.add(model);

        model = new SlidingMenuModel();
        model.setTitle(getString(R.string.menu_my_profile));
        model.setIcon(R.drawable.menu_profile_selector);
        slidingMenu.add(model);

        model = new SlidingMenuModel();
        model.setTitle(getString(R.string.menu_my_cart));
        model.setIcon(R.drawable.menu_cart_selector);
        slidingMenu.add(model);

        model = new SlidingMenuModel();
        model.setTitle(getString(R.string.menu_ewallet));
        model.setIcon(R.drawable.menu_ewallate_selector);
        slidingMenu.add(model);

        model = new SlidingMenuModel();
        model.setTitle(getString(R.string.menu_notification));
        model.setIcon(R.drawable.menu_notification_selector);
        slidingMenu.add(model);

        model = new SlidingMenuModel();
        model.setTitle(getString(R.string.menu_myorder));
        model.setIcon(R.drawable.menu_order_selector);
        slidingMenu.add(model);

        model = new SlidingMenuModel();
        model.setTitle(getString(R.string.menu_logout));
        model.setIcon(R.drawable.menu_logout_selector);
        slidingMenu.add(model);


        View header = (View) getLayoutInflater().inflate(R.layout.menu_header, null);
        lvMenuList.addHeaderView(header);

        ivProfileRound = (ImageView) header.findViewById(R.id.menu_header_ivProfileRound);
        tvName = (TextView) header.findViewById(R.id.menu_header_tvName);

        mMenuAdapter = new MenuAdapter(MenuBarActivity.this, slidingMenu);
        lvMenuList.setAdapter(mMenuAdapter);


        lvMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int mPosition, long id) {


                mMenuAdapter.notifyDataSetChanged();

                if (isNavDrawerOpen()) {
                    listClickOpenfragment(mPosition);
                    closeNavDrawer();
                }

                view.setSelected(true);
                mMenuAdapter.notifyDataSetChanged();

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int topMargin = (int) getResources().getDimension(R.dimen.menu_profile_pic_row_top_margin);
            int imgWidthHeight = (int) getResources().getDimension(R.dimen.menu_header_image_width_hieght);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imgWidthHeight, imgWidthHeight);
            lp.setMargins(0, topMargin, 0, 0);
            ivProfileRound.setLayoutParams(lp);

        }
        final String username = AutumobileAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_userName)), "");
        final String profilePic = AutumobileAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_userProfilePic)), "");

        tvName.setText("" + username);


        if (profilePic != null && !profilePic.isEmpty()) {

            Glide.with(MenuBarActivity.this).load(Uri.parse(profilePic)).asBitmap().placeholder(R.drawable.thumb_personel).centerCrop().into(new BitmapImageViewTarget(ivProfileRound) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(MenuBarActivity.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivProfileRound.setImageDrawable(circularBitmapDrawable);
                }
            });

        }

        ivProfileRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileFragment profileFragment = new ProfileFragment();
                openFragment(profileFragment);
            }
        });


        ProductListFragment mSearchProductFragment = new ProductListFragment();
        openFragment(mSearchProductFragment);


        lvMenuList.setSelected(true);
        lvMenuList.setItemChecked(1, true);
        lvMenuList.setSelection(1);
        lvMenuList.setSelectionAfterHeaderView();

    }


    private void listClickOpenfragment(int position) {


        closeNavDrawer();
        mFragment = null;

        switch (position) {

            case 1:
                mFragment = new ProductListFragment();
                openFragment(mFragment);
                break;
            case 2:
                mFragment = new ProfileFragment();
                openFragment(mFragment);
                break;
            case 3:
                mFragment = new ProductListFragment();
                openFragment(mFragment);
                break;
            case 4:
                mFragment = new ProductListFragment();
                openFragment(mFragment);
                break;
            case 5:
                mFragment = new ProductListFragment();
                openFragment(mFragment);
                break;
            case 6:
                mFragment = new ProductListFragment();
                openFragment(mFragment);
                break;
            case 7:
                openLogoutDaolog();
                break;


        }


    }


    public void isNavDrawerLock(final boolean isLock) {
        drawerLayout.setDrawerLockMode(isLock ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    protected boolean isNavDrawerOpen() {

        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }


    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    public void setUpToolbar(final String title, final boolean isShowback) {
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);

        isNavDrawerLock(isShowback ? false : true);
        getSupportActionBar().setHomeAsUpIndicator(isShowback ? R.drawable.ic_back : R.drawable.ic_menu);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            openNotificationDaolog(bundle);
        }

    }

    private void openNotificationDaolog(Bundle bundle) {

        final String title = bundle.getString(getString(R.string.title_val));
        final String nID = bundle.getString(getString(R.string.notification_id));
        final String message = bundle.getString(getString(R.string.value));


        AlertDialog.Builder builder = new AlertDialog.Builder(MenuBarActivity.this);
        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));

//        String positiveText = getString(android.R.string.ok);
//        builder.setPositiveButton(positiveText,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // positive button logic
//                    }
//                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();


//        NotificationDetailsFragment notificationDetailsFragment=new NotificationDetailsFragment();
//        Bundle bundle1 = new Bundle();
//        bundle1.putString(getString(R.string.value), message);
//        bundle1.putString(getString(R.string.title_val), title);
//        notificationDetailsFragment.setArguments(bundle1);
//        Utils.addNextFragment(MenuBarActivity.this, notificationDetailsFragment, mFragment, false);

    }


    private void openLogoutDaolog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(MenuBarActivity.this);
        builder.setTitle(getString(R.string.app_name_new));
        builder.setMessage(getString(R.string.are_you_sure_do_you_want_to_logout));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    FirebaseInstanceId.getInstance().deleteInstanceId();
                                    AutumobileAplication.getmInstance().clearePreferenceData();
                                    Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
                                    finish();


                                } catch (Exception bug) {
                                    bug.printStackTrace();
                                }

                            }
                        });

                        thread.start();






                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

    }

}
