package com.automobile.service.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.automobile.service.AutumobileAplication;
import com.automobile.service.R;


/****************************************************************************
 * SplashActivity
 *
 * @CreatedDate:
 * @ModifiedBy: not yet
 * @ModifiedDate: not yet
 * @purpose:This Class is user for SplashActivity.
 ***************************************************************************/

public class SplashActivity extends AppCompatActivity {
    private int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new SplashTask().execute();
    }

    /**
     * AsycTask for setting splash screen by sleep thread for some time
     */
    private class SplashTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(SPLASH_TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            boolean isLogin = AutumobileAplication.getmInstance().getSharedPreferences().getBoolean(getString((R.string.preferances_islogin)), false);
            //isLogin=true;


            if (isLogin) {

                Intent mLoginIntent = new Intent(SplashActivity.this, MenuBarActivity.class);
                startActivity(mLoginIntent);

                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
                finish();

            } else {
                Intent mMenuIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mMenuIntent);
                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
                finish();
            }


        }
    }
}
