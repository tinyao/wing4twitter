package im.zico.wingtwitter.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;

import java.lang.reflect.Method;
import java.util.ArrayList;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.adapter.TimeLineAdapter;
import im.zico.wingtwitter.dao.WingDataHelper;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.ui.MainActivity;
import im.zico.wingtwitter.ui.TweetComposeActivity;
import im.zico.wingtwitter.ui.TweetDetailActivity;
import im.zico.wingtwitter.ui.view.LoadingFooter;
import im.zico.wingtwitter.ui.view.TweetListView;
import im.zico.wingtwitter.utils.TweetUtils;
import twitter4j.AsyncTwitter;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;

/**
 * Created by tinyao on 12/4/14.
 */
public abstract class BaseStatusesListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    public static final String ACTION_ACTIONBAR_CLICKED = "action_actionbar_clicked";
    private static final int TASK_TIMELINE = 1;
    private static final int TASK_FAVORITE = 2;
    private static final int TASK_UNFAVORITE = 3;
    private static final int TASK_EXCPTION = -1;
    public SwipeRefreshLayout mSwipeRefresh;
    TweetListView mListView;
    TimeLineAdapter mAdapter;
    int mPageId = 0;

    private LoadingFooter mLoadingFooter;
    private WingDataHelper DBHelper;

    private ActionBarTapReceiver receiver;
    private int mLastFirstVisibleItem;
    private int mLastChildY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("DEBUG", "register");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        mListView = (TweetListView) rootView.findViewById(R.id.list);
        mLoadingFooter = new LoadingFooter(getActivity());
        mListView.addFooterView(mLoadingFooter.getView());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAsyncTwitter().addListener(listener);
        bindSwipeToRefresh((ViewGroup) view);

        DBHelper = new WingDataHelper(getActivity());
        mAdapter = new TimeLineAdapter(getActivity(), mListView);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);

        mListView.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {
            @Override
            public void onClick(View itemView, View clickedView, final int position) {
                final WingTweet tweet = mAdapter.getItem(position);
                switch (clickedView.getId()) {
                    case R.id.expand_action_reply:
                        Bundle bundle = new Bundle();
                        bundle.putString("user", "@" + tweet.screen_name);
                        bundle.putLong("inReplyId", tweet.tweet_id);
                        TweetComposeActivity.showDialog(getActivity(), bundle);
                        break;
                    case R.id.expand_action_share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "@" + tweet.screen_name + ": " + tweet.content);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                    case R.id.expand_action_favorite:
                        WingTweet favTweet = mAdapter.getItem(position);
//                        favTweet.favorited = true;
//                        getDBHelper().update(favTweet);
                        if (!favTweet.favorited) {
                            getAsyncTwitter().createFavorite(tweet.tweet_id);
                        } else {
                            getAsyncTwitter().destroyFavorite(tweet.tweet_id);
                        }
                        break;
                    case R.id.expand_action_retweet:
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Retweet or Quote ?")
                                .setNeutralButton(R.string.cancel, null)
                                .setPositiveButton(R.string.retweet, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getAsyncTwitter().retweetStatus(tweet.tweet_id);
                                    }
                                })
                                .setNegativeButton(R.string.quote, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Bundle bnd = new Bundle();
                                        bnd.putString("quote", "RT " + tweet.content);
                                        TweetComposeActivity.showDialog(getActivity(), bnd);
                                    }
                                }).create().show();
                        break;
                    case R.id.expand_action_copy:
                        ClipboardManager clipboard = (ClipboardManager)
                                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("wing", tweet.content);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), "Copyed to clipboard !", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.expand_action_delete:
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Delete")
                                .setMessage("Do you want to delete this Tweet?")
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getDBHelper().delete(tweet);
                                        getAsyncTwitter().destroyStatus(tweet.tweet_id);
                                    }
                                })
                                .setNegativeButton(R.string.no, null)
                                .create().show();
                        break;
                    case R.id.expand_action_filter:
                        // ToDo: Filter user or source
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Add to filter")
                                .setMessage("User: " + tweet.user_name + "\nSoure: " + tweet.source)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setNegativeButton(R.string.no, null)
                                .create().show();
                        break;
                }

                mListView.collapse();
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                          @Override
                                          public void onScrollStateChanged(AbsListView view, int scrollState) {

//                                              if (view.getId() == view.getId()) {
//                                                  final int currentFirstVisibleItem = view.getFirstVisiblePosition();
//                                                  if (currentFirstVisibleItem > mLastFirstVisibleItem) {
//                                                      onScrollDown();
//                                                      Log.d("DEBUG", "scrolling down...");
//                                                  } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
//                                                      onScrollUp();
//                                                      Log.d("DEBUG", "scrolling up...");
//                                                  }
//                                                  mLastFirstVisibleItem = currentFirstVisibleItem;
//                                              }


                                          }

                                          @Override
                                          public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                                               int totalItemCount) {

                                              if (mLastFirstVisibleItem==firstVisibleItem && view.getChildAt(0) != null) {
                                                  int scrollY = mLastChildY - view.getChildAt(0).getTop();
                                                  if (scrollY > 20 ) {
                                                      onScrollDown();
                                                      Log.d("DEBUG", "scrolling down...");
                                                  } else if (scrollY < -20 ) {
                                                      onScrollUp();
                                                      Log.d("DEBUG", "scrolling up...");
                                                  }
                                              }
//
                                              if (view.getChildAt(0) != null) {
                                                  mLastChildY = view.getChildAt(0).getTop();
                                              }
                                              mLastFirstVisibleItem = firstVisibleItem;

                                              if (mLoadingFooter.getState() == LoadingFooter.State.Loading
                                                      || mLoadingFooter.getState() == LoadingFooter.State.TheEnd) {
                                                  return;
                                              }
                                              if (firstVisibleItem + visibleItemCount >= totalItemCount
                                                      && totalItemCount != 0
                                                      && totalItemCount != mListView.getHeaderViewsCount() + mListView.getFooterViewsCount()
                                                      && mAdapter.getCount() > 0) {
                                                  if (mLoadingFooter.getState() != LoadingFooter.State.TheEnd) {
                                                      mLoadingFooter.setState(LoadingFooter.State.Loading);
                                                      loadNext();
                                                  }
                                              }

                                          }
                                      }

        );
    }


    protected void bindSwipeToRefresh(ViewGroup v) {
        mSwipeRefresh = new SwipeRefreshLayout(getActivity());

        // Move child to SwipeRefreshLayout, and add SwipeRefreshLayout to root view
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

    public void scrollTop() {
        mListView.smoothScrollToPositionFromTop(0, 0);
    }


    protected synchronized void onScrollUp() {

    }

    protected synchronized void onScrollDown() {

    }

    public WingDataHelper getDBHelper() {
        return DBHelper;
    }

    public abstract int getType();

    abstract AsyncTwitter getAsyncTwitter();

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("DEBUG", "createLoader");
        return DBHelper.getCursorLoader(getType());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("DEBUG", "count: " + data.getCount());
        mAdapter.changeCursor(data);
        if (data != null && data.getCount() == 0) {
            loadLatest();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    @Override
    public void onRefresh() {
        loadLatest();
    }

    public abstract void loadLatest();

    public abstract void loadNext();

    public boolean isListEmpty() {
        return mAdapter == null || mAdapter.getCount() == 0;
    }

    private TwitterListener listener = new TwitterAdapter() {
        @Override
        public void gotHomeTimeline(ResponseList<Status> statuses) {
            super.gotHomeTimeline(statuses);
            onTwitterResult(statuses);
        }

        @Override
        public void gotMentions(ResponseList<Status> statuses) {
            super.gotMentions(statuses);
        }

        @Override
        public void createdFavorite(Status status) {
            onTwitterFaved(status);
        }

        @Override
        public void destroyedFavorite(Status status) {
            super.destroyedFavorite(status);
            onTwitterUnfaved(status);
        }

        @Override
        public void onException(TwitterException te, TwitterMethod method) {
            super.onException(te, method);
            onTwitterError(te, method);
        }

    };

    public void onTwitterResult(ResponseList<Status> statuses) {
        Message msg = mHandler.obtainMessage();
        msg.obj = statuses;
        msg.what = TASK_TIMELINE;
        mHandler.sendMessage(msg);
    }

    public void onTwitterError(TwitterException te, TwitterMethod method) {
        String error2Show = TweetUtils.parseException(te, method);
        Message msg = new Message();
        msg.obj = error2Show;
        msg.arg1 = method.ordinal();
        msg.what = TASK_EXCPTION;
        mHandler.sendMessage(msg);
    }

    public void onTwitterFaved(Status status) {
        Message msg = new Message();
        msg.what = TASK_FAVORITE;
        msg.obj = status.getId();
        mHandler.sendMessage(msg);
    }

    public void onTwitterUnfaved(Status status) {
        Message msg = new Message();
        msg.what = TASK_UNFAVORITE;
        msg.obj = status.getId();
        mHandler.sendMessage(msg);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case TASK_EXCPTION:
                    if (msg.arg1 == TwitterMethod.HOME_TIMELINE.ordinal()) {
                        mSwipeRefresh.setRefreshing(false);
                        Toast.makeText(getActivity(), msg.obj + "", Toast.LENGTH_SHORT).show();
                    } else if (msg.arg1 == TwitterMethod.CREATE_FAVORITE.ordinal()) {
                        Toast.makeText(getActivity(), "Fail to favorite", Toast.LENGTH_SHORT).show();
                    } else if (msg.arg1 == TwitterMethod.DESTROY_FAVORITE.ordinal()) {
                        Toast.makeText(getActivity(), "Fail to delete favorite", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case TASK_FAVORITE:
                    WingTweet favTweet = getDBHelper().getTweet((long) msg.obj);
                    favTweet.favorited = true;
                    switch (getType()) {
                        case WingStore.TYPE_TWEET:
                            getDBHelper().update(favTweet);
                            break;
                        case WingStore.TYPE_MENTION:
                            getDBHelper().updateMention(favTweet);
                            break;
                    }
                    break;
                case TASK_UNFAVORITE:
                    WingTweet unfavTweet = getDBHelper().getTweet((long) msg.obj);
                    unfavTweet.favorited = false;
                    switch (getType()) {
                        case WingStore.TYPE_TWEET:
                            getDBHelper().update(unfavTweet);
                            break;
                        case WingStore.TYPE_MENTION:
                            getDBHelper().updateMention(unfavTweet);
                            break;
                    }
                    break;
                case TASK_TIMELINE:
                    ResponseList<Status> statuses = (ResponseList<Status>) msg.obj;

                    if (!mSwipeRefresh.isRefreshing()) {
                        // load old
                        if (statuses == null || statuses.size() < 10) {
                            Log.d("DEBUG", "the end");
                            mLoadingFooter.setState(LoadingFooter.State.TheEnd);
                        } else {
                            mLoadingFooter.setState(LoadingFooter.State.Idle, 3000);
                        }
                    } else {
                        if (statuses.size() > 0) {
                            Toast.makeText(getActivity(), statuses.size() + " New Tweets",
                                    Toast.LENGTH_SHORT).show();
                        }
                        mSwipeRefresh.setRefreshing(false);
                        // about 20 new tweets, delete all previous ones
                        if (statuses.size() >= 17) {
                            DBHelper.deleteAllTweets();
                        }
                    }

                    ArrayList<WingTweet> wingTweets = new ArrayList<>();
                    for (Status ss : statuses) {
                        WingTweet tweet = new WingTweet(ss);
                        wingTweets.add(tweet);
                    }
                    switch (getType()) {
                        case WingStore.TYPE_TWEET:
                            getDBHelper().saveAll(wingTweets);
                            break;
                        case WingStore.TYPE_MENTION:
                            getDBHelper().saveAllMention(wingTweets);
                            break;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };


    class ActionBarTapReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("DEBUG", "mPageId=" + mPageId);
            if (intent.getAction().equals(ACTION_ACTIONBAR_CLICKED)) {
                int updatePage = intent.getIntExtra("pager_id", -1);
                Log.d("DEBUG", "mPageId=" + mPageId + ", updatePage=" + updatePage);
                if (mPageId == updatePage)
                    mListView.smoothScrollToPositionFromTop(0, 0);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.registerReceiver(receiver, new IntentFilter(ACTION_ACTIONBAR_CLICKED));
    }

    @Override
    public void onDetach() {
//        getActivity().unregisterReceiver(receiver);
        super.onDetach();
    }
}
