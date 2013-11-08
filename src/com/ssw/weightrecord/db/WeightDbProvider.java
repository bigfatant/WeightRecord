package com.ssw.weightrecord.db;

import java.util.HashMap;
import java.util.Map;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class WeightDbProvider extends ContentProvider {
	private static final String DATABASE_NAME = "weight.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "weightrecord";
	private WeightDbHelper dbHelper = null;
	private static UriMatcher sUriMatcher = null;
	private static final int RECORD = 1;
	private static final int RECORD_ID = 2;
	private static Map<String, String> recordColumnMap = null;
	static {
		sUriMatcher = new UriMatcher(0);
		sUriMatcher.addURI(Record.AUTHORITY, "records", RECORD);
		sUriMatcher.addURI(Record.AUTHORITY, "records/#", RECORD_ID);
		recordColumnMap = new HashMap<String, String>();
		recordColumnMap.put(Record._ID, Record._ID);
		recordColumnMap.put(Record.DATE, Record.DATE);
		recordColumnMap.put(Record.DATE_TIME, Record.DATE_TIME);
		recordColumnMap.put(Record.ISCLEAR, Record.ISCLEAR);
		recordColumnMap.put(Record.ISLIMOSIS, Record.ISLIMOSIS);
		recordColumnMap.put(Record.WEIGHT, Record.WEIGHT);

	}

	private class WeightDbHelper extends SQLiteOpenHelper {

		public WeightDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table " + TABLE_NAME + "(" + Record._ID
					+ " integer primary key," + Record.DATE + " text,"
					+ Record.DATE_TIME + " text," + Record.ISCLEAR
					+ " integer," + Record.ISLIMOSIS + " integer,"
					+ Record.WEIGHT + " float");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table  if exists " + TABLE_NAME + ";");
			onCreate(db);

		}

	}

	@Override
	public boolean onCreate() {
		dbHelper = new WeightDbHelper(this.getContext());
		return false;
	}

	/**
	 * projection:��ɸѡ�� selection: ��ɸѡ�� selectionArgs����ɸѡ���� sortOrder����������
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(recordColumnMap);
		qb.setTables(TABLE_NAME);
		switch (sUriMatcher.match(uri)) {
		case RECORD:
			break;
		case RECORD_ID:
			qb.appendWhere(Record._ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new SQLException("��ѯ·������");
		}
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = qb
				.query(db, projection, selection, selectionArgs, null, null,
						sortOrder == null ? Record.DEFAULT_SORT_ORDER
								: sortOrder);
		c.setNotificationUri(this.getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case RECORD:
			return Record.CONTENT_TYPE;
		case RECORD_ID:
			return Record.CONTENT_ITEM_TYPE;
		default:
			throw new SQLException("�����Uri:" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (sUriMatcher.match(uri) != RECORD) {
			throw new IllegalArgumentException("�����URI :" + uri);
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowid = db.insert(TABLE_NAME, null, values);
		if (rowid > 0) {
			Uri recordUri = ContentUris.withAppendedId(Record.CONTENT_URI,
					rowid);
			this.getContext().getContentResolver()
					.notifyChange(recordUri, null);
			return recordUri;
		}
		throw new SQLException("��������ʧ�� " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case RECORD:
			count = db.delete(TABLE_NAME, selection, selectionArgs);
			break;
		case RECORD_ID:
			count = db.delete(TABLE_NAME,
					Record._ID
							+ "= "
							+ uri.getPathSegments().get(1)
							+ (TextUtils.isEmpty(selection) ? "" : " and ( "
									+ selection + " )"), selectionArgs);
			break;
		default:
			throw new SQLException("�����Uri:" + uri);
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case RECORD:
			count = db.update(TABLE_NAME, values, selection, selectionArgs);
			break;
		case RECORD_ID:
			count = db.update(TABLE_NAME, values,
					Record._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (TextUtils.isEmpty(selection) ? "" : "and ("
									+ selection + ")"), selectionArgs);
			break;
		default:
			throw new SQLException("�����Uri:" + uri);
		}
		return count;
	}

}
