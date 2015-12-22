package csci3310.cuhk.edu.hk.project.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import csci3310.cuhk.edu.hk.project.AccountActivity;
import csci3310.cuhk.edu.hk.project.R;
import csci3310.cuhk.edu.hk.project.bean.Account;
import csci3310.cuhk.edu.hk.project.db.AccountsDataHelper;

public class ListDialogFragment extends AppCompatDialogFragment {

    public static final String POSITION = "position";

    public int position;
    private OnDialogListItemSelectListener mListener;
    private AccountsDataHelper mDataHelper;
    private List<Account> accountList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataHelper = new AccountsDataHelper(getActivity());
        accountList = mDataHelper.query();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        position = getArguments().getInt(POSITION);
        // Use the Builder for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (position == 0 && getListItems(position).length == 0) {
            builder.setItems(new String[]{"Please Create Account First"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), AccountActivity.class);
                    getActivity().startActivity(intent);
                }
            });
        } else {
            builder.setItems(getListItems(position), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mListener != null) {
                        mListener.onDialogListItemSelect(position, which);
                    }
                }
            });
        }
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
                List<String> nameList = new ArrayList<>();
                for (Account account : accountList) {
                    nameList.add(account.name);
                }
                listItems = nameList.toArray(new String[accountList.size()]);
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
