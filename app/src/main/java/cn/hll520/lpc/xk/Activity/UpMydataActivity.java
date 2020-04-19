package cn.hll520.lpc.xk.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.xk.R;
import cn.hll520.lpc.xk.Thread.ThreadUlits;
import cn.hll520.lpc.xk.Thread.UserUtil;
import cn.hll520.lpc.xk.data.People;
import cn.hll520.lpc.xk.data.User;
import cn.hll520.lpc.xk.provider.ContactProvider;
import cn.hll520.lpc.xk.provider.UserProvider;

public class UpMydataActivity extends AppCompatActivity {

    @InjectView(R.id.upmydata_back)
    ImageView upmydataBack;
    @InjectView(R.id.Up_Mydata_IMG)
    ImageView UpMydataIMG;
    @InjectView(R.id.up_mydata_phone)
    EditText upMydataPhone;
    @InjectView(R.id.up_mydata_QQ)
    EditText upMydataQQ;
    @InjectView(R.id.up_mydata_house)
    EditText upMydataHouse;
    @InjectView(R.id.Upmydata_day)
    TextView UpmydataDay;
    @InjectView(R.id.upmydata_choseday)
    DatePicker upmydataChoseday;
    @InjectView(R.id.Upmydata_Campus)
    Spinner UpmydataCampus;
    @InjectView(R.id.Upmydata_College)
    Spinner UpmydataCollege;
    @InjectView(R.id.up_mydata_passinfo)
    EditText upMydataPassinfo;
    @InjectView(R.id.up_mydata_passkey)
    EditText upMydataPasskey;
    @InjectView(R.id.button)
    Button button;
    @InjectView(R.id.UpmyDataING)
    LinearLayout UpmyDataING;
    @InjectView(R.id.up_mydata_banji)
    Spinner upMydataBanji;
    private boolean flag = false;
    private People peo;
    private User user;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    upMydata();
                    break;
                case 2:
                    UpmyDataING.setVisibility(View.GONE);
                    Toast.makeText(UpMydataActivity.this, "保存失败！" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_mydata);
        ButterKnife.inject(this);
        peo = (People) getIntent().getSerializableExtra("peo");
        user = (User) getIntent().getSerializableExtra("user");
        if (peo.getBirthday() == null)
            peo.setBirthday("2020-04-01");
        upMydataPhone.setText(peo.getPhone());
        upMydataQQ.setText(peo.getQQ());
        String banji[]=getResources().getStringArray(R.array.ChoseBanji);
        upMydataBanji.setSelection(banji.length-1);
        for(int i=0;i<banji.length;i++){
            if(banji[i].equals(peo.getBanji()))
                upMydataBanji.setSelection(i);
        }
        String ca[]=getResources().getStringArray(R.array.Chosecampus);
        UpmydataCampus.setSelection(ca.length-1);
        for(int i=0;i<ca.length;i++){
            if(ca[i].equals(peo.getCampus()))
                UpmydataCampus.setSelection(i);
        }
        String co[]=getResources().getStringArray(R.array.Chosecollege);
        UpmydataCollege.setSelection(co.length-1);
        for(int i=0;i<co.length;i++){
            if(co[i].equals(peo.getCollege()))
                UpmydataCollege.setSelection(i);
        }
        upMydataHouse.setText(peo.getHouse());
        UpmydataDay.setText(peo.getBirthday());
        upMydataPassinfo.setText(user.getPassinfo());
        upMydataPasskey.setText(user.getPasskey());
    }

    @OnClick({R.id.upmydata_back, R.id.Up_Mydata_IMG, R.id.Upmydata_day, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.upmydata_back:
                finish();
                break;
            case R.id.Up_Mydata_IMG:
                Toast.makeText(this, "头像功能维护中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Upmydata_day:
                if (!flag) {
                    String[] data = UpmydataDay.getText().toString().split("-");
                    int moth = 0;
                    if (Integer.valueOf(data[1]) == 1)
                        upmydataChoseday.init(Integer.valueOf(data[0]), Integer.valueOf(data[1]) - 1, Integer.valueOf(data[2]), null);
                    upmydataChoseday.setVisibility(View.VISIBLE);
                } else {
                    upmydataChoseday.setVisibility(View.GONE);
                    int year = upmydataChoseday.getYear();
                    int moth = upmydataChoseday.getMonth() + 1;
                    UpmydataDay.setText(year + "-" + moth + "-" + upmydataChoseday.getDayOfMonth());
                }
                flag = !flag;
                break;
            case R.id.button:
                if (check())
                    okUpMydata();
                break;
        }
    }

    private boolean check() {
        if (upMydataHouse.getText().toString().length() == 0) {
            Toast.makeText(this, "宿舍不能为空！", Toast.LENGTH_SHORT).show();
            upMydataHouse.setError("宿舍不能为空！");
            return false;
        }
        if (upMydataPassinfo.getText().toString().length() == 0) {
            Toast.makeText(this, "密保不能为空", Toast.LENGTH_SHORT).show();
            upMydataPassinfo.setError("密保不能为空！");
            return false;
        }
        if (upMydataPasskey.getText().toString().length() == 0) {
            Toast.makeText(this, "密保答案不能为空", Toast.LENGTH_SHORT).show();
            upMydataPasskey.setError("答案不能为空！");
            return false;
        }
        if (flag) {
            Toast.makeText(this, "请再次点击日期栏关闭日历确定生日！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void okUpMydata() {
        user.setPassinfo(upMydataPassinfo.getText().toString());
        user.setPasskey(upMydataPasskey.getText().toString());
        peo.setPhone(upMydataPhone.getText().toString());
        peo.setQQ(upMydataQQ.getText().toString());
        peo.setBanji(upMydataBanji.getSelectedItem().toString());
        peo.setBirthday(UpmydataDay.getText().toString());
        peo.setCampus(UpmydataCampus.getSelectedItem().toString());
        peo.setCollege(UpmydataCollege.getSelectedItem().toString());
        if (!People.Check(peo).equals("true")) {
            Toast.makeText(this, People.Check(peo), Toast.LENGTH_SHORT).show();
            return;
        }
        UpmyDataING.setVisibility(View.VISIBLE);
        ThreadUlits.runInThread(new Runnable() {
            @Override
            public void run() {
                UserUtil userUtil = new UserUtil();
                Message msg = new Message();
                if (userUtil.updataUser(user) && userUtil.updataPeo(peo))
                    msg.what = 1;
                else
                    msg.what = 2;
                msg.obj = userUtil.getEffinfo();
                handler.sendMessage(msg);
            }
        });
    }

    private void upMydata() {
        UpmyDataING.setVisibility(View.GONE);
        Toast.makeText(UpMydataActivity.this, "修改资料完成！", Toast.LENGTH_SHORT).show();
        user.setPasskey("");
        this.getContentResolver().update(UserProvider.URI_CCONTACT, user.getCv(), "id=?", new String[]{user.getID()});
        this.getContentResolver().update(ContactProvider.URI_CCONTACT, peo.getCv(), "id=?", new String[]{peo.getId()});
        finish();
    }
}
