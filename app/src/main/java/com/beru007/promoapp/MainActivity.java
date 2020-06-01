package com.beru007.promoapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.beru007.promoapp.ui.main.SectionsPagerAdapter;
import com.example.promoapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import pro.userx.UserX;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;


public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppEventsLogger logger = AppEventsLogger.newLogger(MainActivity.this);
        logger.logEvent("sentFriendRequest");
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        UserX.init("0159dc0f-b663-4127-9e7f-6193eaa47795");
        OneSignal.startInit(this).setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
            @Override
            public void notificationOpened(OSNotificationOpenResult result) {

                String launchURL = result.notification.payload.launchURL;

                if (launchURL != null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", launchURL);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }).init();

        // Creating an extended library configuration.
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("9a44b3f1-3564-444b-9e33-f3d5a7899941").build();
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(getApplication());
    }

}