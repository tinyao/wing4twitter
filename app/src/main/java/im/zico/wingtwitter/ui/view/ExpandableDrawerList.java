package im.zico.wingtwitter.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by tinyao on 12/4/14.
 */
public class ExpandableDrawerList extends ExpandableListView{

    public ExpandableDrawerList(Context context) {
        super(context);
    }

    public ExpandableDrawerList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableDrawerList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExpandableDrawerList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setItemChecked(int position, boolean value) {
        super.setItemChecked(position, value);

        // 非选中项恢复

        // 设置选中项的高亮

    }



}
