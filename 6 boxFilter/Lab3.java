/**

In this task you will implement the method boxFilter of the class Lab3 which applies the box smooth filter on the image i. 

You may handle the boundary case in any way that you see fit. E.g., by keeping the pixels unchanged. 

The expected output is provided in the files solution3.png and solution7.png. 

The solution files are provided for qualitative comparison. Output could be slightly different because of differences in floating point arithmetic. 

**/

import java.util.Scanner;
import java.time.Instant;
import java.time.Duration;
public class Lab3 {
	public Lab3() {
		Img img = new Img("Fig0441.png");
		System.out.print("Size: ");
		Scanner in = new Scanner(System.in); //let user decide the size of the box filter
		int size = in.nextInt();
		Instant start = Instant.now();
		boxFilter(img, size);
		Instant stop = Instant.now();
		System.out.println("Elapsed time: "+Duration.between(start, stop).toMillis()+"ms");
		img.save();
	}
	
	public void boxFilter(Img i, int size) {
		//Your code here
		int width = i.width;
		int height = i.height;
		byte[] filteredImg = new byte[i.img.length]; //create a new list for the filtered image
		int border = size/2; //the box is centered
		System.arraycopy(i.img, 0, filteredImg, 0, i.img.length); //copy the original image to the new list of filtered image
		
		//loop the pixels
		for (int x = border; x< height-border; x++){
			for (int y = border; y < width-border; y++){
				//calculate pixel intensity values inside the box
				double sum = 0;
				for (int j = -border; j<=border; j++){
					for (int k = -border; k <= border; k++){
						sum += (double) (filteredImg[(x+j)*width+y+k] & 0xFF)/(size*size); //sum up the pixels inside the box around the center pixel
					}
				}
				
				//calculate averageg value for the center pixel
				i.img[x*i.width+y] = (byte) sum; //assign the sum value to the center pixel
			}
		}
	}
		
	public static void main(String[] args) {
		new Lab3();
	}
}
