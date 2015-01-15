package im.zico.wingtwitter.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
import java.util.ArrayList;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.adapter.UserTimeLineAdapter;
import im.zico.wingtwitter.dao.WingDataHelper;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.ui.TweetComposeActivity;
import im.zico.wingtwitter.ui.view.LoadingFooter;
import im.zico.wingtwitter.ui.view.TweetListView;
import twitter4j.AsyncTwitter;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;

/**
 * Created by tinyao on 1/9/15.
 */
public class UserTimeLineFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, TweetListView.ScrollDetectCallback{

    private TweetListView mListView;
    private LoadingFooter mLoadingFooter;
    private AsyncTwitter mAsyncTwitter;
    public SwipeRefreshLayout mSwipeRefresh;
    public UserTimeLineAdapter mAdapter;
    public ArrayList<WingTweet> userTweets;
    public String mScreenName;

    public UserTimeLineFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScreenName = getActivity().getIntent().getStringExtra(WingStore.UserColumns.SCREEN_NAME);
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
        mAsyncTwitter = WingApp.newTwitterInstance();
        mAsyncTwitter.addListener(listener);
        bindSwipeToRefresh((ViewGroup) view);
        userTweets = new ArrayList<>();
        mAdapter = new UserTimeLineAdapter(mListView, userTweets);
        mListView.setScrollCallback(this);
        mListView.setAdapter(mAdapter);

        loadLatest();

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
                        if (!favTweet.favorited) {
                            mAsyncTwitter.createFavorite(tweet.tweet_id);
                        } else {
                            mAsyncTwitter.destroyFavorite(tweet.tweet_id);
                        }
                        break;
                    case R.id.expand_action_retweet:
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Retweet or Quote ?")
                                .setNeutralButton(R.string.cancel, null)
                                .setPositiveButton(R.string.retweet, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mAsyncTwitter.retweetStatus(tweet.tweet_id);
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
                                        mAsyncTwitter.destroyStatus(tweet.tweet_id);
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
    }

    /**
     * Listener for TwitterMethod Callback
     */
    private TwitterListener listener = new TwitterAdapter() {

        @Override
        public void gotUserTimeline(ResponseList<Status> statuses) {
            super.gotUserTimeline(statuses);
            Log.d("DEBUG", "user timeline: " + statuses);
            ArrayList<WingTweet> wingTweets = new ArrayList<>();
            for (Status ss : statuses) {
                WingTweet tweet = new WingTweet(ss);
                wingTweets.add(tweet);
            }
            Message msg = new Message();
            msg.obj = wingTweets;
            mHandler.sendMessage(msg);
        }

    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArrayList<WingTweet> wingTweets = (ArrayList<WingTweet>) msg.obj;
            new WingDataHelper(getActivity()).saveAll(wingTweets, WingStore.TYPE_COMMON_TWEET);
            if(mLoadingFooter.getState() == LoadingFooter.State.Loading) {
                userTweets.addAll(wingTweets);
            } else {
                userTweets.addAll(0, wingTweets);
                mSwipeRefresh.setRefreshing(false);
            }
            if (wingTweets == null || wingTweets.size() < 10) {
                Log.d("DEBUG", "the end");
                mLoadingFooter.setState(LoadingFooter.State.TheEnd);
            } else {
                mLoadingFooter.setState(LoadingFooter.State.Idle, 3000);
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    /**
     * Bind a SwipeRefreshLayout to the contentView
     * @param v
     */
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

    @Override
    public void onRefresh() {
        loadLatest();
    }

    private void loadLatest() {
        mAsyncTwitter.getUserTimeline(mScreenName);
    }

    @Override
    public void onScrollDown() {

    }

    @Override
    public void onScrollUp() {

    }

    @Override
    public void onScrollFooter() {
        if (mAdapter.getCount() == 0){
            return;
        }
        if (mLoadingFooter.getState() != LoadingFooter.State.Loading
                && mLoadingFooter.getState() != LoadingFooter.State.TheEnd) {
            mLoadingFooter.setState(LoadingFooter.State.Loading);
            loadNext();
        }
    }

    private void loadNext() {
        WingTweet tweet = mAdapter.getItem(mAdapter.getCount()-1);
        Log.d("DEBUG", "tweet content: " + tweet.content);
        mAsyncTwitter.getUserTimeline(mScreenName,
                new Paging(1, 20).maxId(mAdapter.getItem(mAdapter.getCount() - 1).tweet_id - 1));
    }
}
