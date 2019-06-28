package com.automobile.service.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.automobile.service.AutumobileAplication;
import com.automobile.service.R;
import com.automobile.service.customecomponent.CustomEdittext;
import com.automobile.service.model.Register.RegisterResponseModel;
import com.automobile.service.mvp.Register.DaggerRegisterScreenComponent;
import com.automobile.service.mvp.Register.RegisterScreen;
import com.automobile.service.mvp.Register.RegisterScreenModule;
import com.automobile.service.mvp.Register.RegisterScreenPresenter;
import com.automobile.service.util.Utils;

import javax.inject.Inject;


public class RegisterActivity extends BaseActivity implements RegisterScreen.View {

    private LinearLayout llContainer;
    private CustomEdittext etEmail;
    private CustomEdittext etPassword;
    private EditText etUsername;
    private CustomEdittext etPhone;
    private Button btnLogin;
    private TextView tvReg;
    private ImageView ivClose;

    private String emailAdd;
    private String password;
    private String phone;
    private String username;
    private ProgressDialog progress;

    @Inject
    RegisterScreenPresenter mainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        initComponents();
        setupDagger();


    }


    @Override
    public void initComponents() {

        llContainer = (LinearLayout) findViewById(R.id.activity_register_llContainer);
        etEmail = (CustomEdittext) findViewById(R.id.activity_register_etEmail);
        etPassword = (CustomEdittext) findViewById(R.id.activity_register_etPsw);
        etPhone = (CustomEdittext) findViewById(R.id.activity_register_etPhone);
        etUsername = (EditText) findViewById(R.id.activity_register_etUsername);
        btnLogin = (Button) findViewById(R.id.activity_register_btnDone);
        tvReg = (TextView) findViewById(R.id.activity_register_tvReg);
        ivClose = (ImageView) findViewById(R.id.activity_register_ivClose);

        btnLogin.setOnClickListener(this);
        tvReg.setOnClickListener(this);
        ivClose.setOnClickListener(this);


    }


    /**
     * Validating form
     */

    private void submitForm() {

        username = etUsername.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        emailAdd = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            Utils.snackbar(llContainer, getString(R.string.val_enter_username), true, RegisterActivity.this);
        } else if (emailAdd.isEmpty()) {
            Utils.snackbar(llContainer, getString(R.string.val_enter_email), true, RegisterActivity.this);
        } else if (!Utils.isValidEmail(emailAdd)) {
            Utils.snackbar(llContainer, getString(R.string.val_enter_valid_email), true, RegisterActivity.this);
        } else if (password.isEmpty()) {
            Utils.snackbar(llContainer, getString(R.string.val_enter_password), true, RegisterActivity.this);
        } else if (phone.isEmpty()) {
            Utils.snackbar(llContainer, getString(R.string.val_enter_phone), true, RegisterActivity.this);
        } else {
            register();
        }

    }


    @Override
    public void onClick(View v) {

        Utils.hideKeyboard(RegisterActivity.this);

        if (v == btnLogin) {

            submitForm();
        } else if (v == tvReg) {

            finish();
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
            Utils.hideKeyboard(RegisterActivity.this);

        } else if (v == ivClose) {

            finish();
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
            Utils.hideKeyboard(RegisterActivity.this);

        }

    }

    private void setupDagger() {
        DaggerRegisterScreenComponent.builder().netComponent(((AutumobileAplication) getApplicationContext()).getNetComponent())
                .registerScreenModule(new RegisterScreenModule(this))
                .build().inject(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
        Utils.hideKeyboard(RegisterActivity.this);
    }

    public void register() {

        if (Utils.isOnline(RegisterActivity.this, true)) {

            final String uniqId = Utils.getDeviceID(RegisterActivity.this);
            mainPresenter.doRegister(username, password, uniqId, emailAdd, phone);


        } else {
            Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, RegisterActivity.this);

        }
    }

    @Override
    public void showResponse(RegisterResponseModel responseBase) {

        if (responseBase.getStatus().toString().equalsIgnoreCase("1")) {
            Utils.snackbar(llContainer, "" + responseBase.getMsg(), true, RegisterActivity.this);
            overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
            finish();
        } else {
            Utils.snackbar(llContainer, "" + responseBase.getMsg(), true, RegisterActivity.this);
        }

    }

    @Override
    public void showError(String message) {

        Utils.snackbar(llContainer, "" + message, true, RegisterActivity.this);
    }


}
