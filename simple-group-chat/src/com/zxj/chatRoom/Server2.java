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
	 * 与客户端进行连接
	 * @author QnMy
	 *
	 */
	class Servers implements Runnable{

		@Override
		public void run() {
			try {
			
				while(true){
					Socket s = sc.accept();
					
					System.out.println("客户端: " + s.getPort() + " 加入连接");
					
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
	 * 向客户端发送信息
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

				//object	包含id,socket,接收方id,文本，文件
				try {
					InputStream is =  so.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
					Messagebean msgBean = (Messagebean)ois.readObject() ;
					
					//对客户端集合中的成员群发消息
					for(Socket cl : clientList){
						OutputStream os = cl.getOutputStream();
						ObjectOutputStream oos = new ObjectOutputStream(os);
						oos.writeObject(msgBean);
					}
					
					
				} catch (ClassNotFoundException e1) {
					System.out.println("异常1");
					e1.printStackTrace();
				} catch (IOException e1) {
					System.out.println("客户端:" + so.getPort() + "退出登录");
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
	
	//开启群聊服务器
	public static void main(String[] args) {
		Server2 server = new Server2();
		Servers servers = server.new Servers();
		new Thread(servers).start();
	}
}
