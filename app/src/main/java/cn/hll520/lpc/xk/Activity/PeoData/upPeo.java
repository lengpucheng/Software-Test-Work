package cn.hll520.lpc.xk.Activity.PeoData;

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
import cn.hll520.lpc.xk.R;
import cn.hll520.lpc.xk.data.People;
import cn.hll520.lpc.xk.provider.ContactProvider;

public class upPeo extends AppCompatActivity {

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
        peo = (People) getIntent().getSerializableExtra("peo");
        UpDataTile.setText("编辑联系人");
        dataSEX.setVisibility(View.VISIBLE);
        DATAID.setVisibility(View.VISIBLE);
        DATANAME.setVisibility(View.VISIBLE);
        DATAPASSINFO.setVisibility(View.GONE);
        DATAPASSWORD.setVisibility(View.GONE);
        upMydataID.setText(peo.getId());
        upMydataName.setText(peo.getName());
        if (peo.getGender().equals("男"))
            dataNan.setChecked(true);
        else
            dataNV.setChecked(true);
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
                upthePeo();
                break;
        }
    }

    private void upthePeo() {
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
            this.getContentResolver().update(ContactProvider.URI_CCONTACT, peo.getCv(),
                    "ID=?", new String[]{peo.getId()});
            Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
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