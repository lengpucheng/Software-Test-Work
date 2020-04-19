package cn.hll520.lpc.xk.Activity.PeoData;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.xk.R;
import cn.hll520.lpc.xk.Thread.ThreadUlits;
import cn.hll520.lpc.xk.Thread.ToastUtils;
import cn.hll520.lpc.xk.data.People;
import cn.hll520.lpc.xk.provider.ContactProvider;
import cn.hll520.lpc.xk.uilt.PeoAdapter;

public class ImproActivity extends AppCompatActivity {

    @InjectView(R.id.peolistBack)
    ImageView peolistBack;
    @InjectView(R.id.peoListMenu)
    ImageView peoListMenu;
    @InjectView(R.id.peolisttoolbar)
    Toolbar peolisttoolbar;
    @InjectView(R.id.PeoList)
    ListView PeoList;
    @InjectView(R.id.peolIstTile)
    TextView peolIstTile;

    private PeoAdapter myAdapter;
    private List<People> peos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peolist);
        ButterKnife.inject(this);
        peolIstTile.setText("导入联系人");
        initData();
        initView();
        registerForContextMenu(PeoList);
        setSupportActionBar(peolisttoolbar);
    }

    //读取联系人
    private void initData() {
        //判断申请权限,如果他的权限不是被允许的  PackageManager.PERMISSION_GRANTED===是否拥有
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //请求权限   1,请求的活动，2、权限，3、编号（相当于 msg.what）
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            importContact();
        }
    }

    private void initView() {
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                if (myAdapter == null)
                    myAdapter = new PeoAdapter(ImproActivity.this, peos);
                PeoList.setAdapter(myAdapter);
            }
        });
    }

    private void importContact() {
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                //查找联系人的数据
                Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                // 遍历查询结果，获取系统中所有联系人
                while (cursor.moveToNext()) {
                    //获取联系人ID
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    //获取联系人名字
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //使用ContentResolver查找联系人的电话号码
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null,
                            null);
                    //只获取第一个电话号码
                    String phoneNumber = "";
                    if (phones.moveToFirst())
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //关闭phone记录集
                    phones.close();
                    //使用ContentResolver查找联系人的email
                    Cursor emails = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,
                            null, null);
                    //只获取第一个email
                    String emailAddress = "";
                    if (emails.moveToFirst())
                        emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    //关闭emails记录集
                    emails.close();
                    //创建一个联系人对象
                    People peo = new People();
                    if (phoneNumber.length() > 10)
                        peo.setId(phoneNumber.substring(0, 10));
                    else
                        peo.setId(phoneNumber);
                    peo.setName(name);
                    peo.setPhone(phoneNumber);
                    peo.setQQ(emailAddress);
                    peo.setFlag(false);
                    //将contact加入到contacts集合中
                    peos.add(peo);
                    Log.i("peoo",peo.toString());
                }
                cursor.close();
            }
        });
    }


    @OnClick({R.id.peolistBack, R.id.peoListMenu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.peolistBack:
                finish();
                break;
            case R.id.peoListMenu:
                openOptionsMenu();
                break;
        }
    }

    //创建上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("导入操作");
        menu.add(Menu.NONE, Menu.FIRST, 1, "全选");
        menu.add(Menu.NONE, Menu.FIRST + 1, 2, "全不选");
        menu.add(Menu.NONE, Menu.FIRST + 2, 3, "导入");
    }

    //上下文菜单选项事件处理
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                selectALL();
                break;
            case Menu.FIRST + 1:
                selectNOALL();
                break;
            case Menu.FIRST + 2:
                doImprotPeo();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void doImprotPeo() {
        ThreadUlits.runInThread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                for (People peo : peos) {
                    if (peo.isFlag()) {
                        Cursor cursor = getContentResolver().query(ContactProvider.URI_CCONTACT,
                                null,
                                "ID=?",
                                new String[]{peo.getId()}, null);
                        if (cursor.getCount()==0) {
                            getContentResolver().insert(ContactProvider.URI_CCONTACT, peo.getCv());
                            count++;
                            cursor.close();
                        } else {
                            ToastUtils.showToastSafe(ImproActivity.this, "已存在");
                        }
                    }
                }
                ToastUtils.showToastSafe(ImproActivity.this, "导入" + count + "位");
            }
        });
    }

    private void selectNOALL() {
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                for (People peo : peos) {
                    peo.setFlag(false);
                }
                initView();
            }
        });
    }

    private void selectALL() {
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                for (People peo : peos) {
                    peo.setFlag(true);
                }
                initView();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, Menu.FIRST, 1, "全选");
        menu.add(1, Menu.FIRST + 1, 2, "全不选");
        menu.add(1, Menu.FIRST + 2, 3, "导入");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case Menu.FIRST:
                selectALL();
                break;
            case Menu.FIRST + 1:
                selectNOALL();
                break;
            case Menu.FIRST + 2:
                doImprotPeo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
    }

    //复写方法，响应权限请求的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    importContact();
                } else {
                    Toast.makeText(this, "需要联系人权限才可运行", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
