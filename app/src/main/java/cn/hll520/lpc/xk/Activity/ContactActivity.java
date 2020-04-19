package cn.hll520.lpc.xk.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.xk.Activity.PeoData.ImproActivity;
import cn.hll520.lpc.xk.Activity.PeoData.NewPeoActivity;
import cn.hll520.lpc.xk.Activity.PeoData.OutPeoActivity;
import cn.hll520.lpc.xk.Activity.PeoData.upPeo;
import cn.hll520.lpc.xk.Activity.kcb.KcbActivity;
import cn.hll520.lpc.xk.R;
import cn.hll520.lpc.xk.Thread.ThreadUlits;
import cn.hll520.lpc.xk.app.APP;
import cn.hll520.lpc.xk.data.People;
import cn.hll520.lpc.xk.provider.ContactProvider;

public class ContactActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.PeopleList)
    ListView PeopleList;
    @InjectView(R.id.PeopleING)
    LinearLayout PeopleING;
    @InjectView(R.id.My)
    ImageView My;
    @InjectView(R.id.add)
    ImageView add;
    @InjectView(R.id.info)
    LinearLayout info;
    @InjectView(R.id.people)
    LinearLayout people;
    @InjectView(R.id.classTab)
    LinearLayout classTab;

    AlertDialog.Builder Box;
    MyCoatObsever coatObsever = new MyCoatObsever(new Handler());
    CursorAdapter cursorAdapter;
    Intent in = new Intent();
    Cursor currentCursor;
    People peo = new People();
    @InjectView(R.id.MentOk)
    Button MentOk;
    @InjectView(R.id.MentCheose)
    Spinner MentCheose;
    @InjectView(R.id.MainMenu)
    ImageView MainMenu;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    Intent intent;
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.inject(this);
        initData(null, null);
        PeopleList.setOnItemClickListener(this);
        //绑定组件
        initListener();
        infoshow();//显示全局公告
    }

    private void infoshow() {
        SharedPreferences preferences=getSharedPreferences(APP.app,MODE_PRIVATE);
        String key=preferences.getString(APP.appinfo,"");
        Log.i("mms","key="+key+"\napp="+APP.isINFO);
        if(!APP.isINFO.equals(key)){
            SharedPreferences.Editor editor=preferences.edit();
            showInfo();
            editor.putString(APP.appinfo,APP.isINFO);
            editor.apply();
        }
    }

    private void showInfo() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).setIcon(R.mipmap.wtu).setTitle("公告")
                .setMessage(APP.INFO).setPositiveButton("知道啦", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.create().show();
    }

    private void initListener() {
        registerForContextMenu(PeopleList);
        setSupportActionBar(toolbar);
        //绑定内容观察者
        getContentResolver().registerContentObserver(ContactProvider.URI_CCONTACT, true, coatObsever);
        PeopleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //获取被选中的联系人
                currentCursor = (Cursor) parent.getItemAtPosition(position);
                //false表示该事件向外传播，这样可以激活ContextMenu,ture表示事件处理完毕，系统不再执行后续操作
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("操作联系人");
        //不分组，第一项，顺序，名称
        menu.add(Menu.NONE, Menu.FIRST, 1, "编辑联系人");
        menu.add(Menu.NONE, Menu.FIRST + 1, 2, "删除联系人");
        menu.add(Menu.NONE, Menu.FIRST + 2, 3, "更多操作");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                intent = new Intent(this, upPeo.class);
                People peo2 = new People();
                peo2.setForcursor(currentCursor);
                intent.putExtra("peo", peo2);
                startActivity(intent);
                break;
            case Menu.FIRST + 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("确定删除吗？").
                        setIcon(R.mipmap.wtu).setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        People peo = new People();
                        peo.setForcursor(currentCursor);
                        getContentResolver().delete(ContactProvider.URI_CCONTACT, "ID=?", new String[]{peo.getId()});
                        Toast.makeText(ContactActivity.this, "联系人" + peo.getName() + "已删除", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ContactActivity.this, "取消删除", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
                break;
            case Menu.FIRST + 2:
                intent = new Intent(ContactActivity.this, PeocardActivity.class);
                peo.setForcursor(currentCursor);
                intent.putExtra("peo", peo);
                Log.i("msc", peo.toString());
                startActivity(intent);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, Menu.FIRST, 1, "新建联系人");
        menu.add(1, Menu.FIRST + 1, 2, "导入联系人");
        menu.add(1, Menu.FIRST + 2, 3, "导出联系人");
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                addPeo();
                break;
            case Menu.FIRST + 1:
                improPeo();
                break;
            case Menu.FIRST + 2:
                outPeo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void outPeo() {
        Intent intent = new Intent(this, OutPeoActivity.class);
        startActivity(intent);
    }

    private void improPeo() {
        Intent intent = new Intent(this, ImproActivity.class);
        startActivity(intent);
    }

    private void addPeo() {
        Intent intent = new Intent(this, NewPeoActivity.class);
        startActivity(intent);
    }

    //activity被关闭时所执行
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定
        getContentResolver().unregisterContentObserver(coatObsever);
    }

    //点击卡片的触发
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ContactActivity.this, PeocardActivity.class);
        currentCursor = (Cursor) parent.getItemAtPosition(position);
        peo.setForcursor(currentCursor);
        intent.putExtra("peo", peo);
        Log.i("msc", peo.toString());
        startActivity(intent);
    }

    @OnClick({R.id.My, R.id.add, R.id.people, R.id.classTab, R.id.info, R.id.MentOk, R.id.MainMenu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.My:
                in = new Intent(this, MyActivity.class);
                startActivity(in);
                break;
            case R.id.add:
                //搜索框
                final EditText nameTV = new EditText(this);
//                模糊查询
                Box = new AlertDialog.Builder(this).setTitle("输入需要收索的关键字").setView(nameTV)
                        .setIcon(R.mipmap.wtu)
                        .setPositiveButton("搜索", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (nameTV.getText().toString().length() == 0)
                                    return;
                                initData("ID LIKE ? or name LIKE ? or college LIKE ? or ment LIKE ?",
                                        new String[]{"%" + nameTV.getText().toString() + "%", "%" + nameTV.getText().toString() + "%", "%" + nameTV.getText().toString() + "%", "%" + nameTV.getText().toString() + "%"});
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ContactActivity.this, "退出搜索", Toast.LENGTH_SHORT).show();
                            }
                        });
                Box.create().show();
                break;
            case R.id.people:
                break;
            case R.id.classTab:
                in = new Intent(this, KcbActivity.class);
                startActivity(in);
                finish();
                break;
            case R.id.info:
                in = new Intent(this, infolistActivity.class);
                startActivity(in);
                finish();
                break;
            case R.id.MentOk:
                if (MentCheose.getSelectedItem().toString().equals("全部"))
                    initData(null, null);
                else
                    initData("banji=?", new String[]{MentCheose.getSelectedItem().toString()});
                break;
            case R.id.MainMenu:
                openOptionsMenu();
                break;
        }
    }

    //刷新数据
    private void initData(final String where, final String[] keys) {
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = getContentResolver().query(ContactProvider.URI_CCONTACT, null, where, keys, null);
                if (cursor.getCount() == 0)
                    return;
                cursorAdapter = new CursorAdapter(ContactActivity.this, cursor, true) {
                    @Override
                    public View newView(Context context, Cursor cursor, ViewGroup parent) {
                        //把视图与布局文件绑定
                        View view = View.inflate(context, R.layout.peocard, null);
                        return view;
                    }

                    @Override
                    public void bindView(View view, Context context, Cursor cursor) {
                        //把组件与数据绑定
                        People peo = new People();
                        peo.setForcursor(cursor);
                        TextView nameTV = view.findViewById(R.id.name);
                        TextView callTV = view.findViewById(R.id.call);
                        TextView calssTV = view.findViewById(R.id.peoCalss);
                        TextView postTV = view.findViewById(R.id.peoPost);
                        nameTV.setText(peo.getName());
                        callTV.setText(peo.getPhone());
                        calssTV.setText(peo.getMent());
                        postTV.setText(peo.getPosition());
                    }
                };
                PeopleList.setAdapter(cursorAdapter);
            }
        });

    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            initData(null, null);
            MentCheose.setSelection(0);
        }
    }
}
