/**

In this task you will implement the method medianFilter of the class Lab4 which applies the median filter on the image. 

You should handle the boundary case by keeping the pixels unchanged. 

The expected output is provided in the files solution3.png and solution7.png.

**/

import java.util.Scanner;
import java.time.Instant;
import java.time.Duration;
import java.util.Arrays;

public class Lab4 {
	public Lab4() {
		Img img = new Img("Fig0335.png");
		System.out.print("Size: ");
		Scanner in = new Scanner(System.in);
		int size = in.nextInt();
		Instant start = Instant.now();
		medianFilter(img, size);
		Instant stop = Instant.now();
		//System.out.println("Elapsed time: "+Duration.between(start, stop).toMillis()+"ms");
		img.save();
	}

	public void medianFilter(Img i, int size) {
		//Your code here
		
		int width = i.width;
		int height = i.height;
		int[] filter = new int[size*size];
		byte[] filteredImg = new byte[i.img.length];
		int border = size/2;
		System.arraycopy (i.img, 0, filteredImg, 0, i.img.length);
		
		for (int x=border; x<height-border; x++){
			for (int y=border; y<width-border; y++){
				
				int c = 0;
				for (int j=-border; j<=border; j++){
					for (int k=-border; k<=border; k++){
							filter[c] = filteredImg[(x+j)*width+y+k] & 0xFF;
						c++;
					}
				}
				
				
				Arrays.sort(filter);
				int mid = size*size/2;
				int median;
				if (mid % 2 == 0){
					median = (filter[mid-1]+filter[mid])/2;
				} else {
					median = filter[mid];
				}
				
				i.img[x*width+y] = (byte)median; 
			
			}
		}
	}
		
	public static void main(String[] args) {
		new Lab4();
	}
}
