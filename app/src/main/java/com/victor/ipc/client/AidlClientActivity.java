package com.victor.ipc.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.victor.ipc.server.IpcAidlInterface;

public class AidlClientActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "AidlClientActivity";
    private Button mBtnBind,mBtnUnBind;
    private TextView mTvMsg;

    private IpcAidlInterface mIpcAidlInterface;
    private boolean isBind = false;

    private ServiceConnection mConn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIpcAidlInterface = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBind = true;
            mIpcAidlInterface = IpcAidlInterface.Stub.asInterface(service);
            showAidlMess();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_client);

        initialize();
    }

    private void initialize () {
        mBtnBind = (Button) findViewById(R.id.btn_bind);
        mBtnUnBind = (Button) findViewById(R.id.btn_unbind);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);
        mBtnUnBind.setOnClickListener(this);
        mBtnBind.setOnClickListener(this);

    }

    private void showAidlMess(){
        try {
            if (mIpcAidlInterface != null) {
                mTvMsg.setText(mIpcAidlInterface.getName() + "count = " + mIpcAidlInterface.getCount());
                Toast.makeText(getApplicationContext(),mIpcAidlInterface.getName() + "count = " + mIpcAidlInterface.getCount(),Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bind:
                if (!isBind) {
                    Intent intent = new Intent("com.victor.aidl.service");//aidl 服务端注册的action
                    intent.setPackage("com.victor.ipc.server");//aidl 服务端packageName
                    bindService(intent, mConn, Context.BIND_AUTO_CREATE);
                } else {
                    showAidlMess();
                }
                break;
            case R.id.btn_unbind:
                if (isBind) {
                    unbindService(mConn);
                    isBind = false;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind) {
            unbindService(mConn);
        }
    }
}
