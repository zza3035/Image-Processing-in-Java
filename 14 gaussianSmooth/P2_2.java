/**
  * In this task you will implement the method gaussianSmooth of the class P2_2 which will apply 2D Gaussian smoothing to the image. 
  *
  * You should implement the 2D convolution using 2 x 1D masks (first x then y) for performance reasons. You should also output the size of the mask and the values used for the smoothing mask.
  * 
  * Note that you should cut off the Gaussian, as discussed in class. Consider the following input/output:
  *
  * Sigma: 0.5
  * Size: 3
  * Mask: [0.10650697891920077, 0.7869860421615985, 0.10650697891920077]
  *
  * Sigma: 1
  * Size: 7
  * Mask: [0.004433048175243746, 0.05400558262241449, 0.24203622937611433, 0.3990502796524549, 0.24203622937611433, 0.05400558262241449, 0.004433048175243746]
  *
  * Note that the mask is always symmetric and sums to one. 
  * Don't worry if you cannot generate the exact values. We will manually check the correctness of your solution. 
  * For simplicity, you should handle the boundary case simply by using the original intensities there.
  *
  **/
import java.util.Scanner;
import java.util.Arrays;

public class P2_2 {
	public P2_2() { 
		Img img = new Img("Fig0457.png");
		//input the Sigma
		System.out.print("Sigma: ");
		Scanner in = new Scanner(System.in);
		double s = in.nextDouble();
		gaussianSmooth(img, s);
		img.save();
	}

	public void gaussianSmooth(Img i, double sigma) {
		//Your code here
		
		int O=0;
		int Size=0;
		double GFactor=1;
		
		
		//1.determin the truncation of Gaussian Smoothing Filter
			//fomular in PPT page24
		while(true){
			GFactor = (1 / (sigma*Math.sqrt(2*Math.PI))) * Math.exp(-Math.pow(O,2) / (2*Math.pow(sigma,2)));
			
			//according to course PPT, we can truncate the Gaussian filter at 0.001
			if (GFactor<=0.001){
				Size=2*(O-1)+1;
				//output the Size
				System.out.println("Size: "+Size);
				break;
			}
			
			O++;
		}
		
		//2.calculate the mask using Gaussian function
		double[] Mask = new double[Size];
		double Sum=0; //store the Sum of Mask
		for (int a=0; a<Size; a++){
			Mask[a] = (1 / (sigma*Math.sqrt(2*Math.PI))) * Math.exp(-Math.pow(a-Size/2,2) / (2*Math.pow(sigma,2)));
			
			Sum += Mask[a];
		}
			//normalize the mask so that Sum=1
		for (int j=0; j<Size; j++){
			Mask[j] /= Sum;
		}
			//output the Mask
		System.out.println("Mask: " + Arrays.toString(Mask));
		
		
		//3.apply 2 times of 1D convolution to the image
		int W2 = i.width;
		int H2 = i.height;
		byte[] Result = new byte[i.img.length];
		
			//1D convolution in y direction first
			//ignore the boundary chopped by the mask
		int s=Size/2;
		for (int x=s; x<H2-s; x++){
			for (int y=s; y<W2-s; y++){
				double Sum2=0;
				for (int a=0; a<Size; a++){
					int Index = y+a-s;
					int Pixel = i.img[x*W2+Index] & 0xFF;
					Sum2 += Pixel*Mask[a];
				}
				Result[x*W2+y] = (byte) Sum2;
			}
		}
			//1D convolution in x direction
		for (int x=s; x<H2-s; x++){
			for (int y=s; y<W2-s; y++){
				double Sum3=0;
				for (int j=0; j<Size; j++){
					int Index2 = x+j-s;
					int Pixel2 = Result[Index2*W2+y] & 0xFF;
					Sum3 += Pixel2*Mask[j];
				}
				i.img[x*W2+y] = (byte) Sum3;
			}
		}
	}
	
		
	public static void main(String[] args) {
		new P2_2();
	}
}
