package com.example.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MathService extends Service {
    private static final String TAG = "MathService";

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
            return allResult;
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
