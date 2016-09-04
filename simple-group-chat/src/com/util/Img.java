package com.util;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class Img extends JPanel{
	private Image img;
	
		public Img(Image img){
			this.img = img;
		}
		
		public  void paintComponent(Graphics g) {
			g.drawImage(img,0, 0, this.getWidth(), this.getHeight(), this);
		}
}
