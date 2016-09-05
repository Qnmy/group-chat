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
 * Ⱥ�ķ�����
 * 	���ExecutorService APIʵ���̳߳صķ�ʽ,
 *  ����ÿ�����Ӿ�����һ���̣߳����������������ܡ�
 * @author QnMy
 *
 */
public class Server implements Runnable{
	private ServerSocket sc;
	private List<Socket> clientList;
	private final ExecutorService pool;
	
	public Server() throws IOException{
		 sc = new ServerSocket(6787);
		 //�����̰߳�ȫ����,ʹ��CopyOnWriteArrayList��ſͻ���socket
		 clientList  = new CopyOnWriteArrayList<Socket>();
		 //��ʼ���̳߳�
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
				System.out.println("�ͻ��˼�������ʧ��");
			}
		}
	}
	
	/**
	 * ����ͻ��������߳�
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
				 * ÿ�ζ�ȡָ����С������
				 * 	 read() Ϊ����ʽ����
				 *   	This method blocks until input data is available,
				 *    	end of file is detected, or an exception is thrown.
				 * 	 -1 != is.read() ��ѭ��
				 */
				byte[] byteArray = new byte[2 * 1024];
				while(-1 != is.read(byteArray) ){
					for(Socket clientSocket : clientList){
						clientSocket.getOutputStream().write(byteArray);
					}
				}
				
				/*
				 * ���µ���ʵ��ռ��CPU�϶�
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
				System.out.println("�ͻ���: " + s.getInetAddress() + "�쳣");
			}
		}
		
	}
	
	//����Ⱥ�ķ�����
	public static void main(String[] args) throws IOException {
		new Thread(new Server()).start();
		System.out.println("�����������ɹ�!");
	}
}
