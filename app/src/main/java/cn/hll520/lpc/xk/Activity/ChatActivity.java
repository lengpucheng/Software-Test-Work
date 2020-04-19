package cn.hll520.lpc.xk.Activity;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.xk.R;
import cn.hll520.lpc.xk.Thread.InfoUtil;
import cn.hll520.lpc.xk.Thread.ThreadUlits;
import cn.hll520.lpc.xk.data.Info;
import cn.hll520.lpc.xk.data.People;
import cn.hll520.lpc.xk.data.User;
import cn.hll520.lpc.xk.provider.ContactProvider;
import cn.hll520.lpc.xk.provider.InfoProvider;

public class ChatActivity extends AppCompatActivity {

    @InjectView(R.id.her)
    ImageView her;
    @InjectView(R.id.charName)
    TextView charName;
    @InjectView(R.id.seeting)
    ImageView seeting;
    @InjectView(R.id.PeopleING)
    LinearLayout PeopleING;
    @InjectView(R.id.CharleList)
    ListView CharleList;
    @InjectView(R.id.CharSendinfo)
    EditText CharSendinfo;
    @InjectView(R.id.Chatsend)
    Button Chatsend;
    People peo = new People();
    User user;
    MyCoatObsever coatObsever = new MyCoatObsever(new Handler());
    CursorAdapter cursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getIntent().getStringExtra("id");
        peo.index(getContentResolver().query(ContactProvider.URI_CCONTACT, null,
                "id=?", new String[]{id}, null));
        user = User.getLoginUser(this);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        charName.setText(peo.getName());
        initData();
        initListener();
    }

    private void initListener() {
        getContentResolver().registerContentObserver(InfoProvider.URI_CCONTACT, true, coatObsever);
    }

    //activity被关闭时所执行
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定
        getContentResolver().unregisterContentObserver(coatObsever);
    }

    private void initData() {
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                if (cursorAdapter != null) {
                    cursorAdapter.getCursor().requery();
                    return;
                }
                Cursor cursor = Info.getCursorsByID(ChatActivity.this, peo.getId());
                if (cursor.getCount() == 0)
                    return;
                cursorAdapter = new CursorAdapter(ChatActivity.this, cursor, true) {
                    @Override
                    public View newView(Context context, Cursor cursor, ViewGroup parent) {
                        //把视图与布局文件绑定
                        View view = View.inflate(context, R.layout.chatcord, null);
                        return view;
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void bindView(View view, Context context, Cursor cursor) {
                        //把组件与数据绑定
                        Info info = Info.getInfo(cursor);
                        TextView nameTV = view.findViewById(R.id.ChatCardName);
                        TextView varTV = view.findViewById(R.id.ChatVar);
                        TextView timeTV = view.findViewById(R.id.ChatTime);
                        ImageView herTV=view.findViewById(R.id.ChatIMG1);
                        ImageView youTV=view.findViewById(R.id.ChatIMG2);
                        LinearLayout MainTv=view.findViewById(R.id.ChatMain);
                        LinearLayout InfoTv=view.findViewById(R.id.ChatInfo);
                        String id=info.getSendID();
                        //如果是自己的消息
                        if(user.getID().equals(info.getSendID())){
                            //获取名字
                            id=user.getUsername();
                            //姓名颜色加红
                            nameTV.setTextColor(Color.parseColor("#F44336"));
                            //名字靠右
                            nameTV.setGravity(Gravity.RIGHT);
                            //头像显示右边
                            herTV.setVisibility(View.GONE);
                            youTV.setVisibility(View.VISIBLE);
                            //消息在右边
                            MainTv.setGravity(Gravity.RIGHT);
                            InfoTv.setGravity(Gravity.RIGHT);
                        }else{
                            id=peo.getName();
                            nameTV.setTextColor(Color.parseColor("#111111"));
                            //名字靠右
                            nameTV.setGravity(Gravity.LEFT);
                            //头像显示右边
                            herTV.setVisibility(View.VISIBLE);
                            youTV.setVisibility(View.GONE);
                            //消息在右边
                            MainTv.setGravity(Gravity.LEFT);
                            InfoTv.setGravity(Gravity.LEFT);
                        }
                        nameTV.setText(id);
                        varTV.setText(info.getVar());
                        timeTV.setText(info.getTime());
                    }
                };
                CharleList.setAdapter(cursorAdapter);
                CharleList.setSelection(CharleList.getBottom());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.her, R.id.seeting, R.id.Chatsend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.her:
                finish();
                break;
            case R.id.seeting:
                Toast.makeText(this, "功能维护中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Chatsend:
                final Info info = new Info();
                info.setSendID(user.getID());
                info.setGetID(peo.getId());
                info.setVar(CharSendinfo.getText().toString());
                info.sendInfo(this);
                CharSendinfo.setText("");
                ThreadUlits.runInThread(new Runnable() {
                    @Override
                    public void run() {
                        InfoUtil infoUtil=new InfoUtil();
                        infoUtil.sendInfo(info);
                    }
                });
                break;
        }
    }

    //观察者
    class MyCoatObsever extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyCoatObsever(Handler handler) {
            super(handler);
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            initData();
        }
    }
}

