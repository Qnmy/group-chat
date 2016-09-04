package com.zxj.chatRoom;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server2 {

	private List<Socket> clientList = new ArrayList<Socket>();
	private ServerSocket sc ;
	
	public Server2(){
		try {
			 sc = new ServerSocket(6787);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ͻ��˽�������
	 * @author QnMy
	 *
	 */
	class Servers implements Runnable{

		@Override
		public void run() {
			try {
			
				while(true){
					Socket s = sc.accept();
					
					System.out.println("�ͻ���: " + s.getPort() + " ��������");
					
					clientList.add(s);
					
					new Thread(new SendMesThread(s)).start();
					
//					SendMesThread sendThread = new SendMesThread(s);
//					Thread thread = new Thread(sendThread);
//					thread.start();
				}	
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}	
	}
	
	/**
	 * ��ͻ��˷�����Ϣ
	 * @author QnMy
	 *
	 */
	class SendMesThread implements Runnable{
		Socket so;
		public SendMesThread(Socket s){
			this.so = s;
	
		}
		@Override
		public void run() {
			while(true){

				//object	����id,socket,���շ�id,�ı����ļ�
				try {
					InputStream is =  so.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
					Messagebean msgBean = (Messagebean)ois.readObject() ;
					
					//�Կͻ��˼����еĳ�ԱȺ����Ϣ
					for(Socket cl : clientList){
						OutputStream os = cl.getOutputStream();
						ObjectOutputStream oos = new ObjectOutputStream(os);
						oos.writeObject(msgBean);
					}
					
					
				} catch (ClassNotFoundException e1) {
					System.out.println("�쳣1");
					e1.printStackTrace();
				} catch (IOException e1) {
					System.out.println("�ͻ���:" + so.getPort() + "�˳���¼");
					try {
						so.close();
						clientList.remove(so);
							break;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	
	//����Ⱥ�ķ�����
	public static void main(String[] args) {
		Server2 server = new Server2();
		Servers servers = server.new Servers();
		new Thread(servers).start();
	}
}
