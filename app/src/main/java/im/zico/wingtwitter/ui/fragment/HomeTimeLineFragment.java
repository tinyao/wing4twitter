package im.zico.wingtwitter.ui.fragment;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.adapter.TimeLineAdapter;
import im.zico.wingtwitter.dao.WingDataHelper;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.type.WingTweet;
import twitter4j.AsyncTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;

/**
 * Created by tinyao on 12/4/14.
 */
public class HomeTimeLineFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

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

    ActionSlideExpandableListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container,
                false);

        mListView = (ActionSlideExpandableListView) rootView.findViewById(R.id.list);

        DBHelper = new WingDataHelper(getActivity());
        mAdapter = new TimeLineAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

        return rootView;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindSwipeToRefresh((ViewGroup) view);
        asyncTwitter = WingApp.getTwitterInstance();
        asyncTwitter.addListener(listener);

//        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                View expandV = view.findViewById(R.id.expandable);
//                expand(expandV);
//                return false;
//            }
//        });

        Log.d("DEBUG", "onCreatedView");
    }

    private SwipeRefreshLayout mSwipeRefresh;
    private AsyncTwitter asyncTwitter;
    private TwitterListener listener = new TwitterAdapter(){
        @Override
        public void gotHomeTimeline(ResponseList<Status> statuses) {
            super.gotHomeTimeline(statuses);
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    @Override
    public void onRefresh() {
        asyncTwitter.getHomeTimeline();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSwipeRefresh.setRefreshing(false);
            ResponseList<Status> statuses = (ResponseList<Status>) msg.obj;
            Log.d("DEBUG", "tweets: " + statuses.size());
            for (Status ss : statuses) {
                WingTweet tweet = new WingTweet(ss);
                DBHelper.insert(tweet);
            }
            super.handleMessage(msg);
        }
    };
}
