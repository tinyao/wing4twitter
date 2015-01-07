package im.zico.wingtwitter.ui.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.ui.view.LoadingFooter;
import im.zico.wingtwitter.utils.PrefKey;
import im.zico.wingtwitter.utils.PreferencesManager;
import twitter4j.AsyncTwitter;
import twitter4j.Paging;


public class FavoriteFragment extends BaseStatusesListFragment {

    public static FavoriteFragment newInstance(int frameId) {
        FavoriteFragment fragment = new FavoriteFragment();
        setParams(fragment, frameId);
        return fragment;
    }

    public FavoriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.fab_compose).setVisibility(View.GONE);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getType() {
        return WingStore.TYPE_FAVORITE;
    }

    @Override
    public void onScrollFooter() {
        super.onScrollFooter();
    }

    @Override
    public void loadLatest() {
        Log.d("DEBUG", "Load latest favs");
        if (isListEmpty()) {
            getAsyncTwitter().getFavorites();
        } else {
            getAsyncTwitter().getFavorites(new Paging(1, 20, mAdapter.getItem(0).tweet_id));
        }
    }

    @Override
    public void loadNext() {
        Log.d("DEBUG", "Load next favs");
        getAsyncTwitter().getFavorites(new Paging(1, 20)
                .maxId(mAdapter.getItem(mAdapter.getCount() - 1).tweet_id - 1));
    }

}
