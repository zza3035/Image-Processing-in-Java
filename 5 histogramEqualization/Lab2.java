/**

In this task you will implement the method histogramEqualization of the class Lab2 which will perform histogram equalization.  

The expected output is provided in the files solution1.png and solution2.png.

**/

import java.util.Scanner;
public class Lab2 {
	public Lab2() {
		Img img = new Img("Fig03161.png");
		histogramEqualization(img);
		img.save("out1.png");
		img = new Img("HawkesBay.png");
		histogramEqualization(img);
		img.save("out2.png");
	}

	public void histogramEqualization(Img i) {
		//Your code here
		int[] Histogram = new int[256]; //create a histogram list (0,255)
		int PixelNo = i.width*i.height; // know total pixel numbers of the image
		
		//calculate histogram
		for (byte b:i.img){
			int ImgIntensity = b & 0xFF; 
			Histogram[ImgIntensity]++; //read intensity of each pixel and add the histogram value by intensity index
		}
		
		//calculate cumulated histogram
		int[] CumHistogram = new int[256]; //create a new list for cumulated histogram
		CumHistogram[0] = Histogram[0]; //first levels are the same
		for (int a = 1; a < 256; a++) {
			CumHistogram[a] = CumHistogram[a-1]+Histogram[a]; //loop the next 255 levels and calculate the cumulated histogram
		}	
		
		
		//Histogram Equalization
		byte[] EqualHisImg = new byte[PixelNo]; //create a new list for histogram equalization
		for (int c = 0; c < PixelNo; c++ ) {
			// loop among the all pixels
			int NewIntensity = i.img[c] & 0xFF; //actually the same as ImgIntensity above
			int EqualIntensity = (int)(255.0*CumHistogram[NewIntensity]/PixelNo); //formula sk = (L-1)(culmulated histogram value)/MN
			EqualHisImg[c] = (byte)EqualIntensity;
		}
		i.img = EqualHisImg;
	}
		
	public static void main(String[] args) {
		new Lab2();
	}
}
