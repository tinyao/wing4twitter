package com.tjerkw.slideexpandable.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.utils.PrefKey;
import im.zico.wingtwitter.utils.PreferencesManager;

/**
 * A more specific expandable listview in which the expandable area
 * consist of some buttons which are context actions for the item itself.
 *
 * It handles event binding for those buttons and allow for adding
 * a listener that will be invoked if one of those buttons are pressed.
 *
 * @author tjerk
 * @date 6/26/12 7:01 PM
 */
public class ActionSlideExpandableListView extends SlideExpandableListView {

    public static final int[] TWEET_ACTIONS = {
            R.id.expand_action_delete, R.id.expand_action_reply,
            R.id.expand_action_retweet, R.id.expand_action_favorite, R.id.expand_action_share,
            R.id.expand_action_filter, R.id.expand_action_copy};

    private OnActionClickListener listener;

	public ActionSlideExpandableListView(Context context) {
		super(context);
	}

	public ActionSlideExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ActionSlideExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setItemActionListener(OnActionClickListener listener) {
		this.listener = listener;
	}

	/**
	 * Interface for callback to be invoked whenever an action is clicked in
	 * the expandle area of the list item.
	 */
	public interface OnActionClickListener {
		/**
		 * Called when an action item is clicked.
		 *
		 * @param itemView the view of the list item
		 * @param clickedView the view clicked
		 * @param position the position in the listview
		 */
		public void onClick(View itemView, View clickedView, int position);
	}

	public void setAdapter(final ListAdapter adapter) {
		super.setAdapter(new WrapperListAdapterImpl(adapter) {
			@Override
			public View getView(final int position, View view, ViewGroup viewGroup) {
				final View listView = wrapped.getView(position, view, viewGroup);
                WingTweet tweet = (WingTweet)adapter.getItem(position);

                boolean isMyTweet = (tweet.user_id ==
                        Long.valueOf(PreferencesManager.getInstance(WingApp.getContext()).getLongValue(PrefKey.KEY_USERID)));

				// add the action listeners
				if(TWEET_ACTIONS != null && listView!=null) {
					for(int id : TWEET_ACTIONS) {
						View buttonView = listView.findViewById(id);

                        if(isMyTweet && id == R.id.expand_action_delete) {
                            buttonView.setVisibility(VISIBLE);
                        }

						if(buttonView != null) {
							View btnV = buttonView.findViewById(id);
                            btnV.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View view) {
									if(listener!=null) {
										listener.onClick(listView, view, position);
									}
								}
							});
						}
					}
				}
				return listView;
			}
		});
	}
}
