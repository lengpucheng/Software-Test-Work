package cn.hll520.lpc.xk.Activity.kcb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.xk.Activity.ContactActivity;
import cn.hll520.lpc.xk.Activity.MyActivity;
import cn.hll520.lpc.xk.Activity.infolistActivity;
import cn.hll520.lpc.xk.R;
import cn.hll520.lpc.xk.Thread.ThreadUlits;
import cn.hll520.lpc.xk.data.Course;

public class KcbActivity extends AppCompatActivity {
    public static final String KCB = "KCB";

    Intent in;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(KcbActivity.this, LoginKcbActivity.class);

                    startActivity(intent);
                    break;
            }
        }
    };
    @InjectView(R.id.My)
    ImageView My;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.reset)
    ImageView reset;
    @InjectView(R.id.weekNums)
    TextView weekNums;
    @InjectView(R.id.okWeek)
    LinearLayout okWeek;
    @InjectView(R.id.weekOne)
    TextView weekOne;
    @InjectView(R.id.weekTwo)
    TextView weekTwo;
    @InjectView(R.id.weekSan)
    TextView weekSan;
    @InjectView(R.id.weekSi)
    TextView weekSi;
    @InjectView(R.id.weekWU)
    TextView weekWU;
    @InjectView(R.id.weekLiu)
    TextView weekLiu;
    @InjectView(R.id.weekDAY)
    TextView weekDAY;
    @InjectView(R.id.Top)
    LinearLayout Top;
    @InjectView(R.id.textView16)
    TextView textView16;
    @InjectView(R.id.textView17)
    TextView textView17;
    @InjectView(R.id.textView18)
    TextView textView18;
    @InjectView(R.id.textView19)
    TextView textView19;
    @InjectView(R.id.textView20)
    TextView textView20;
    @InjectView(R.id.textView22)
    TextView textView22;
    @InjectView(R.id.textView21)
    TextView textView21;
    @InjectView(R.id.textView23)
    TextView textView23;
    @InjectView(R.id.textView24)
    TextView textView24;
    @InjectView(R.id.textView25)
    TextView textView25;
    @InjectView(R.id.textView27)
    TextView textView27;
    @InjectView(R.id.textView28)
    TextView textView28;
    @InjectView(R.id.PFF)
    FrameLayout PFF;
    @InjectView(R.id.JCK)
    LinearLayout JCK;
    @InjectView(R.id.body)
    ScrollView body;
    @InjectView(R.id.imageView7)
    ImageView imageView7;
    @InjectView(R.id.textView3)
    TextView textView3;
    @InjectView(R.id.info)
    LinearLayout info;
    @InjectView(R.id.imageView8)
    ImageView imageView8;
    @InjectView(R.id.textView4)
    TextView textView4;
    @InjectView(R.id.people)
    LinearLayout people;
    @InjectView(R.id.imageView9)
    ImageView imageView9;
    @InjectView(R.id.textView9)
    TextView textView9;
    @InjectView(R.id.classTab)
    LinearLayout classTab;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kcb);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        //创建一个sharedpreferences对象（三个字段，flag是否设置周数，week设置第几周，data设置时的时间
        SharedPreferences.Editor editor = getSharedPreferences(KCB, MODE_PRIVATE).edit();
        editor.apply();
        iniView();

        okWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置周数
                setWeek();
            }
        });
        classTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    private void setWeek() {
        final Spinner spinner = new Spinner(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        for (int i = 1; i < 32; i++) // Maximum size of i upto --> Your Array Size
        {
            dataAdapter.add("第" + i + "周");
        }
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0, true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("设置当前周数").setView(spinner).setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int week = Integer.parseInt(spinner.getSelectedItem().toString().replaceAll("[^\\d]", ""));
                SharedPreferences.Editor editor = getSharedPreferences(KCB, MODE_PRIVATE).edit();
                editor.putBoolean("flag", true);
                editor.putInt("week", week);
                editor.putInt("year", Calendar.getInstance().get(Calendar.YEAR));
                editor.putInt("math", Calendar.getInstance().get(Calendar.MONTH));
                editor.putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                editor.apply();
                Toast.makeText(KcbActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                reView();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(KcbActivity.this, "取消设置", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    //刷新界面
    private void reView() {
        finish();
        Intent intent = new Intent(KcbActivity.this, KcbActivity.class);
        startActivity(intent);
    }


    //加载数据
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void iniView() {
        //获取屏幕宽度，平均为8
        int d = getWindowManager().getDefaultDisplay().getWidth() / 8;
        TextView[] weekTexts = {weekOne, weekTwo, weekSan, weekSi, weekWU, weekLiu, weekDAY};
        //获取当前周数
        int weeks = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        //打开SP
        SharedPreferences sp = getSharedPreferences(KCB, MODE_PRIVATE);
        //判断是否设置周数
        if (sp.getBoolean("flag", false)) {
            //读取设置时候的,如果没有就是weeks,默认为1
            int theWeek = sp.getInt("week", 1);
            //读取设置时候的日期
            int y = sp.getInt("year", 2020);
            int m = sp.getInt("math", 3);
            int day = sp.getInt("day", 3);
            //读取设置时候的日期
            Calendar calendar = new GregorianCalendar(y, m, day);//日期对象
            //获取设置那天的周数
            int theWeekofY = calendar.get(Calendar.WEEK_OF_YEAR);
            Log.d("TAGG", "iniView: " + theWeekofY);
            //得到当前周数
            weeks = (weeks - theWeekofY) + theWeek;
        }


        weekNums.setText(weeks + "周");

        //获取当前周几，转换为中国周（周一为第一天）
        int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        //设置日期
        for (int i = 0; i < 7; i++)
            weekTexts[i].append("\n" + getDate(i - weekday));

        //设置当天的背景为有颜色
        TextView textNow = new TextView(this);
        LinearLayout.LayoutParams paramNow = new LinearLayout.LayoutParams(d, dip2px(14 * 65));
        textNow.setBackgroundColor(0x40E91E63);
        paramNow.setMargins((weekday - 1) * d, 0, 0, 0);
        textNow.setLayoutParams(paramNow);
        PFF.addView(textNow);


        Cursor cursor = getContentResolver().query(CourseProvider.URI_COURSE, null, null, null, null);
        List<Course> courses = Course.getCourses(cursor);
        if (courses.size() < 2) {
            Toast.makeText(this, "发现到课表仅" + courses.size() + "节课，可能是教务系统还未更新，建议重新导入", Toast.LENGTH_LONG).show();
        }
        int i = 1;
        for (final Course course : courses) {
            //新建文本
            final TextView textView = new TextView(this);
            //设置颜色
            i++;
            switch (i % 5) {
                case 0:
                    textView.setBackgroundColor(0xffCDDC39);
                    break;
                case 1:
                    textView.setBackgroundColor(0xff22ccff);
                    break;
                case 2:
                    textView.setBackgroundColor(0xff22ffcc);
                    break;
                case 3:
                    textView.setBackgroundColor(0xff00BCD4);
                    break;
                case 4:
                    textView.setBackgroundColor(0xffF44336);
                    break;
            }
            //显示字符串
            String str = "";

            //获取当前课的周期,转换为纯数字和-，按-分隔
            int MIN = course.getwMin();
            int MAX = course.getwMax();
            if (MIN > weeks || weeks > MAX) {
                str += "[非本周]";
                //设置灰色
                textView.setBackgroundColor(0xcccccccc);
            }
            str += course.getName();
            str += "@" + course.getRoom();
            str += "#" + course.getTeacher() + course.getJob();
            str += "|" + course.getTest();
            textView.setText(str);
            //设置高度
            MIN = course.gettMin();
            MAX = course.gettMax();
            int height = MAX - MIN + 1;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(d, dip2px(height * 65));
            //设置位置
            int week = 6;
            switch (course.getWeek()) {
                case "星期一":
                    week = 0;
                    break;
                case "星期二":
                    week = 1;
                    break;
                case "星期三":
                    week = 2;
                    break;
                case "星期四":
                    week = 3;
                    break;
                case "星期五":
                    week = 4;
                    break;
                case "星期六":
                    week = 5;
                    break;
                case "星期七":
                    week = 6;
                    break;
                default:
                    break;
            }
            //左边-星期，  上边-节
            params.setMargins(week * d, dip2px((MIN - 1) * 65), 0, 0);
            textView.setLayoutParams(params);
            textView.setTextSize(12);
            textView.setTextColor(Color.WHITE);
            //结尾显示省略号
            textView.setEllipsize(TextUtils.TruncateAt.END);


            //添加到容器
            PFF.addView(textView);


            //添加点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(KcbActivity.this).setTitle(course.getName()).setMessage(course.showInfo())
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(KcbActivity.this, "认真听讲别上课摸鱼哦(･∀･)", Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("编辑", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(KcbActivity.this, EditCourseActivity.class);
                                    intent.putExtra("course", course);
                                    startActivity(intent);
                                }
                            }).setNeutralButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    del(course);
                                }
                            });
                    builder.create().show();
                }
            });
        }
    }

    private void del(final Course course) {
        AlertDialog.Builder check = new AlertDialog.Builder(KcbActivity.this).setTitle("确定删除Σ(ﾟдﾟ;)？")
                .setPositiveButton("我点错了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(KcbActivity.this, "哈哈！每个人都有手滑的时候\n已经帮你取消删除啦ε=ε=(ノ≧∇≦)ノ", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("确定删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContentResolver().delete(CourseProvider.URI_COURSE, "_id=?", new String[]{course.getId()});
                        Toast.makeText(KcbActivity.this, "删除成功（￣▽￣）", Toast.LENGTH_SHORT).show();
                        reView();
                    }
                });
        check.create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getDate(int day) {
        Date date = new Date();//取当前时间
        Calendar calendar = new GregorianCalendar();//日期对象
        calendar.setTime(date);//日期设置为当前时间
        calendar.add(calendar.DATE, day + 1);//往后推一天.整数往后推,负数往前移动（Calendar为0开始，故推一天为今天）
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");//设置日期格式
        String Day = formatter.format(date);
        return Day;
    }

    //dp转px
    public int dip2px(float dpValue) {
        Context context = this;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Intent intent;
        intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.My, R.id.info, R.id.people, R.id.reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.My:
                in = new Intent(this, MyActivity.class);
                startActivity(in);
                break;
            case R.id.info:
                in = new Intent(this, infolistActivity.class);
                startActivity(in);
                finish();
                break;
            case R.id.people:
                in = new Intent(this, ContactActivity.class);
                startActivity(in);
                finish();
                break;
            case R.id.reset:
                openOptionsMenu();
                break;
        }
    }


    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, 1, "导入课表");
        menu.add(Menu.NONE, Menu.FIRST + 1, 2, "添加课程");
        menu.add(Menu.NONE, Menu.FIRST + 2, 3, "设置周数");
        menu.add(Menu.NONE, Menu.FIRST + 3, 4, "关于课表");
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                AlertDialog.Builder Imp = new AlertDialog.Builder(this).setTitle("注意！")
                        .setMessage("重新导入课表会自动从教务系统获取课表，但会清空目前课程表中的所有课程，确定导入吗？")
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ThreadUlits.runInThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getContentResolver().delete(CourseProvider.URI_COURSE, null, null);
                                        Message msg = new Message();
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                    }
                                });
                            }
                        });
                Imp.create().show();
                break;
            case Menu.FIRST + 1:
                Intent add = new Intent(KcbActivity.this, EditCourseActivity.class);
                startActivity(add);
                break;
            case Menu.FIRST + 2:
                setWeek();
                break;
            case Menu.FIRST + 3:
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("关于课表").setIcon(R.mipmap.wtu)
                        .setMessage("课表模块版本：v2.4\n开发者：lengPucheng\n隐私说明：" +
                                "学号和密码仅用于登录教务系统获取课表，软件不会收集和保存任何关于使用者的账号信息，所有操作均在本地完成，不会链接除教务系统以外的" +
                                "任何服务器，请放心使用\n如果觉得有用的话把我分享给更多小伙伴或者左下角三连支持一波（⌒▽⌒）").setPositiveButton("知道啦", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(KcbActivity.this, "别忘记把我分享给朋友们哦（￣▽￣）", Toast.LENGTH_SHORT).show();
                            }
                        }).setNeutralButton("支持一下", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //支付宝跳转
                                String URL_FORMAT = "intent://platformapi/startapp?saId=10000007&" +
                                        "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Ffkx12146ctg0bm8jgtesvdc%3F_s" +
                                        "%3Dweb-other&_t=1472443966571#Intent;" + "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";
                                try {
                                    Intent intent = Intent.parseUri(URL_FORMAT, Intent.URI_INTENT_SCHEME);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://qr.alipay.com/fkx12146ctg0bm8jgtesvdc"));
                                    startActivity(intent);
                                }
                            }
                        }).setNeutralButtonIcon(getDrawable(R.mipmap.wtu));
                builder.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}