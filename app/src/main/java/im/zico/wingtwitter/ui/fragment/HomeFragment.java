package im.zico.wingtwitter.ui.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import im.zico.wingtwitter.APIKey;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.dao.WingDataHelper;
import im.zico.wingtwitter.type.Tweet;
import im.zico.wingtwitter.view.UnderlinePageIndicator;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterListener;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by tinyao on 12/1/14.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener{

    HomePagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    UnderlinePageIndicator indicator;

    public static HomeFragment newInstance(int frameId) {
        HomeFragment fragment = new HomeFragment();
        setParams(fragment, frameId);
        return fragment;
    }

    public HomeFragment() {
    }

    WingDataHelper DBHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set custom toolbar layout
        LayoutInflater inflator =
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_indicator, null);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        getActivity().getActionBar().setDisplayShowCustomEnabled(true);
        getActivity().getActionBar().setCustomView(v, lp);

        indicator = (UnderlinePageIndicator) v.findViewById(R.id.indicator);

        DBHelper = new WingDataHelper(getActivity());


        v.findViewById(R.id.tab_home).setOnClickListener(this);
        v.findViewById(R.id.tab_mention).setOnClickListener(this);
        v.findViewById(R.id.tab_message).setOnClickListener(this);
    }

    private ProgressBar pb;
    private TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DEBUG", "drawer fragment - onCreatedView");
        View contentView = inflater.inflate(R.layout.fragment_home, container, false);

        mSectionsPagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) contentView.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        indicator.setViewPager(mViewPager);

        pb = (ProgressBar) contentView.findViewById(R.id.load_progress);
        tv = (TextView) contentView.findViewById(R.id.tv_status);

        AsyncTwitter asyncTwitter = WingApp.getTwitterInstance();
        asyncTwitter.addListener(listener);
        asyncTwitter.getHomeTimeline();

        Log.d("DEBUG", "get home timeline ...");

        return contentView;
    }

    private TwitterListener listener = new TwitterAdapter() {
        @Override
        public void gotHomeTimeline(ResponseList<Status> statuses) {
            super.gotHomeTimeline(statuses);

            Log.d("DEBUG", "home timeline: " + statuses);

            Log.d("DEBUG", "FIRST TWEET: " + statuses.get(0));

            Tweet tweet = new Tweet(statuses.get(0));
            DBHelper.insert(tweet);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_home:
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.tab_mention:
                mViewPager.setCurrentItem(1, true);
                break;
            case R.id.tab_message:
                mViewPager.setCurrentItem(2, true);
                break;
        }
    }

    private class HomePagerAdapter extends FragmentPagerAdapter {

        int TAB_SIZE = 3;

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TimeLineFragment.newInstance(position);
                case 1:
                    return MentionedFragment.newInstance(position);
                case 2:
                    return DMFragment.newInstance(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            return TAB_SIZE;
        }
    }
}
