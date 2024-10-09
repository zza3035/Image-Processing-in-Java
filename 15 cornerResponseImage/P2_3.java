/**
  * In this task you will implement the method cornerResponseImage of the class P2_3 which will change the image to the response map R of the Harris corner detector. As usual, ignore the boundary. 
  * 
  * Set pixels to 255 if R > threshold and otherwise set pixels to 0. 
  * 
  **/
import java.util.Scanner;
import java.util.Arrays;

public class P2_3 {
	public P2_3() { 
		Img img = new Img("chessmat.png");
		System.out.print("Sigma: ");
		Scanner in = new Scanner(System.in);
		double s = in.nextDouble();
		System.out.print("Threshold: ");
		double t = in.nextDouble();
		cornerResponseImage(img, s, t);
		img.save();
	}
	
	//P2_2 code
	//the parameters are changed according to cornerResponseImage function
	public void gaussianSmooth(int W3, int H3, double[] img, double sigma) {
		
		int O=0;
		int Size=0;
		double GFactor=1;
		
		//1.determin the truncation of Gaussian Smoothing Filter
		while(true){
			GFactor = (1 / (sigma*Math.sqrt(2*Math.PI))) * Math.exp(-Math.pow(O,2) / (2*Math.pow(sigma,2)));
			
			if (GFactor<=0.001){
				Size=2*(O-1)+1;
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
		
		//3.apply 2 times of 1D convolution to the image
		int W2 = W3;
		int H2 = H3;
		byte[] Result = new byte[img.length];

		int s=Size/2;
		for (int x=s; x<H2-s; x++){
			for (int y=s; y<W2-s; y++){
				double Sum2=0;
				for (int a=0; a<Size; a++){
					int Index = y+a-s;
					Sum2 += img[x*W2+Index]*Mask[a];
				}
				Result[x*W2+y] = (byte) Sum2;
			}
		}
		
		for (int x=s; x<H2-s; x++){
			for (int y=s; y<W2-s; y++){
				double Sum3=0;
				for (int j=0; j<Size; j++){
					int Index2 = x+j-s;
					int Pixel2 = Result[Index2*W2+y] & 0xFF;
					Sum3 += Pixel2*Mask[j];
				}
				img[x*W2+y] = (byte) Sum3;
			}
		}
	}	

	public void cornerResponseImage(Img i, double sigma, double threshold) {
		//Your code here
		int W3 = i.width;
		int H3 = i.height;
		int L3 = i.img.length;
		
		//1. approimate fx and fy of each pixel f(x,y) using mask[-1,0,1]
		//2. form the three images fx^2, fy^2 and fxfy
		double[] Fxx = new double[L3];
		double[] Fxy = new double[L3];
		double[] Fyy = new double[L3];
		int[] Mask = new int[]{-1,0,1};
		
		//each pixel is calculated by its two neighbours
		//the boundary is offset by 1 pixel
		for (int x=1; x<H3-1; x++){
			for (int y=1; y<W3-1; y++){
				//gradient of fx and fy by mask (ignore 0)
				double fx=0;
				double fy=0;
				for (int a=-1; a<=1; a++){
					fx += (double)(i.img[(x-a)*W3+y]&0xFF)*Mask[1-a];
					fy += (double)(i.img[(x*W3+y-a)]&0xFF)*Mask[1-a];
				}
				Fxx[x*W3+y] = fx*fx;
				Fxy[x*W3+y] = fx*fy;
				Fyy[x*W3+y] = fy*fy;
			}
		}
		
		//3. Gaussian mask on fx^2, fy^2 and fxfy
		gaussianSmooth(W3, H3, Fxx, sigma);
		gaussianSmooth(W3, H3, Fxy, sigma);
		gaussianSmooth(W3, H3, Fyy, sigma);
		
		//4. form an image of R, det and trace
		for (int x=0; x<W3; x++){
			for (int y=0; y<H3; y++){
				int In = x*W3+y;
				double k =0.04;
				
				double ImageR = (Fxx[In]*Fyy[In] - Fxy[In]*Fxy[In]) - k*Math.pow(Fxx[In]+Fyy[In],2);
				i.img[In] = (ImageR > threshold)? (byte)255:(byte)0;
			}
		}
	}

	public static void main(String[] args) {
		new P2_3();
	}
}
