package im.zico.wingtwitter.ui.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by tinyao on 12/4/14.
 */
public class CursorStatusesListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

//    private ShotsDataHelper mDataHelper;

//    private ShotsAdapter mAdapter;

    private ListView mListView;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}