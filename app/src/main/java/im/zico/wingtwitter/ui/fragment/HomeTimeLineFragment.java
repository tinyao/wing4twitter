package im.zico.wingtwitter.ui.fragment;

import android.app.Activity;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.Calendar;

import im.zico.wingtwitter.APIKey;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.ui.MainActivity;
import im.zico.wingtwitter.ui.TweetComposeActivity;
import im.zico.wingtwitter.utils.PrefKey;
import im.zico.wingtwitter.utils.PreferencesManager;
import twitter4j.AsyncTwitter;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by tinyao on 12/4/14.
 */
public class HomeTimeLineFragment extends BaseStatusesListFragment {

    private static final String TAG = "HOME_TIMELINE";
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ImageButton composeBtn;
    private boolean fabVisible = true;

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static HomeTimeLineFragment newInstance(int sectionNumber) {
        HomeTimeLineFragment fragment = new HomeTimeLineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeTimeLineFragment() {

    }

    boolean isLaunch = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPageId = getArguments().getInt(ARG_SECTION_NUMBER);
        Log.d("DEBUG", "HomeTimeline onCreate: " + Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        composeBtn = (ImageButton) view.findViewById(R.id.fab_compose);
        composeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TweetComposeActivity.showDialog(getActivity(), new Bundle());
            }
        });
        Log.d("DEBUG", "HomeTimeline onViewCreated: " + Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public int getType() {
        return WingStore.TYPE_TWEET;
    }

    @Override
    public void loadLatest() {
        Log.d(TAG, "Load latest tweets ...");
        if (isListEmpty()) {
            getAsyncTwitter().getHomeTimeline();
        } else {
            getAsyncTwitter().getHomeTimeline(
                    new Paging(1, 20, mAdapter.getItem(0).tweet_id));
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        if (isLaunch) {
            int index = PreferencesManager.getInstance(getActivity()).getIntValue("timeline_position_index");
            int top = PreferencesManager.getInstance(getActivity()).getIntValue("timeline_position_top");
            mListView.setSelectionFromTop(index, top);
            isLaunch = false;
        }
    }

    @Override
    public void loadNext() {
        getAsyncTwitter().getHomeTimeline(
                new Paging(1, 20).maxId(mAdapter.getItem(mAdapter.getCount() - 1).tweet_id - 1));
    }

    @Override
    public synchronized void onScrollDown() {
        if (fabVisible) {
            // 显示
//            showToolbar(false);
            ViewPropertyAnimator.animate(composeBtn).setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(200)
                    .translationY(getResources().getDimensionPixelSize(R.dimen.fab_offset))
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fabVisible = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
    }

    @Override
    public synchronized void onScrollUp() {
        if (!fabVisible) {
//            showToolbar(true);
            ViewPropertyAnimator.animate(composeBtn).setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(200)
                    .translationY(0)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fabVisible = true;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
    }

//    @Override
//    public void onScrollHeader() {
//        showToolbar(true);
//    }
//
//    public synchronized void showToolbar(boolean toShow) {
//        Toolbar toolbar = ((MainActivity) getActivity()).getToolBar();
//        ViewPropertyAnimator.animate(toolbar).setInterpolator(new AccelerateDecelerateInterpolator())
//                .setDuration(200)
//                .translationY(toShow ? 0 : 0 - getResources().getDimensionPixelSize(R.dimen.toolbar_height));
//    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("DEBUG", "HomeTimeline onAttach: " + Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int index = mListView.getFirstVisiblePosition();
        View v = mListView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - mListView.getPaddingTop());
        outState.putInt("index", index);
        outState.putInt("top", top);
        PreferencesManager.getInstance(getActivity()).setValue("timeline_position_index", index);
        PreferencesManager.getInstance(getActivity()).setValue("timeline_position_top", top);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        onSaveInstanceState(new Bundle());
        super.onDestroy();
    }
}
