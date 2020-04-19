package cn.hll520.lpc.xk.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;
import java.util.List;

public class People implements Serializable {
    private String id = "0000000000";
    private String name = "保密哦";
    private String gender = "男";
    private String phone = "00000";
    private String birthday = "1958-10-18";
    private String QQ = "00000";
    private String ment = "团员";
    private String position = "学生";
    private String campus="未设置";
    private String college="未设置";
    private String banji="保密";
    private String house="保密";
    private String room="保密";
    private String img="1";
    private boolean flag = false;
    //数据集合

    public People() { }

    public People(String id) {
        this.id = id;
    }

    public void setFlag(boolean flag) { this.flag = flag; }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public void setMent(String ment) {
        this.ment = ment;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setBanji(String banji) {
        this.banji = banji;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public void setRoom(String house) {
        this.house = house;
    }

    public void setImg(String Img) {
        this.img = img;
    }

    public void setForCV(ContentValues cv) {
        id = cv.getAsString("id");
        name = cv.getAsString("name");
        gender = cv.getAsString("gender");
        phone = cv.getAsString("phone");
        birthday = cv.getAsString("birthday");
        QQ = cv.getAsString("QQ");
        ment = cv.getAsString("ment");
        position = cv.getAsString("position");
        campus = cv.getAsString("campus");
        college = cv.getAsString("college");
        banji = cv.getAsString("banji");
        house = cv.getAsString("house");
        room = cv.getAsString("room");
        img = cv.getAsString("img");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getQQ() {
        return QQ;
    }

    public String getMent() {
        return ment;
    }

    public String getPosition() {
        return position;
    }

    public String getCampus() {
        return campus;
    }

    public String getCollege() {
        return college;
    }

    public String getBanji() {
        return banji;
    }

    public String getHouse() {
        return house;
    }

    public String getRoom() {
        return room;
    }

    public String getImg() {
        return img;
    }

    public boolean isFlag() { return flag; }

    @Override
    public String toString() {
        return "People{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", birthday='" + birthday + '\'' +
                ", QQ='" + QQ + '\'' +
                ", ment='" + ment + '\'' +
                ", position='" + position + '\'' +
                ", campus='" + campus + '\'' +
                ", college='" + college + '\'' +
                ", banji='" + banji + '\'' +
                ", house='" + house + '\'' +
                ", room='" + room + '\'' +
                ", img='" + img + '\'' +
                ", flag=" + flag +
                '}';
    }

    public ContentValues getCv() {
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("name", name);
        cv.put("gender", gender);
        cv.put("phone", phone);
        cv.put("birthday", birthday);
        cv.put("QQ", QQ);
        cv.put("ment", ment);
        cv.put("position", position);
        cv.put("campus", campus);
        cv.put("college", college);
        cv.put("banji", banji);
        cv.put("house", house);
        cv.put("room", room);
        cv.put("img", img);
        return cv;
    }

    public void setForcursor(Cursor cursor) {
        id = cursor.getString(1);
        name = cursor.getString(2);
        gender = cursor.getString(3);
        phone = cursor.getString(4);
        birthday = cursor.getString(5);
        QQ = cursor.getString(6);
        ment = cursor.getString(7);
        position = cursor.getString(8);
        campus = cursor.getString(9);
        college = cursor.getString(10);
        banji = cursor.getString(11);
        house = cursor.getString(12);
        room = cursor.getString(13);
        img = cursor.getString(14);
    }

    public void index(Cursor cursor) {
        while (cursor.moveToNext()) {
            id = cursor.getString(1);
            name = cursor.getString(2);
            gender = cursor.getString(3);
            phone = cursor.getString(4);
            birthday = cursor.getString(5);
            QQ = cursor.getString(6);
            ment = cursor.getString(7);
            position = cursor.getString(8);
            campus = cursor.getString(9);
            college = cursor.getString(10);
            banji = cursor.getString(11);
            house = cursor.getString(12);
            room = cursor.getString(13);
            img = cursor.getString(14);
        }
    }

    public static String Check(People peo) {
        String flag = "true";
        if (!peo.getPhone().matches("^[1][3,4,5,7,8][0-9]{9}$"))
            flag = "手机号码格式不正确";
        if (!peo.getQQ().matches("[1-9][0-9]+"))
            flag = "QQ号码格式不正确";
        return flag;
    }
}
