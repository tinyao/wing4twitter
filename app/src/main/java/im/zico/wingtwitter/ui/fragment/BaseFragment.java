package im.zico.wingtwitter.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.ui.MainActivity;

/**
 * Created by tinyao on 12/4/14.
 */
public class BaseFragment extends Fragment {

    public static final String FRAGMENT_ID = "fragmentId";
    private int fragmentId;

    public BaseFragment() {

    }

    public static void setParams(BaseFragment fragment, int id) {
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_ID, id);
        fragment.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG", "Drawer Fragment - onCreate");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("DEBUG", "Drawer Fragment - onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("DEBUG", "Drawer Fragment - Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("DEBUG", "Drawer Fragment - Pause");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("DEBUG", "Drawer Fragment - onCreatedView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
