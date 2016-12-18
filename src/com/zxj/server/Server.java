package com.zxj.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 群聊服务器
 * 	对于每一次的socket连接均创建一个线程处理
 * @author QnMy
 *
 */
public class Server implements Runnable{
	private ServerSocket sc;
	private List<Socket> clientList;
	
	public Server(){
		try {
			 sc = new ServerSocket(6787);
			 clientList  = new ArrayList<Socket>();
		} catch (IOException e) {
			System.out.println("Server socket create exception!");
		}
	}

	@Override
	public void run() {
		while(true){
			try {
				Socket s = sc.accept();
				System.out.println("客户端: " + s.getInetAddress() + " 端口号 "
						+ s.getPort() + " 加入连接");
				
				//将客户端端口号加入客户端集合列表   用于群发消息
				clientList.add(s);
				
				//对于每一次的连接均创建一个新的线程处理
				new Thread(new ConnectClientThread(s)).start();
			} catch (IOException e) {
				System.out.println("客户端加入连接失败");
			}
		}
	}
	
	/**
	 * 处理客户端请求线程
	 * @author QnMy
	 *
	 */
	class ConnectClientThread implements Runnable{
		private Socket s;
		
		public ConnectClientThread(Socket s) {
			this.s = s;
		}
		
		@Override
		public void run() {
			try {
				InputStream is = s.getInputStream();
				/**
				 * 每次读取指定大小的数据
				 * 	 read() 为阻塞式方法
				 *   	This method blocks until input data is available,
				 *    	end of file is detected, or an exception is thrown.
				 * 	 -1 != is.read() 死循环
				 */
				byte[] byteArray = new byte[2 * 1024];
				while(-1 != is.read(byteArray) ){
					for(Socket clientSocket : clientList){
						clientSocket.getOutputStream().write(byteArray);
					}
				}
				
				/*
				 * 以下的现实会占用CPU较多
				 * 
				 * while(true){
					int arraySize = is.available();
					if(0 != arraySize){
						byte[] byteArr = new byte[arraySize];
						is.read(byteArr);
						for(Socket clientSocket : clientList){
							clientSocket.getOutputStream().write(byteArr);
						}
					}
				}*/
			} catch (IOException e) {
				System.out.println("客户端: " + s.getInetAddress() + "异常");
			}
		}
		
	}
	
	//启动群聊服务器
	public static void main(String[] args) {
		new Thread(new Server()).start();
		System.out.println("服务器启动成功!");
	}
}
