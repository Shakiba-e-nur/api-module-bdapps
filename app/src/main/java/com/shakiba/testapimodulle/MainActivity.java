package com.shakiba.testapimodulle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.smnadim21.api.BdApps;
import com.smnadim21.api.Constants;
import com.smnadim21.api.SubscriptionStatusListener;

public class MainActivity extends AppCompatActivity implements SubscriptionStatusListener {

boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constants.MSG_TEXT = "start 123sa";
        Constants.APP_ID = "APP_016475";
        Constants.APP_PASSWORD = "f36f24ba800203e608718261e2d7d725";
        //BdApps.checkSubStatus();
        BdApps.registerAPP();
        BdApps.checkSubscriptionStatus(MainActivity.this);




    }

    @Override
    public void onSuccess(boolean isSubscribed) {

    }

    @Override
    public void onFailed(String message) {

    }

    public void CheckAPI(View view) {
        if(!flag)
        {
          // BdApps.showDialog(MainActivity.this,this);
        }
    }
}