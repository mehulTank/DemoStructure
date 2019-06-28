package com.automobile.service.fragment;

import android.animation.LayoutTransition;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.automobile.service.AutumobileAplication;
import com.automobile.service.R;
import com.automobile.service.adapter.ProductListAdapter;
import com.automobile.service.model.product.ProductModel;
import com.automobile.service.model.product.ProductResponseModel;
import com.automobile.service.mvp.Product.DaggerProductScreenComponent;
import com.automobile.service.mvp.Product.ProductScreen;
import com.automobile.service.mvp.Product.ProductScreenModule;
import com.automobile.service.mvp.Product.ProductScreenPresenter;
import com.automobile.service.util.ParamsConstans;
import com.automobile.service.util.Utils;
import com.automobile.service.Activity.MenuBarActivity;

import java.util.ArrayList;

import javax.inject.Inject;


public class ProductListFragment extends BaseFragment implements ProductListAdapter.OnItemClickListener, ProductScreen.View {


    //Declaration
    private LinearLayout llContainer;
    private LinearLayout llProgress;
    private RecyclerView rvSleeptipsList;
    private LinearLayout llLoadMoreProgress;
    private RelativeLayout rlEmpty;


    private SearchView searchView;
    private String searchKeyWord = "";
    private boolean isFromSearch = false;
    private boolean isDataLoadingFromServer = false;


    private ArrayList<ProductModel> modelArrayList = new ArrayList<>();
    private ProductListAdapter productListAdapter;

    private static int MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;
    private int lastVisibleItem;
    private int totalItemCount;
    private int visibleThreshold = 1;
    private int pageItemCount = 0;
    private int pageIndex = 0;

    @Inject
    ProductScreenPresenter mainPresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_productlist, container, false);
        initToolbar();

        initComponents(rootView);
        setHasOptionsMenu(true);

        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDagger();

    }


    private void setupDagger() {
        DaggerProductScreenComponent.builder()
                .netComponent(((AutumobileAplication) getActivity().getApplicationContext()).getNetComponent())
                .productScreenModule(new ProductScreenModule(this))
                .build().inject(this);
    }


    @Override
    public void initComponents(View rootView) {

        llContainer = (LinearLayout) rootView.findViewById(R.id.fragment_productlist_llContainer);
        llLoadMoreProgress = (LinearLayout) rootView.findViewById(R.id.fragment_productlist_llLoadMoreProgress);
        rvSleeptipsList = (RecyclerView) rootView.findViewById(R.id.fragment_productlist_rvProductList);
        rlEmpty = (RelativeLayout) rootView.findViewById(R.id.fragment_rlEmpty);
        llProgress = (LinearLayout) rootView.findViewById(R.id.fragment_productlist_llProgress);

        rvSleeptipsList.setHasFixedSize(true);

        setUpAdapater(modelArrayList);
        getProductListData(false);
        notificationoff();


    }

    private void notificationoff() {

        AppOpsManager mAppOps = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAppOps = (AppOpsManager) getActivity().getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = getActivity().getApplicationInfo();
            String pkg = getActivity().getApplicationContext().getPackageName();
            int uid = appInfo.uid;

           // mAppOps.finishOp(Context.NOTIFICATION_SERVICE, uid, pkg);


        }



    }


    public void initToolbar() {
        ((MenuBarActivity) getActivity()).setUpToolbar(getString(R.string.menu_product), false);

    }


    public void getProductListData(final boolean isLoadmore) {

        if (Utils.isOnline(getActivity(), true)) {

            isDataLoadingFromServer = true;
            llProgress.setVisibility(isLoadmore ? View.GONE : View.VISIBLE);
            mainPresenter.getProductList(searchKeyWord, "" + pageIndex);

        } else {
            llProgress.setVisibility(View.GONE);
            emptyView();
            Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, getActivity());

        }
    }

    @Override
    public void onItemClick(View view, ProductModel viewModel) {

        /**
         * Logic to Prevent the Launch of the Fragment Twice if User makes
         * the Tap(Click) very Fast.
         */
        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        Utils.hideKeyboard(getActivity());

    }

    /**
     * Option menu for Searchview
     *
     * @param menu
     * @param inflater
     */

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem menuItem = menu.findItem(R.id.menu_search);


        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.actionbar_search_hint));
        searchView.setGravity(Gravity.END);
        final LinearLayout searchBar = (LinearLayout) searchView.findViewById(R.id.search_bar);
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setStartDelay(LayoutTransition.APPEARING, 100);
        searchBar.setLayoutTransition(layoutTransition);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchKeyWord = "";
                return true;
            }
        });


        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchKeyWord = "";
                isFromSearch = false;

                if (Utils.isNetworkAvailable(getActivity())) {
                    modelArrayList.clear();
                    pageIndex = 0;
                    getProductListData(false);
                } else {
                    Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, getActivity());
                }

                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                if (!isDataLoadingFromServer) {
                    isFromSearch = true;
                    searchKeyWord = query;

                    //Clear list data before search
                    pageIndex = 0;
                    modelArrayList.clear();
                    getProductListData(false);
                    return false;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (isFromSearch) {
            menuItem.expandActionView();
            if (!TextUtils.isEmpty(searchKeyWord))
                searchView.setQuery(searchKeyWord, false);
        }

    }


    private void emptyView() {

        rlEmpty.setVisibility(View.VISIBLE);
        rvSleeptipsList.setVisibility(View.GONE);
    }

    private void setUpAdapater(final ArrayList<ProductModel> guidelineModelArrayList) {

        rlEmpty.setVisibility(View.GONE);
        rvSleeptipsList.setVisibility(View.VISIBLE);

        rvSleeptipsList.removeAllViews();
        rvSleeptipsList.setHasFixedSize(true);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvSleeptipsList.setLayoutManager(mLayoutManager);

        productListAdapter = new ProductListAdapter(ProductListFragment.this, getActivity(), guidelineModelArrayList);
        productListAdapter.setOnItemClickListener(this);
        rvSleeptipsList.setAdapter(productListAdapter);

        rvSleeptipsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                if (totalItemCount > 0) {

                    if (!productListAdapter.isLoading() && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (pageItemCount > totalItemCount)
                        {
                            productListAdapter.setLoading();
                            llLoadMoreProgress.setVisibility(View.VISIBLE);
                            pageIndex++;
                            getProductListData(true);
                        }
                    }
                }
            }
        });


    }


    @Override
    public void onClick(View v) {


    }


    /**
     * Called when coming back from next screen
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHasOptionsMenu(true);
            initToolbar();

        }
    }

    @Override
    public void showResponse(ProductResponseModel responseBase) {

        isDataLoadingFromServer = false;

        if (searchView != null) {
            searchView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }

            }, 300);
        }


        if (responseBase.getStatus().toString().equalsIgnoreCase("1")) {

            ArrayList<ProductModel> productModelArrayList = responseBase.getDetails();
            pageItemCount = Integer.parseInt(responseBase.getTotal_page());


            if (productListAdapter != null && productListAdapter.isLoading()) {
                llLoadMoreProgress.setVisibility(View.GONE);
                productListAdapter.setLoaded();

            }

            if (productModelArrayList != null && productModelArrayList.size() > 0) {

                if (productListAdapter != null) {
                    modelArrayList.addAll(productModelArrayList);
                    productListAdapter.addRecord(modelArrayList);
                    productListAdapter.notifyDataSetChanged();
                } else {
                    modelArrayList.addAll(productModelArrayList);
                    setUpAdapater(modelArrayList);
                }
                llProgress.setVisibility(View.GONE);
                rlEmpty.setVisibility(View.GONE);
                rvSleeptipsList.setVisibility(View.VISIBLE);

            } else {

                if (modelArrayList.size() < 0) {
                    llProgress.setVisibility(View.GONE);
                    Utils.snackbar(llContainer, "" + getString(R.string.no_servicelist_found), true, getActivity());
                    emptyView();
                }
            }
        } else {
            llProgress.setVisibility(View.GONE);
            Utils.snackbar(llContainer, "" + responseBase.getMsg(), true, getActivity());
            emptyView();
        }


    }

    @Override
    public void showError(String message) {
        Utils.snackbar(llContainer, "" + message, true, getActivity());
    }


}
