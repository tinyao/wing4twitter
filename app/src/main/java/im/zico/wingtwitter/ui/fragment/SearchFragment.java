package im.zico.wingtwitter.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import im.zico.wingtwitter.R;


public class SearchFragment extends BaseFragment {

    public static SearchFragment newInstance(int frameId) {
        SearchFragment fragment = new SearchFragment();
        setParams(fragment, frameId);
        return fragment;
    }

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

}
