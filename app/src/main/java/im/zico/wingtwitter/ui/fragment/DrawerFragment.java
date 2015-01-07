package im.zico.wingtwitter.ui.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.adapter.DrawerListAdapter;


public class DrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private NavigationDrawerCallbacks mCallbacks;
    private int mCurrentSelectedPosition = 0;
    private int mCurrentSelectedChildPosition = 0;

    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private ImageView avatarView;
    private TextView userName;
    private boolean mFromSavedInstanceState;
    private ExpandableListView expListView;
    private DrawerListAdapter listAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    public DrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_drawer, container, false);

        expListView = (ExpandableListView) contentView.findViewById(R.id.explist_drawer);
        prepareListData();
        listAdapter = new DrawerListAdapter(getActivity(), parentMenus,
                parentMenuIcons, childMenuItems);
        expListView.setAdapter(listAdapter);

        View headerCover = getActivity().getLayoutInflater().inflate(R.layout.drawer_menu_header, null);
        avatarView = (ImageView) headerCover.findViewById(R.id.drawer_user_avatar);
        userName = (TextView) headerCover.findViewById(R.id.drawer_user_name);
        expListView.addHeaderView(headerCover);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (groupPosition == 2 || groupPosition == 3) {
                    expListView.setItemChecked(groupPosition + 1, true);
                    expListView.setItemChecked(groupPosition + 1, false);
                } else {
                    selectItem(groupPosition, -1);
                }
                return false;
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 2 || groupPosition == 3) {
                    selectItem(groupPosition, childPosition);
                }
                return false;
            }
        });

        selectItem(mCurrentSelectedPosition, -1);
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
        for (int i = 0; i < parentMenus.size(); i++) {
            childMenuItems.put(parentMenus.get(i), emptyChild);
        }
        childMenuItems.put(parentMenus.get(2), listChild);
    }

    /**
     * Select the drawer item
     *
     * @param groupPos
     * @param childPos
     */
    private void selectItem(int groupPos, int childPos) {
        Toast.makeText(getActivity(), "child: " + groupPos + "-" + childPos, Toast.LENGTH_SHORT).show();
        mCurrentSelectedPosition = groupPos;
        mCurrentSelectedChildPosition = childPos;
        if (expListView != null) {
            if (childPos == -1) {
                expListView.setItemChecked(groupPos + 1, true);
            }
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(groupPos, childPos);
        }
    }

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
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void setUp(int drawer_resId, DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
        drawerLayout.setFitsSystemWindows(true);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(android.R.color.transparent));
        mFragmentContainerView = getActivity().findViewById(drawer_resId);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout,
                R.string.open,
                R.string.close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        drawerLayout.setDrawerListener(mDrawerToggle);
        enlargeDrawerDragger(drawerLayout);
    }

    /**
     * Enlarge the trigger edge for the sliding drawer
     *
     * @param mDrawerLayout
     */
    private void enlargeDrawerDragger(DrawerLayout mDrawerLayout) {
        Field mDragger = null;
        try {
            mDragger = ((Object) mDrawerLayout).getClass().getDeclaredField(
                    "mLeftDragger");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        mDragger.setAccessible(true);

        ViewDragHelper draggerObj = null;
        try {
            draggerObj = (ViewDragHelper) mDragger
                    .get(mDrawerLayout);
            Field mEdgeSize = draggerObj.getClass().getDeclaredField(
                    "mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(draggerObj);

            mEdgeSize.setInt(draggerObj, edge * 3);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public ActionBarDrawerToggle getToggle() {
        return mDrawerToggle;
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int groupPos, int childPos);
    }

}
