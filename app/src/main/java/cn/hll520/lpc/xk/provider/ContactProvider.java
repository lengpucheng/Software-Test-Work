package cn.hll520.lpc.xk.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.hll520.lpc.xk.data.WtuOpenHelper;

//内容提供器
public class ContactProvider extends ContentProvider {
    public static final String AUTHORITIES=ContactProvider.class.getCanonicalName();
    private WtuOpenHelper openHelper;
    //统一资源标识匹配器
    static UriMatcher uriMatcher;
    public static final Uri URI_CCONTACT=Uri.parse("content://"+AUTHORITIES+"/wtuxk");
    public static final int CONTACT = 1;
    //静态方法块 无论new多少次 都只执行一次
    static {
        //匹配路径，匹配类型，返回
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITIES, "/wtuxk", CONTACT);
    }
    //是否创建
    @Override
    public boolean onCreate() {
        openHelper = new WtuOpenHelper(getContext());
        if (openHelper != null)
            return true;
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] colmus, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //查询内容   Cursor内容集
        int match = uriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case CONTACT:
                //获取数据库对象,可读
                SQLiteDatabase db = openHelper.getReadableDatabase();
                //表名，返回需要那些字段，查询语句，查询参数，是否分组，having，排序
                cursor = db.query(WtuOpenHelper.T_STUDENT, colmus, selection, selectionArgs, null, null, sortOrder);
                Log.i("msc", "查询成功");
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues cv) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case CONTACT:
                //获取数据库对象，可写
                SQLiteDatabase db = openHelper.getWritableDatabase();
                long id = db.insert(WtuOpenHelper.T_STUDENT, "", cv);
                if (id != -1) {
                    Log.i("msc", "插入成功！");
                    //拼接生成新的URI
                    uri = ContentUris.withAppendedId(uri, id);
                    //
                    getContext().getContentResolver().notifyChange(ContactProvider.URI_CCONTACT,null);
                }
                break;
            default:
                break;
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] whereArgs) {
        int match = uriMatcher.match(uri);
        int deletecount = 0;
        switch (match) {
            case CONTACT:
                //获取数据库对象，可写
                SQLiteDatabase db = openHelper.getWritableDatabase();
                deletecount = db.delete(WtuOpenHelper.T_STUDENT, whereClause, whereArgs);
                if (deletecount > 0){
                    Log.i("msc", "删除成功!");
                getContext().getContentResolver().notifyChange(ContactProvider.URI_CCONTACT,null);
                }
                break;
            default:
                break;
        }
        return deletecount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues cv, @Nullable String whereClause, @Nullable String[] whereArgs) {
        int match = uriMatcher.match(uri);
        int updatecount = 0;
        switch (match) {
            case CONTACT:
                //获取数据库对象，可写
                SQLiteDatabase db = openHelper.getWritableDatabase();
                updatecount = db.update(WtuOpenHelper.T_STUDENT, cv, whereClause, whereArgs);
                if (updatecount > 0){
                    Log.i("msc", "更新成功");
                getContext().getContentResolver().notifyChange(ContactProvider.URI_CCONTACT,null);}
                break;
            default:
                break;
        }
        return updatecount;
    }
}
