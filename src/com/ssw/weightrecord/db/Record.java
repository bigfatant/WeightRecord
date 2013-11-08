package com.ssw.weightrecord.db;

import android.net.Uri;

public class Record {
	public static final String AUTHORITY = "ssw.weightrecord.db.provider.record";
	public static final String _ID = "_ID";
	/** 日期 **/
	public static final String DATE = "date";
	/** 时间 **/
	public static final String DATE_TIME = "time";
	/** 体重 */
	public static final String WEIGHT = "weight";
	/** 是否空腹 1：是 0：否 */
	public static final String ISLIMOSIS = "islimosis";
	/** 是否着装 1：是 0：否 */
	public static final String ISCLEAR = "isclear";

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ssw.weightrecord";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.ssw.weightrecord";

	public static final String DEFAULT_SORT_ORDER = " date DESC";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/records");

}
