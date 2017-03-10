package com.victor.ipc.client;import android.content.ComponentName;import android.content.Intent;import android.content.ServiceConnection;import android.content.pm.PackageManager;import android.content.pm.ResolveInfo;import android.os.Handler;import android.os.IBinder;import android.os.Message;import android.os.Messenger;import android.os.RemoteException;import android.support.v7.app.AppCompatActivity;import android.os.Bundle;import android.view.View;import android.widget.Button;import android.widget.TextView;public class MessengerClientActivity extends AppCompatActivity implements View.OnClickListener{    private Button mBtnBind,mBtnUnBind;    private TextView mTvMsg;    private static final int SEND_MESSAGE_CODE = 0x0001;    private static final int RECEIVE_MESSAGE_CODE = 0x0002;    private boolean isBound = false;    private String SERVICE_ACTION = "com.service.messengerservice";    private Messenger serviceMessenger = null;    private Messenger clientMessenger;    Handler mHandler = new Handler(){        @Override        public void handleMessage(Message msg) {            super.handleMessage(msg);            if(msg.what == RECEIVE_MESSAGE_CODE){                Bundle data = msg.getData();                if(data != null){                    String str = data.getString("msg");                    mTvMsg.setText(str);                }            }        }    };    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_messenger);        initialize();    }    private void initialize () {        mBtnBind = (Button) findViewById(R.id.btn_bind);        mBtnUnBind = (Button) findViewById(R.id.btn_unbind);        mTvMsg = (TextView) findViewById(R.id.tv_msg);        mBtnUnBind.setOnClickListener(this);        mBtnBind.setOnClickListener(this);        clientMessenger = new Messenger(mHandler);    }    private ServiceConnection serviceConnection = new ServiceConnection() {        @Override        public void onServiceConnected(ComponentName name, IBinder service) {            serviceMessenger = new Messenger(service);            isBound = true;            Message msg = Message.obtain();            msg.what = SEND_MESSAGE_CODE;            Bundle data = new Bundle();            data.putString("msg","你好，MyService，我是客户端");            msg.setData(data);            msg.replyTo = clientMessenger;            try {                serviceMessenger.send(msg);            } catch (RemoteException e) {                e.printStackTrace();            }        }        @Override        public void onServiceDisconnected(ComponentName name) {            serviceMessenger = null;            isBound = false;        }    };    @Override    public void onClick(View view) {        switch (view.getId()) {            case R.id.btn_bind:                if(!isBound){                    Intent intent = new Intent();                    intent.setAction(SERVICE_ACTION);                    intent.addCategory(Intent.CATEGORY_DEFAULT);                    PackageManager pm = getPackageManager();                    ResolveInfo info = pm.resolveService(intent,0);                    if(info != null){                        String packageName = info.serviceInfo.packageName;                        String serviceName = info.serviceInfo.name;                        ComponentName componentName = new ComponentName(packageName,serviceName);                        intent.setComponent(componentName);                        bindService(intent,serviceConnection,BIND_AUTO_CREATE);                    }                }                break;            case R.id.btn_unbind:                if(isBound){                    unbindService(serviceConnection);                    isBound = false;                }                break;        }    }}