package cn.hll520.lpc.SoftwareTest.Thread;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showToastSafe(final Context context, final String str){
        //ctrl+shift+空格自动提示代码 匿名内部类---线程
        //ctrl+j 调用模板填充
        //ctrl+o 重写方法
        //ctrl+d 复制这行
        //alt+enter 自动改错提示
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, str, Toast.LENGTH_LONG).show();
            }
        });
    }
}