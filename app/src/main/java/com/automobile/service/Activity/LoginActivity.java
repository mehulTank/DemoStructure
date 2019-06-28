package com.automobile.service.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.automobile.service.AutumobileAplication;
import com.automobile.service.R;
import com.automobile.service.model.Login.LoginModelData;
import com.automobile.service.mvp.Login.DaggerLoginScreenComponent;
import com.automobile.service.mvp.Login.LoginScreen;
import com.automobile.service.mvp.Login.LoginScreenModule;
import com.automobile.service.mvp.Login.LoginScreenPresenter;
import com.automobile.service.util.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.inject.Inject;


public class LoginActivity extends BaseActivity implements LoginScreen.View {

    private LinearLayout llContainer;
    private EditText etPassword;
    private EditText etEmail;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView ivForgotPsw;
    private ImageView ivFbLogin;

    private String password;
    private String email;
    private ProgressDialog progress;

    //Facebook Related components
    private CallbackManager callbackFbManager;

    private String authId;
    private String emailId;
    private String fname;
    private String lname;
    private String authType;

    @Inject
    LoginScreenPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);
        initComponents();
        generateKeyHash();
        initFacebook();
        setupDagger();

    }


    @Override
    public void initComponents() {

        llContainer = (LinearLayout) findViewById(R.id.activity_login_llContainer);
        etPassword = (EditText) findViewById(R.id.activity_login_etPsw);
        etEmail = (EditText) findViewById(R.id.activity_login_etEmail);
        btnLogin = (Button) findViewById(R.id.activity_login_btnDone);
        tvRegister = (TextView) findViewById(R.id.activity_login_tvReg);
        ivForgotPsw = (TextView) findViewById(R.id.activity_login_tvForgotPsw);
        ivFbLogin = (ImageView) findViewById(R.id.activity_login_ivFacebook);

        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        ivForgotPsw.setOnClickListener(this);
        ivFbLogin.setOnClickListener(this);


        Log.d("initComponents", "TOKEN==" + FirebaseInstanceId.getInstance().getToken());


    }


    /**
     * Validating form
     */

    private void submitForm() {


        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            Utils.snackbar(llContainer, getString(R.string.val_enter_email), true, LoginActivity.this);
        } else if (!Utils.isValidEmail(email)) {
            Utils.snackbar(llContainer, getString(R.string.val_enter_valid_email), true, LoginActivity.this);
        } else if (password.isEmpty()) {
            Utils.snackbar(llContainer, getString(R.string.val_enter_password), true, LoginActivity.this);
        } else {
            login();
        }

    }


    @Override
    public void onClick(View v) {

        Utils.hideKeyboard(LoginActivity.this);

        if (v == btnLogin) {

            submitForm();
        } else if (v == tvRegister) {


            Intent mMenuIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(mMenuIntent);
            overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
        } else if (v == ivForgotPsw) {


            Intent mMenuIntent = new Intent(LoginActivity.this, ForgotActivity.class);
            startActivity(mMenuIntent);
            overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
        } else if (v == ivFbLogin) {

            if (Utils.isOnline(LoginActivity.this, true)) {
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            } else {
                Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, getApplicationContext());
            }
        }


    }


    private void initFacebook() {
        // FacebookSdk.sdkInitialize(getApplicationContext());
        callbackFbManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackFbManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject fbResObj, GraphResponse response) {
                        String id = null;

                        try {

                            id = fbResObj.getString("id");
                            Log.d("onSuccess", "onSuccess" + id);

                            String emailAddress = fbResObj.optString("phone");
                            String first_name = fbResObj.optString("first_name");
                            String last_name = fbResObj.optString("last_name");
                            if (id != null) {
                                Log.d("first_name", "first_name==" + first_name);
                                socialLoginService(emailAddress, id, first_name, last_name, "facebook");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("onSuccess", "onSuccess" + e.getMessage());
                        }

                    }
                });
                final Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

                Log.d("onError", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("onError", "onError" + error.getMessage());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackFbManager.onActivityResult(requestCode, resultCode, data);
    }


    private void generateKeyHash() {
        try {
            final PackageInfo info = getPackageManager().getPackageInfo("com.automobile.service", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", "==" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.automobile.service", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key====", "==" + something);
                Log.d("hash key====", "==" + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

    }

    private void socialLoginService(final String emailAddress, final String id, final String fnam, final String lnam, final String facebook) {


        Log.d("socialLoginService", "socialLoginService" + id);
        authId = id;
        emailId = emailAddress;
        authType = facebook;
        fname = fnam;
        lname = lnam;

        if (Utils.isOnline(LoginActivity.this, true)) {
            final String uniqId = Utils.getDeviceID(LoginActivity.this);
            mainPresenter.doSocialLogin(authId, uniqId, emailId, fnam + " " + lnam);

        } else {
            Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, LoginActivity.this);
        }


    }


    public void login() {

        if (Utils.isOnline(LoginActivity.this, true)) {

            final String uniqId = Utils.getDeviceID(LoginActivity.this);
            mainPresenter.doLogin("LoginAppUsers", "sumit1@vocso.com", "123456", "sdfdrtertertertyertert");

          /*  Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(WsConstants.BASE_URL)
                    .build();

            LoginScreenPresenter.LoginScreenService service = retrofit.create(LoginScreenPresenter.LoginScreenService.class);
            Call<String> result = service.doLogin("LoginAppUsers", "sumit1@vocso.com", "123456", "sdfdrtertertertyertert");

            result.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response.isSuccessful()) {
                        String responseString = response.body();

                        Log.d("----- response", responseString);

                        if (WSUtil.isDataAvaible(responseString)) {

                            Gson gson = new Gson();
                            LoginModelData loginModelData = gson.fromJson(responseString, LoginModelData.class);
                            Log.d("----- user emai", loginModelData.getData().getUserEmail());

                        } else {
                            Log.d("----- user emai", "not succes message");
                        }

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });*/

        } else {
            Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, LoginActivity.this);

        }
    }

    @Override
    public void showResponse(boolean isData, LoginModelData loginModelData) {

        if (isData) {
            Log.d("--- login response", loginModelData.getData().getUserEmail());
        } else {
            Log.d("--- login response", "no data avaible");
        }

      /*  Log.d("--- login response", responseBase.body().toString());
        if (responseBase.isSuccessful()) {
            String responseString = responseBase.body().toString();

            Log.d("----- response", responseString);

            if (WSUtil.isDataAvaible(responseString)) {

                Gson gson = new Gson();
                LoginModelData loginModelData = gson.fromJson(responseString, LoginModelData.class);
                Log.d("----- user emai", loginModelData.getData().getUserEmail());

            } else {
                Log.d("----- user emai", "not succes message");
            }

        }*/


      /*  if (responseBase.getStatus().toString().equalsIgnoreCase("1")) {


            LoginDetailsModel loginDetailsModel = responseBase.getDetails();

            AutumobileAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_userID), loginDetailsModel.getUserId());
            AutumobileAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_userName), loginDetailsModel.getUserName());
            AutumobileAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_userEmail), loginDetailsModel.getUserEmail());
            AutumobileAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_userPhone), loginDetailsModel.getUserPhone());
            AutumobileAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_userProfilePic), loginDetailsModel.getUserProfile());
            AutumobileAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_wallate), loginDetailsModel.getUserWalletAmount());


            AutumobileAplication.getmInstance().savePreferenceDataBoolean(getString(R.string.preferances_isNormallogin), false);
            goToHome();

        } else {

            Utils.snackbar(llContainer, "" + responseBase.getMsg(), true, LoginActivity.this);

        }*/
    }

    @Override
    public void showError(String message) {
        Utils.snackbar(llContainer, "" + message, true, LoginActivity.this);
    }


    private void setupDagger() {
        DaggerLoginScreenComponent.builder().netComponent(((AutumobileAplication) getApplicationContext()).getNetComponent())
                .loginScreenModule(new LoginScreenModule(this))
                .build().inject(this);


    }


    private void goToHome() {


        if (Build.VERSION.SDK_INT < 23) {
            openHomeActivity();
            finish();
        } else {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openHomeActivity();
            } else {
                Utils.checkPermitionCameraGaller(LoginActivity.this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openHomeActivity();
                } else {
                    // permission denied
                }
            }
        }
    }

    private void openHomeActivity() {

        AutumobileAplication.getmInstance().savePreferenceDataBoolean(getString(R.string.preferances_islogin), true);
        Intent intent = new Intent(getApplicationContext(), MenuBarActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
        finish();
    }


    @Override
    protected void onStart() {

        super.onStart();
    }


}
