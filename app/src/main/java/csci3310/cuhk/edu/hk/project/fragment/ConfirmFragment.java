package csci3310.cuhk.edu.hk.project.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import csci3310.cuhk.edu.hk.project.R;

public class ConfirmFragment extends AppCompatDialogFragment {

    private OnDialogButtonClickListener mListener;
    private String recordId;
    private int recordPosition;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        recordId = getArguments().getString(ItemsFragment.RECORD_ID);
        recordPosition = getArguments().getInt(ItemsFragment.RECORD_POSITION);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Confirm Delete ?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.OnDialogButtonClick(recordId, recordPosition, Boolean.TRUE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.OnDialogButtonClick(recordId, recordPosition, Boolean.FALSE);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnDialogButtonClickListener) {
            mListener = (OnDialogButtonClickListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDialogButtonClickListener {
        void OnDialogButtonClick(String recordId, int recordPosition, Boolean confirm);
    }
}
