package im.zico.wingtwitter.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.zico.wingtwitter.R;

public class ListsFragment extends BaseFragment {

    public static ListsFragment newInstance(int frameId) {
        ListsFragment fragment = new ListsFragment();
        setParams(fragment, frameId);
        return fragment;
    }

    public ListsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lists, container, false);
    }
}
