package csci3310.cuhk.edu.hk.project.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import csci3310.cuhk.edu.hk.project.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AttributeFragment extends Fragment {

    public static final String ICON_RESOURCE_ARRAY = "iconArray";
    public static final String LABEL_STRING_ARRAY = "labelArray";
    public static final String VALUE_STRING_ARRAY = "valueArray";

    private View container_view;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AttributeFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AttributeFragment newInstance(String[] labelArray, String[] valueArray, int[] iconArray) {
        AttributeFragment fragment = new AttributeFragment();
        Bundle args = new Bundle();
        args.putStringArray(AttributeFragment.LABEL_STRING_ARRAY, labelArray);
        args.putStringArray(AttributeFragment.VALUE_STRING_ARRAY, valueArray);
        args.putIntArray(AttributeFragment.ICON_RESOURCE_ARRAY, iconArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container_view = inflater.inflate(R.layout.fragment_attribute_list, container, false);

        // Set the adapter
        Context context = container_view.getContext();
        RecyclerView recyclerView = (RecyclerView) container_view.findViewById(R.id.attribute_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new AttributeRecyclerViewAdapter(
                getArguments().getStringArray(AttributeFragment.LABEL_STRING_ARRAY),
                getArguments().getStringArray(AttributeFragment.VALUE_STRING_ARRAY),
                getArguments().getIntArray(AttributeFragment.ICON_RESOURCE_ARRAY),
                mListener));

        return container_view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateAttributeValue(int index, String value) {
        TextView valueView = (TextView) ((RecyclerView) container_view.findViewById(R.id.attribute_list)).getChildAt(index).findViewById(R.id.attribute_value);
        valueView.setText(value);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(int position);
    }
}
