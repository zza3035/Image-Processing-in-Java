/**

In this task 3 of project 1 you will design a filter based on the Gaussian (use multiple Gaussian with different centers if neccessary) and apply it to the car image to attenuate the impulse-like bursts in the image. You may reuse existing code from your tasks 1 and 2. 

Hint1: Note that the spatial resolution of the car.png is not a power of 2, i.e., it is not of the form 2^m x 2^n. You should create a new suitable image with padded black pixels by changing code in the constructor of the class P1_3. 

To avoid reverse engineering, we do not provide a sample solution for this task.

**/

import java.time.Instant;
import java.time.Duration;
public class P1_3 {
	public P1_3() {
		//Change this code. 
		Img img = new Img("car.png");
		
		//Pad the image to 2^m x 2^n
		int iH = img.height;
		int iW = img.width;
		int newWidth = PowerOfTwo(iW);
		int newHeight = PowerOfTwo(iH);
		byte[] Padimg = new byte[newWidth*newHeight];
		for (int x=0; x<newHeight; x++){
			for (int y=0; y<newWidth; y++){
				Padimg[x*newWidth+y]= (x<iH && y<iW)? img.img[x*iW+y] :(byte)0;
			}
		}
		img.width = newWidth;
		img.height = newHeight;
		img.img = Padimg;
		
		//
		Instant start = Instant.now();
		filterImage(img);
		
		//Crop the padding pixels
		byte[] Newimg = new byte[iW*iH];
		for (int x=0; x<iH; x++){
			for (int y=0; y<iW; y++){
				Newimg[x*iW+y] = img.img[x*newWidth+y];
			}
		}
		img.width = iW;
		img.height = iH;
		img.img = Newimg;
		
		//
		Instant stop = Instant.now();
		System.out.println("Elapsed time: "+Duration.between(start, stop).getSeconds()+"s");
		img.save();
	}
	
	private int PowerOfTwo(int number){
		int PoT = 1;
		while (PoT < number){
			PoT *= 2;
		}
		return PoT;
	}

    public void filterImage(Img i) {
		Complex[] F = fastFourierTransfrom(i);
		//Your code here
		int Width1 = i.width;
		int Height1 = i.height;
		int YCENTER = Width1/2;
		int XCENTER = Height1/2;
		double Sigma = 50; // a deviation of the Gaussian filter, can be adjusted
		
		for (int p=0; p<Height1; p++){
			for (int q=0; q<Width1; q++){
				double Distance1 = Math.sqrt(Math.pow(p-XCENTER,2) + Math.pow(q-YCENTER,2));
				double H1 = Math.exp(-Math.pow(Distance1,2) / (2*Math.pow(Sigma,2)));
				F[p*Width1+q].mul(H1);
			}
		}

		inverseFastFourierTransfrom(F, i);
    }

	//P1_1 code here
	public Complex[] FFTmath(Complex[] Input){
		int L = Input.length;
		Complex[] New = new Complex[L];
		
		//end of recursion
		if (L<=1){
			return Input;
		}
		
		// split the even and odd
		int K = L/2;
		Complex[] Even = new Complex[K];
		Complex[] Odd = new Complex[K];
		
		// fill the Even and Odd list
		for (int x=0; x<K; x++){
			Even[x] = Input[x*2];
			Odd[x] = Input[x*2+1];
		}
		
		//recursion
		Complex[] Even2 = FFTmath(Even);
		Complex[] Odd2 = FFTmath(Odd);
		
		for (int u=0; u<K; u++){
			double angle = -2*Math.PI*u/(double)L;
			Complex W = new Complex(Math.cos(angle), Math.sin(angle));
			W.mul(Odd2[u]);
			//the range between 0-K
			New[u] = new Complex();
			New[u].plus(Even2[u]);
			New[u].plus(W);
			//according to principle2: the range between K-L
			New[u+K] = new Complex();
			New[u+K].plus(Even2[u]);
			New[u+K].minus(W);
		}
		
		return New;
		
	}

    public Complex[] fastFourierTransfrom(Img i) {
		//My code here
		int Height = i.height;
		int Width = i.width;
		
		Complex[] F = new Complex[Width*Height];
		for (int u=0; u<Height; u++){
			Complex[] Row = new Complex[Width];
			for (int v=0; v<Width; v++){
				Row[v] = new Complex((double)(i.img[u*Width+v]&0xFF) * Math.pow(-1, u+v), 0);
			}
			Complex[] FFTrow = FFTmath(Row);
			for (int v=0; v<Width; v++){
				F[u*Width+v] = FFTrow[v];
			}
		}
		
		//then column
		Complex[] F2 = new Complex[Width*Height];
		for (int v=0; v<Width; v++){
			Complex[] Column = new Complex[Height];
			for (int u=0; u<Height; u++){
				Column[u] = F[u*Width+v];
			}
			Complex[] FFTcolumn = FFTmath(Column);
			for (int u=0; u<Height; u++){
				F2[u*Width+v] = FFTcolumn[u];
			}
		}
		
		return F2;
	}

	//P1_2 code here
	private void inverseFastFourierTransfrom(Complex[] F, Img i) {
		//My code here
		int L2 = F.length;
		int width = i.width;
		int height = i.height;
		
		//conjungate all complex in F
		for (int x=0; x<L2; x++){
			F[x].i = -F[x].i;
		}//result new F
		
		
		//again FFT the row fist then the column
		for (int x=0; x<height; x++){
			Complex[] iRow = new Complex[height];
			for (int y=0; y<width; y++){
				iRow[y] = F[x*width+y];
			}
			Complex[] iFFTrow = FFTmath(iRow);
			for (int y=0; y<width; y++){
				F[x*width+y] = iFFTrow[y];
			}
		}//result new F after 1D FFT
		
		//now FFT the column
		for (int y=0; y<width; y++){
			Complex[] iColumn = new Complex[width];
			for (int x=0; x<height; x++){
				iColumn[x] = F[x*width+y];
			}
			Complex[] iFFTcolumn = FFTmath(iColumn);
			for (int x=0; x<height; x++){
				F[x*width+y] = iFFTcolumn[x];
			}
		}//result new F after 2D FFT
		
		
		//adopt the part of real of the complex as the value of pixels in image
		for (int m=0; m<height; m++){
			for (int n=0; n<width; n++){
				Complex pixel = F[m*width+n];
				pixel.div(width*height);
				pixel.mul(Math.pow(-1, m+n));
				
				// make sure the value is in the range of 0-255
				if (pixel.r<0)	pixel.r=0;
				if (pixel.r>255)	pixel.r=255;
				
				//assign the value to the pixel location in image
				i.img[m*width+n] = (byte) pixel.r;
			}
		}
	}

	public static void main(String[] args) {
		new P1_3();
	}
}
