package com.hl.indpark.utils;


 /**
  * Created by yjl on 2021/3/30 15:40
  * Function：
  * Desc：
  */
public class Contact {
	public Contact() {

	}

	public Contact(String name, String number, String sortKey,int sex,int id,String userId,String deptName) {
		this.name = name;
		this.number = number;
		this.sortKey = sortKey;
		this.sex = sex;
		this.id = id;
		this.userId = userId;
		this.deptName = deptName;
		if(number!=null){
			this.simpleNumber=number.replaceAll("\\-|\\s", "");
		}
	}

	public String name;
	public String number;
	public String simpleNumber;
	public String sortKey;
	public int sex;
	public int id;
	public String userId;
	public String deptName;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((sortKey == null) ? 0 : sortKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (sortKey == null) {
			if (other.sortKey != null)
				return false;
		} else if (!sortKey.equals(other.sortKey))
			return false;
		return true;
	}

}
