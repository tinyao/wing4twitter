package im.zico.wingtwitter.ui.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.adapter.DrawerListAdapter;
import im.zico.wingtwitter.view.NestedListView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrawerFragment.NavigationDrawerCallbacks} interface
 * to handle interaction events.
 * Use the {@link DrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawerFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private NavigationDrawerCallbacks mCallbacks;

    private ExpandableListView expListView;
    private DrawerListAdapter listAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrawerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrawerFragment newInstance(String param1, String param2) {
        DrawerFragment fragment = new DrawerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView =  inflater.inflate(R.layout.fragment_drawer, container, false);

        expListView = (ExpandableListView) contentView.findViewById(R.id.explist_drawer);
        prepareListData();
        listAdapter = new DrawerListAdapter(getActivity(), parentMenus,
                parentMenuIcons, childMenuItems);
        expListView.setAdapter(listAdapter);

        View headerCover = getActivity().getLayoutInflater().inflate(R.layout.drawer_menu_header, null);
        expListView.addHeaderView(headerCover);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                parent.setSelectedGroup(groupPosition);
//                parent.setItemChecked(groupPosition, true);
                Toast.makeText(getActivity(), "GroupPosition: " + groupPosition, Toast.LENGTH_SHORT).show();
                CheckBox checkBox = (CheckBox) v.findViewById(R.id.check_drawer_group);
                checkBox.setChecked(!checkBox.isChecked());

                return false;
            }
        });

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expListView.setIndicatorBounds(expListView.getRight()-40, expListView.getWidth());
        } else {
            expListView.setIndicatorBoundsRelative(expListView.getRight()-40, expListView.getWidth());
        }

//        NestedListView listView = (NestedListView) contentView.findViewById(R.id.drawer_group_list);
//        String[] lists = getResources().getStringArray(R.array.user_list_sample);
//        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.group_list_item, R.id.tv_drawer_list_item, lists);
//        listView.setAdapter(adapter);

        return contentView;
    }


    private List<String> parentMenus;
    private HashMap<String, List<String>> childMenuItems;
    private TypedArray parentMenuIcons;

    private void prepareListData() {
        parentMenus = Arrays.asList(getResources().getStringArray(R.array.drawer_menu_group));
        parentMenuIcons = getResources().obtainTypedArray(R.array.draw_menu_icons);
        childMenuItems = new HashMap<String, List<String>>();

        List<String> listChild = Arrays.asList(getResources().getStringArray(R.array.user_list_sample));
        List<String> emptyChild = new ArrayList<String>();
        for(int i=0; i<parentMenus.size(); i++) {
            childMenuItems.put(parentMenus.get(i), emptyChild);
        }
        childMenuItems.put(parentMenus.get(3), listChild);
    }

//    private void selectItem(int position) {
//        mCurrentSelectedPosition = position;
//        if (mDrawerListView != null) {
//            mDrawerListView.setItemChecked(position, true);
//        }
//        if (mDrawerLayout != null) {
//            mDrawerLayout.closeDrawer(mFragmentContainerView);
//        }
//        if (mCallbacks != null) {
//            mCallbacks.onNavigationDrawerItemSelected(position);
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

}
