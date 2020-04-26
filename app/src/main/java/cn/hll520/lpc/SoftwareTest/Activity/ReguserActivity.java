package cn.hll520.lpc.SoftwareTest.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.SoftwareTest.R;
import cn.hll520.lpc.SoftwareTest.Thread.ThreadUlits;
import cn.hll520.lpc.SoftwareTest.Thread.UseRdsUlit;
import cn.hll520.lpc.SoftwareTest.data.People;
import cn.hll520.lpc.SoftwareTest.data.User;
import cn.hll520.lpc.SoftwareTest.uilt.ToastShow;

public class ReguserActivity extends AppCompatActivity {
    @InjectView(R.id.ReguserName)
    EditText ReguserName;
    @InjectView(R.id.ReguserID)
    EditText ReguserID;
    @InjectView(R.id.radioNan)
    RadioButton radioNan;
    @InjectView(R.id.radioNV)
    RadioButton radioNV;
    @InjectView(R.id.Regment)
    Spinner Regment;
    @InjectView(R.id.ReguserPhone)
    EditText ReguserPhone;
    @InjectView(R.id.ReguserQQ)
    EditText ReguserQQ;
    @InjectView(R.id.RegPassWord)
    EditText RegPassWord;
    @InjectView(R.id.ChackPass)
    EditText ChackPass;
    @InjectView(R.id.ReguserYQM)
    EditText ReguserYQM;
    @InjectView(R.id.reg)
    Button reg;
    @InjectView(R.id.reset)
    Button reset;
    @InjectView(R.id.RegING)
    ProgressBar RegING;
    @InjectView(R.id.ChackPassInfo)
    EditText ChackPassInfo;
    @InjectView(R.id.ChackPassKey)
    EditText ChackPassKey;
    private People peo;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reguser);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.reg, R.id.reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reg:
                RegUser();
                break;
            case R.id.reset:
                Reset();
                break;
        }
    }

    private void Reset() {
        ReguserID.setText("");
        ReguserName.setText(" ");
        radioNan.isChecked();
        Regment.setSelection(0);
        ReguserQQ.setText("");
        ReguserPhone.setText("");
        ReguserYQM.setText("");
        RegPassWord.setText(" ");
        ChackPassInfo.setText(" ");
        ChackPassKey.setText("");
    }

    private void RegUser() {
        RegING.setVisibility(View.VISIBLE);
        if (CheckUser()) {
            RegING.setVisibility(View.VISIBLE);
            user = new User();
            peo = new People();
            user.setID(ReguserID.getText().toString());
            user.setUsername(ReguserName.getText().toString());
            user.setPassword(RegPassWord.getText().toString());
            user.setBm(Regment.getSelectedItem().toString());
            user.setPassinfo(ChackPassInfo.getText().toString());
            user.setPassinfo(ChackPassKey.getText().toString());
            peo.setId(user.getID());
            peo.setName(user.getUsername());
            peo.setQQ(ReguserQQ.getText().toString());
            peo.setPhone(ReguserPhone.getText().toString());
            peo.setMent(user.getBm());
            if (radioNV.isChecked())
                peo.setGender("女");
            else
                peo.setGender("男");
            final String yqm = ReguserYQM.getText().toString();
            ThreadUlits.runInThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    UseRdsUlit useRdsUlit = new UseRdsUlit();
                    if (useRdsUlit.regUser(user, peo, yqm)) {
                        ToastShow.showToastLong(ReguserActivity.this, "注册成功,请登录吧！");
                        finish();
                    } else {
                        ToastShow.showToastLong(ReguserActivity.this, "注册失败:" + useRdsUlit.getEffinfo());
                    }
                }
            });
        }
        RegING.setVisibility(View.GONE);
    }

    private boolean CheckUser() {
        //ID
        if (!ReguserID.getText().toString().matches("[1-9][0-9]{9}")) {
            Toast.makeText(this, "学号需为10位纯数字", Toast.LENGTH_SHORT).show();
            ReguserID.setError("学号应为10位纯数字");
            return false;
        }
        //name
        if (ReguserName.getText().toString().length() == 0) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            ReguserName.setError("姓名格式不正确");
            return false;
        }
        //密码
        if (!RegPassWord.getText().toString().matches("\\S{6,}")) {
            Toast.makeText(this, "密码需至少6位非空字符", Toast.LENGTH_SHORT).show();
            RegPassWord.setError("密码需至少6位非空字符");
            return false;
        }
        //确认密码
        if (!ChackPass.getText().toString().equals(RegPassWord.getText().toString())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            ChackPass.setError("两次密码不一致");
            return false;
        }
        if(!ReguserPhone.getText().toString().matches("^[1][3,4,5,7,8][0-9]{9}$")){
            Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            ReguserPhone.setError("手机号码应该是11位");
            return false;
        }
        if(!ReguserQQ.getText().toString().matches("[1-9][0-9]+")){
            Toast.makeText(this, "QQ号码格式不正确", Toast.LENGTH_SHORT).show();
            ReguserQQ.setError("QQ格式不正确");
            return false;
        }
        if(ChackPassInfo.getText().toString().length()==0){
            Toast.makeText(this, "请设置密保问题", Toast.LENGTH_SHORT).show();
            ChackPassInfo.setError("请设置密保问题");
            return false;
        }
        if(ChackPassKey.getText().toString().length()==0){
            Toast.makeText(this, "请设置密保答案", Toast.LENGTH_SHORT).show();
            ChackPassKey.setError("请设置密保答案");
            return false;
        }
        return true;
    }


}