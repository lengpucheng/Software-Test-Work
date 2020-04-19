package cn.hll520.lpc.xk.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.xk.R;
import cn.hll520.lpc.xk.Thread.ThreadUlits;
import cn.hll520.lpc.xk.data.InfoCard;
import cn.hll520.lpc.xk.data.People;
import cn.hll520.lpc.xk.provider.ContactProvider;
import cn.hll520.lpc.xk.provider.InfoListProvider;

public class infolistActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.My)
    ImageView My;
    @InjectView(R.id.add)
    ImageView add;
    @InjectView(R.id.PeopleING)
    LinearLayout PeopleING;
    @InjectView(R.id.infoCardList)
    ListView infoCardList;
    @InjectView(R.id.info)
    LinearLayout info;
    @InjectView(R.id.people)
    LinearLayout people;
    @InjectView(R.id.classTab)
    LinearLayout classTab;
    Intent in=new Intent();

    MyCoatObsever coatObsever = new MyCoatObsever(new Handler());
    CursorAdapter cursorAdapter;
    Cursor currentCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infolist);
        ButterKnife.inject(this);
        //加载数据
        initData();
        initListener();
        infoCardList.setOnItemClickListener(this);
    }

    //绑定事件
    private void initListener() {
        //绑定上下文菜单
        registerForContextMenu(infoCardList);
        //绑定监听
        getContentResolver().registerContentObserver(InfoListProvider.URI_CCONTACT, true, coatObsever);
        //获取被选中的card
        infoCardList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentCursor = (Cursor) parent.getItemAtPosition(position);
                return false;
            }
        });
    }

    //activity被关闭时所执行
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定
        getContentResolver().unregisterContentObserver(coatObsever);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Intent intent;
        intent = new Intent(this,ContactActivity.class);
        startActivity(intent);
        return super.onKeyDown(keyCode, event);
    }

    //初始化
    private void initData() {
        ThreadUlits.runInUIThread(new Runnable() {
            @Override
            public void run() {
                if (cursorAdapter != null) {
                    cursorAdapter.getCursor().requery();
                    return;
                }
                Cursor cursor = getContentResolver().query(InfoListProvider.URI_CCONTACT, null, null, null, null);
                if (cursor.getCount() == 0)
                    return;
                cursorAdapter = new CursorAdapter(infolistActivity.this, cursor, true) {
                    @Override
                    public View newView(Context context, Cursor cursor, ViewGroup parent) {
                        //把视图与布局文件绑定
                        View view = View.inflate(context, R.layout.infocord, null);
                        return view;
                    }

                    @Override
                    public void bindView(View view, Context context, Cursor cursor) {
                        //把组件与数据绑定
                        InfoCard card=InfoCard.getInfoCardByCu(cursor);
                        TextView nameTV=view.findViewById(R.id.InfoCardName);
                        TextView varTV=view.findViewById(R.id.infoCardVar);
                        TextView newsTV=view.findViewById(R.id.infosum);
                        TextView timeTV=view.findViewById(R.id.infoCardtiem);
                        People peo=new People();
                        peo.index(getContentResolver().query(ContactProvider.URI_CCONTACT,null,
                                "id=?",new String[]{card.getId()},null));
                        nameTV.setText(peo.getName());
                        varTV.setText(card.getVar());
                        newsTV.setText(card.getNews()+"");
                        timeTV.setText(card.getTime());
                        if(card.getNews()==0)
                            newsTV.setVisibility(View.INVISIBLE);
                    }
                };
                infoCardList.setAdapter(cursorAdapter);
            }
        });
    }

    //组件点击触发
    @OnClick({R.id.My, R.id.add, R.id.info, R.id.people, R.id.classTab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.My:
                in = new Intent(this, MyActivity.class);
                startActivity(in);
                break;
            case R.id.add:
                Toast.makeText(this, "搜索功能维护中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.people:
                in = new Intent(this, ContactActivity.class);
                startActivity(in);
                finish();
                break;
            case R.id.classTab:
//                Toast.makeText(this, "课表功能维护中", Toast.LENGTH_SHORT).show();
                in = new Intent(this, MyActivity.class);
                startActivity(in);
                break;
            case R.id.info:
                break;
        }
    }

    //当listview卡片被单击时
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ChatActivity.class);
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        InfoCard card=InfoCard.getInfoCardByCu(cursor);
        intent.putExtra("id", card.getId());
        card.setNews(0);
        card.upData(this);
        startActivity(intent);
    }

    //创建ContextMenu(长按菜单）
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("请选择操作");
        //不分组，第一项，顺序，名称
        menu.add(Menu.NONE, Menu.FIRST, 1, "删除");
        menu.add(Menu.NONE, Menu.FIRST + 1, 2, "标记未读");
        menu.add(Menu.NONE, Menu.FIRST + 2, 3, "清空列表");
    }

    //长按菜单选项触发
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case Menu.FIRST:
                InfoCard card=InfoCard.getInfoCardByCu(currentCursor);
                this.getContentResolver().delete(InfoListProvider.URI_CCONTACT,"ID=?",new String[]{card.getId()});
                break;
            case Menu.FIRST+1:
                InfoCard card1=InfoCard.getInfoCardByCu(currentCursor);
                card1.setNews(card1.getNews()+1);
                this.getContentResolver().update(InfoListProvider.URI_CCONTACT,card1.getCv(),"ID=?",new String[]{card1.getId()});
                break;
            case Menu.FIRST+2:
                this.getContentResolver().delete(InfoListProvider.URI_CCONTACT,null,null);
                break;
        }
        return super.onContextItemSelected(item);
    }


    //创建观察者监听数据库
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
