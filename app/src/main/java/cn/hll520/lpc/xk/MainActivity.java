package cn.hll520.lpc.xk;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.hll520.lpc.xk.Activity.ChatActivity;
import cn.hll520.lpc.xk.Thread.CheckAppUpdate;
import cn.hll520.lpc.xk.Thread.InfoUtil;
import cn.hll520.lpc.xk.Thread.ThreadUlits;
import cn.hll520.lpc.xk.Thread.UseRdsUlit;
import cn.hll520.lpc.xk.app.APP;
import cn.hll520.lpc.xk.data.Info;
import cn.hll520.lpc.xk.data.User;
import cn.hll520.lpc.xk.provider.InfoProvider;
import cn.hll520.lpc.xk.provider.UserProvider;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class MainActivity extends AppCompatActivity {
    User user = new User();
    Intent intent = new Intent();
    private Cursor cursor;
    ContentValues cv;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            upAppTo();
            super.handleMessage(msg);
        }
    };
    AlertDialog.Builder dialog;
    SharedPreferences.Editor editor;
    String version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cursor = this.getContentResolver().query(UserProvider.URI_CCONTACT, null, null, null, null);
        user = User.getLoginUser(this);

        //创建一个sharedpreferences对象

        editor=getSharedPreferences(APP.app,MODE_PRIVATE).edit();
        editor.apply();
        upApp();
        sever();
    }


    //判断登录
    private void checkLogin() {
        ThreadUlits.runInThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                if (cursor.moveToNext()) {
                    UseRdsUlit useRdsUlit = new UseRdsUlit();
                    useRdsUlit.upLogintime(user);
                    user.setForCursor(cursor);
                    intent.setAction("cn.hll520.wtu.xk.MAINTWO");
                    intent.addCategory("cn.hll520.wtu.xk.contact");
                    startActivity(intent);
                    finish();
                } else {
                    intent.setAction("cn.hll520.wtu.xk.MAINONE");
                    intent.addCategory("cn.hll520.wtu.xk.login");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    //检测更新
    private void upApp() {
        ThreadUlits.runInThread(new Runnable() {
            @Override
            public void run() {
                CheckAppUpdate appUpdate = new CheckAppUpdate();
                version = getString(R.string.version);
                cv = appUpdate.isChaek(version);
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        });
    }

    //返回更新版本后的执行
    private void upAppTo() {
        switch (cv.getAsString(CheckAppUpdate.FLAG)) {
            case CheckAppUpdate.FALSE:
                dialog = new AlertDialog.Builder(this).setTitle("需要更新").setIcon(R.mipmap.wtu).setMessage("最新版本：" + cv.getAsString(CheckAppUpdate.VS) +
                        "\n更新时间：" + cv.getAsString(CheckAppUpdate.TIME) + "\n更新内容：" + cv.getAsString(CheckAppUpdate.VAR)).
                        setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(cv.getAsString(CheckAppUpdate.URL)));
                                startActivity(intent);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "取消更新", Toast.LENGTH_SHORT).show();
                        checkLogin();
                    }
                }).setCancelable(false);;
                //.setCancelable(false);必须做出选择
                dialog.create().show();
                break;
            case CheckAppUpdate.STOP:
                dialog = new AlertDialog.Builder(this).setTitle("版本过低").setIcon(R.mipmap.wtu).
                        setMessage("警告！当前版本过低，必须更新才能使用"+"\n最新版本：" + cv.getAsString(CheckAppUpdate.VS) +
                        "\n更新时间：" + cv.getAsString(CheckAppUpdate.TIME) + "\n停用原因：" + cv.getAsString(CheckAppUpdate.STOPVAR)).
                        setPositiveButton("确认更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(cv.getAsString(CheckAppUpdate.URL)));
                                startActivity(intent);
                                System.exit(0);
                            }
                        }).setNegativeButton("停止使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "已自动退出", Toast.LENGTH_SHORT).show();
                        System.exit(0);
                    }
                }).setCancelable(false);
                dialog.create().show();
                break;
            default:
                //判断第一次登录
                SharedPreferences sp=getSharedPreferences(APP.app,MODE_PRIVATE);
                //如果没有字段，默认未否
                if(!sp.getBoolean(version,false)){
                    AlertDialog.Builder builder=new AlertDialog.Builder(this).setTitle("新版本特性：").
                            setMessage(cv.getAsString(CheckAppUpdate.VAR)).setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkLogin();
                        }
                    });
                    builder.create().show();
                    editor.putBoolean(version,true);
                    editor.apply();
                }else
                checkLogin();
                break;
        }
    }


    //后台线程
    private void sever() {
        ThreadUlits.runInThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                while (true) {
                    MediaPlayer player;
                    player = MediaPlayer.create(MainActivity.this, R.raw.info);
                    //创建一个通知管理器
                    NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(MainActivity.this.NOTIFICATION_SERVICE);
                    SystemClock.sleep(100);
                    InfoUtil infoUtil = new InfoUtil();
                    List<Info> infos = new ArrayList<>();
                    infos = infoUtil.getInfo(User.getLoginUser(MainActivity.this));
                    if (infos.size() != 0) {
                        for (int i = 0; i < infos.size(); i++) {
                            MainActivity.this.getContentResolver().insert(InfoProvider.URI_CCONTACT, infos.get(i).getCv());
                            infos.get(i).getInfoUpCard(MainActivity.this);
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            intent.putExtra("id", infos.get(i).getSendID());
                            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (Build.VERSION.SDK_INT >= 26) {
                                //当sdk版本大于26
                                String id = "channel_1";
                                String description = "143";
                                int importance = NotificationManager.IMPORTANCE_LOW;
                                NotificationChannel channel = new NotificationChannel(id, description, importance);
//                     channel.enableLights(true);
//                     channel.enableVibration(true);//
                                manager.createNotificationChannel(channel);
                                Notification notification = new Notification.Builder(MainActivity.this, id)
                                        .setCategory(Notification.CATEGORY_MESSAGE)
                                        .setSmallIcon(R.mipmap.wtu)
                                        .setContentTitle("来自" + infos.get(i).getSendID() + "的新消息")
                                        .setContentText(infos.get(i).getVar())
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(true)
                                        .build();
                                manager.notify(1, notification);
                            } else {//当sdk版本小于26
                                Notification notification = new NotificationCompat.Builder(MainActivity.this)
                                        .setContentTitle("来自" + infos.get(i).getSendID() + "的新消息")
                                        .setContentText(infos.get(i).getVar())
                                        .setContentIntent(pendingIntent)
                                        .setSmallIcon(R.mipmap.wtu)
                                        .build();
                                manager.notify(1, notification);
                            }
                            player.start();
                        }
                    }
                }
            }
        });
    }

}
