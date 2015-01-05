package by.dzmitryslutskiy.swipetodismiss;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * RVAdapter
 * Version information
 * 04.01.2015
 * Created by Dzmitry Slutskiy.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    public static final String TAG = RVAdapter.class.getSimpleName();
    private List<String> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewGroup mRootView;
        public View mSelected;

        public ViewHolder(View v) {
            super(v);
            mSelected = v.findViewById(R.id.selected_background);

            mRootView = (ViewGroup) v.findViewById(R.id.item);
            /*mRootView.setLongClickable(true);
            mRootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG, "onLongClick");
                    mSelected.setVisibility(mSelected.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                    return false;
                }
            });
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick");
                    mSelected.setVisibility(mSelected.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                }
            });*/

            mTextView = (TextView) v.findViewById(R.id.text_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RVAdapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.rv_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        View rightDismiss = layoutInflater.inflate(R.layout.right_dismiss_layout, parent, false);
        ((ViewGroup) v.findViewById(R.id.right_dismiss)).addView(rightDismiss);

        View leftDismiss = layoutInflater.inflate(R.layout.left_dismiss_layout, parent, false);
        ((ViewGroup) v.findViewById(R.id.left_dismiss)).addView(leftDismiss);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
