package im.zico.wingtwitter.ui.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.adapter.TimeLineAdapter;
import im.zico.wingtwitter.dao.WingDataHelper;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.ui.MainActivity;
import im.zico.wingtwitter.ui.view.LoadingFooter;
import im.zico.wingtwitter.ui.view.TweetListView;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterListener;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by tinyao on 12/4/14.
 */
public class HomeTimeLineFragment extends BaseStatusesListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ObservableScrollViewCallbacks {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private WingDataHelper DBHelper;
    TimeLineAdapter mAdapter;


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

    //    ActionSlideExpandableListView mListView;
    LoadingFooter mLoadingFooter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        mListView = (TweetListView) rootView.findViewById(R.id.list);
        mLoadingFooter = new LoadingFooter(getActivity());
        mListView.addFooterView(mLoadingFooter.getView());
        return rootView;
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

    private void loadNextPage() {
        mLoadingFooter.setState(LoadingFooter.State.Loading);
        asyncTwitter.getHomeTimeline(
                new Paging(1, 20).maxId(mAdapter.getItem(mAdapter.getCount() - 1).tweet_id));
    }


    int index, top;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindSwipeToRefresh((ViewGroup) view);

        asyncTwitter = WingApp.newTwitterInstance();
        asyncTwitter.addListener(listener);

        DBHelper = new WingDataHelper(getActivity());
        mAdapter = new TimeLineAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);

        mListView.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {
            @Override
            public void onClick(View itemView, View clickedView, int position) {
                switch (clickedView.getId()) {
                    case R.id.expand_action_reply:
                        Toast.makeText(getActivity(), "REPLY: " + mAdapter.getItem(position).content, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.expand_action_share:
                        Toast.makeText(getActivity(), "SHARE: " + mAdapter.getItem(position).content, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.expand_action_favorite:
                        break;
                    case R.id.expand_action_retweet:
                        break;
                }
            }
        });


        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("postion");
            top = savedInstanceState.getInt("top");
        }

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (mLoadingFooter.getState() == LoadingFooter.State.Loading
                        || mLoadingFooter.getState() == LoadingFooter.State.TheEnd) {
                    return;
                }
                if (firstVisibleItem + visibleItemCount >= totalItemCount
                        && totalItemCount != 0
                        && totalItemCount != mListView.getHeaderViewsCount() + mListView.getFooterViewsCount()
                        && mAdapter.getCount() > 0) {
                    loadNextPage();
                }
            }
        });

//        mListView.setScrollViewCallbacks(this);

        Log.d("DEBUG", "onCreatedView");
    }

    private boolean mFirstScroll = false;
    private boolean mDragging = false;

    private SwipeRefreshLayout mSwipeRefresh;
    private AsyncTwitter asyncTwitter;

    private TwitterListener listener = new TwitterAdapter() {
        @Override
        public void gotHomeTimeline(ResponseList<Status> statuses) {
            super.gotHomeTimeline(statuses);
            Log.d("DEBUG", "Got Home Timeline: " + statuses.size());
            Message msg = mHandler.obtainMessage();
            msg.obj = statuses;
            mHandler.sendMessage(msg);
        }
    };

    protected void bindSwipeToRefresh(ViewGroup v) {
        mSwipeRefresh = new SwipeRefreshLayout(getActivity());

        // Move child to SwipeRefreshLayout, and add SwipeRefreshLayout to root
        // view
        v.removeViewInLayout(mListView);
        v.addView(mSwipeRefresh, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mSwipeRefresh.addView(mListView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        mSwipeRefresh.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        mSwipeRefresh.setOnRefreshListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return DBHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("DEBUG", "count: " + data.getCount());
        mAdapter.changeCursor(data);
        if (data != null && data.getCount() == 0) {
            loadFirstPage();
        }
    }

    private void loadFirstPage() {
        asyncTwitter.getHomeTimeline();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    @Override
    public void onRefresh() {
        Log.d("DEBUG", "Get Home Timeline");
        if (mAdapter.getCount() == 0) {
            asyncTwitter.getHomeTimeline();
        } else {
            asyncTwitter.getHomeTimeline(
                    new Paging(1, 20, mAdapter.getItem(0).tweet_id));
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!mSwipeRefresh.isRefreshing()) {
                mLoadingFooter.setState(LoadingFooter.State.Idle, 3000);
            } else {
                mSwipeRefresh.setRefreshing(false);
            }

            index = mListView.getFirstVisiblePosition();
            View v = mListView.getChildAt(0);
            top = (v == null) ? 0 : v.getTop();

            ResponseList<Status> statuses = (ResponseList<Status>) msg.obj;

            Toast.makeText(getActivity(), statuses.size() + " New Tweets", Toast.LENGTH_SHORT).show();

            for (Status ss : statuses) {
                WingTweet tweet = new WingTweet(ss);
                DBHelper.insert(tweet);
            }

            mListView.setSelectionFromTop(index, top);
            super.handleMessage(msg);
        }
    };

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getActivity().getActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}
