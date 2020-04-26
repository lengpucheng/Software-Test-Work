package cn.hll520.lpc.SoftwareTest.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import cn.hll520.lpc.SoftwareTest.data.People;

public class ContactProviderTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    People peo = new People();

    @Test
    public void query() {
        Cursor cursor = appContext.getContentResolver().query(ContactProvider.URI_CCONTACT, null, null, null, null);
        int comus = cursor.getColumnCount();
        while (cursor.moveToNext()) {
            People peos = new People();
            peos.setForcursor(cursor);
            Log.i("msc", peos.toString());
        }
    }

    @Test
    public void insert() {
        peo.setId("1704270198");
        peo.setName("LLP");
        peo.setGender("na");
        ContentValues cv = peo.getCv();
        appContext.getContentResolver().insert(ContactProvider.URI_CCONTACT, cv);
    }

    @Test
    public void delete() {
        appContext.getContentResolver().delete(ContactProvider.URI_CCONTACT, null, null);
    }

    @Test
    public void update() {
        peo.setName("LPC");
        peo.setBirthday("1999-05-27");
        peo.setGender("男");
        appContext.getContentResolver().update(ContactProvider.URI_CCONTACT,peo.getCv(),"id=?",new String[]{"1704270128"});
    }
}