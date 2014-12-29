package im.zico.wingtwitter.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.ui.TweetComposeActivity;
import twitter4j.AsyncTwitter;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;

/**
 * Created by tinyao on 12/1/14.
 */
public class MentionedFragment extends BaseStatusesListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    AsyncTwitter asyncTwitter;

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static MentionedFragment newInstance(int sectionNumber) {
        MentionedFragment fragment = new MentionedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MentionedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPageId = getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        asyncTwitter = WingApp.newTwitterInstance();
        asyncTwitter.addListener(listener);
        super.onViewCreated(view, savedInstanceState);
    }

    private TwitterListener listener = new TwitterAdapter() {
        @Override
        public void gotMentions(ResponseList<Status> statuses) {
            super.gotMentions(statuses);
            onTwitterResult(statuses);
        }
    };

    @Override
    public int getType() {
        return WingStore.TYPE_MENTION;
    }

    @Override
    AsyncTwitter getAsyncTwitter() {
        return asyncTwitter;
    }

    @Override
    public void loadLatest() {
        if (isListEmpty()) {
            asyncTwitter.getMentions();
        } else {
            asyncTwitter.getMentions(
                    new Paging(1, 20, mAdapter.getItem(0).tweet_id));
        }
    }

    @Override
    public void loadNext() {
        Log.d("DEBUG", "load more");
        asyncTwitter.getMentions(new Paging(1, 20)
                .maxId(mAdapter.getItem(mAdapter.getCount() - 1).tweet_id - 1));
    }
}
