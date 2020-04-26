package cn.hll520.lpc.SoftwareTest.Activity.PeoData;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.hll520.lpc.SoftwareTest.R;
import cn.hll520.lpc.SoftwareTest.data.People;
import cn.hll520.lpc.SoftwareTest.provider.ContactProvider;

public class NewPeoActivity extends AppCompatActivity {

    @InjectView(R.id.upmydata_back)
    ImageView upmydataBack;
    @InjectView(R.id.UpDataTile)
    TextView UpDataTile;
    @InjectView(R.id.Up_Mydata_IMG)
    ImageView UpMydataIMG;
    @InjectView(R.id.up_mydata_ID)
    EditText upMydataID;
    @InjectView(R.id.DATAID)
    LinearLayout DATAID;
    @InjectView(R.id.up_mydata_name)
    EditText upMydataName;
    @InjectView(R.id.DATANAME)
    LinearLayout DATANAME;
    @InjectView(R.id.dataNan)
    RadioButton dataNan;
    @InjectView(R.id.dataNV)
    RadioButton dataNV;
    @InjectView(R.id.dataSEX)
    LinearLayout dataSEX;
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
    @InjectView(R.id.DATAPASSINFO)
    LinearLayout DATAPASSINFO;
    @InjectView(R.id.DATAPASSWORD)
    LinearLayout DATAPASSWORD;
    @InjectView(R.id.button)
    Button button;
    @InjectView(R.id.up_mydata_banji)
    Spinner upMydataBanji;
    private boolean flag = false;
    People peo = new People();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_mydata);
        ButterKnife.inject(this);
        UpDataTile.setText("新建联系人");
        dataSEX.setVisibility(View.VISIBLE);
        DATAID.setVisibility(View.VISIBLE);
        DATANAME.setVisibility(View.VISIBLE);
        DATAPASSINFO.setVisibility(View.GONE);
        DATAPASSWORD.setVisibility(View.GONE);
    }

    @OnClick({R.id.upmydata_back, R.id.Up_Mydata_IMG, R.id.Upmydata_day, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.upmydata_back:
                finish();
                break;
            case R.id.Up_Mydata_IMG:
                Toast.makeText(this, "暂时不能设置头像", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Upmydata_day:
                if (!flag) {
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
                addPeo();
                break;
        }
    }

    private void addPeo() {
        if (check()) {
            peo.setId(upMydataID.getText().toString());
            peo.setName(upMydataName.getText().toString());
            if (dataNan.isChecked())
                peo.setGender("男");
            if (dataNV.isChecked())
                peo.setGender("女");
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
            if (getContentResolver().query(ContactProvider.URI_CCONTACT, null,
                    "ID=?", new String[]{peo.getId()}, null).getCount() != 0) {
                Toast.makeText(this, "联系人已经存在", Toast.LENGTH_SHORT).show();
                return;
            }
            this.getContentResolver().insert(ContactProvider.URI_CCONTACT, peo.getCv());
            Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean check() {
        if (upMydataID.getText().toString().length() == 0) {
            Toast.makeText(this, "ID不能为空", Toast.LENGTH_SHORT).show();
            upMydataID.setError("ID不能为空");
            return false;
        }

        if (upMydataHouse.getText().toString().length() == 0) {
            Toast.makeText(this, "宿舍不能为空！", Toast.LENGTH_SHORT).show();
            upMydataHouse.setError("宿舍不能为空！");
            return false;
        }

        if (upMydataName.getText().toString().length() == 0) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            upMydataName.setError("姓名不能为空！");
            return false;
        }

        if (flag) {
            Toast.makeText(this, "请再次点击日期栏关闭日历确定生日！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
