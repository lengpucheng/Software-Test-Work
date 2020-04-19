package cn.hll520.lpc.xk.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.xk.R;
import cn.hll520.lpc.xk.Thread.ThreadUlits;
import cn.hll520.lpc.xk.Thread.UseRdsUlit;
import cn.hll520.lpc.xk.Thread.UserUtil;
import cn.hll520.lpc.xk.app.APP;
import cn.hll520.lpc.xk.data.People;
import cn.hll520.lpc.xk.data.User;
import cn.hll520.lpc.xk.provider.ContactProvider;
import cn.hll520.lpc.xk.provider.InfoListProvider;
import cn.hll520.lpc.xk.provider.InfoProvider;
import cn.hll520.lpc.xk.provider.UserProvider;

public class MyActivity extends AppCompatActivity {
    @InjectView(R.id.MyCradBack)
    ImageView MyCradBack;
    @InjectView(R.id.MyCradShar)
    ImageView MyCradShar;
    @InjectView(R.id.MyCardTo)
    LinearLayout MyCardTo;
    @InjectView(R.id.UpMydate)
    LinearLayout UpMydate;
    @InjectView(R.id.Manger)
    LinearLayout Manger;
    @InjectView(R.id.UpMyPass)
    LinearLayout UpMyPass;
    @InjectView(R.id.ClenData)
    LinearLayout ClenData;
    @InjectView(R.id.ReustDate)
    LinearLayout ReustDate;
    @InjectView(R.id.AboutThis)
    LinearLayout AboutThis;
    @InjectView(R.id.EndLogin)
    LinearLayout EndLogin;
    @InjectView(R.id.MyIco)
    ImageView MyIco;
    @InjectView(R.id.MyName)
    TextView MyName;
    @InjectView(R.id.MyTime)
    TextView MyTime;
    People peo = new People();
    User user = new User();
    Intent in;
    @InjectView(R.id.MyING)
    LinearLayout MyING;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyING.setVisibility(View.GONE);
                    Toast.makeText(MyActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    MyING.setVisibility(View.GONE);
                    Toast.makeText(MyActivity.this, "刷新已完成", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    MyING.setVisibility(View.GONE);
                    Toast.makeText(MyActivity.this, "刷新失败：请检测网络链接", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    MyING.setVisibility(View.GONE);
                    in=new Intent(MyActivity.this, UpMydataActivity.class);
                    in.putExtra("peo",peo);
                    in.putExtra("user",user);
                    startActivity(in);
                    break;
                case 4:
                    MyING.setVisibility(View.GONE);
                    Toast.makeText(MyActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    AlertDialog.Builder dialogBox;
    private EditText passOK=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.inject(this);
        user.index(MyActivity.this.getContentResolver().query(UserProvider.URI_CCONTACT, null, null, null, null));
        peo.index(MyActivity.this.getContentResolver().query(ContactProvider.URI_CCONTACT, null, "ID=?", new String[]{user.getID()}, null));
        MyName.setText(peo.getName());
        MyTime.setText("注册时间：" + user.getRegTime());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.MyCradBack, R.id.MyCradShar, R.id.MyCardTo, R.id.UpMydate, R.id.Manger, R.id.UpMyPass, R.id.ClenData, R.id.ReustDate, R.id.AboutThis, R.id.EndLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.MyCradBack:
                finish();
                break;
            case R.id.MyCradShar:
                Toast.makeText(this, "分享功能维护中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.MyCardTo:
                in = new Intent(this, PeocardActivity.class);
                in.putExtra("peo", peo);
                startActivity(in);
                break;
            case R.id.UpMydate:
                upMyData();
                break;
            case R.id.Manger:
                if (user.getSafety() > 4 || user.getSafety() == 0)
                    Toast.makeText(this, "控制台功能维护中", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "权限不足", Toast.LENGTH_SHORT).show();
                break;
            case R.id.UpMyPass:
                in = new Intent(MyActivity.this, ForgetActivity.class);
                in.putExtra("user", user);
                startActivity(in);
                break;
            case R.id.ClenData:
                MyING.setVisibility(View.VISIBLE);
                //给一个弹窗确认
                dialogBox=new AlertDialog.Builder(this).setIcon(R.mipmap.wtu).setTitle("提示").setMessage("清空缓存将会导致聊天记录丢失！\n确定清空缓存吗？").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cleanRom();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MyActivity.this, "取消清空", Toast.LENGTH_SHORT).show();
                            MyING.setVisibility(View.GONE);
                    }
                });
                dialogBox.create().show();
                break;
            case R.id.ReustDate:
                MyING.setVisibility(View.VISIBLE);
                dialogBox=new AlertDialog.Builder(this).setIcon(R.mipmap.wtu).setTitle("注意").setMessage("刷新数据将会同云端保持" +
                        "同步,这样可能会导致自定义的新增的联系人数据丢失！\n确定刷新吗？").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reustDate();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MyActivity.this, "取消刷新", Toast.LENGTH_SHORT).show();
                        MyING.setVisibility(View.GONE);
                    }
                });
                dialogBox.create().show();
                break;
            case R.id.AboutThis:
                in = new Intent(MyActivity.this, AboutActivity.class);
                startActivity(in);
                break;
            case R.id.EndLogin:
                MyActivity.this.getContentResolver().delete(ContactProvider.URI_CCONTACT,null,null);
                MyActivity.this.getContentResolver().delete(InfoListProvider.URI_CCONTACT,null,null);
                this.getContentResolver().delete(UserProvider.URI_CCONTACT, null, null);
                in = new Intent(this, LoginActivity.class);
                startActivity(in);
                finishAffinity();
                break;
        }
    }

    //修改资料
    private void upMyData() {
        passOK=new EditText(this);
        passOK.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialogBox=new AlertDialog.Builder(this).setTitle("请输入密码进行身份验证")
                .setIcon(R.mipmap.wtu).setView(passOK).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            MyING.setVisibility(View.VISIBLE);
                            ThreadUlits.runInThread(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void run() {
                                    UserUtil userUtil=new UserUtil();
                                    user.setPassword(passOK.getText().toString());
                                    Message message=new Message();
                                    if(userUtil.checkPassTOkey(user))
                                        message.what=3;
                                    else
                                        message.what=4;
                                    handler.sendMessage(message);
                                }
                            });
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MyActivity.this, "身份验证失败", Toast.LENGTH_SHORT).show();
            }
        });
        dialogBox.create().show();
    }

    //刷新资料
    private void reustDate() {
        ThreadUlits.runInThread(new Runnable() {
            @Override
            public void run() {
                List<People> data = null;
                UseRdsUlit useRdsUlit = new UseRdsUlit();
                data = useRdsUlit.getContact();
                if (data.size() == 0 || data == null) {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    MyActivity.this.getContentResolver().delete(ContactProvider.URI_CCONTACT, null, null);
                    for (int i = 0; i < data.size(); i++) {
                        MyActivity.this.getContentResolver().insert(ContactProvider.URI_CCONTACT, data.get(i).getCv());
                    }
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    //清理缓存
    private void cleanRom() {
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(500);
                MyActivity.this.getContentResolver().delete(InfoProvider.URI_CCONTACT,null,null);
                MyActivity.this.getContentResolver().delete(InfoListProvider.URI_CCONTACT,null,null);
                Message msg = new Message();
                msg.what = 0;
                msg.obj=new String("缓存已清空");
                handler.sendMessage(msg);
            }
        });
    }
}
