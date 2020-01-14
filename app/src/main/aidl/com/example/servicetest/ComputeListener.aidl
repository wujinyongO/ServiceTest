// ComputeListener.aidl
package com.example.servicetest;

// Declare any non-default types here with import statements

interface ComputeListener {

    void OnFinishCompute(long a, long b);

    void OnHashmapCallback(inout Map map);
}