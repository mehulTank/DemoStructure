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

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created date on 16/05/17.
 * This class is makiing api call for Change Psw  webservice
 */

public class WSChangePsw {

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


    public WSChangePsw(final Context context) {
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

    public void executeService(Context context, final String old_pse, final String new_pse) {

        final String url = WsConstants.BASE_URL + WsConstants.METHOD_CHANGE_PSW;
        final String response = new WSUtil(context).callServiceHttpPost(context, url, generateLoginRequest(old_pse, new_pse));
        parseResponse(response);

    }

    private RequestBody generateLoginRequest(final String old_pse, final String new_pse) {
        final String userId = AutumobileAplication.getmInstance().getSharedPreferences().getString(context.getString((R.string.preferances_userID)), "");

        final FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        formEncodingBuilder.add(ParamsConstans.PARAM_USERID, userId);
        formEncodingBuilder.add(ParamsConstans.PARAM_OLD_PSW, old_pse);
        formEncodingBuilder.add(ParamsConstans.PARAM_NEW_PSW, new_pse);
        return formEncodingBuilder.build();
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


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
