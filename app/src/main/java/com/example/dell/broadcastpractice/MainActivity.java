package com.example.dell.broadcastpractice;

import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dell.broadcastpractice.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {


    private static String SAMPLE_ACTION="com.example.dell.receiver.SAMPLE_ACTION";
    private static String GLOBAL_ACTION="com.example.dell.receiver.GLOBAL";
    private ActivityMainBinding binding;
    private MyBroadcastReceiver myBroadcastReceiver;
    private LocalBroadcastReceiver localBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        myBroadcastReceiver= new MyBroadcastReceiver();
        localBroadcastReceiver = new LocalBroadcastReceiver();


    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myBroadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
       LocalBroadcastManager.getInstance(this).registerReceiver(localBroadcastReceiver, new IntentFilter(LocalBroadcastReceiver.LOCAL_ACTION));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(myBroadcastReceiver);
        unregisterReceiver(localBroadcastReceiver);
        super.onPause();
    }
    public void sendBroadcast(View view) {
        switch(view.getId()){
            case R.id.localBroadcastBtn:
                Intent localIntent= new Intent(LocalBroadcastReceiver.LOCAL_ACTION);
                localIntent.putExtra("local msg","local broadcast message");
                LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
                break;
            case R.id.globalBroadcastBtn1:
                Intent globalIntentOne= new Intent(SAMPLE_ACTION);
                globalIntentOne.putExtra("broadcastOne","Broadcast 1 simple text");
                sendBroadcast(globalIntentOne);
                break;
            case R.id.globalBroadcastBtn2:
                Intent globalIntentTwo= new Intent(GLOBAL_ACTION);
                globalIntentTwo.putExtra("broadcastOne","Broadcast 2 simple text registered in menifest");
                sendBroadcast(globalIntentTwo);
                break;

        }
    }
    public class LocalBroadcastReceiver extends BroadcastReceiver{


        private static final String LOCAL_ACTION="com.example.dell.broadcastpractice.LOCAL_ACTION";
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("broadcast","receiver 2 called");
            Log.e("broadcst","local received called");
                String msg = intent.getStringExtra("local msg");
            Toast.makeText(MainActivity.this, "local: "+msg, Toast.LENGTH_SHORT).show();
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info!=null && info.isConnected())
            {
                binding.showMsg.setText("Connected");
            }
            else {
                binding.showMsg.setText("Disconnected");
            }
        }
    }
    public class BootCompleteReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Boot Completed", Toast.LENGTH_SHORT).show();
            Log.d("Boot", "completed");
        }
    }




}
