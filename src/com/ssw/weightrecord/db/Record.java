package com.ssw.weightrecord.db;

import android.net.Uri;

public class Record {
	public static final String AUTHORITY = "ssw.weightrecord.db.provider.record";
	public static final String _ID = "_ID";
	/** ���� **/
	public static final String DATE = "date";
	/** ʱ�� **/
	public static final String DATE_TIME = "time";
	/** ���� */
	public static final String WEIGHT = "weight";
	/** �Ƿ�ո� 1���� 0���� */
	public static final String ISLIMOSIS = "islimosis";
	/** �Ƿ���װ 1���� 0���� */
	public static final String ISCLEAR = "isclear";

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ssw.weightrecord";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.ssw.weightrecord";

	public static final String DEFAULT_SORT_ORDER = " date DESC";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/records");

}
