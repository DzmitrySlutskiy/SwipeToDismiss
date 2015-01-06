package by.dzmitryslutskiy.swipetodismiss;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private RVAdapter mAdapter;
    private List<String> mDataset = new ArrayList<>(Arrays.asList(Cheeses.sCheeseStrings));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new RVAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        SwipeDismissRecyclerViewTouchListener touchListener =
                new SwipeDismissRecyclerViewTouchListener(
                        recyclerView,
                        new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(final RecyclerView recyclerView, int[] reverseSortedPositions) {
                                final RVAdapter adapter = mAdapter;
                                for (int position : reverseSortedPositions) {
                                    if (position != 0) {
                                        int prevItemViewType = adapter.getItemViewType(position - 1);
                                        if (prevItemViewType == 1 && position + 1 < adapter.getItemCount()) {
                                            int nextItemViewType = adapter.getItemViewType(position + 1);
                                            if (nextItemViewType == 1) {
                                                int pos = position - 1;
                                                adapter.notifyItemRemoved(pos);
                                            }
                                        } else {
                                            if (prevItemViewType == 1) {
                                                int pos = position - 1;
                                                adapter.notifyItemRemoved(pos);
                                            }
                                        }
                                    }
                                    mDataset.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                recyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.getRecycledViewPool().clear();
                                        adapter.notifyDataSetChanged();
                                    }
                                }, (/*getItemAnimator().getRemoveDuration() + getItemAnimator().getMoveDuration()*/200) * 2);

                            }

                            @Override
                            public void onDismissRight(View mRootView, boolean dismissRight) {
                                if (dismissRight) {
                                    mRootView.findViewById(R.id.left_dismiss).setVisibility(View.INVISIBLE);
                                    mRootView.findViewById(R.id.right_dismiss).setVisibility(View.VISIBLE);
                                } else {
                                    mRootView.findViewById(R.id.left_dismiss).setVisibility(View.VISIBLE);
                                    mRootView.findViewById(R.id.right_dismiss).setVisibility(View.INVISIBLE);
                                }
                            }
                        });
        recyclerView.setOnTouchListener(touchListener);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, MotionEvent e) {
                Log.d(TAG, "onItemClick: " + view);
                View backgroundView = view.findViewById(R.id.selected_background);

                backgroundView.setPressed(true);
                backgroundView.invalidate();
                backgroundView.setPressed(false);
//                backgroundView.setVisibility(backgroundView.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void onLongClick(View view, int position, MotionEvent e) {
                Log.d(TAG, "onLongClick: " + view);
                View backgroundView = view.findViewById(R.id.selected_background);

                backgroundView.setPressed(true);
                backgroundView.invalidate();
//                backgroundView.setPressed(false);
            }
        }, recyclerView));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent activity: " + event);

        return super.onTouchEvent(event);
    }


}
