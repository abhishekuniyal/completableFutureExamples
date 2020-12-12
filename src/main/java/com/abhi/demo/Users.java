package com.abhi.demo;

public class Users {

	private int userId;
	private String userName;
	private String userDep;
	private int sal;

	public Users(int userId, String userName, String userDep, int sal) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userDep = userDep;
		this.sal = sal;
	}

	public int getUserId() {
		return userId;
	}

	public int getSal() {
		return sal;
	}

	public void setSal(int sal) {
		this.sal = sal;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserDep() {
		return userDep;
	}

	public void setUserDep(String userDep) {
		this.userDep = userDep;
	}

}
