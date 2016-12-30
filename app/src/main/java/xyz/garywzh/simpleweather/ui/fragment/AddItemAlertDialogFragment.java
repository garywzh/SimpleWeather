package xyz.garywzh.simpleweather.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import xyz.garywzh.simpleweather.R;

/**
 * Created by garywzh on 2016/7/27.
 */
public class AddItemAlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private EditText input;

    public static AddItemAlertDialogFragment newInstance() {
        return new AddItemAlertDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        int margin_in_dp = 24;
        final float scale = getResources().getDisplayMetrics().density;
        int margin_in_px = (int) (margin_in_dp * scale + 0.5f);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = margin_in_px;
        params.rightMargin = margin_in_px;
        input = new EditText(activity);
        input.setSingleLine(true);
        input.setLayoutParams(params);
        FrameLayout container = new FrameLayout(activity);
        container.addView(input);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_title)
                .setView(container)
                .setPositiveButton(R.string.alert_dialog_ok, this)
                .setNegativeButton(R.string.alert_dialog_cancel, this)
                .create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case Dialog.BUTTON_NEGATIVE:
                break;
            case Dialog.BUTTON_POSITIVE:
                String address = input.getText().toString().trim();
                if (!address.isEmpty())
                    ((OnAddAddressListener) getActivity()).OnAddAddress(input.getText().toString());
                break;
        }
    }

    public interface OnAddAddressListener {
        void OnAddAddress(String address);
    }
}
