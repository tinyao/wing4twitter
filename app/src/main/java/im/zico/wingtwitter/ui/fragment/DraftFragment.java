package im.zico.wingtwitter.ui.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.zico.wingtwitter.R;


public class DraftFragment extends BaseFragment {

    public static DraftFragment newInstance(int frameId) {
        DraftFragment fragment = new DraftFragment();
        setParams(fragment, frameId);
        return fragment;
    }

    public DraftFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_draft, container, false);
    }

}
