package com.zxj.chatRoom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zxj.vo.Users;

public class Messagebean implements Serializable{

	private int id;			//发送方ID
	private int socket;
	private int receiveId;
	private String name;	//发送方名字
	private String msg;		//发送信息内容
	private List<Users> usersId = new ArrayList<>();
	//private Object object;
	
	
	
	
	public Messagebean(int id, int socket, int receiveId,String name, String msg
			) {
		super();
		this.id = id;
		this.socket = socket;
		this.receiveId = receiveId;
		this.name = name;
		this.msg = msg;
		
	}
	
	public Messagebean(int id, int socket, int receiveId, String name,
			String msg, List<Users> usersId) {
		super();
		this.id = id;
		this.socket = socket;
		this.receiveId = receiveId;
		this.name = name;
		this.msg = msg;
		this.setUsersId(usersId);
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSocket() {
		return socket;
	}
	public void setSocket(int socket) {
		this.socket = socket;
	}
	public int getReceiveId() {
		return receiveId;
	}
	public void setReceiveId(int receiveId) {
		this.receiveId = receiveId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
//	public Object getObject() {
//		return object;
//	}
//	public void setObject(Object object) {
//		this.object = object;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Users> getUsersId() {
		return usersId;
	}

	public void setUsersId(List<Users> usersId) {
		this.usersId = usersId;
	}
	
	
}
