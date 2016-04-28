package com.jhlc.km.sb.oss;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OSS on 2015/12/22 0022.
 *
 */
public class CallbackGroup implements Runnable {
    private List<Runnable> list = new ArrayList<>();

    public synchronized void add(@NonNull Runnable r) {
        list.add(r);
    }

    public synchronized Runnable pop() {
        if (!list.isEmpty()) {
            Runnable r = list.get(0);
            list.remove(0);
            return r;
        }
        else {
            return null;
        }
    }

    public void run() {
        Runnable r = pop();
        while (r != null) {
            r.run();
            r = pop();
        }
    }
}
