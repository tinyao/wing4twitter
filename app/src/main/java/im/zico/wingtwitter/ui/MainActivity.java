package im.zico.wingtwitter.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toolbar;

import java.util.Calendar;

import im.zico.wingtwitter.R;
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

//        final Intent intent = new Intent(this, TweetService.class);
//        intent.setAction(TweetService.INTENT_ACTION_GET_FOLLOWERS);
//        startService(intent);
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
        Log.d("DEBUG", "ItemSelected: " + Calendar.getInstance().getTimeInMillis());
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                .replace(R.id.container, getFragment(groupPos), "" + groupPos)
                .commit();

        Log.d("DEBUG", "Transaction: " + Calendar.getInstance().getTimeInMillis());
        setTitle(getResources().getStringArray(R.array.drawer_menu_group)[groupPos]);

        if (groupPos == 0) {
            setTitle(getResources().getString(R.string.app_name));
        } else {
            getActionBar().setDisplayShowCustomEnabled(false);
        }
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

}