package com.automobile.service.webservice;

import android.content.Context;
import android.util.Log;

import com.automobile.service.AutumobileAplication;
import com.automobile.service.R;
import com.automobile.service.util.ParamsConstans;
import com.automobile.service.util.Utils;
import com.automobile.service.util.WsConstants;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by Skywevas Technologies on 16/05/17.
 * This class is makiing api call for Register User webservice
 */

public class WSProfileUpdate {

    private Context context;
    private String message;
    private boolean success;
    private String userId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public WSProfileUpdate(final Context context) {
        this.context = context;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void executeService(Context context, final String username, final String email, final String address, final String phone, final String imagePath) {

        final String url = WsConstants.BASE_URL + WsConstants.METHOD_PROFILE;
        final String response = new WSUtil(context).callServiceHttpPost(context, url, generateLoginRequest(username, email, address, phone, imagePath));
        parseResponse(response);

    }

    private RequestBody generateLoginRequest(final String username, final String email, final String address, final String phone, final String imagePath) {
        // final String uniqId = ImliveAplication.getmInstance().getSharedPreferences().getString(context.getString((R.string.preferances_token)), "");
        final String userId = AutumobileAplication.getmInstance().getSharedPreferences().getString(context.getString((R.string.preferances_userID)), "");
        final String uniqId = Utils.getDeviceID(context);
        Log.d("imagePath===", "imagePath===" + imagePath);

        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
        File sourceFile = new File(imagePath);
        RequestBody requestBody = null;


        if (imagePath != null && !imagePath.isEmpty()) {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(ParamsConstans.PARAM_PROFILE, imagePath, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .addFormDataPart(ParamsConstans.PARAM_USERID, userId)
                    .addFormDataPart(ParamsConstans.PARAM_USERNAME, username)
                    .addFormDataPart(ParamsConstans.PARAM_ADDRESS, address)
                    .addFormDataPart(ParamsConstans.PARAM_DEVICE_TOKEN, uniqId)
                    .addFormDataPart(ParamsConstans.PARAM_PHONE, phone)
                    .build();
        } else {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(ParamsConstans.PARAM_USERID, userId)
                    .addFormDataPart(ParamsConstans.PARAM_USERNAME, username)
                    .addFormDataPart(ParamsConstans.PARAM_ADDRESS, address)
                    .addFormDataPart(ParamsConstans.PARAM_DEVICE_TOKEN, uniqId)
                    .addFormDataPart(ParamsConstans.PARAM_PHONE, phone)
                    .build();
        }


        return requestBody;


//        final FormBody.Builder formEncodingBuilder = new FormBody.Builder();
//        formEncodingBuilder.add(ParamsConstans.PARAM_USERID, userId);
//        formEncodingBuilder.add(ParamsConstans.PARAM_USERNAME, username);
//        formEncodingBuilder.add(ParamsConstans.PARAM_ADDRESS, address);
//        formEncodingBuilder.add(ParamsConstans.PARAM_DEVICE_TOKEN, uniqId);
//        formEncodingBuilder.add(ParamsConstans.PARAM_PROFILE, imagePath);
//        //formEncodingBuilder.add(ParamsConstans.PARAM_EMAIL_ID, email);
//        formEncodingBuilder.add(ParamsConstans.PARAM_PHONE, phone);
//        return formEncodingBuilder.build();
    }

    private void parseResponse(final String response) {

        if (response != null && response.trim().length() > 0) {

            try {
                final JSONObject jsonObject = new JSONObject(response);
                final WsConstants wsConstants = new WsConstants();

                success = jsonObject.optString(wsConstants.PARAMS_SUCCESS).equals("1") ? true : false;
                message = jsonObject.optString(wsConstants.PARAMS_MESSAGE);

                setMessage(message);
                setSuccess(success);


                if (isSuccess()) {
                    final JSONObject jsonObjectData = jsonObject.optJSONObject(wsConstants.PARAMS_DATA);
                    AutumobileAplication.getmInstance().savePreferenceDataString(context.getString(R.string.preferances_userName), jsonObjectData.getString("user_name"));
                    AutumobileAplication.getmInstance().savePreferenceDataString(context.getString(R.string.preferances_userEmail), jsonObjectData.getString("user_email"));
                    AutumobileAplication.getmInstance().savePreferenceDataString(context.getString(R.string.preferances_userPhone), jsonObjectData.getString("user_phone"));
                    AutumobileAplication.getmInstance().savePreferenceDataString(context.getString(R.string.preferances_useAddress), jsonObjectData.getString("user_address"));
                    AutumobileAplication.getmInstance().savePreferenceDataString(context.getString(R.string.preferances_userProfilePic), jsonObjectData.getString("user_profile"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
