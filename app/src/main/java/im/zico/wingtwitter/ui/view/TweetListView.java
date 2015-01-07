package im.zico.wingtwitter.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;


/**
 * Created by tinyao on 12/18/14.
 */
public class TweetListView extends ActionSlideExpandableListView {

    private static final String TAG = "TweetListView";

    public TweetListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TweetListView(Context context) {
        super(context);
        init();
    }

    public TweetListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.setOnScrollListener(scrollListener);
    }

    private int mLastFirstVisibleItem;
    private int mLastChildY;
    private ScrollDetectCallback mCallback;

    public void setScrollCallback(ScrollDetectCallback callback) {
        mCallback = callback;
    }

    private OnScrollListener scrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mLastFirstVisibleItem == firstVisibleItem && view.getChildAt(0) != null) {
                int scrollY = mLastChildY - view.getChildAt(0).getTop();
                if (scrollY > 20) {
                    mCallback.onScrollDown();
                    Log.d(TAG, "Scrolling down...");
                } else if (scrollY < -20) {
                    mCallback.onScrollUp();
                    Log.d(TAG, "Scrolling up...");
                }
            }

            if (view.getChildAt(0) != null) {
                mLastChildY = view.getChildAt(0).getTop();
            }
            mLastFirstVisibleItem = firstVisibleItem;

            if (firstVisibleItem + visibleItemCount >= totalItemCount
                    && totalItemCount != 0
                    && totalItemCount != TweetListView.this.getHeaderViewsCount() + TweetListView.this.getFooterViewsCount()
                    && getAdapter().getCount() > 0) {
                Log.d(TAG, "Scrolling footer...");
                mCallback.onScrollFooter();
            }
        }
    };


    public interface ScrollDetectCallback {
        public void onScrollDown();

        public void onScrollUp();

        public void onScrollFooter();
    }

//    private void init() {
//        mChildrenHeights = new SparseIntArray();
//        super.setOnScrollListener(mScrollListener);
//    }

//    private OnScrollListener mOriginalScrollListener;
//
//    private OnScrollListener mScrollListener = new OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            if (mOriginalScrollListener != null) {
//                mOriginalScrollListener.onScrollStateChanged(view, scrollState);
//            }
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            if (mOriginalScrollListener != null) {
//                mOriginalScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
//            }
//
//            onScrollChanged();
//        }
//    };

//    @Override
//    public void setScrollViewCallbacks(ObservableScrollViewCallbacks listener) {
//        mCallbacks = listener;
//    }


//    @Override
//    public void scrollVerticallyTo(int y) {
//        View firstVisibleChild = getChildAt(0);
//        if (firstVisibleChild != null) {
//            int baseHeight = firstVisibleChild.getHeight();
//            int position = y / baseHeight;
//            setSelection(position);
//        }
//    }
//
//    @Override
//    public int getCurrentScrollY() {
//        return mScrollY;
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (mCallbacks != null) {
//            switch (ev.getActionMasked()) {
//                case MotionEvent.ACTION_DOWN:
//                    LogUtils.v(TAG, "onTouchEvent: ACTION_DOWN");
//                    mFirstScroll = mDragging = true;
//                    mCallbacks.onDownMotionEvent();
//                    break;
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    LogUtils.v(TAG, "onTouchEvent: ACTION_UP|ACTION_CANCEL");
//                    mDragging = false;
//                    mCallbacks.onUpOrCancelMotionEvent(mScrollState);
//                    break;
//            }
//        }
//        return super.onTouchEvent(ev);
//    }

//    private void onScrollChanged() {
//
//        if (mCallbacks != null) {
//            if (getChildCount() > 0) {
//                int firstVisiblePosition = getFirstVisiblePosition();
//                for (int i = getFirstVisiblePosition(), j = 0; i <= getLastVisiblePosition(); i++, j++) {
//                    if (mChildrenHeights.indexOfKey(i) < 0 || getChildAt(j).getHeight() != mChildrenHeights.get(i)) {
//                        mChildrenHeights.put(i, getChildAt(j).getHeight());
//                    }
//                }
//
//                View firstVisibleChild = getChildAt(0);
//                if (firstVisibleChild != null) {
//                    if (mPrevFirstVisiblePosition < firstVisiblePosition) {
//                        // scroll down
//                        int skippedChildrenHeight = 0;
//                        if (firstVisiblePosition - mPrevFirstVisiblePosition != 1) {
//                            LogUtils.v(TAG, "Skipped some children while scrolling down: " + (firstVisiblePosition - mPrevFirstVisiblePosition));
//                            for (int i = firstVisiblePosition - 1; i > mPrevFirstVisiblePosition; i--) {
//                                if (0 < mChildrenHeights.indexOfKey(i)) {
//                                    skippedChildrenHeight += mChildrenHeights.get(i);
//                                    LogUtils.v(TAG, "Calculate skipped child height at " + i + ": " + mChildrenHeights.get(i));
//                                } else {
//                                    LogUtils.v(TAG, "Could not calculate skipped child height at " + i);
//                                    // Approximate each item's height to the first visible child.
//                                    // It may be incorrect, but without this, scrollY will be broken
//                                    // when scrolling from the bottom.
//                                    skippedChildrenHeight += firstVisibleChild.getHeight();
//                                }
//                            }
//                        }
//                        mPrevScrolledChildrenHeight += mPrevFirstVisibleChildHeight + skippedChildrenHeight;
//                        mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
//                    } else if (firstVisiblePosition < mPrevFirstVisiblePosition) {
//                        // scroll up
//                        int skippedChildrenHeight = 0;
//                        if (mPrevFirstVisiblePosition - firstVisiblePosition != 1) {
//                            LogUtils.v(TAG, "Skipped some children while scrolling up: " + (mPrevFirstVisiblePosition - firstVisiblePosition));
//                            for (int i = mPrevFirstVisiblePosition - 1; i > firstVisiblePosition; i--) {
//                                if (0 < mChildrenHeights.indexOfKey(i)) {
//                                    skippedChildrenHeight += mChildrenHeights.get(i);
//                                    LogUtils.v(TAG, "Calculate skipped child height at " + i + ": " + mChildrenHeights.get(i));
//                                } else {
//                                    LogUtils.v(TAG, "Could not calculate skipped child height at " + i);
//                                    // Approximate each item's height to the first visible child.
//                                    // It may be incorrect, but without this, scrollY will be broken
//                                    // when scrolling from the bottom.
//                                    skippedChildrenHeight += firstVisibleChild.getHeight();
//                                }
//                            }
//                        }
//                        mPrevScrolledChildrenHeight -= firstVisibleChild.getHeight() + skippedChildrenHeight;
//                        mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
//                    } else if (firstVisiblePosition == 0) {
//                        mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
//                    }
//                    if (mPrevFirstVisibleChildHeight < 0) {
//                        mPrevFirstVisibleChildHeight = 0;
//                    }
//                    mScrollY = mPrevScrolledChildrenHeight - firstVisibleChild.getTop();
//                    mPrevFirstVisiblePosition = firstVisiblePosition;
//
//                    LogUtils.v(TAG, "first: " + firstVisiblePosition + " scrollY: " + mScrollY + " first height: " + firstVisibleChild.getHeight() + " first top: " + firstVisibleChild.getTop());
//                    mCallbacks.onScrollChanged(mScrollY, mFirstScroll, mDragging);
//                    if (mFirstScroll) {
//                        mFirstScroll = false;
//                    }
//
//                    if (mPrevScrollY < mScrollY) {
//                        //down
//                        mScrollState = ScrollState.UP;
//                    } else if (mScrollY < mPrevScrollY) {
//                        //up
//                        mScrollState = ScrollState.DOWN;
//                    } else {
//                        mScrollState = ScrollState.STOP;
//                    }
//                    mPrevScrollY = mScrollY;
//
//                } else {
//                    LogUtils.v(TAG, "first: " + firstVisiblePosition);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onRestoreInstanceState(Parcelable state) {
//        SavedState ss = (SavedState) state;
//        mPrevFirstVisiblePosition = ss.prevFirstVisiblePosition;
//        mPrevFirstVisibleChildHeight = ss.prevFirstVisibleChildHeight;
//        mPrevScrolledChildrenHeight = ss.prevScrolledChildrenHeight;
//        mPrevScrollY = ss.prevScrollY;
//        mScrollY = ss.scrollY;
//        mChildrenHeights = ss.childrenHeights;
//        super.onRestoreInstanceState(ss.getSuperState());
//    }
//
//    @Override
//    public Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState ss = new SavedState(superState);
//        ss.prevFirstVisiblePosition = mPrevFirstVisiblePosition;
//        ss.prevFirstVisibleChildHeight = mPrevFirstVisibleChildHeight;
//        ss.prevScrolledChildrenHeight = mPrevScrolledChildrenHeight;
//        ss.prevScrollY = mPrevScrollY;
//        ss.scrollY = mScrollY;
//        ss.childrenHeights = mChildrenHeights;
//        return ss;
//    }
//
//    static class SavedState extends BaseSavedState {
//        int prevFirstVisiblePosition;
//        int prevFirstVisibleChildHeight = -1;
//        int prevScrolledChildrenHeight;
//        int prevScrollY;
//        int scrollY;
//        SparseIntArray childrenHeights;
//
//        SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        private SavedState(Parcel in) {
//            super(in);
//            prevFirstVisiblePosition = in.readInt();
//            prevFirstVisibleChildHeight = in.readInt();
//            prevScrolledChildrenHeight = in.readInt();
//            prevScrollY = in.readInt();
//            scrollY = in.readInt();
//            childrenHeights = new SparseIntArray();
//            final int numOfChildren = in.readInt();
//            if (0 < numOfChildren) {
//                for (int i = 0; i < numOfChildren; i++) {
//                    final int key = in.readInt();
//                    final int value = in.readInt();
//                    childrenHeights.put(key, value);
//                }
//            }
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags) {
//            super.writeToParcel(out, flags);
//            out.writeInt(prevFirstVisiblePosition);
//            out.writeInt(prevFirstVisibleChildHeight);
//            out.writeInt(prevScrolledChildrenHeight);
//            out.writeInt(prevScrollY);
//            out.writeInt(scrollY);
//            final int numOfChildren = childrenHeights == null ? 0 : childrenHeights.size();
//            out.writeInt(numOfChildren);
//            if (0 < numOfChildren) {
//                for (int i = 0; i < numOfChildren; i++) {
//                    out.writeInt(childrenHeights.keyAt(i));
//                    out.writeInt(childrenHeights.valueAt(i));
//                }
//            }
//        }
//
//        public static final Parcelable.Creator<SavedState> CREATOR
//                = new Parcelable.Creator<SavedState>() {
//            @Override
//            public SavedState createFromParcel(Parcel in) {
//                return new SavedState(in);
//            }
//
//            @Override
//            public SavedState[] newArray(int size) {
//                return new SavedState[size];
//            }
//        };
//    }

}
