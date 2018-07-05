package com.employstream.applicantkioske;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.employstream.applicantkioske.Controllers.Api;
import com.employstream.applicantkioske.Controllers.Session;
import com.employstream.applicantkioske.Controllers.ThemeColors;
import com.employstream.applicantkioske.Interfaces.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ApiInterface {
    private SharedPreferences mPrefs;

    private ImageView mCompanyLogoOne;
    private ImageView mCompanyLogoTwo;
    private LinearLayout mScreen;
    private TextView mCustomerCode;
    private ViewSwitcher mCompanyLogoSwitcher;

    private Session mSession = Session.getSession();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeColors(this);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences("employstream", MODE_PRIVATE);
        mPrefs.edit().clear().apply();
        if(!mPrefs.contains("colorPrimary")) {
            mPrefs.edit().putString("colorPrimaryDark", "#0c2f4d").putString("colorPrimary", "#303f9f").putString("colorAccent", "#39a046").apply();
        }

        mCustomerCode = (TextView) findViewById(R.id.customerCode);
        mCustomerCode.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    new Api.Get("company", "https://testapp.employstream.com/api/unauthprofile/customer?customer_code=" + mCustomerCode.getText(), MainActivity.this).execute((Void) null);
                    new Api.Get("logo", "https://testapp.employstream.com/api/branding/logo/" + mCustomerCode.getText(), MainActivity.this).execute((Void) null);
                    return true;
                }
                return false;
            }
        });


        mCompanyLogoOne = (ImageView)findViewById(R.id.imgOne);
        mCompanyLogoTwo = (ImageView) findViewById(R.id.imgTwo);
        mCompanyLogoSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
        if(mPrefs.contains("logo")) {
            byte[] decodedString = Base64.decode(mPrefs.getString("logo", ""), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            mCompanyLogoOne.setImageBitmap(decodedByte);
        }

        mScreen = (LinearLayout) findViewById(R.id.screen);

        ObjectAnimator colorFade = ObjectAnimator.ofObject(mScreen, "backgroundColor", new ArgbEvaluator(), mSession.hex2Rgb(mPrefs.getString("colorPrimary", "")), mSession.hex2Rgb(mPrefs.getString("colorPrimaryDark", "")));
        colorFade.setDuration(1000);
        colorFade.start();
    }

    @Override
    public void onRequestComplete(String request, JSONObject requestData) {
        try {
            if (request.equalsIgnoreCase("company")) {
                handleCompanyReturn(requestData);
            } else if (request.equalsIgnoreCase("logo")) {
                handleLogoReturn(requestData);
            } else if (request.equalsIgnoreCase("brandingColors")) {
                handleBrandingColorsReturn(requestData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchViews() {
        if (mCompanyLogoSwitcher.getDisplayedChild() == 0) {
            mCompanyLogoSwitcher.showNext();
        } else {
            mCompanyLogoSwitcher.showPrevious();
        }
    }

    private void handleCompanyReturn(JSONObject requestData) throws Exception {
        new Api.Get("brandingColors", "https://testapp.employstream.com/api/v2/customers/" + requestData.getString("customer_id") + "/branding/colors?customer_code=" + mCustomerCode.getText(), MainActivity.this).execute((Void) null);
    }

    private void handleLogoReturn(JSONObject requestData) throws Exception {
        mPrefs.edit().putString("logo", requestData.getString("img")).apply();
        byte[] decodedString = Base64.decode(requestData.getString("img"), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if(mCompanyLogoOne.getVisibility() == View.GONE) {
            mCompanyLogoOne.setImageBitmap(decodedByte);
        } else if (mCompanyLogoTwo.getVisibility() == View.GONE) {
            mCompanyLogoTwo.setImageBitmap(decodedByte);
        }
        switchViews();
    }

    private void handleBrandingColorsReturn(JSONObject requestData) throws Exception {
        System.out.println(requestData);
        final JSONArray values = requestData.getJSONArray("colors");
        ObjectAnimator colorFade = ObjectAnimator.ofObject(mScreen, "backgroundColor", new ArgbEvaluator(), mSession.hex2Rgb(mPrefs.getString("colorPrimaryDark", "")), mSession.hex2Rgb((String)values.get(0)));
        colorFade.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    mPrefs.edit().putString("colorPrimaryDark", (String) values.get(0)).putString("colorPrimary", (String) values.get(1)).putString("colorAccent", (String) values.get(2)).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        colorFade.setDuration(1000);
        colorFade.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, ApplicationActivity.class);
                startActivity(i);
            }
        }, 3000);
    }
}
