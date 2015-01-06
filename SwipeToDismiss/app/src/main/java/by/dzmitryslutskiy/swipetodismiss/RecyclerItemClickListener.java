package by.dzmitryslutskiy.swipetodismiss;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Dzmitry_Slutski on 05.01.2015.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    public static final String TAG = RecyclerItemClickListener.class.getSimpleName();
    private OnItemClickListener mListener;
    private RecyclerView mRecyclerView;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position, MotionEvent e);

        public void onLongClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener,
                                     RecyclerView recyclerView) {
        mListener = listener;
        mRecyclerView = recyclerView;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp");
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                Log.d(TAG, "onLongPress: " + e);
                View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                mListener.onLongClick(childView, mRecyclerView.getChildPosition(childView));
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        Log.d(TAG, "onInterceptTouchEvent: " + childView);
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            Log.d(TAG, "onClick: " + childView);
            mListener.onItemClick(childView, view.getChildPosition(childView), e);
            return true;
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        Log.d(TAG, "onTouchEvent");
    }
}