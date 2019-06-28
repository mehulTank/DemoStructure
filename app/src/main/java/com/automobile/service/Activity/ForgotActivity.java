package com.automobile.service.Activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.automobile.service.AutumobileAplication;
import com.automobile.service.R;
import com.automobile.service.model.Forgot.ForgotResponseModel;
import com.automobile.service.mvp.ForgotPsw.DaggerForgotScreenComponent;
import com.automobile.service.mvp.ForgotPsw.ForgotScreen;
import com.automobile.service.mvp.ForgotPsw.ForgotScreenModule;
import com.automobile.service.mvp.ForgotPsw.ForgotScreenPresenter;
import com.automobile.service.util.Utils;

import javax.inject.Inject;


public class ForgotActivity extends BaseActivity implements ForgotScreen.View
{


    private LinearLayout llContainer;
    private EditText etEmailId;
    private Button btnSubmit;
    private TextView tvLogin;
    private ImageView ivClose;
    private String emailId;
    private static int MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;

    @Inject
    ForgotScreenPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot);
        initComponents();
        setupDagger();

    }

    private void setupDagger() {
        DaggerForgotScreenComponent.builder().netComponent(((AutumobileAplication) getApplicationContext()).getNetComponent())
                .forgotScreenModule(new ForgotScreenModule(this))
                .build().inject(this);


    }


    @Override
    public void initComponents() {

        llContainer = (LinearLayout) findViewById(R.id.activity_forgot_llContainer);
        etEmailId = (EditText) findViewById(R.id.activity_forgot_etEmailId);
        btnSubmit = (Button) findViewById(R.id.activity_forgot_btnDone);
        tvLogin = (TextView) findViewById(R.id.activity_forgot_tvSign);
        ivClose = (ImageView) findViewById(R.id.activity_forgot_ivClose);
        btnSubmit.setOnClickListener(this);
        ivClose.setOnClickListener(this);

    }


    /**
     * Validating form
     */

    private void submitForm() {

        emailId = etEmailId.getText().toString().trim();


        if (emailId.isEmpty()) {
            Utils.snackbar(llContainer, getString(R.string.val_enter_email), true, ForgotActivity.this);
        } else if (!Utils.isValidEmail(emailId)) {
            Utils.snackbar(llContainer, getString(R.string.val_enter_valid_email), true, ForgotActivity.this);
        } else {

            forgotPsw();
        }

    }


    @Override
    public void onClick(View v) {

        Utils.hideKeyboard(ForgotActivity.this);

        /**
         * Logic to Prevent the Launch of the Fragment Twice if User makes
         * the Tap(Click) very Fast.
         */
        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {

            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (v == btnSubmit) {
            submitForm();
        } else if (v == ivClose) {
            finish();
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
        }


    }

    public void forgotPsw() {

        if (Utils.isOnline(ForgotActivity.this, true)) {
            final String uniqId = Utils.getDeviceID(ForgotActivity.this);
            mainPresenter.doForgot(emailId);

        } else {
            Utils.snackbar(llContainer, getString(R.string.check_connection), true, ForgotActivity.this);

        }
    }

    @Override
    public void showResponse(ForgotResponseModel responseBase)
    {

        if (responseBase.getStatus().toString().equalsIgnoreCase("1")) {
            Utils.snackbar(llContainer, "" + responseBase.getMsg(), true, ForgotActivity.this);
            overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
            finish();
        } else {
            Utils.snackbar(llContainer, "" + responseBase.getMsg(), true, ForgotActivity.this);
        }
    }

    @Override
    public void showError(String message) {

        Utils.snackbar(llContainer, "" +message, true, ForgotActivity.this);
    }






    @Override
    public void onDestroy() {
        super.onDestroy();



    }
}
