package com.employstream.applicantkioske;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.employstream.applicantkioske.Controllers.Session;
import com.employstream.applicantkioske.Controllers.ThemeColors;
import com.employstream.applicantkioske.Fragments.AddEducationHistoryFragment;
import com.employstream.applicantkioske.Fragments.AddWorkHistoryFragment;
import com.employstream.applicantkioske.Fragments.ApplicationCompleteFragment;
import com.employstream.applicantkioske.Fragments.BasicInformationFragment;
import com.employstream.applicantkioske.Fragments.EducationHistoryFragment;
import com.employstream.applicantkioske.Fragments.JobPrefsFragment;
import com.employstream.applicantkioske.Fragments.WorkHistoryFragment;

public class ApplicationActivity extends AppCompatActivity
        implements JobPrefsFragment.OnFragmentInteractionListener,
        BasicInformationFragment.OnFragmentInteractionListener,
        WorkHistoryFragment.OnFragmentInteractionListener,
        EducationHistoryFragment.OnFragmentInteractionListener,
        AddWorkHistoryFragment.OnFragmentInteractionListener,
        AddEducationHistoryFragment.OnFragmentInteractionListener,
        ApplicationCompleteFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private SharedPreferences mPrefs;

    private Session mSession = Session.getSession();

    private Button mContinue;
    private Button mBackContinueBack;
    private Button mBackContinueContinue;
    private Button mBack;

    private LinearLayout mBackContinue;

    private ImageView mCompanyLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mPrefs = getSharedPreferences("employstream", MODE_PRIVATE);
        ((CoordinatorLayout) findViewById(R.id.main_content)).setBackgroundColor(mSession.hex2Rgb(mPrefs.getString("colorPrimaryDark", "")));
        //applyGradient();

        byte[] decodedString = Base64.decode(mPrefs.getString("logo", ""), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        mCompanyLogo = (ImageView) findViewById(R.id.companyLogo);
        mCompanyLogo.setImageBitmap(decodedByte);

        mContinue = (Button)findViewById(R.id.nextView);
        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContinue.setVisibility(View.GONE);
                mBackContinue.setVisibility(View.VISIBLE);
                nextView();
            }
        });

        mBackContinueBack = (Button)findViewById(R.id.back_back_button);
        mBackContinueBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getCurrentItem() == 1) {
                    mContinue.setVisibility(View.VISIBLE);
                    mBackContinue.setVisibility(View.GONE);
                }
                previousView();
            }
        });

        mBackContinueContinue = (Button)findViewById(R.id.back_continue_button);
        mBackContinueContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getCurrentItem() == 4) {
                    mBackContinue.setVisibility(View.GONE);
                    mBack.setVisibility(View.VISIBLE);
                }
                nextView();
            }
        });

        mBack = (Button)findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBack.setVisibility(View.GONE);
                mBackContinue.setVisibility(View.VISIBLE);
                previousView();
            }
        });

        mBackContinue = (LinearLayout)findViewById(R.id.back_continue);
    }

    private void nextView() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
    }

    private void previousView() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_application, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position) {
                case 0:
                    return BasicInformationFragment.newInstance();
                case 1:
                    return JobPrefsFragment.newInstance();
                case 2:
                    return AddEducationHistoryFragment.newInstance();
                case 3:
                    return AddWorkHistoryFragment.newInstance();
                case 4:
                    return ApplicationCompleteFragment.newInstance();
                case 5:
                    break;
                default:
                    break;
            }

            return BasicInformationFragment.newInstance();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }
    }

    public void applyGradient() {
        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_content);
        int[] colors = {mSession.hex2Rgb(mPrefs.getString("colorPrimaryDark", "")), mSession.hex2Rgb(mPrefs.getString("colorPrimary", ""))};

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        gd.setCornerRadius(0f);

        layout.setBackground(gd);
    }
}

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     *
     * */
