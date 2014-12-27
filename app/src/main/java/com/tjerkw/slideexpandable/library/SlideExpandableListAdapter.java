package com.tjerkw.slideexpandable.library;

import android.view.View;
import android.widget.ListAdapter;

import im.zico.wingtwitter.R;

/**
 * ListAdapter that adds sliding functionality to a list.
 * Uses R.id.expandalbe_toggle_button and R.id.expandable id's if no
 * ids are given in the contructor.
 *
 * @author tjerk
 * @date 6/13/12 8:04 AM
 */
public class SlideExpandableListAdapter extends AbstractSlideExpandableListAdapter {
//	private int toggle_button_id;
    private int click_toggle_id;
    private int long_click_toggle_id;
	private int expandable_view_id;

	public SlideExpandableListAdapter(ListAdapter wrapped, int click_toggle_id,
                                      int long_click_toggle_id, int expandable_view_id) {
		super(wrapped);
		this.click_toggle_id = click_toggle_id;
        this.long_click_toggle_id = long_click_toggle_id;
		this.expandable_view_id = expandable_view_id;
	}

	public SlideExpandableListAdapter(ListAdapter wrapped) {
		this(wrapped, R.id.main_card_content, R.id.tweet_card_more,  R.id.expandable);
	}

	@Override
	public View getExpandToggleButton(View parent) {
		return parent.findViewById(click_toggle_id);
	}

    @Override
    public View getExpandLongToggleButton(View parent) {
        return parent.findViewById(long_click_toggle_id);
    }

	@Override
	public View getExpandableView(View parent) {
		return parent.findViewById(expandable_view_id);
	}
}
