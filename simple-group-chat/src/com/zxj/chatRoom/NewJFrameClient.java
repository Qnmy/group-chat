package com.zxj.chatRoom;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.zxj.vo.Users;
import com.util.Img;


public class NewJFrameClient extends javax.swing.JFrame {

	private JButton jButton1;
	private JTextArea jTextArea1;
	private JScrollPane jScrollPane1;
	private JTextField jTextField1;
	private Socket s = null;
	private int id;
	private String name;
	private List<Users> usersID;

	
	private Img jPanel1=	new Img(new ImageIcon("./src/image/chat1.png").getImage());
	
	private Img contentPane=	new Img(new ImageIcon("./src/image/chat.gif").getImage());
	
//	public static void main(String[] args) {
//		
////		NewJFrameClient inst = new NewJFrameClient(1);
////		inst.setLocationRelativeTo(null);
////		inst.setVisible(true);
//	
//	}
	public static void main(String[] args) {
		NewJFrameClient inst = new NewJFrameClient(1, "MyName", new ArrayList<Users>());
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);
	}
	
	public NewJFrameClient(int id, String name, List<Users> usersID) {
		super();
		this.id = id;
		this.name = name;
		this.usersID = usersID;
		initGUI();
		try {
//			s = new Socket("10.25.130.38", 6787);
			s = new Socket("localhost", 6787);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//开启接收消息线程
		new Thread(new ReceiveMes()).start();
	}
	
	
	private void initGUI() {
	
		try {
			contentPane.setBounds(438, 5, 177, 273);
			contentPane.setLayout(null);
			jPanel1.setBounds(0, 0, this.WIDTH, this.HEIGHT);
			jPanel1.setLayout(null);
			jPanel1.add(contentPane);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("\u7fa4\u804a\u7a97\u53e3");
			{
				jPanel1.setLayout(null);
				jPanel1.setPreferredSize(new java.awt.Dimension(614, 393));
				{
					jTextField1 = new JTextField();
					jPanel1.add(jTextField1);
					jTextField1.setBounds(71, 244, 358, 34);
					
				}
				{
					jButton1 = new JButton();
					jPanel1.add(jButton1);
					jButton1.setText("\u53d1\u9001");
					jButton1.setBounds(350, 318, 79, 29);
					jButton1.setFont(new java.awt.Font("楷体",0,18));
					jButton1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jButton1ActionPerformed(evt);
						}
					});
				}
				{
					jScrollPane1 = new JScrollPane();
					jPanel1.add(jScrollPane1);
					jScrollPane1.setBounds(70, 27, 358, 205);
					{
						jTextArea1 = new JTextArea();
						jScrollPane1.setViewportView(jTextArea1);
						jTextArea1.setEditable(false);
					}
				}
			}
			pack();
			this.setSize(630, 425);
			//设置当前窗口大小不可更改
			this.setResizable(false);
			getContentPane().add(jPanel1, BorderLayout.CENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jButton1ActionPerformed(ActionEvent evt) {
		String str = jTextField1.getText();
		boolean flag = true;
		if(str.equals("")){
			JOptionPane.showMessageDialog(NewJFrameClient.this, "您输入的信息为空");
			flag = false;
		}
		
		if(flag){
			try {
				OutputStream os = s.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
//				Messagebean msgBean = new Messagebean(id, s.getPort(), 0,name, str);
				Messagebean msgBean = new Messagebean(id, s.getPort(), 0,name, str, usersID);
				oos.writeObject(msgBean);
				jTextField1.setText("");
				jTextField1.requestFocus();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 内部类
	 */
	class ReceiveMes implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					
					InputStream is = s.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
					try {
						Messagebean msgBean = (Messagebean) ois.readObject();
						
						Iterator<Users> iter = msgBean.getUsersId().iterator();
						
						if(msgBean.getId() == id){
							String dateTime = Calendar.getInstance().getTime().toLocaleString();
							jTextArea1.append(dateTime +"\n" + msgBean.getName() +" 消息:\n " + msgBean.getMsg() + "\n");
							//设置滚动条置于文本区最下方
							jTextArea1.setCaretPosition(jTextArea1.getText().length());
						}
						//对所有在线好友群发
						while(iter.hasNext()){
							if(iter.next().getId() == id ){
								String dateTime = Calendar.getInstance().getTime().toLocaleString();
								jTextArea1.append(dateTime +"\n" + msgBean.getName() +" 消息:\n " + msgBean.getMsg() + "\n");
								//设置滚动条置于文本区最下方
								jTextArea1.setCaretPosition(jTextArea1.getText().length());
							}
						}
						
						//对所有在线用户群发
//						String dateTime = Calendar.getInstance().getTime().toLocaleString();
//						jTextArea1.append(dateTime +"\n" + msgBean.getName() +" 消息:\n " + msgBean.getMsg() + "\n");
//						//设置滚动条置于文本区最下方
//						jTextArea1.setCaretPosition(jTextArea1.getText().length());

					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					
				} catch (IOException e) {
					break;
//					e.printStackTrace();
				}	
			}
		}
		
	}
}



	

