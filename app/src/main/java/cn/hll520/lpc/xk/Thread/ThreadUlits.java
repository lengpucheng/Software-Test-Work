package cn.hll520.lpc.xk.Thread;

import android.os.Handler;

public class ThreadUlits {
    private static Handler handler = new Handler();

    //启动一个普通线程,传入一个线程参数
    public static void runInThread(Runnable task) {
        new Thread(task).start();
    }

    //启动一个UI线程下执行的Handler线程task
    public static void runInUIThread(Runnable task) {
        //handler相应的手柄，在主线程下执行该线程，可以进行界面改变，事件处理等
        handler.post(task);
    }
}
