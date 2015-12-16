package csci3310.cuhk.edu.hk.project.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import csci3310.cuhk.edu.hk.project.R;

public class EmptyFragment extends Fragment {

    private static final String HINT_TEXT = "empty_hint_text";
    private String empty_hint_text;
    private TextView emptyHintTextview;

    public EmptyFragment() {
        // Required empty public constructor
    }

    public static EmptyFragment newInstance(String empty_hint_text) {
        EmptyFragment fragment = new EmptyFragment();
        Bundle args = new Bundle();
        args.putString(EmptyFragment.HINT_TEXT, empty_hint_text);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            empty_hint_text = getArguments().getString(EmptyFragment.HINT_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        emptyHintTextview = (TextView) view.findViewById(R.id.emptyFragment_text);
        emptyHintTextview.setText(empty_hint_text);

        return view;
    }

}
