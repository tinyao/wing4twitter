package im.zico.wingtwitter.ui.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.zico.wingtwitter.R;


public class TrendsFragment extends BaseFragment {

    public static TrendsFragment newInstance(int frameId) {
        TrendsFragment fragment = new TrendsFragment();
        setParams(fragment, frameId);
        return fragment;
    }

    public TrendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trends, container, false);
    }

}
