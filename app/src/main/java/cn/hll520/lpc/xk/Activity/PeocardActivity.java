package cn.hll520.lpc.xk.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.xk.R;
import cn.hll520.lpc.xk.data.People;
import cn.hll520.lpc.xk.data.User;

public class PeocardActivity extends AppCompatActivity {
    @InjectView(R.id.DataCardShar)
    ImageView DataCardShar;
    @InjectView(R.id.dataName)
    TextView dataName;
    @InjectView(R.id.dataMent)
    TextView dataMent;
    @InjectView(R.id.dataPost)
    TextView dataPost;
    @InjectView(R.id.dataPhone)
    TextView dataPhone;
    @InjectView(R.id.dataQQ)
    TextView dataQQ;
    @InjectView(R.id.DataCardBack)
    ImageView DataCardBack;
    @InjectView(R.id.dataID)
    TextView dataID;
    @InjectView(R.id.dataBith)
    TextView dataBith;
    @InjectView(R.id.dataAge)
    TextView dataAge;
    @InjectView(R.id.dataCampus)
    TextView dataCampus;
    @InjectView(R.id.dataCollage)
    TextView dataCollage;
    @InjectView(R.id.dataBanji)
    TextView dataBanji;
    @InjectView(R.id.dataHouse)
    TextView dataHouse;
    @InjectView(R.id.ToCall)
    Button ToCall;
    @InjectView(R.id.ToShort)
    Button ToShort;
    @InjectView(R.id.ToEmail)
    Button ToEmail;
    @InjectView(R.id.Toxx)
    Button Toxx;
    @InjectView(R.id.DataCradIng)
    ProgressBar DataCradIng;
    @InjectView(R.id.dataGender)
    TextView dataGender;
    @InjectView(R.id.PeoCardIMG)
    ImageView PeoCardIMG;
    private People peo;
    private Intent in;
    private Uri uri;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peocard);
        ButterKnife.inject(this);
        peo = (People) this.getIntent().getSerializableExtra("peo");
        user = (User) this.getIntent().getSerializableExtra("user");
        dataGender.setText(peo.getGender());
        dataID.setText(peo.getId());
        dataName.setText(peo.getName());
        dataMent.setText(peo.getMent());
        dataPost.setText(peo.getPosition());
        dataPhone.setText(peo.getPhone());
        dataQQ.setText(peo.getQQ());
        dataBith.setText(peo.getBirthday());
        dataCampus.setText(peo.getCampus());
        dataCollage.setText(peo.getCollege());
//        dataAge.setText();
        dataBanji.setText(peo.getBanji());
        dataHouse.setText(peo.getHouse());
        if(peo.getImg()!=null&&peo.getImg().length()!=0)
            switch (peo.getImg()){
                case "1":
                    PeoCardIMG.setImageResource(R.drawable.avatar);
                    break;
                case "2":
                    break;
                case "3":break;
                default:
                    Bitmap avatar = BitmapFactory.decodeFile(peo.getImg());
                    PeoCardIMG.setImageBitmap(avatar);
                    break;
            }
    }

    @OnClick({R.id.DataCardShar, R.id.DataCardBack, R.id.ToCall, R.id.ToShort, R.id.ToEmail, R.id.Toxx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.DataCardShar:
                in = new Intent(Intent.ACTION_SEND);
                in.setType("text/plain");
                in.putExtra(Intent.EXTRA_TEXT, "我通过" + getString(R.string.app_name) + "分享了" + peo.getName() + "'的信息\n电话：" + peo.getPhone() + "\nQQ:" + peo.getQQ());
                startActivity(Intent.createChooser(in, "来自" + getString(R.string.app_name) + "的分享"));
//                Toast.makeText(this, "分享功能维护中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.DataCardBack:
                finish();
                break;
            case R.id.ToCall:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //请求权限   1,请求的活动，2、权限，3、编号（相当于 msg.what）
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    String telstr = String.format("tel:%s", peo.getPhone());
                    uri = Uri.parse(telstr);
                    in = new Intent(Intent.ACTION_CALL, uri);
                    startActivity(in);
                }
                break;
            case R.id.ToShort:
                uri = Uri.parse("smsto:" + peo.getPhone());
                in = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(in);
                break;
            case R.id.ToEmail:
                if (peo.getQQ().contains("@"))
                    uri = Uri.parse("mailto:" + peo.getQQ() + "@qq.com");
                else
                    uri = Uri.parse("mailto:" + peo.getQQ());
                String[] email = {peo.getQQ() + "@qq.com"};
                in = new Intent(Intent.ACTION_SEND, uri);
                in.setType("message/rfc822"); // 设置邮件格式
                in.putExtra(Intent.EXTRA_EMAIL, email); // 接收人
                in.putExtra(Intent.EXTRA_TEXT, "\n来自WTU客户端的：" + user.getUsername()); // 正文
                startActivity(Intent.createChooser(in, "请选择邮件类应用"));
                break;
            case R.id.Toxx:
                in = new Intent(this, ChatActivity.class);
                in.putExtra("id", peo.getId());
                startActivity(in);
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String telstr = String.format("tel:%s", peo.getPhone());
                    uri = Uri.parse(telstr);
                    in = new Intent(Intent.ACTION_CALL, uri);
                    startActivity(in);
                } else {
                    Toast.makeText(this, "需要呼叫权限才能拨打电话哦", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}