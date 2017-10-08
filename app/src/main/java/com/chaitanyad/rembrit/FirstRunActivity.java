package com.chaitanyad.rembrit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class FirstRunActivity extends AppIntro2 implements Fragment_FirstRun1.OnFragmentInteractionListener,Fragment_FirstRun2.OnFragmentInteractionListener,Fragment_FirstRun3.OnFragmentInteractionListener,Fragment_FirstRun4.OnFragmentInteractionListener,Fragment_FirstRun5.OnFragmentInteractionListener,Fragment_FirstRun6.OnFragmentInteractionListener,Fragment_FirstRun7.OnFragmentInteractionListener,Fragment_FirstRun8.OnFragmentInteractionListener,Fragment_FirstRun9.OnFragmentInteractionListener,Fragment_FirstRun9_1.OnFragmentInteractionListener,Fragment_FirstRun10.OnFragmentInteractionListener {

    public static int globWakeupHour;
    public static int globWakeupMin;
    public static int globLeaveForWorkHour;
    public static int globLeaveForWorkMin;
    public static int globReachWorkHour;
    public static int globReachWorkMin;
    public static int globLunchHour;
    public static int globLunchMin;
    public static int globLeaveFromWorkHour;
    public static int globLeaveFromWorkMin;
    public static int globReachHomeHour;
    public static int globReachHomeMin;
    public static int globDinnerHour;
    public static int globDinnerMin;
    public static int globSleepHour;
    public static int globSleepMin;
    DatabaseHelper db;
    public boolean isFirstStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_first_run);
        Fragment_FirstRun1 fragment_firstRun1 = new Fragment_FirstRun1();
        Fragment_FirstRun2 fragment_firstRun2 = new Fragment_FirstRun2();
        Fragment_FirstRun3 fragment_firstRun3 = new Fragment_FirstRun3();
        Fragment_FirstRun4 fragment_firstRun4 = new Fragment_FirstRun4();
        Fragment_FirstRun5 fragment_firstRun5 = new Fragment_FirstRun5();
        Fragment_FirstRun6 fragment_firstRun6 = new Fragment_FirstRun6();
        Fragment_FirstRun7 fragment_firstRun7 = new Fragment_FirstRun7();
        Fragment_FirstRun8 fragment_firstRun8 = new Fragment_FirstRun8();
        Fragment_FirstRun9 fragment_firstRun9 = new Fragment_FirstRun9();
        Fragment_FirstRun9_1 fragment_firstRun9_1 = new Fragment_FirstRun9_1();
        Fragment_FirstRun10 fragment_firstRun10 = new Fragment_FirstRun10();

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        isFirstStart = getPrefs.getBoolean("firstStart", true);

        //  If the activity has never started before...
        if (isFirstStart) {
            SharedPreferences.Editor e = getPrefs.edit();

            //  Edit preference to make it false because we don't want this to run again
            e.putBoolean("firstStart", false);

            //  Apply changes
            e.apply();
            addSlide(fragment_firstRun1);
        }

        addSlide(fragment_firstRun2);
        addSlide(fragment_firstRun3);
        addSlide(fragment_firstRun4);
        addSlide(fragment_firstRun5);
        addSlide(fragment_firstRun6);
        addSlide(fragment_firstRun7);
        addSlide(fragment_firstRun8);
        addSlide(fragment_firstRun9);
        addSlide(fragment_firstRun9_1);
        addSlide(fragment_firstRun10);

        db=new DatabaseHelper(this);
        skipButton.setEnabled(false);

    }

    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Toast.makeText(getApplicationContext(), "Skip", Toast.LENGTH_SHORT).show(); // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
       /* Log.e("Wake Time- ",globWakeupHour+":"+globWakeupMin);
        Log.e("Leave for Work Time- ",globLeaveForWorkHour+":"+globLeaveForWorkMin);
        Log.e("Lunch Time- ",globLunchHour+":"+globLunchMin);
        Log.e("Leave from Work Time- ",globLeaveFromWorkHour+":"+globLeaveFromWorkMin);
        Log.e("Reach home Time- ",globReachHomeHour+":"+globReachHomeMin);
        Log.e("Dinner Time- ",globDinnerHour+":"+globDinnerMin);
        Log.e("Sleep Time- ",globSleepHour+":"+globSleepMin);
       */
        db.insertData_routine(String.valueOf(globWakeupHour),String.valueOf(globWakeupMin),String.valueOf(globLeaveForWorkHour),String.valueOf(globLeaveForWorkMin),String.valueOf(globReachWorkHour),String.valueOf(globReachWorkMin),String.valueOf(globLunchHour),String.valueOf(globLunchMin),String.valueOf(globLeaveFromWorkHour),String.valueOf(globLeaveFromWorkMin),String.valueOf(globReachHomeHour),String.valueOf(globReachWorkMin),String.valueOf(globDinnerHour),String.valueOf(globDinnerMin),String.valueOf(globSleepHour),String.valueOf(globSleepMin));
        Intent intent=new Intent(this,MainActivity.class);
        finish();
        startActivity(intent);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
