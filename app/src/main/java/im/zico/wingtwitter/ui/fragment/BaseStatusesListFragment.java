package im.zico.wingtwitter.ui.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;

import im.zico.wingtwitter.ui.MainActivity;
import im.zico.wingtwitter.ui.view.TweetListView;

/**
 * Created by tinyao on 12/4/14.
 */
public class BaseStatusesListFragment extends BaseFragment {

    public static final String ACTION_ACTIONBAR_CLICKED = "action_actionbar_clicked";
    TweetListView mListView;
    int mPageId = 0;

    private ActionBarTapReceiver receiver;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void scrollTop() {
        mListView.smoothScrollToPositionFromTop(0, 0);
    }

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
