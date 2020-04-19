package cn.hll520.lpc.xk.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.xk.R;
import cn.hll520.lpc.xk.Thread.CheckAppUpdate;
import cn.hll520.lpc.xk.Thread.ThreadUlits;

public class AboutActivity extends AppCompatActivity {

    @InjectView(R.id.AboutBack)
    ImageView AboutBack;
    @InjectView(R.id.upapp)
    LinearLayout upapp;
    @InjectView(R.id.gotoweb)
    LinearLayout gotoweb;
    @InjectView(R.id.aboutHelp)
    LinearLayout aboutHelp;
    @InjectView(R.id.aboutsever)
    LinearLayout aboutsever;
    @InjectView(R.id.aboutsugge)
    LinearLayout aboutsugge;
    Intent intent = new Intent();
    @InjectView(R.id.AboutING)
    LinearLayout AboutING;
    @InjectView(R.id.vieso)
    TextView vieso;



    ContentValues values = new ContentValues();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            AboutING.setVisibility(View.GONE);
            switch (msg.what) {
                case 1:
                    Toast.makeText(AboutActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                    break;
                case 2: case 3:
                    vieso.setText("最新版本："+values.get(CheckAppUpdate.VS)+"\n更新时间："+values.getAsString(CheckAppUpdate.TIME));
                    upApp();
                    break;
                case 0:
                    Toast.makeText(AboutActivity.this, "检测更新失败" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //弹窗
    private void upApp(){
        AlertDialog.Builder dialog;
        dialog=new AlertDialog.Builder(this).setTitle("发现新版本").setIcon(R.mipmap.wtu).setMessage("最新版本："+values.getAsString(CheckAppUpdate.VS)+
                "\n更新时间："+values.getAsString(CheckAppUpdate.TIME)+"\n更新内容："+values.getAsString(CheckAppUpdate.VAR)).
                setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(values.getAsString(CheckAppUpdate.URL)));
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.upapp, R.id.gotoweb, R.id.aboutHelp, R.id.aboutsever, R.id.aboutsugge, R.id.AboutBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.upapp:
                AboutING.setVisibility(View.VISIBLE);
                ThreadUlits.runInThread(new Runnable() {
                    @Override
                    public void run() {
                        CheckAppUpdate appUpdate = new CheckAppUpdate();
                        String version = getString(R.string.version);
                        ContentValues cv = appUpdate.isChaek(version);
                        Message msg = new Message();
                        values = cv;
                        switch (cv.getAsString(CheckAppUpdate.FLAG)) {
                            case CheckAppUpdate.TRUE:
                                msg.what = 1;
                                break;
                            case CheckAppUpdate.NOUP:
                                msg.what = 2;
                                break;
                            case CheckAppUpdate.FALSE:
                                msg.what = 3;
                                break;
                            case CheckAppUpdate.EFF:
                                msg.what = 0;
                                msg.obj = appUpdate.getEffinfo();
                                break;
                        }
                        handler.sendMessage(msg);
                    }
                });
                break;
            case R.id.gotoweb:
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.hll520.cn/app/wtu/main.html"));
                startActivity(intent);
                break;
            case R.id.aboutHelp:
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.hll520.cn/app/wtu/help.html"));
                startActivity(intent);
                break;
            case R.id.aboutsever:
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.hll520.cn/app/wtu/sever.html"));
                startActivity(intent);
            case R.id.aboutsugge:
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.hll520.cn/app/wtu/sugge.html"));
                startActivity(intent);
                break;
            case R.id.AboutBack:
                finish();
                break;
        }
    }
}
