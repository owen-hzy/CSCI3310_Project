package csci3310.cuhk.edu.hk.project.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import csci3310.cuhk.edu.hk.project.R;
import csci3310.cuhk.edu.hk.project.fragment.AttributeFragment.OnListFragmentInteractionListener;


/**
 * TODO: Replace the implementation with code for your data type.
 */
public class AttributeRecyclerViewAdapter extends RecyclerView.Adapter<AttributeRecyclerViewAdapter.ViewHolder> {

    private String[] labelArray;
    private String[] valueArray;
    private int[] iconResourceArray;
    private final OnListFragmentInteractionListener mListener;

    public AttributeRecyclerViewAdapter(String[] labelArray, String[] valueArray, int[] iconResourceArray, OnListFragmentInteractionListener listener) {
        this.labelArray = labelArray;
        this.valueArray = valueArray;
        this.iconResourceArray = iconResourceArray;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_attribute, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.iconView.setImageResource(iconResourceArray[position]);
        holder.labelView.setText(labelArray[position]);
        holder.valueView.setText(valueArray[position]);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return labelArray.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView iconView;
        public final TextView labelView;
        public final TextView valueView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            iconView = (ImageView) view.findViewById(R.id.attribute_icon);
            labelView = (TextView) view.findViewById(R.id.attribute_label);
            valueView = (TextView) view.findViewById(R.id.attribute_value);
        }

    }
}
