package com.automobile.service.locationServices;

/**
 * Created by root on 13/7/18.
 */

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.util.Log;

import com.sports.connect.R;
import com.sports.connect.SportsEventAplication;
import com.sports.connect.model.ConatctSync.ContactModel;
import com.sports.connect.model.ConatctSync.ContactSyncResponseModel;
import com.sports.connect.mvp.ContactSync.ContactSync;
import com.sports.connect.sqlite.DatabaseHelper;
import com.sports.connect.util.ParamsConstans;
import com.sports.connect.util.WsConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactService extends IntentService {

    private DatabaseHelper dbHelper;
    private ArrayList<ContactModel> listContactModel=new ArrayList<ContactModel>();
    private ContactModel contactModel=null;
    private Retrofit retrofit;
    private ContentResolver cResolver;

    public ContactService() {
        super("ContactService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("Data","Call");
        try {

            addUpdatePhoneBookContacts();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addUpdatePhoneBookContacts() {

        try {

            boolean isCheckFirst=false;
            dbHelper=new DatabaseHelper(ContactService.this);
            Cursor phones=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
            String timestampPef=SportsEventAplication.getmInstance().getSharedPreferences().getString(getString(R.string.preferances_timestamp),"");
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            if (timestampPef.equalsIgnoreCase("")) {
                timestampPef=sdf.format(new Date());
                SportsEventAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_timestamp),timestampPef);
                isCheckFirst=true;
            }


            while (phones.moveToNext()) {

                final String id=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                final String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String firstname="";
                String lastname="";
                final String phoneNumber=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String timestamp=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP));


                String email="";

                if (name.contains(" ")) {
                    List<String> items=Arrays.asList(name.split(" "));
                    lastname=items.get(items.size()-1);
                    int endIndex=name.lastIndexOf(" ");
                    if (endIndex != -1) {
                        firstname=name.substring(0,endIndex);
                    }

                } else {
                    firstname=name;
                }


                contactModel=new ContactModel();
                contactModel.setId(id);
                contactModel.setFistname(firstname);
                contactModel.setLastname(lastname);
                contactModel.setPhoneno(phoneNumber);


                SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datePre=sdf1.parse(timestampPef);
                Date dateCur=null;

                dateCur=sdf1.parse(getDate(Long.parseLong(timestamp)));


                if (isCheckFirst) {
                    listContactModel.add(contactModel);
                    timestampPef=sdf.format(new Date());
                    SportsEventAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_timestamp),timestampPef);
                } else {
                    if (datePre.before(dateCur)) {
                        listContactModel.add(contactModel);

                        timestampPef=sdf.format(new Date());
                        SportsEventAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_timestamp),timestampPef);
                    }
                }

            }

            if(listContactModel!=null && listContactModel.size() > 0)
            {
                executeService(listContactModel);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private String getDate(long time) {
        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date=DateFormat.format("yyyy-MM-dd HH:mm:ss",cal).toString();
        return date;
    }

    public void executeService(final ArrayList<ContactModel> conatacsmodel) {


        Log.d("conatacsmodel","conatacsmodel"+conatacsmodel);

        try {

            final String userID=SportsEventAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_userID)),"");
            final String token=SportsEventAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_token)),"");
            final String timestamp=SportsEventAplication.getmInstance().getSharedPreferences().getString(getString((R.string.preferances_contact_timestamp)),"");


            Handler mHandler=new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient client=new OkHttpClient();
                    client=getClient(interceptor);

                    retrofit=new Retrofit.Builder()
                            .baseUrl(WsConstants.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .build();





                    Observable<ContactSyncResponseModel> productObservable=retrofit.create(ContactSync.class).
                            doContactSync(userID,token,timestamp,getSelectContactsArr(conatacsmodel).toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io());

                    productObservable.subscribe(new Observer<ContactSyncResponseModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(ContactSyncResponseModel contactResponseModel) {


                            Log.d("executeService","getMessage==="+contactResponseModel.getMessage());

                            if (contactResponseModel.getSuccess().toString().equalsIgnoreCase("1")) {

                                SportsEventAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_contact_timestamp),""+contactResponseModel.getData().getLastSync().getTimestamp());
                            }
                        }
                    });
                }
            });


        } catch (Exception e) {

            e.printStackTrace();
        }

    }



    private OkHttpClient getClient(HttpLoggingInterceptor interceptor) {
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.MINUTES)
                .readTimeout(30,TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build();
        return client;
    }

    private JSONArray getSelectContactsArr(ArrayList<ContactModel> conatacsmodel) {
        JSONArray selcetArr=new JSONArray();

        for (int i=0; i < conatacsmodel.size(); i++) {
            JSONObject jsonObjectReq=new JSONObject();

            try {
                jsonObjectReq.put(ParamsConstans.PARAM_FIRSTNAME,conatacsmodel.get(i).getFistname());
                jsonObjectReq.put(ParamsConstans.PARAM_LASTNAME,conatacsmodel.get(i).getLastname());
                jsonObjectReq.put(ParamsConstans.PARAM_CONTACTPHONE,conatacsmodel.get(i).getPhoneno());
                selcetArr.put(jsonObjectReq);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("text/plain"),selcetArr.toString());

        return selcetArr;
    }


}






