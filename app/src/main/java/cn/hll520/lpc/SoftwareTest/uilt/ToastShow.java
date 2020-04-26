package cn.hll520.lpc.SoftwareTest.uilt;

import android.content.Context;
import android.widget.Toast;

import cn.hll520.lpc.SoftwareTest.Thread.ThreadUlits;

public class ToastShow {
    public static void showToastShor(final Context context,final String val){
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, val, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showToastLong(final Context context,final String val){
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, val, Toast.LENGTH_LONG).show();
            }
        });
    }

}
