package cn.hll520.lpc.wtu.provider;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cn.hll520.lpc.xk.data.InfoCard;
import cn.hll520.lpc.xk.provider.InfoListProvider;

public class InfoListProviderTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    @Test
    public void query() {
//       Cursor cursor=appContext.getContentResolver().query(InfoListProvider.URI_CCONTACT,null,null,null,null);
//        while (cursor.moveToNext()){
//            Log.i("msc",cursor.getString(1));
//        }
        List<InfoCard> cards=InfoCard.getInfoCardList(appContext);
        for(int i=0;i<cards.size();i++){
        Log.i("msc",cards.get(i).toString());
        }
    }

    @Test
    public void insert() {
        InfoCard infoCard=new InfoCard();
        infoCard.setId("1704270128");
        infoCard.setCount(12);
        infoCard.setNews(199);
        infoCard.setVar("我说这是快捷方式打开附件零件联赛附加赛两地分居15个");
        infoCard.setTime("2019-02-31 22:22:40");
        appContext.getContentResolver().insert(InfoListProvider.URI_CCONTACT,infoCard.getCv());
    }

    @Test
    public void delete() {
        appContext.getContentResolver().delete(InfoListProvider.URI_CCONTACT,null,null);
    }

    @Test
    public void update() {
        List<InfoCard> cards=new ArrayList<>();
        cards=InfoCard.getInfoCardList(appContext);
        for(int i=0;i<cards.size();i++){
            cards.get(i).setTime("2020-04-01 04:01:44");
            cards.get(i).upData(appContext);
        }
    }
}