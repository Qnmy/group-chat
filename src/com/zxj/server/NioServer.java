package com.zxj.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioServer {

	/**
	 * ��������ӵ�ͨ��(�漰���̲߳�����ʹ��CopyOnWriteArrayList��֤�̰߳�ȫ)
	 */
	private List<SocketChannel> clientChannelList = new CopyOnWriteArrayList<SocketChannel>();
	/**
	 * ʹ���̳߳�������ͻ��˵Ķ�д����
	 */
	private ExecutorService pool = Executors.newFixedThreadPool(50);
	
	private void startup() throws IOException{
		Selector sel = Selector.open();
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.bind(new InetSocketAddress("localhost", 6787));
		ssc.register(sel, SelectionKey.OP_ACCEPT);
		for(;;){
			//blocking selection operation �˲�����
			sel.select();
			Set<SelectionKey> set = sel.selectedKeys();
			Iterator<SelectionKey> iter = set.iterator();
			System.out.println("Have SelectedKey Number " + set.size());
			while(iter.hasNext()){
				SelectionKey key = iter.next();
				if((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
					ServerSocketChannel serversc = (ServerSocketChannel) key.channel();
					SocketChannel sc = serversc.accept();
					sc.configureBlocking(false);
					sc.register(sel, SelectionKey.OP_READ);
					System.out.println("connect " + sc.getRemoteAddress());
					clientChannelList.add(sc);
					iter.remove();
				} else if ((key.readyOps() & SelectionKey.OP_READ)
				          == SelectionKey.OP_READ){
					pool.execute(new HandleClient((SocketChannel)key.channel()));
					iter.remove();
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		new NioServer().startup();
	}
	
	class HandleClient implements Runnable{
		private SocketChannel sc;
		
		public HandleClient(SocketChannel sc){
			this.sc = sc;
		}
		
		@Override
		public void run() {
			ByteBuffer bf = ByteBuffer.allocate(1024);
			while(true){
				bf.clear();
				int r = 0;
				try {
					 r = sc.read(bf);
				} catch (Exception e) {
					System.err.println("IOException �ͻ���ǿ�ƹر�");
					clientChannelList.remove(sc);
					try {
						sc.close();
					} catch (IOException e1) {
						e1.printStackTrace();
						return;
					}
					break;
				} 
				if(r <= 0){
					break;
				}
				bf.flip();
				byte[] msgByte = new byte[bf.limit()];
				bf.get(msgByte);
				System.out.println(String.format("ת������ǰ  %d ���ͻ���", clientChannelList.size()));
				for(SocketChannel csc : clientChannelList){
					bf.clear();
					bf.put(msgByte);
					bf.flip();
					try {
						csc.write(bf);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		}
		
	}
}
