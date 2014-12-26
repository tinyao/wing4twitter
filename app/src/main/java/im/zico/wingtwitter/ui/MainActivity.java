package im.zico.wingtwitter.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.app.ActionBar.LayoutParams;

import java.lang.reflect.Field;
import java.util.Locale;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.service.TweetService;
import im.zico.wingtwitter.ui.fragment.BaseStatusesListFragment;
import im.zico.wingtwitter.ui.fragment.DMFragment;
import im.zico.wingtwitter.ui.fragment.DraftFragment;
import im.zico.wingtwitter.ui.fragment.DrawerFragment;
import im.zico.wingtwitter.ui.fragment.FavoriteFragment;
import im.zico.wingtwitter.ui.fragment.HomeFragment;
import im.zico.wingtwitter.ui.fragment.ListsFragment;
import im.zico.wingtwitter.ui.fragment.SearchFragment;
import im.zico.wingtwitter.ui.fragment.TrendsFragment;


public class MainActivity extends BaseActivity implements DrawerFragment.NavigationDrawerCallbacks {

    private ActionBarDrawerToggle toggle;
    private DrawerFragment mNavigationDrawerFragment;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        mNavigationDrawerFragment = (DrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        final Intent intent = new Intent(this, TweetService.class);
        intent.setAction(TweetService.INTENT_ACTION_GET_FOLLOWERS);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public Toolbar getToolBar() {
        return toolbar;
    }

    @Override
    public void onNavigationDrawerItemSelected(int groupPos, int childPos) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, getFragment(groupPos), ""+groupPos)
                .commit();


        switch (groupPos) {
            case 0:
                homeFragment = (HomeFragment) fragmentManager.findFragmentByTag("" + groupPos);
                break;
            case 1:
                favoriteFragment = (FavoriteFragment) fragmentManager.findFragmentByTag("" + groupPos);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                searchFragment = (SearchFragment) fragmentManager.findFragmentByTag("" + groupPos);
                break;
            case 5:
                draftFragment = (DraftFragment) fragmentManager.findFragmentByTag("" + groupPos);
                break;
        }

        setTitle(getResources().getStringArray(R.array.drawer_menu_group)[groupPos]);
        if (groupPos == 0) setTitle(getResources().getString(R.string.app_name));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mNavigationDrawerFragment.getToggle().onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private HomeFragment homeFragment;
    private FavoriteFragment favoriteFragment;
    private TrendsFragment trendsFragment;
    private SearchFragment searchFragment;
    private DraftFragment draftFragment;

    public Fragment getFragment(int position) {

        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance(position);
                    Log.d("DEBUG", "HOME NULL");
                }
                return homeFragment;
            case 1:
                if (favoriteFragment == null) {
                    favoriteFragment = FavoriteFragment.newInstance(position);
                    Log.d("DEBUG", "FAVORITE NULL");
                }
                return favoriteFragment;
            case 2:
                return ListsFragment.newInstance(position);
            case 3:
                return TrendsFragment.newInstance(position);
            case 4:
                if (searchFragment == null) {
                    searchFragment = SearchFragment.newInstance(position);
                    Log.d("DEBUG", "SEARCH NULL");
                }
                return searchFragment;
            case 5:
                if (draftFragment == null) {
                    draftFragment = DraftFragment.newInstance(position);
                    Log.d("DEBUG", "DRAFT NULL");
                }
                return draftFragment;
        }

        return null;
    }

}