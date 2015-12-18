package csci3310.cuhk.edu.hk.project.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import csci3310.cuhk.edu.hk.project.R;

public class ListDialogFragment extends AppCompatDialogFragment {

    public static final String POSITION = "position";

    public int position;
    private OnDialogListItemSelectListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        position = getArguments().getInt(POSITION);
        // Use the Builder for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(getListItems(position), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onDialogListItemSelect(position ,which);
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnDialogListItemSelectListener) {
            mListener = (OnDialogListItemSelectListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnDialogListItemSelectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDialogListItemSelectListener {
        void onDialogListItemSelect(int caller_position, int position);
    }

    public String[] getListItems(int caller_position) {
        String[] listItems = null;
        switch (position) {
            case 0:
                listItems = new String[] {"1", "2", "3"};
                break;
            case 1:
                listItems = getResources().getStringArray(R.array.record_type);
                break;
            case 2:
                listItems = getResources().getStringArray(R.array.category);
                break;
            default:
                throw new IllegalArgumentException("Unknown Position: " + position);
        }
        return listItems;
    }
}
