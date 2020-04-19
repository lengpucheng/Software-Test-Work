package cn.hll520.lpc.xk.Activity.PeoData;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import cn.hll520.lpc.xk.Activity.ContactActivity;
import cn.hll520.lpc.xk.R;
import cn.hll520.lpc.xk.Thread.ThreadUlits;
import cn.hll520.lpc.xk.data.People;
import cn.hll520.lpc.xk.provider.ContactProvider;
import cn.hll520.lpc.xk.uilt.PeoAdapter;

public class OutPeoActivity extends AppCompatActivity {

    @InjectView(R.id.peolIstTile)
    TextView peolIstTile;
    @InjectView(R.id.peolisttoolbar)
    Toolbar peolisttoolbar;
    @InjectView(R.id.secahvar)
    EditText secahvar;
    @InjectView(R.id.secah)
    LinearLayout secah;
    @InjectView(R.id.PeoList)
    ListView PeoList;
    CursorAdapter cursorAdapter;
    List<People> peos = new ArrayList<>();
    PeoAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peolist);
        ButterKnife.inject(this);
        peolIstTile.setText("导出联系人");
        secah.setVisibility(View.VISIBLE);
        initData(null, null);
        registerForContextMenu(PeoList);
        setSupportActionBar(peolisttoolbar);
    }


    private void initData(final String where, final String[] keys) {
        Cursor cursor = OutPeoActivity.this.getContentResolver().query(ContactProvider.URI_CCONTACT, null, where, keys, null);
        while (cursor.moveToNext()) {
            People peo = new People();
            peo.setForcursor(cursor);
            peo.setFlag(false);
            peos.add(peo);
        }
        initView();
    }


    private void initView() {
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                if (myAdapter == null)
                    myAdapter = new PeoAdapter(OutPeoActivity.this, peos);
                PeoList.setAdapter(myAdapter);
            }
        });
    }


    @OnClick({R.id.peolistBack, R.id.peoListMenu, R.id.PeolistMentOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.peolistBack:
                finish();
                break;
            case R.id.peoListMenu:
                openOptionsMenu();
                break;
            case R.id.PeolistMentOk:
                String where = "phone LIKE ? or name LIKE ?";
                String var = secahvar.getText().toString();
                if (secahvar.getText().toString().length() != 0)
                    initData(where, new String[]{var, var});
                else
                    initData(null, null);
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("导出操作");
        menu.add(Menu.NONE, Menu.FIRST, 1, "全选");
        menu.add(Menu.NONE, Menu.FIRST + 1, 2, "全不选");
        menu.add(Menu.NONE, Menu.FIRST + 2, 3, "导出");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, 1, "全选");
        menu.add(Menu.NONE, Menu.FIRST + 1, 2, "全不选");
        menu.add(Menu.NONE, Menu.FIRST + 2, 3, "导出");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                selectALL();
                break;
            case Menu.FIRST + 1:
                unselectALL();
                break;
            case Menu.FIRST + 2:
                doOutPeo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //上下文菜单选项事件处理
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                selectALL();
                break;
            case Menu.FIRST + 1:
                unselectALL();
                break;
            case Menu.FIRST + 2:
                doOutPeo();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void doOutPeo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //请求权限   1,请求的活动，2、权限，3、编号（相当于 msg.what）
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
        } else {
            peoTOphone();
        }
    }

    //写入到手机列表
    private void peoTOphone() {
        Context context = this;
        int Count = 0;
        for (People peo : peos) {
            if (peo.isFlag()) {
                ContentValues values = new ContentValues();
                Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
                long rawContactId = ContentUris.parseId(rawContactUri);
                values.clear();
                //插入raw_contacts表，并获取_id属性
                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
                ContentResolver resolver = context.getContentResolver();
                rawContactId = ContentUris.parseId(resolver.insert(uri, values));
                //插入data表
                uri = Uri.parse("content://com.android.contacts/data");
                //add Name
                values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/name");
                values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, peo.getName());
                resolver.insert(uri, values);
                values.clear();

                //写入手机号码

                values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                // 联系人的电话号码
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, peo.getPhone());
                // 电话类型
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                // 向联系人电话号码URI添加电话号码
                context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                values.clear();
                //写入Emial
                if (peo.getQQ() != null && peo.getQQ().length() != 0 && !peo.getQQ().contains("@"))
                    peo.setQQ(peo.getQQ() + "@qq.com");
                values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                // 联系人的Email地址
                values.put(ContactsContract.CommonDataKinds.Email.DATA, peo.getQQ());
                // 电子邮件的类型
                values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                // 向联系人Email URI添加Email数据


                //插入data表
                uri = Uri.parse("content://com.android.contacts/data");
                context.getContentResolver().insert(uri, values);
                Count++;
            }
        }
        Toast.makeText(context, "共添加" + Count + "条数据", Toast.LENGTH_SHORT).show();
    }

    private void unselectALL() {
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
    public void openOptionsMenu() {
        super.openOptionsMenu();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    peoTOphone();
                } else {
                    Toast.makeText(this, "需要联系人权限才可运行", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
