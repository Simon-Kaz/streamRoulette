package org.simonsays.strimroulette.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import org.simonsays.strimroulette.fragment.CountFragment;
import org.simonsays.strimroulette.fragment.GamesFragment;
import org.simonsays.strimroulette.R;
import org.simonsays.strimroulette.utils.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // number of tabs
    private static final int NUM_ITEMS = 2;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HttpUtils httpUtils;
    private int[] tabIcons = {
            R.drawable.ic_tab_games,
            R.drawable.ic_tab_count
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GamesFragment(), getString(R.string.games_tab_title));
        adapter.addFragment(new CountFragment(), getString(R.string.count_tab_title));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        TextView gamesTab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        gamesTab.setText(getString(R.string.games_tab_title));
        gamesTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_games, 0, 0);
        tabLayout.getTabAt(0).setCustomView(gamesTab);

        TextView countTab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        countTab.setText(getString(R.string.count_tab_title));
        countTab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_count, 0, 0);
        tabLayout.getTabAt(1).setCustomView(countTab);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    // onClick action for the start_roulette_fab button
    public void launchStreamActivity(View view) {
        Intent intent = new Intent(this, StreamActivity.class);
        startActivity(intent);
    }
}