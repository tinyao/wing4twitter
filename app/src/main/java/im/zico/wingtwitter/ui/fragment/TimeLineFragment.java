package im.zico.wingtwitter.ui.fragment;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Loader;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import im.zico.wingtwitter.R;

/**
 * Created by tinyao on 12/4/14.
 */
public class TimeLineFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static TimeLineFragment newInstance(int sectionNumber) {
        TimeLineFragment fragment = new TimeLineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TimeLineFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container,
                false);
        TextView textView = (TextView) rootView
                .findViewById(R.id.hello_txt);
        textView.setText("HOME");
        return rootView;
    }

}
