package im.zico.wingtwitter.ui.fragment;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.provider.ContactsContract;

/**
 * Created by tinyao on 12/4/14.
 */
abstract class BaseStatusesListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<ContactsContract.Data> {


}
