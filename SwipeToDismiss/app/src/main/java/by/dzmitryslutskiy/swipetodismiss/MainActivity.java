package by.dzmitryslutskiy.swipetodismiss;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mDataset = new ArrayList<String>(Arrays.asList("aaaaaaaaaaaaaa", "bbbbbbbbbbbbbb", "cccccccccccccc"
            , "dddddddddddddd", "eeeeeeeeeeeeee", "ffffffffffffff", "gggggggggggggg", "aaaaaaaaaaaaaa", "bbbbbbbbbbbbbb", "cccccccccccccc"
            , "dddddddddddddd", "eeeeeeeeeeeeee", "ffffffffffffff", "gggggggggggggg"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RVAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);


        SwipeDismissRecyclerViewTouchListener touchListener =
                new SwipeDismissRecyclerViewTouchListener(
                        mRecyclerView,
                        new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(final RecyclerView recyclerView, int[] reverseSortedPositions) {
                                final RecyclerView.Adapter<RecyclerView.ViewHolder> adapter = mAdapter;
                                for (int position : reverseSortedPositions) {
//                                    ContentValues values = mResult.get(position);
                                    if (position != 0) {
                                        int prevItemViewType = adapter.getItemViewType(position - 1);
                                        if (prevItemViewType == 1 && position + 1 < adapter.getItemCount()) {
                                            int nextItemViewType = adapter.getItemViewType(position + 1);
                                            if (nextItemViewType == 1) {
                                                int pos = position - 1;
//                                                mResult.remove(pos);
                                                adapter.notifyItemRemoved(pos);
                                            }
                                        } else {
                                            if (prevItemViewType == 1) {
                                                int pos = position - 1;
//                                                mResult.remove(pos);
                                                adapter.notifyItemRemoved(pos);
                                            }
                                        }
                                    }
                                    int pos = position;
                                    Log.d(TAG,"remove: "+pos);
                                    mDataset.remove(pos);
                                    adapter.notifyItemRemoved(pos);
//                                    int pos = mResult.indexOf(values);
//                                    mResult.remove(values);
//                                    adapter.notifyItemRemoved(pos);
//                                    if (pos - 1 >= 0) {
//                                        adapter.notifyItemChanged(pos - 1);
//                                    }
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
        mRecyclerView.setOnTouchListener(touchListener);
//        setOnScrollListViewListener(touchListener.makeScrollListener());
        /*mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                Log.d(TAG, "onInterceptTouchEvent: " + motionEvent);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                Log.d(TAG, "onTouchEvent: "+ motionEvent);
            }
        });*/
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: "+ view);
                View backgroundView =view.findViewById(R.id.selected_background);
                backgroundView.setVisibility(backgroundView.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void onLongClick(View view, int position) {
                Log.d(TAG, "onLongClick: "+ view);
            }
        }));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
