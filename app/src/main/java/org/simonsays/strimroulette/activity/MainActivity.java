package org.simonsays.strimroulette.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.simonsays.strimroulette.R;
import org.simonsays.strimroulette.fragment.GamesFragment;
import org.simonsays.strimroulette.fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private int[] tabIcons = {
            R.drawable.ic_videogame_asset,
            R.drawable.ic_settings_applications
    };

    private String[] tabTitles = {
            "GAMES",
            "SETTINGS"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GamesFragment(), getString(R.string.games_tab_title));
        adapter.addFragment(new SettingsFragment(), getString(R.string.settings_tab_title));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        RelativeLayout gamesTab = getRelativeLayout(tabTitles[0], tabIcons[0]);
        tabLayout.getTabAt(0).setCustomView(gamesTab);

        RelativeLayout settingsTab = getRelativeLayout(tabTitles[1], tabIcons[1]);
        tabLayout.getTabAt(1).setCustomView(settingsTab);
    }

    @NonNull
    private RelativeLayout getRelativeLayout(String text, int imageResource) {
        RelativeLayout countTab = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);

        TabViewHolder tabViewHolder = new TabViewHolder(countTab);

        tabViewHolder.icon.setImageResource(imageResource);
        tabViewHolder.title.setText(text);
        return countTab;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
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

    class TabViewHolder {
        @BindView(R.id.tab_text)
        TextView title;
        @BindView(R.id.tab_img)
        ImageView icon;

        TabViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}