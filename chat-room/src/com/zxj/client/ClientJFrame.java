package com.zxj.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.zxj.util.Img;


public class ClientJFrame extends javax.swing.JFrame {

	private JButton jButton1;
	private JTextArea jTextArea1;
	private JScrollPane jScrollPane1;
	private JTextField jTextField1;
	private Socket s = null;

	private Img jPanel1 = new Img(new ImageIcon("./src/image/chat1.png").getImage());
	
	private Img contentPane = new Img(new ImageIcon("./src/image/chat.gif").getImage());
	
//	public static void main(String[] args) {
//		
////		NewJFrameClient inst = new NewJFrameClient(1);
////		inst.setLocationRelativeTo(null);
////		inst.setVisible(true);
//	
//	}
	public static void main(String[] args) {
		ClientJFrame inst = new ClientJFrame();
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);
	}
	
	public ClientJFrame() {
		super();
		initGUI();
		try {
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
			JOptionPane.showMessageDialog(ClientJFrame.this, "您输入的信息为空");
			flag = false;
		}
		
		if(flag){
			try {
				//发送信息
				OutputStream os = s.getOutputStream();
				os.write(str.getBytes());
				
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
					byte[] byteArr = new byte[1024];
					StringBuilder stringBuilder = new StringBuilder();
					is.read(byteArr);
					String s = new String(byteArr, "UTF-8");
					String dateTime = Calendar.getInstance().getTime().toLocaleString();
					jTextArea1.append(dateTime + "\n消息:\n " + s + "\n");
					//设置滚动条置于文本区最下方
					jTextArea1.setCaretPosition(jTextArea1.getText().length());
				} catch (IOException e) {
					break;
//					e.printStackTrace();
				}	
			}
		}
		
	}
}



	

