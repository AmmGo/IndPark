package com.hl.indpark.utils;


 /**
  * Created by yjl on 2021/3/30 15:41
  * Function：
  * Desc：
  */
public class SortToken {
	/** 简拼 */
	public String simpleSpell = "";
	/** 全拼 */
	public String wholeSpell = "";
	/** 中文全名 */
	public String chName = "";
	@Override
	public String toString() {
		return "[simpleSpell=" + simpleSpell + ", wholeSpell=" + wholeSpell + ", chName=" + chName + "]";
	}
}
