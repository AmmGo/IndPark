package com.hl.indpark.utils;

public class SortModel extends Contact {

	/** 显示数据拼音的首字母 */
	public String sortLetters;

	public SortToken sortToken = new SortToken();

	public SortModel(String name, String number, String sortKey,int sex) {
		super(name, number, sortKey,sex);
	}

	@Override
	public String toString() {
		return "SortModel [sortLetters=" + sortLetters + ", sortToken=" + sortToken.toString() + "]";
	}
}
