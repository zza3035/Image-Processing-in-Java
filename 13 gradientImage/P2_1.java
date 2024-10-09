/**
  * In this task you will implement the method gradientImage of the class P2_1 which will calculate the gradient image.
  *
  * To determine the gradient in images in x and y directions use the masks [-1 0 1]^T and [-1 0 1], respectively.
  *
  * Note that values should be scaled to [0, 255]. This can be done by multiplying with 1 / sqrt(2). 
  *
  **/

import java.lang.Math;

public class P2_1 {
	public P2_1() {
		Img img = new Img("Fig0314a.png");
		gradientImage(img);
		img.save();
	}
	
	public void gradientImage(Img i) {
		//Your code here
		int W1 = i.width;
		int H1 = i.height;
		double F1 = 1/Math.sqrt(2); //the factor will be used later 
		
			//store the processed gradient image
		byte[] GradImg = new byte[i.img.length];
		System.arraycopy(i.img, 0, GradImg, 0, i.img.length);
		
		
		//1.calculate the gradient in x direction first
			// mask: [-1,0,1]^T
	    //2.then calculate the gradient in y direction 
			// mask: [-1,0,1]
			// ignore the boundary pixels
		for (int y=1; y<W1-1; y++){
			for (int x=1; x<H1-1; x++){
			    //x mask ignore the pixel itself because of 0
				int Px = -1*(GradImg[(x-1)*W1+y]&0xFF) + 1*(GradImg[(x+1)*W1+y]&0xFF);
				//y mask also ignore 0
				int Py = -1*(GradImg[x*W1+y-1]&0xFF) + 1*(GradImg[x*W1+y+1]&0xFF);
				
				double P = Math.sqrt(Math.pow(Px,2) + Math.pow(Py,2)) * F1;
				
				i.img[x*W1+y] = (byte)P;
			}
		}
	}
		
	public static void main(String[] args) {
		new P2_1();
	}
}
