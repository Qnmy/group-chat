package com.zxj.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.zxj.util.Img;


public class ClientJFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private JButton jButton1;
	private JTextArea jTextArea1;
	private JScrollPane jScrollPane1;
	private JTextField jTextField1;
	private SocketChannel sc;
	private Selector selector;

	private Img jPanel1 = new Img(new ImageIcon("./src/image/chat1.png").getImage());
	
	private Img contentPane = new Img(new ImageIcon("./src/image/chat.gif").getImage());
	
	public static void main(String[] args) {
		ClientJFrame inst = new ClientJFrame();
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);
	}
	
	public ClientJFrame() {
		super();
		initGUI();
		try {
			selector = Selector.open();
			sc = SocketChannel.open(new InetSocketAddress("localhost", 6787));
			sc.configureBlocking(false);
			sc.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//开启接收消息线程
		new Thread(new ReceiveMes()).start();
	}
	
	
	private void initGUI() {
	
		try {
			contentPane.setBounds(438, 5, 177, 273);
			contentPane.setLayout(null);
			jPanel1.setBounds(0, 0, ImageObserver.WIDTH, ImageObserver.HEIGHT);
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
		if(str.equals("")){
			JOptionPane.showMessageDialog(ClientJFrame.this, "您输入的信息为空");
			return;
		}
		ByteBuffer bf = ByteBuffer.allocate(1024);
		try {
			bf.clear();
			bf.put(str.getBytes("UTF-8"));
			bf.flip();
			sc.write(bf);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		jTextField1.setText("");
		jTextField1.requestFocus();
	}
	
	/*
	 * 内部类
	 */
	class ReceiveMes implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					selector.select();//blocking selection operation
				} catch (IOException e1) {
					e1.printStackTrace();
					return;
				}
				Set<SelectionKey> set = selector.selectedKeys();
				Iterator<SelectionKey> iter = set.iterator();
				while(iter.hasNext()){
					SelectionKey key = iter.next();
					SocketChannel csc = (SocketChannel) key.channel();
					ByteBuffer bf = ByteBuffer.allocate(1024);
					String s = "";
					while(true){
						bf.clear();
						int num = 0;
						try {
							num = csc.read(bf);
						} catch (IOException e) {
							System.err.println("读取消息失败");
							return;
						} 
						if(num <= 0){
							break;
						}
						bf.flip();
						int length = bf.limit();
						byte[] byteArr = new byte[length];
						bf.get(byteArr);
						try {
							s += new String(byteArr, "UTF-8").trim();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							break;
						}
					}
					@SuppressWarnings("deprecation")
					String dateTime = Calendar.getInstance().getTime().toLocaleString();
					jTextArea1.append(dateTime + "\n消息:\n " + s + "\n");
					//设置滚动条置于文本区最下方
					jTextArea1.setCaretPosition(jTextArea1.getText().length());
					iter.remove();
				}
			}
		}
		
	}
}



	

