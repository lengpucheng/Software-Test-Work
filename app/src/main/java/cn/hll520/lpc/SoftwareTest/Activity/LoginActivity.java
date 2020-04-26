package cn.hll520.lpc.SoftwareTest.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.SoftwareTest.R;
import cn.hll520.lpc.SoftwareTest.Thread.ThreadUlits;
import cn.hll520.lpc.SoftwareTest.Thread.UseRdsUlit;
import cn.hll520.lpc.SoftwareTest.data.People;
import cn.hll520.lpc.SoftwareTest.data.User;
import cn.hll520.lpc.SoftwareTest.provider.ContactProvider;
import cn.hll520.lpc.SoftwareTest.provider.UserProvider;
import cn.hll520.lpc.SoftwareTest.uilt.ToastShow;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.userName)
    EditText userName;
    @InjectView(R.id.passWord)
    EditText passWord;
    @InjectView(R.id.login)
    ImageView login;
    @InjectView(R.id.forgetPass)
    TextView forgetPass;
    @InjectView(R.id.regUser)
    TextView regUser;
    @InjectView(R.id.LoginIng)
    LinearLayout LoginIng;
    @InjectView(R.id.LogAbout)
    TextView LogAbout;
    private Context context;
    private User user = new User(true);
    private MediaPlayer player;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //如果为1就做下面那个事情
                    doSomething();
                    break;
                case 2:
                    LoginIng.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void doSomething() {
        LoginIng.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);
        ToastShow.showToastLong(context, "上次登录时间：" + user.getLogintime());
        Intent intent = new Intent("cn.hll520.wtu.xk.MAINTWO");
        intent.addCategory("cn.hll520.wtu.xk.contact");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        ButterKnife.inject(this);
        player = MediaPlayer.create(this, R.raw.info);
    }

    @OnClick({R.id.login, R.id.forgetPass, R.id.regUser,R.id.LogAbout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                player.start();
                user.setID(userName.getText().toString());
                user.setUsername(userName.getText().toString());
                user.setPassword(passWord.getText().toString());
                if (uesrChek()) {
                    LoginIng.setVisibility(View.VISIBLE);
                    login.setVisibility(View.INVISIBLE);
                    login();
                }
                break;
            case R.id.forgetPass:
//                Toast.makeText(this, "忘记密码功能正在维护中", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ForgetActivity.class);
                startActivity(intent);
                break;
            case R.id.regUser:
//                Toast.makeText(this, "注册用户", Toast.LENGTH_SHORT).show();
                Intent reg = new Intent(LoginActivity.this, ReguserActivity.class);
                startActivity(reg);
                break;
            case R.id.LogAbout:
                Intent about = new Intent(LoginActivity.this, AboutActivity.class);
                startActivity(about);
                break;
        }
    }

    private boolean uesrChek() {
        if (user.getID().length() == 0) {
            userName.setError("用户名不能为空！");
            return false;
        }
        if (user.getPassword().length() == 0) {
            passWord.setError("密码不能为空！");
            return false;
        }
        return true;
    }

    private void login() {
        ThreadUlits.runInThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                UseRdsUlit useRdsUlit = new UseRdsUlit();
                if (useRdsUlit.login(user)) {
                    ThreadUlits.runInThread(new Runnable() {
                        @Override
                        public void run() {
                            List<People> data;
                            UseRdsUlit useRdsUlit = new UseRdsUlit();
                            data = useRdsUlit.getContact();
                            if (data.size() == 0)
                                ToastShow.showToastLong(LoginActivity.this, "联系人获取失败:" + useRdsUlit.getEffinfo());
                            else {
                                Message msg = new Message();
                                LoginActivity.this.getContentResolver().insert(UserProvider.URI_CCONTACT, user.getCv());
                                for (int i = 0; i < data.size(); i++) {
                                    LoginActivity.this.getContentResolver().insert(ContactProvider.URI_CCONTACT, data.get(i).getCv());
                                }
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
                        }
                    });
                } else {
                    ToastShow.showToastShor(context, "登录失败：" + useRdsUlit.getEffinfo());
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
