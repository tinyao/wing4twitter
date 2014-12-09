package im.zico.wingtwitter.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import im.zico.wingtwitter.R;

/**
 * Created by tinyao on 12/1/14.
 */
public class MentionedFragment extends BaseTimeLineFragment {


    private static final String ARG_SECTION_NUMBER = "section_number";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container,
                false);
        return rootView;
    }

}
