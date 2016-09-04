package com.zxj.vo;

import java.io.Serializable;
import java.util.Date;

public class Users implements Serializable{
	
	/**
	 * 		建立一个包含users属性的java类
	 */
	
		private int id;
		private String name;
		private String password;
		private String phone;
		private String sex;
		private String address;
		private Date birth;
		private String email;
		private String classify;   // 分类
		private String remark; //备注
		
		private int state;//设置好友是否在线
		
		
		public int getState() {
			return state;
		}


		public void setState(int state) {
			this.state = state;
		}


		public Users(int id, String name, String password, String phone,
				String sex, String address, Date birth, String email) {
			super();
			this.id = id;
			this.name = name;
			this.password = password;
			this.phone = phone;
			this.sex = sex;
			this.address = address;
			this.birth = birth;
			this.email = email;
		}


		public String getRemark() {
			return remark;
		}


		public void setRemark(String remark) {
			this.remark = remark;
		}


		public String getClassify() {
			return classify;
		}


		public void setClassify(String classify) {
			this.classify = classify;
		}


	

		public Users(int id, String name, String password, String phone,
				String sex, String address, Date birth, String email,
				String classify,String remark) {
			super();
			this.id = id;
			this.name = name;
			this.password = password;
			this.phone = phone;
			this.sex = sex;
			this.address = address;
			this.birth = birth;
			this.email = email;
			this.classify = classify;
			this.remark = remark;
		}


		public Users() {
			super();
		}


		public int getId() {
			return id;
		}


		public void setId(int id) {
			this.id = id;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public String getPassword() {
			return password;
		}


		public void setPassword(String password) {
			this.password = password;
		}


		public String getPhone() {
			return phone;
		}


		public void setPhone(String phone) {
			this.phone = phone;
		}


		public String getSex() {
			return sex;
		}


		public void setSex(String sex) {
			this.sex = sex;
		}


		public String getAddress() {
			return address;
		}


		public void setAddress(String address) {
			this.address = address;
		}


		public Date getBirth() {
			return birth;
		}


		public void setBirth(Date birth) {
			this.birth = birth;
		}


		public String getEmail() {
			return email;
		}


		public void setEmail(String email) {
			this.email = email;
		}
		
		
		
}
