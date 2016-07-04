package com.xs.signboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xs.signboardwidget.view.SignBoardView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG  = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SignBoardView mSbv = (SignBoardView) findViewById(R.id.sbv);
        final Button mBtnClear = (Button) findViewById(R.id.btn_clear);
        final Button mBtnRed = (Button) findViewById(R.id.btn_red);
        final Button mBtnBlack = (Button) findViewById(R.id.bnt_black);
        final Button mBtnGray = (Button) findViewById(R.id.btn_gray);
        final Button mBtnSmaller = (Button) findViewById(R.id.btn_smaller);
        final Button mBtnLarger = (Button) findViewById(R.id.btn_larger);
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSbv.clearView();
            }
        });

        mBtnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSbv.setPaintColor(android.R.color.holo_red_dark);
            }
        });

        mBtnBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSbv.setPaintColor(android.R.color.black);
            }
        });

        mBtnGray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSbv.setPaintColor(android.R.color.darker_gray);
            }
        });

        mBtnSmaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSbv.setmPaintStrokWidthSmaller();
            }
        });

        mBtnLarger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSbv.setmPaintStrokWidthLarger();
            }
        });


    }
}
