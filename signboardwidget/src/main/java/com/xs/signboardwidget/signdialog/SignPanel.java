package com.xs.signboardwidget.signdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xs.signboardwidget.R;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: Xs
 * @date: 2016-07-05 11:37
 * @email Xs.lin@foxmail.com
 */
public class SignPanel extends DialogFragment {
    public static final String TAG = "SignPanel";
    public static SignPanel getPanel() {
        final SignPanel sp = new SignPanel();
        final Bundle bundle = new Bundle();
        return sp;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setCancelable(false);
    }

        @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View mView = LayoutInflater.from(getActivity()).inflate(R.layout.view_signboard_dialog,null);
        return new AlertDialog.Builder(getActivity())
                .setView(mView)
                .show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final Button mBtnNo = (Button) view.findViewById(R.id.btn_no);

        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Toast.makeText(getActivity(),"dd",Toast.LENGTH_LONG).show();

            }
        });
    }
}
