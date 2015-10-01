package com.example.samples.dialogfrgment;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import android.util.TypedValue;
import android.view.Display;

/**
 * Created by hassan on 14-09-15.
 */
@SuppressLint("NewApi")
public class _CameraDialogFragment extends android.app.DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preventScreenRotation();
    }

    @Override
    public void onDestroy() {
        releaseScreenRotation();
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart() {
        super.onStart();

        // change dialog width
        if (getDialog() != null) {

            int fullWidth = getDialog().getWindow().getAttributes().width;
            int fullHeight = getDialog().getWindow().getAttributes().height;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                fullWidth = size.x;
                fullHeight = size.y;
            } else {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                fullWidth = display.getWidth();
                fullHeight = display.getHeight();
            }

            final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
                    .getDisplayMetrics());

            int w = fullWidth - padding;
            int h = fullHeight - padding * 2;

            getDialog().getWindow().setLayout(w, h);
        }
    }


    private void preventScreenRotation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void releaseScreenRotation() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}
