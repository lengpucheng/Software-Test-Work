package cn.hll520.lpc.wtu.provider;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import cn.hll520.lpc.xk.data.User;
import cn.hll520.lpc.xk.provider.UserProvider;

public class UserProviderTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    User user=new User();
    @Test
    public void query() {
        Cursor cursor = appContext.getContentResolver().query(UserProvider.URI_CCONTACT, null, null, null, null);
        while (cursor.moveToNext()) {
           user.setForCursor(cursor);
            Log.i("msc", user.toString());
        }
    }

    @Test
    public void insert() {
        user=new User();
        user.setID("801");
        user.setLogin(1);
        appContext.getContentResolver().insert(UserProvider.URI_CCONTACT,user.getCv());
    }

    @Test
    public void delete() {
        appContext.getContentResolver().delete(UserProvider.URI_CCONTACT,null,null);
    }

    @Test
    public void update() {
        user=User.getLoginUser(appContext);
        user.setID("10010");
        user.setLogin(1);
        appContext.getContentResolver().update(UserProvider.URI_CCONTACT,user.getCv(),null,null);
    }
}