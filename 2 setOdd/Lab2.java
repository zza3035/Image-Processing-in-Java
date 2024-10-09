/**

In this task you will implement the method setOdd of the class Lab2 which will set all intensity values with odd coordinates to white (255), all others will be set to black (0). An odd coordinate is defined as a coordinate that has and odd x value or an odd y value.

The expected output is provided in the file solution.png.

**/

import java.util.Scanner;
public class Lab2 { 
	public Lab2() {
		Img img = new Img("Fig0314a.png");
		setOdd(img);
		img.save();
	}
	/**
	 * Sets all odd coordinates to 255 and others to 0.
	 * @param img in row major format
	 */
	public void setOdd(Img i) {
		//Your code here
		for (int y = 0; y<i.width; ++y){
			for (int x = 0; x<i.height; ++x){
				if(x%2 == 1 || y%2 ==1){
					i.img[x*i.width + y] = (byte) 255;
				} else{
					i.img[x*i.width + y] = (byte) 0;
				}
			}
		}
	}
	public static void main(String[] args) {
		new Lab2();
	}
}
