package com.zxj.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 群聊服务器
 * 	借鉴ExecutorService API实用线程池的方式,
 *  不在每次连接均创建一个线程，提升服务器的性能。
 * @author QnMy
 *
 */
public class Server implements Runnable{
	private ServerSocket sc;
	private List<Socket> clientList;
	private final ExecutorService pool;
	
	public Server() throws IOException{
		 sc = new ServerSocket(6787);
		 //考虑线程安全问题,使用CopyOnWriteArrayList存放客户端socket
		 clientList  = new CopyOnWriteArrayList<Socket>();
		 //初始化线程池
		 pool = Executors.newFixedThreadPool(50);
	}

	@Override
	public void run() {
		for(;;){
			try {
				Socket s = sc.accept();
				clientList.add(s);
				pool.execute(new ConnectClientThread(s));
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
	public static void main(String[] args) throws IOException {
		new Thread(new Server()).start();
		System.out.println("服务器启动成功!");
	}
}
