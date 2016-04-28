package com.jhlc.km.sb.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.constants.DbConstants;
import com.jhlc.km.sb.model.CategoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * SqliteUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-10-21
 */
public class SqliteUtils {

//    private static volatile SqliteUtils instance;

    private DbHelper                    dbHelper;
    private SQLiteDatabase              db;

    public SqliteUtils(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

//    public static SqliteUtils getInstance(Context context) {
//        if (instance == null) {
//            synchronized (SqliteUtils.class) {
//                if (instance == null) {
//                    instance = new SqliteUtils(context);
//                }
//            }
//        }
//        return instance;
//    }

    public SQLiteDatabase getDb() {
        return db;
    }

    /**
     * 插入分类信息
     * @param id 分类id
     * @param name 分类名称
     */
    public void insertCategory(String id, String name){
        ContentValues cv = new ContentValues();
        cv.put(DbConstants.ANTIQUE_CATEGORY_TABLE_CATEGORY_ID,id);
        cv.put(DbConstants.ANTIQUE_CATEGORY_TABLE_CATEGORY_NAME,name);
        db.insert(DbConstants.ANTIQUE_CATEGORY_TABLE_TABLE_NAME,null,cv);
    }

    /**
     * 根据id获取name
     * @param id
     * @return
     */
    public String getCategoryName(String id){
        String categoryname = null;
        String sql = "select " + DbConstants.ANTIQUE_CATEGORY_TABLE_CATEGORY_NAME + " from " +
                DbConstants.ANTIQUE_CATEGORY_TABLE_TABLE_NAME+" where " + DbConstants.ANTIQUE_CATEGORY_TABLE_CATEGORY_ID
                + "=?";
        Cursor c = db.rawQuery(sql,new String[]{id});
        if (c.moveToFirst()){
            categoryname = c.getString(c.getColumnIndex(DbConstants.ANTIQUE_CATEGORY_TABLE_CATEGORY_ID));
        }else {
            return null;
        }
        if(!c.isClosed()){
            c.close();
        }
        return categoryname;
    }

    /**
     * 根据name获取id
     * @param name
     * @return
     */
    public String getCategoryId(String name){
        String categoryid = null;
        String sql = "select " + DbConstants.ANTIQUE_CATEGORY_TABLE_CATEGORY_ID + " from " +
                DbConstants.ANTIQUE_CATEGORY_TABLE_TABLE_NAME+" where " + DbConstants.ANTIQUE_CATEGORY_TABLE_CATEGORY_NAME
                + "=?";
        Cursor c = db.rawQuery(sql,new String[]{name});
        if (c.moveToFirst()){
            categoryid = c.getString(c.getColumnIndex(DbConstants.ANTIQUE_CATEGORY_TABLE_CATEGORY_ID));
        }else {
            return null;
        }
        if(!c.isClosed()){
            c.close();
        }
        return categoryid;
    }

    /**
     * 获取所有古玩分类信息
     * @return
     */
    public List<CategoryBean> getCategoryList(){
        List<CategoryBean> list = new ArrayList<>();
        String sql = "select * from " + DbConstants.ANTIQUE_CATEGORY_TABLE_TABLE_NAME;
        Cursor c = db.rawQuery(sql,null);
        while (c.moveToNext()){
            CategoryBean bean = new CategoryBean();
            bean.setId(c.getString(c.getColumnIndex(DbConstants.ANTIQUE_CATEGORY_TABLE_CATEGORY_ID)));
            bean.setName(c.getString(c.getColumnIndex(DbConstants.ANTIQUE_CATEGORY_TABLE_CATEGORY_NAME)));
            list.add(bean);
        }
        if(!c.isClosed()){
            c.close();
        }
        return list;
    }

    public void clearCatefory(){
        String sql = "delete from "+DbConstants.ANTIQUE_CATEGORY_TABLE_TABLE_NAME;
        db.execSQL(sql);
    }

    /**
     * 判断古玩表是否为空
     * @return
     */
    public boolean isBlank(){
        String sql = "select * from " + DbConstants.ANTIQUE_CATEGORY_TABLE_TABLE_NAME;
        Cursor c = db.rawQuery(sql,null);
        return c.getCount() == 0 ? true : false;
    }

}
