package com.bytedance.clockapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bytedance.clockapplication.widget.Clock;

public class MainActivity extends AppCompatActivity {

    private View mRootView;
    private Clock mClockView;
    private static final int MSG_REFSH = 200;
    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            mClockView.invalidate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = findViewById(R.id.root);
        mClockView = findViewById(R.id.clock);

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClockView.setShowAnalog(!mClockView.isShowAnalog());
            }
        });
        AfreshThread refresh = new AfreshThread("Refesh");
        refresh.start();;
    }

    public class AfreshThread extends HandlerThread implements Handler.Callback{
        private Handler mWorkandler;
        private static final int MSG_CLOCK_REFRESH = 100;

        public AfreshThread(String name) {
            super(name);
        }

        @Override
        protected void onLooperPrepared() {
            mWorkandler = new Handler(getLooper(), this);
            mWorkandler.sendEmptyMessage(MSG_CLOCK_REFRESH);
        }

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MSG_CLOCK_REFRESH:
                    mainHandler.sendEmptyMessage(MSG_REFSH);
                    mWorkandler.sendEmptyMessageDelayed(MSG_CLOCK_REFRESH,1000);
                    break;
            }
            return true;
        }
    }

}
