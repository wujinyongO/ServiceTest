// IMathService.aidl
package com.example.servicetest;

import com.example.servicetest.AllResult;
import com.example.servicetest.ComputeListener;
import com.example.servicetest.Person;

// Declare any non-default types here with import statements

interface IMathService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    long add(long a, long b);

    AllResult ComputeAll(long a, long b);

    void setComputeListener(ComputeListener listener);

    List<Person> addPersion(in Person person);
}
