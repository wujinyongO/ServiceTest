package com.example.servicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button bind;
    private Button unbind;
    private Button add;

    private boolean isBound = false;
    private IMathService mathService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mathService = IMathService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mathService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();

        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBound == false) {
                    final Intent serviceIntent = new Intent();
                    serviceIntent.setAction("com.example.servicetest");
                    serviceIntent.setPackage(getPackageName());
                    bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
                    isBound = true;
                }
            }
        });

        unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBound) {
                    unbindService(mConnection);
                    isBound = false;
                    mathService = null;
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mathService == null) {
                    Toast.makeText(MainActivity.this, "未绑定服务", Toast.LENGTH_SHORT).show();
                    return;
                }

                long a = (long) (Math.random() * 100);
                long b = (long) (Math.random() * 100);
                long result = 0;

                try {
                    result = mathService.add(a, b);
                    mathService.basicTypes(0,0,true,0,0,"");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                String msg = String.valueOf(a) + " + " + String.valueOf(b) + " = " + result;
                textView.setText(msg);
            }
        });
    }

    private void findView() {
        textView = findViewById(R.id.textView);
        bind = findViewById(R.id.bind);
        unbind = findViewById(R.id.unbind);
        add = findViewById(R.id.add);
    }
}
