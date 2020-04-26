package cn.hll520.lpc.SoftwareTest.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.SoftwareTest.R;
import cn.hll520.lpc.SoftwareTest.Thread.ThreadUlits;
import cn.hll520.lpc.SoftwareTest.Thread.UseRdsUlit;
import cn.hll520.lpc.SoftwareTest.data.User;
import cn.hll520.lpc.SoftwareTest.uilt.ToastShow;

public class ForgetActivity extends AppCompatActivity {
    @InjectView(R.id.Forgtend)
    ImageView Forgtend;
    @InjectView(R.id.ForgetTitle)
    TextView ForgetTitle;
    @InjectView(R.id.ForgetID)
    EditText ForgetID;
    @InjectView(R.id.fogetNext)
    Button fogetNext;
    @InjectView(R.id.findMyID)
    LinearLayout findMyID;
    @InjectView(R.id.Fogetpassinfo)
    TextView Fogetpassinfo;
    @InjectView(R.id.FogetKey)
    EditText FogetKey;
    @InjectView(R.id.NewPass)
    EditText NewPass;
    @InjectView(R.id.CheckNewPass)
    EditText CheckNewPass;
    @InjectView(R.id.fogetOK)
    Button fogetOK;
    @InjectView(R.id.upPassword)
    LinearLayout upPassword;
    @InjectView(R.id.ForgetING)
    LinearLayout ForgetING;
    @InjectView(R.id.ForgetHome)
    LinearLayout ForgetHome;
    private boolean flag;
    private User user;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.inject(this);
        User users = (User) getIntent().getSerializableExtra("user");
        context = this;
        if (users == null) {
            findMyID.setVisibility(View.VISIBLE);
            upPassword.setVisibility(View.GONE);
            ForgetTitle.setText("找回密码");
            user = new User();
        } else
            user = users;
        Fogetpassinfo.setText(user.getPassinfo());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.Forgtend, R.id.fogetNext, R.id.fogetOK})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Forgtend:
                finish();
                break;
            case R.id.fogetNext:
                ForgetING.setVisibility(View.VISIBLE);
                fogetID();
                break;
            case R.id.fogetOK:
                ForgetING.setVisibility(View.VISIBLE);
                upPassword();
                break;
        }
    }

    private void fogetID() {
        flag = false;
        String id = ForgetID.getText().toString();
        if (id.length() != 0) {
            user.setID(ForgetID.getText().toString());
            ThreadUlits.runInThread(new Runnable() {
                @Override
                public void run() {
                    UseRdsUlit useRdsUlit = new UseRdsUlit();
                    flag = useRdsUlit.findInfo(user);
                    if (!flag)
                        ToastShow.showToastShor(context, "找回失败：" + useRdsUlit.getEffinfo());
                    Message msg = new Message();
                    msg.what = 1;
                    myHandler.sendMessage(msg);
                }
            });
        } else{
            Toast.makeText(this, "ID不能为空", Toast.LENGTH_SHORT).show();
            ForgetING.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void upPassword() {
        flag = false;
        String key = FogetKey.getText().toString();
        if (checak()) {
            if (key.equals(user.getPasskey())) {
                ThreadUlits.runInThread(new Runnable() {
                    @Override
                    public void run() {
                        UseRdsUlit useRdsUlit = new UseRdsUlit();
                        flag = useRdsUlit.upPassWord(user);
                        if (!flag)
                            ToastShow.showToastShor(context, "修改密码失败：" + useRdsUlit.getEffinfo());
                        Message msg = new Message();
                        msg.what = 2;
                        myHandler.sendMessage(msg);
                    }
                });
            } else{
                Toast.makeText(this, "密保答案不正确！", Toast.LENGTH_SHORT).show();
                ForgetING.setVisibility(View.GONE);
            }
        }else
            ForgetING.setVisibility(View.GONE);
    }

    Handler myHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (flag) {
                        findMyID.setVisibility(View.GONE);
                        upPassword.setVisibility(View.VISIBLE);
                        Fogetpassinfo.setText(user.getPassinfo());
                    }
                    ForgetING.setVisibility(View.GONE);
                    break;
                case 2:
                    if (flag) {
                        Toast.makeText(context, "修改成功，请重新登陆！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                    ForgetING.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private boolean checak() {
        user.setPassword(NewPass.getText().toString());
        if (!user.getPassword().matches("\\S{6,}")) {
            Toast.makeText(this, "密码长度应大于6位", Toast.LENGTH_SHORT).show();
            NewPass.setError("密码长度应大于6位");
            return false;
        }
        if (!CheckNewPass.getText().toString().equals(user.getPassword())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            CheckNewPass.setError("两次密码不一致");
            return false;
        }
        return true;
    }

}