package com.example.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

public class MathService extends Service {
    private static final String TAG = "MathService";

    private ComputeListener mComputeListener;

    /*
    1. 建立 IMathService.Stub的实例mBinder并实现AIDL文件定义的远程服务接口
    2. 在onBind()方法中将mBinder返回给远程调用者
    */
    private final IMathService.Stub mBinder = new IMathService.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
        }

        @Override
        public long add(long a, long b) throws RemoteException {
            Log.i(TAG, "add() " + a + "+" + b + "=" + (a+b));
            return a + b;
        }

        @Override
        public AllResult ComputeAll(long a, long b) throws RemoteException {
            AllResult allResult;
            double divResult = (double) a / b;
            allResult = new AllResult(a+b, a-b, a*b, divResult);

            if (mComputeListener != null) {
                mComputeListener.OnFinishCompute(a+10, b+10);

                HashMap<String, String> map = new HashMap<>();
                map.put("a", String.valueOf(a+10));
                map.put("b", String.valueOf(b+10));
                mComputeListener.OnHashmapCallback(map);
            }

            return allResult;
        }

        @Override
        public void setComputeListener(ComputeListener listener) throws RemoteException {
            mComputeListener = listener;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(MathService.this, TAG + " onBind()", Toast.LENGTH_SHORT).show();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(MathService.this, TAG + " onUnbind()", Toast.LENGTH_SHORT).show();
//        android.os.Process.killProcess(android.os.Process.myPid());
        return super.onUnbind(intent);
    }
}
