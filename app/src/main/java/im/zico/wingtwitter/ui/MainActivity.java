package im.zico.wingtwitter.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;
import android.app.ActionBar.LayoutParams;

import java.lang.reflect.Field;
import java.util.Locale;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.ui.fragment.DMFragment;
import im.zico.wingtwitter.ui.fragment.DrawerFragment;
import im.zico.wingtwitter.ui.fragment.HomeFragment;
import im.zico.wingtwitter.ui.fragment.MentionedFragment;
import im.zico.wingtwitter.view.UnderlinePageIndicator;


public class MainActivity extends BaseActivity implements DrawerFragment.NavigationDrawerCallbacks {

    private ActionBarDrawerToggle toggle;

    private DrawerFragment drawerFragment;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        LayoutInflater inflator =
                (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // custom view for viewpager indicator
        View v = inflator.inflate(R.layout.layout_indicator, null);
        UnderlinePageIndicator indicator = (UnderlinePageIndicator)v.findViewById(R.id.indicator);

        // Set the indicator in the actionbar
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
                Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(v, lp);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        indicator.setViewPager(mViewPager);

        // Set DrawerLayout and Toggle
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
        enlargeDrawerDragger(drawerLayout);

        // Drawer fragment
        drawerFragment = (DrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

    }

    /**
     * Enlarge the trigger edge for the sliding drawer
     * @param mDrawerLayout
     */
    private void enlargeDrawerDragger(DrawerLayout mDrawerLayout) {
        Field mDragger = null;
        try {
            mDragger = ((Object)mDrawerLayout).getClass().getDeclaredField(
                    "mLeftDragger");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        mDragger.setAccessible(true);

        ViewDragHelper draggerObj = null;
        try {
            draggerObj = (ViewDragHelper) mDragger
                    .get(mDrawerLayout);
            Field mEdgeSize = draggerObj.getClass().getDeclaredField(
                    "mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(draggerObj);

            mEdgeSize.setInt(draggerObj, edge * 3);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        int TAB_SIZE = 3;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance(position+1);
                case 1:
                    return MentionedFragment.newInstance(position + 1);
                case 2:
                    return DMFragment.newInstance(position + 1);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return TAB_SIZE;
        }
    }

}