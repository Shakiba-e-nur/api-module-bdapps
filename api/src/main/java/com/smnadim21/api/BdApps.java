package com.smnadim21.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.smnadim21.api.model.CheckStatusModel;
import com.smnadim21.api.network.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.smnadim21.api.BdAppsApplication.getContext;
import static com.smnadim21.api.Subscription.getSubCode;
import static com.smnadim21.api.Subscription.setSubCode;
import static com.smnadim21.api.Subscription.setSubscriptionClicked;
import static com.smnadim21.api.Subscription.setSubscriptionStatus;


public class BdApps extends Constants {

    public static final String is_there = "is_there";
    private static String android_id = Settings.Secure.getString(getContext().getContentResolver(),
            Settings.Secure.ANDROID_ID);


//    private static void checkSubStatus(String code) {
//
//
//        ApiClient
//                .getStringInstance()
//                .getStatus(code)
//                .enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//
//                        Log.e("response", response.toString());
//                        if (response.body() != null) {
//                            try {
//                                JSONObject staus = new JSONObject(response.body());
//
//                                if (staus.has(is_there) && staus.getBoolean(is_there)) {
//                                    setSubscriptionStatus(false);
//                                    toast("Subscription Status True");
//                                } else {
//                                    setSubscriptionStatus(true);
//                                    toast("not a valid subscriber");
//                                }
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                toast("not a valid subscriber");
//                            }
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call call, Throwable t) {
//
//                        Log.e("response", t.getMessage());
//                        setSubscriptionStatus(true);
//                        toast("Status Getting Failed");
//                    }
//                });
//
//    }

//
//    public static void checkSubStatus() {
//
//        toast("Checking....");
//
//        ApiClient
//                .getStringInstance()
//                .getStatus(getSubCode())
//                .enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//
//                        Log.e("response", response.toString());
//
//                        if (response.body() != null) {
//                            try {
//                                JSONObject staus = new JSONObject(response.body());
//
//                                if (staus.has(is_there) && staus.getBoolean(is_there)) {
//                                    setSubscriptionStatus(false);
//                                    toast("Subscription Status True");
//                                } else {
//                                    setSubscriptionStatus(true);
//                                    toast("not a valid subscriber");
//                                }
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                toast("not a valid subscriber");
//                            }
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call call, Throwable t) {
//
//                        Log.e("response", t.getMessage());
//                        setSubscriptionStatus(true);
//                        toast("Status Getting Failed");
//                        // Toast.makeText(AppConfig.getContext(), "Status Getting Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }


    public static void registerAPP() {
        ApiClient
                .getStringInstance()
                .register(APP_ID, APP_PASSWORD)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e("response", "reg" + response.toString());

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }


//    public static void subscribe() {
//        ApiClient.getStringInstance()
//                .subscribe(APP_ID, APP_PATH)
//                .enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        Log.e("response", response.toString());
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        Log.e("error", t.toString());
//
//                    }
//                });
//    }


    public static void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    public static void showDialog(final Activity activity, final SubscriptionStatusListener statusListener) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.sub);


        final EditText getCode = dialog.findViewById(R.id.otp_code);


        Button dialogButton = (Button) dialog.findViewById(R.id.button_s_daily);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri uri = Uri.parse("smsto:21213");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", Constants.MSG_TEXT);
                activity.startActivity(intent);
                dialog.dismiss();

            }
        });


        dialog.findViewById(R.id.submit_code)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getCode.getText().toString().isEmpty()) {
                            Toast.makeText(activity, "Write a valid code", Toast.LENGTH_SHORT).show();
                        } else {
                            setSubCode(getCode.getText().toString());
                            setSubscriptionClicked(false);
                            sendOtpWithDeviceId(getCode.getText().toString(), android_id, statusListener);
                            dialog.dismiss();
//                            checkSubStatus(getCode.getText().toString());


                        }

                    }
                });


        dialog.show();

    }


    //shakiba


    public static void checkSubscriptionStatus(final SubscriptionStatusListener statusListener) {


        ApiClient
                .getStringInstance()
                .checkSubStatus(APP_ID, android_id)
                .enqueue(new Callback<CheckStatusModel>() {
                    @Override
                    public void onResponse(Call<CheckStatusModel> call, Response<CheckStatusModel> response) {
                        Log.e("responsenew", response.toString());

                        if (response.body() != null) {

                            CheckStatusModel checkStatusModel = response.body();

                            if (statusListener != null) {
                                if (checkStatusModel.getStatus() == TypeCode.RESPONSE_OK) {
                                    if (checkStatusModel.getData() != null) {
                                        if (checkStatusModel.getData().getStatus().equals(TypeStatus.STATUS_REGISTED)) {
                                            statusListener.onSuccess(true);
                                            toast("Subscribed!");
                                            return;
                                        }
                                    }

                                }
                            }
                            if (statusListener != null) {
                                statusListener.onSuccess(false);
                            }
                            toast("Not Subscribed!");
//                                if (staus.has(is_there) && staus.getBoolean(is_there)) {
//                                    setSubscriptionStatus(false);
//                                    toast("Subscription Status True");
//                                } else {
//                                    setSubscriptionStatus(true);
//                                    toast("not a valid subscriber");
//                                }


                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        if (statusListener != null) {
                            statusListener.onFailed("" + t.getMessage());
                            toast("" + t.getMessage());
                        }
                        Log.e("responsenew", t.getMessage());
//                        setSubscriptionStatus(true);
//                        toast("Status Getting Failed");
                        // Toast.makeText(AppConfig.getContext(), "Status Getting Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public static void sendOtpWithDeviceId(String code, String deviceId, final SubscriptionStatusListener statusListener) {
        ApiClient
                .getStringInstance()
                .sumitOtpwithDevice(code, deviceId)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.e("response", "reg" + response.toString());
                        boolean isActive = Boolean.parseBoolean("" + response.body().get("isActive"));
                        if (isActive) {//valid otp
                            checkSubscriptionStatus(statusListener);

                        } else {
                            toast("Invalid OTP");
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        toast("Otp Failed:" + t.getMessage());
                    }
                });
    }

}
