package im.zico.wingtwitter.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nineoldandroids.view.ViewPropertyAnimator;

import im.zico.wingtwitter.APIKey;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.dao.WingStore;
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

    private static final String ARG_SECTION_NUMBER = "section_number";

    private AsyncTwitter asyncTwitter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPageId = getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save index and top position
        int index = mListView.getFirstVisiblePosition();
        View v = mListView.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
        outState.putInt("postion", index);
        outState.putInt("top", top);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        asyncTwitter = WingApp.newTwitterInstance();
        super.onViewCreated(view, savedInstanceState);
        composeBtn = (ImageButton) view.findViewById(R.id.fab_compose);
        composeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TweetComposeActivity.showDialog(getActivity(), new Bundle());
            }
        });
    }

    @Override
    public void onTwitterError(TwitterException te, TwitterMethod method) {
        super.onTwitterError(te, method);
    }

    @Override
    public int getType() {
        return WingStore.TYPE_TWEET;
    }

    @Override
    public AsyncTwitter getAsyncTwitter() {
        return asyncTwitter;
    }

    @Override
    public void loadLatest() {
        Log.d("DEBUG", "Load latest tweets ...");
        if (isListEmpty()) {
            asyncTwitter.getHomeTimeline();
        } else {
            asyncTwitter.getHomeTimeline(
                    new Paging(1, 20, mAdapter.getItem(0).tweet_id));
        }
    }

    @Override
    public void loadNext() {
        asyncTwitter.getHomeTimeline(
                new Paging(1, 20).maxId(mAdapter.getItem(mAdapter.getCount() - 1).tweet_id - 1));
    }

    @Override
    protected void onScrollDown() {
        if (fabVisible) {
            // 显示
            ViewPropertyAnimator.animate(composeBtn).setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(200)
                    .translationY(getResources().getDimensionPixelSize(R.dimen.fab_offset));
            fabVisible = false;
        }
    }

    @Override
    protected void onScrollUp() {
        if (!fabVisible) {
            ViewPropertyAnimator.animate(composeBtn).setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(200)
                    .translationY(0);
            fabVisible = true;
        }
    }
}
