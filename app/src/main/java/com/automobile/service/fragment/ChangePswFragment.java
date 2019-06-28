package com.automobile.service.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.automobile.service.AutumobileAplication;
import com.automobile.service.R;
import com.automobile.service.model.ChangePsw.ChangePswResponseModel;
import com.automobile.service.mvp.ChangePsw.ChangePswScreen;
import com.automobile.service.mvp.ChangePsw.ChangePswScreenModule;
import com.automobile.service.mvp.ChangePsw.ChangePswScreenPresenter;
import com.automobile.service.mvp.ChangePsw.DaggerChangePswScreenComponent;
import com.automobile.service.mvp.Product.DaggerProductScreenComponent;
import com.automobile.service.mvp.Product.ProductScreenModule;
import com.automobile.service.mvp.Product.ProductScreenPresenter;
import com.automobile.service.util.Utils;
import com.automobile.service.Activity.MenuBarActivity;
import com.automobile.service.webservice.WSChangePsw;

import javax.inject.Inject;


public class ChangePswFragment extends BaseFragment implements ChangePswScreen.View {
    private LinearLayout llContainer;
    private EditText etOldPsw;
    private EditText etNewPsw;
    private EditText etConfirmPsw;
    private Button btnSubmit;


    private String oldPsw;
    private String newPsw;
    private String confirmPsw;

    private MenuItem item;
    private ChangePswUpdateAsyncTask changePswUpdateAsyncTask;
    private ProgressDialog progress;

    @Inject
    ChangePswScreenPresenter mainPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_change_psw, container, false);
        initComponents(rootView);
        initToolbar();
        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDagger();

    }


    @Override
    public void initComponents(View rootView) {


        llContainer = (LinearLayout) rootView.findViewById(R.id.fragment_changepsw__llContainer);
        etOldPsw = (EditText) rootView.findViewById(R.id.fragment_changepsw_etOldPsw);
        etNewPsw = (EditText) rootView.findViewById(R.id.fragment_changepsw_etNewPsw);
        etConfirmPsw = (EditText) rootView.findViewById(R.id.fragment_changepsw_etConfirmPsw);
        btnSubmit = (Button) rootView.findViewById(R.id.fragment_changepsw__btnChangePsw);

        btnSubmit.setOnClickListener(this);


    }


    public void initToolbar() {
        ((MenuBarActivity) getActivity()).setUpToolbar(getString(R.string.menu_change_psw), true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        item = menu.findItem(R.id.menu_left);


    }


    private void setupDagger() {
        DaggerChangePswScreenComponent.builder()
                .netComponent(((AutumobileAplication) getActivity().getApplicationContext()).getNetComponent())
                .changePswScreenModule(new ChangePswScreenModule(this))
                .build().inject(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {

        Utils.hideKeyboard(getActivity());

        if (v == btnSubmit) {

            submitForm();
        }
    }


    /**
     * Validating form
     */

    private void submitForm() {


        oldPsw = etOldPsw.getText().toString().trim();
        newPsw = etNewPsw.getText().toString().trim();
        confirmPsw = etConfirmPsw.getText().toString().trim();


        if (oldPsw.isEmpty()) {
            validateField(llContainer, etOldPsw, getString(R.string.val_enter_old_password));
        } else if (newPsw.isEmpty()) {
            validateField(llContainer, etNewPsw, getString(R.string.val_enter_new_password));
        } else if (confirmPsw.isEmpty()) {
            validateField(llContainer, etConfirmPsw, getString(R.string.val_enter_confirm_password));
        } else if (!newPsw.equals(confirmPsw)) {
            validateField(llContainer, etNewPsw, getString(R.string.val_enter_new_confirm_password_not_match));
        } else {
            updateChangePsw();
        }
    }


    public void updateChangePsw() {

        if (Utils.isOnline(getActivity(), true)) {

            final String userId = AutumobileAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_userID)), "");
            mainPresenter.doChangePsw(userId, oldPsw, newPsw);

        } else {
            Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, getActivity());

        }
    }

    @Override
    public void showResponse(ChangePswResponseModel responseBase) {

        if (responseBase.getStatus().toString().equalsIgnoreCase("1")) {
            Utils.snackbar(llContainer, "" + responseBase.getMsg(), true, getActivity());
        } else {
            Utils.snackbar(llContainer, "" + responseBase.getMsg(), true, getActivity());
        }
    }

    @Override
    public void showError(String message) {

        Utils.snackbar(llContainer, "" + message, true, getActivity());
    }


    private class ChangePswUpdateAsyncTask extends AsyncTask<Void, Void, Void> {


        private WSChangePsw wsProfileUpdate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setMessage(getString(R.string.please_wait));
            progress.setCancelable(false);
            progress.show();


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                wsProfileUpdate = new WSChangePsw(getActivity());
                wsProfileUpdate.executeService(getActivity(), oldPsw, newPsw);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            if (wsProfileUpdate.isSuccess()) {
                Utils.snackbar(llContainer, "" + wsProfileUpdate.getMessage(), true, getActivity());


            } else {

                Utils.snackbar(llContainer, "" + wsProfileUpdate.getMessage(), true, getActivity());

            }

        }

    }


}
