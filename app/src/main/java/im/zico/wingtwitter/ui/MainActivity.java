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
import android.view.Menu;
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
import im.zico.wingtwitter.ui.fragment.DraftFragment;
import im.zico.wingtwitter.ui.fragment.DrawerFragment;
import im.zico.wingtwitter.ui.fragment.FavoriteFragment;
import im.zico.wingtwitter.ui.fragment.HomeFragment;
import im.zico.wingtwitter.ui.fragment.ListsFragment;
import im.zico.wingtwitter.ui.fragment.MentionedFragment;
import im.zico.wingtwitter.ui.fragment.SearchFragment;
import im.zico.wingtwitter.ui.fragment.TrendsFragment;
import im.zico.wingtwitter.view.UnderlinePageIndicator;


public class MainActivity extends BaseActivity implements DrawerFragment.NavigationDrawerCallbacks {

    private ActionBarDrawerToggle toggle;

    private DrawerFragment mNavigationDrawerFragment;

//    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
//    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        mNavigationDrawerFragment = (DrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Drawer fragment
//        drawerFragment = (DrawerFragment)
//                getFragmentManager().findFragmentById(R.id.navigation_drawer);
    }

    //    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        toggle.syncState();
//    }


    @Override
    public void onNavigationDrawerItemSelected(int groupPos, int childPos) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, getFragment(groupPos))
                .commit();
        setTitle(getResources().getStringArray(R.array.drawer_menu_group)[groupPos]);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mNavigationDrawerFragment.getToggle().onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Fragment getFragment(int position) {

        switch (position) {
            case 0:
                return HomeFragment.newInstance(position);
            case 1:
                return FavoriteFragment.newInstance(position);
            case 2:
                return ListsFragment.newInstance(position);
            case 3:
                return TrendsFragment.newInstance(position);
            case 4:
                return SearchFragment.newInstance(position);
            case 5:
                return DraftFragment.newInstance(position);
        }

        return null;
    }


//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        int TAB_SIZE = 3;
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 0:
//                    return HomeFragment.newInstance(position + 1);
//                case 1:
//                    return MentionedFragment.newInstance(position + 1);
//                case 2:
//                    return DMFragment.newInstance(position + 1);
//            }
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            // Show 3 total pages.
//            return TAB_SIZE;
//        }
//    }

}