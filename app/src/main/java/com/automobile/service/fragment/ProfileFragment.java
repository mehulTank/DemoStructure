package com.automobile.service.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.automobile.service.AutumobileAplication;
import com.automobile.service.R;
import com.automobile.service.comman.OnClickListenerWrapper;
import com.automobile.service.imagecrop.CropHandler;
import com.automobile.service.imagecrop.CropHelper;
import com.automobile.service.imagecrop.CropParams;
import com.automobile.service.util.Constans;
import com.automobile.service.util.Utils;
import com.automobile.service.Activity.MenuBarActivity;
import com.automobile.service.webservice.WSProfileUpdate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;


public class ProfileFragment extends BaseFragment implements CropHandler {
    private LinearLayout llContainer;
    private ImageView ivProfile;

    private EditText etUserName;
    private EditText etAddress;
    private EditText etPhone;
    private EditText etEmail;

    private TextView tvClickByCamera;
    private TextView tvPickFromGallery;
    private TextView tvCancel;
    private Button btnChangePsw;

    private String phone;
    private String userName;
    private String email;
    private String address;
    private String profilePic;
    private String cameraUri;
    private String imagePath = "";
    private Boolean isEditable = false;

    private Dialog dialogProfilePicture;
    private File tempFile;
    private AutumobileAplication autumobileAplication;
    private CropParams mCropParams;

    private MenuItem item;
    private ProfileUpdateAsyncTask profileUpdateAsyncTask;
    private ProgressDialog progress;

    private static int MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        initComponents(rootView);
        initToolbar();
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void initComponents(View rootView) {

        autumobileAplication = (AutumobileAplication) getActivity().getApplicationContext();
        mCropParams = new CropParams(getActivity());


        llContainer = (LinearLayout) rootView.findViewById(R.id.fragment_profile_llContainer);
        etAddress = (EditText) rootView.findViewById(R.id.fragment_profile_etAddress);
        etUserName = (EditText) rootView.findViewById(R.id.fragment_profile_etUsername);
        etPhone = (EditText) rootView.findViewById(R.id.fragment_profile_etPhone);
        etEmail = (EditText) rootView.findViewById(R.id.fragment_profile_etEmail);
        ivProfile = (ImageView) rootView.findViewById(R.id.fragment_profile_ivProfileRound);
        btnChangePsw = (Button) rootView.findViewById(R.id.fragment_profile_btnChangePsw);

        ivProfile.setOnClickListener(this);
        btnChangePsw.setOnClickListener(this);


        enableDesable(false);
        setUpProfileData();

    }

    private void setUpProfileData() {

        userName = AutumobileAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_userName)), "");
        email = AutumobileAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_userEmail)), "");
        address = AutumobileAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_useAddress)), "");
        phone = AutumobileAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_userPhone)), "");
        profilePic = AutumobileAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_userProfilePic)), "");

        etAddress.setText(address);
        etUserName.setText(userName);
        etPhone.setText(phone);
        etEmail.setText(email);



        try
        {
            boolean isNormalLogin = AutumobileAplication.getmInstance().getSharedPreferences().getBoolean(getString((R.string.preferances_isNormallogin)), false);
            if(!isNormalLogin)
            {
                btnChangePsw.setVisibility(View.GONE);
            }


            Glide.with(getActivity()).load(profilePic).asBitmap().placeholder(R.drawable.thumb_personel).centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivProfile.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }





    }


    public void initToolbar() {
        ((MenuBarActivity) getActivity()).setUpToolbar(getString(R.string.menu_profile), false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        item = menu.findItem(R.id.menu_left);
        item.setVisible(true);
        setToolBar(item);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Utils.hideKeyboard(getActivity());

                if (isEditable) {
                    submitForm();
                } else {
                    isEditable = true;
                    setToolBar(item);

                }
                return true;
            }
        });
    }


    /****************************************************************************
     * @param item
     * @purpose:This Method use to SetActionBar With UpdateActionbar Button
     * *************************************************************************
     */


    public void setToolBar(MenuItem item) {
        if (!isEditable) {
            item.setIcon(R.drawable.icon_edit);
            ivProfile.setEnabled(false);
            enableDesable(isEditable);
        } else {
            item.setIcon(R.drawable.done_btn);
            ivProfile.setEnabled(true);
            enableDesable(isEditable);
        }

    }


    /****************************************************************************
     * @purpose:This Method use Enable & desable Edittext (if Enable when UpdateData othervise Edittext show Disable)
     ***************************************************************************/

    public void enableDesable(Boolean isUpdated) {
        etEmail.setEnabled(isUpdated);
        etPhone.setEnabled(isUpdated);


        if (isUpdated) {
            etUserName.setEnabled(true);
            etAddress.setEnabled(true);
            etPhone.setEnabled(true);
            etEmail.setEnabled(false);
        } else {
            etUserName.setEnabled(false);
            etAddress.setEnabled(false);
            etPhone.setEnabled(false);
            etEmail.setEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        CropHelper.clearCacheDir();
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        CropHelper.clearCacheDir();
        super.onDestroyView();
    }

    public void openGallery() {
        mCropParams.refreshUri();
        mCropParams.enable = false;
        mCropParams.compress = false;
        Intent intent = CropHelper.buildGalleryIntent(mCropParams, getActivity());
        startActivityForResult(intent, CropHelper.REQUEST_CROP);

    }

    private void chekPermition() {
        if (Build.VERSION.SDK_INT < 23) {

            showMenuDialogToSetProfilePic();

        } else {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showMenuDialogToSetProfilePic();
            } else {
                Utils.checkPermitionCameraGaller(getActivity());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showMenuDialogToSetProfilePic();
                } else {
                    // permission denied
                }
            }
        }
    }

    private void openCamera() {

        mCropParams.refreshUri();
        mCropParams.enable = false;
        mCropParams.compress = false;

        Intent intent = CropHelper.buildCameraIntent(mCropParams, getActivity());
        startActivityForResult(intent, CropHelper.REQUEST_CAMERA);

    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }

    @Override
    public void onPhotoCropped(Uri uri) {

        if (!mCropParams.compress) {
            imagePath = uri.getPath();
            Log.d("onPhotoCropped", "imagePath==" + imagePath);
            //ivProfile.setImageBitmap(BitmapUtil.decodeUriAsBitmap(getActivity(), uri));
            //                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                ivProfile.setImageBitmap(bitmap);


            Glide.with(getActivity()).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivProfile.setImageDrawable(circularBitmapDrawable);
                }
            });


        }
    }

    @Override
    public void onCompressed(Uri uri) {

        imagePath = uri.getPath();
        //ivProfile.setImageBitmap(BitmapUtil.decodeUriAsBitmap(getActivity(), uri));
        Glide.with(getActivity()).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivProfile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivProfile.setImageDrawable(circularBitmapDrawable);
            }
        });


    }


    @Override
    public void onCancel() {
        //Toast.makeText(this, "Crop canceled!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailed(String message) {
        //Toast.makeText(this, "Crop failed: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleIntent(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropHelper.REQUEST_CROP) {
            CropHelper.handleResult(this, requestCode, resultCode, data);
        } else if (requestCode == CropHelper.REQUEST_CAMERA) {
            CropHelper.handleResult(this, requestCode, resultCode, data);
        }

    }


    @Override
    public void onClick(View v) {

        Utils.hideKeyboard(getActivity());

        /**
         * Logic to Prevent the Launch of the Fragment Twice if User makes
         * the Tap(Click) very Fast.
         */
        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (v == ivProfile) {
            if (isEditable)
                chekPermition();
        } else if (v == tvClickByCamera) {
            dialogProfilePicture.dismiss();
            openCamera();

        } else if (v == tvPickFromGallery) {
            // Dismiss the  Dialog
            dialogProfilePicture.dismiss();
            openGallery();

        } else if (v == tvCancel) {
            // Dismiss the  Dialog
            dialogProfilePicture.dismiss();
        }
        else if (v == btnChangePsw) {

            ChangePswFragment productDetailsFragment = new ChangePswFragment();
            Utils.addNextFragment(getActivity(), productDetailsFragment, ProfileFragment.this, true);
        }



    }


    /**
     * Validating form
     */

    private void submitForm() {


        userName = etUserName.getText().toString().trim();
        address = etAddress.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        phone = etPhone.getText().toString().trim();


        if (userName.isEmpty()) {
            validateField(llContainer, etUserName, getString(R.string.val_enter_username));
        } else if (email.isEmpty()) {
            validateField(llContainer, etEmail, getString(R.string.val_enter_email));
        } else if (!Utils.isValidEmail(email)) {
            validateField(llContainer, etEmail, getString(R.string.val_enter_valid_email));
        } else if (phone.isEmpty()) {
            validateField(llContainer, etPhone, getString(R.string.val_enter_phone));
        } else {
            updateProfileData();
        }
    }

    /**
     * Custom Dialog Layout to be opened in order to make User select the Picture from
     * Gallery or to Click one by Camera to Set his/her Profile
     */

    private void showMenuDialogToSetProfilePic() {

        dialogProfilePicture = new Dialog(getActivity(), R.style.picture_dialog_style);
        dialogProfilePicture.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProfilePicture.setContentView(R.layout.dialog_choose_image);
        final WindowManager.LayoutParams wlmp = dialogProfilePicture.getWindow().getAttributes();
        wlmp.gravity = Gravity.BOTTOM;
        wlmp.width = WindowManager.LayoutParams.FILL_PARENT;
        wlmp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogProfilePicture.getWindow().setAttributes(wlmp);
        dialogProfilePicture.show();

        tvClickByCamera = (TextView) dialogProfilePicture.findViewById(R.id.dialog_choose_image_tvCamera);
        tvPickFromGallery = (TextView) dialogProfilePicture.findViewById(R.id.dialog_choose_image_tvGallery);
        tvCancel = (TextView) dialogProfilePicture.findViewById(R.id.dialog_choose_image_tvCancel);

        tvPickFromGallery.setOnClickListener(new OnClickListenerWrapper(this));
        tvClickByCamera.setOnClickListener(new OnClickListenerWrapper(this));
        tvCancel.setOnClickListener(new OnClickListenerWrapper(this));
    }

    public void updateProfileData() {

        if (Utils.isOnline(getActivity(), true)) {
            if (profileUpdateAsyncTask != null && profileUpdateAsyncTask.getStatus() == AsyncTask.Status.PENDING) {
                profileUpdateAsyncTask.execute();
            } else if (profileUpdateAsyncTask == null || profileUpdateAsyncTask.getStatus() == AsyncTask.Status.FINISHED) {
                profileUpdateAsyncTask = new ProfileUpdateAsyncTask();
                profileUpdateAsyncTask.execute();
            }

        } else {
            Utils.snackbar(llContainer, "" + getString(R.string.check_connection), true, getActivity());

        }
    }


    private class ProfileUpdateAsyncTask extends AsyncTask<Void, Void, Void> {


        private WSProfileUpdate wsProfileUpdate;

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

                wsProfileUpdate = new WSProfileUpdate(getActivity());
                wsProfileUpdate.executeService(getActivity(), userName, email, address, phone, imagePath);

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
                enableDesable(false);
                item.setIcon(R.drawable.icon_edit);
                Constans.PROFILE_REFRESS = true;
                isEditable = false;

            } else {

                Utils.snackbar(llContainer, "" + wsProfileUpdate.getMessage(), true, getActivity());

            }

        }

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

}
