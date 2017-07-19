/*
 * 文 件 名:  HRDBManager.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2016年2月2日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.haier.ledai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.haier.ledai.MyApplication;
import com.j256.ormlite.support.ConnectionSource;

import framework.sqlite.DBParam;
import framework.sqlite.DBUpdateListener;
import framework.sqlite.LazyDB;

/**
 * 数据库管理
 * 
 * @author yangli
 * @version [版本号, 2017年6月2日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class HRDBManager {
	private static HRDBManager manager;
	private LazyDB lazyDB;
	private HRDBManager(Context context) {
		DBParam param = new DBParam();
		param.setDbName("ledai.db");
		param.setDbVersion(1);
		lazyDB = new LazyDB(context, param, 5);
		lazyDB.setDBUpdateListener(new DBUpdateListener() {
			@Override
			public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
				if(newVersion>oldVersion){
					try{


						//删除表 ？ 代表表对应的model.class
						//TableUtils.dropTable(connectionSource, ?, true);
					}catch (Exception e){
						//LazyLogger.e(e, "删除表错误");
						e.printStackTrace();
					}
				}
			}
		});
		//init(context);
	}

	public static HRDBManager shareManager() {
		init(MyApplication.getApplication());
		return manager;
	}

	/**
	 * 初始化数据库
	 * 
	 * @param context
	 *            void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public static void init(Context context) {
		if (manager==null) {
			manager = new HRDBManager(context);
		}
	}


}
