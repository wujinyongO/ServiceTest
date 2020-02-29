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

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView mTextView;
    private Button bind;
    private Button unbind;
    private Button add;
    private Button computeAll;
    private Button mAddPerson;

    private MyComputeListener computeListener;

    private boolean isBound = false;
    private IMathService mathService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mathService = IMathService.Stub.asInterface(iBinder);
            try {
                mathService.setComputeListener(computeListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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
        computeListener = new MyComputeListener();

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

        computeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mathService == null) {
                    Toast.makeText(MainActivity.this, "未绑定服务", Toast.LENGTH_SHORT).show();
                    return;
                }

                long a = (long) (Math.random() * 100);
                long b = (long) (Math.random() * 100);
                AllResult result = null;

                try {
                    result = mathService.ComputeAll(a, b);
                    mathService.basicTypes(0,0,true,0,0,"");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                if (result != null) {
                    String msg = String.valueOf(a) + " + " + String.valueOf(b) + " = " + result.addResult + "\n";
                    msg += String.valueOf(a) + " - " + String.valueOf(b) + " = " + result.subResult + "\n";
                    msg += String.valueOf(a) + " * " + String.valueOf(b) + " = " + result.mulResult + "\n";
                    msg += String.valueOf(a) + " / " + String.valueOf(b) + " = " + result.divResult;
                    textView.setText(msg);
                }
            }
        });

        mAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mathService == null) {
                    Toast.makeText(MainActivity.this, "未绑定服务", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Person person = new Person(21,"Tom");
                    List<Person> list = mathService.addPersion(person);
                    mTextView.setText(list.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void findView() {
        textView = findViewById(R.id.textView);
        mTextView = findViewById(R.id.textView2);
        bind = findViewById(R.id.bind);
        unbind = findViewById(R.id.unbind);
        add = findViewById(R.id.add);
        computeAll = findViewById(R.id.computeAll);
        mAddPerson = findViewById(R.id.addperson);
    }

    private class MyComputeListener extends ComputeListener.Stub {

        @Override
        public void OnFinishCompute(long a, long b) throws RemoteException {
            Toast.makeText(MainActivity.this, "onFinishCompute: "
                    +"a="+a+" b="+b, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnHashmapCallback(Map map) throws RemoteException {
            Toast.makeText(MainActivity.this, "OnHashmapCallback: "
                    +"a="+map.get("a")+" b="+map.get("b"), Toast.LENGTH_SHORT).show();
        }
    }
}
