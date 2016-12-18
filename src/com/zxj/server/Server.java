package com.zxj.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Ⱥ�ķ�����
 * 	����ÿһ�ε�socket���Ӿ�����һ���̴߳���
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
				System.out.println("�ͻ���: " + s.getInetAddress() + " �˿ں� "
						+ s.getPort() + " ��������");
				
				//���ͻ��˶˿ںż���ͻ��˼����б�   ����Ⱥ����Ϣ
				clientList.add(s);
				
				//����ÿһ�ε����Ӿ�����һ���µ��̴߳���
				new Thread(new ConnectClientThread(s)).start();
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
	public static void main(String[] args) {
		new Thread(new Server()).start();
		System.out.println("�����������ɹ�!");
	}
}
