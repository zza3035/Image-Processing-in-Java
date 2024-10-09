/**

In this task 2 of project 1 you will implement the inverse fast Fourier transform and perform second order (n=2) ButterWorth low pass filtering in the frequency domain. You should reuse your implementation of the fast Fourier transform from the previous task of this project (P1_1).

In the filterImage() method add your code for the second order (n=2) ButterWorth low pass filtering.

Implement the inverse fast Fourier transform in the method inverseFourierTransfrom().

You may use methods declared in the class Complex.java for your convenience.

The solution file is provided for qualitative comparison. It was generated with d0=10, i.e., with the command

java P1_2 10

Output could be different because of differences in floating point arithmetic and differences in the way the rescaling is performed. 

**/

import java.time.Instant;
import java.time.Duration;
public class P1_2 {
	public P1_2(double d0) {
		Img img = new Img("ic512.png");
		Instant start = Instant.now();
		filterImage(img, d0);
		Instant stop = Instant.now();
		System.out.println("Elapsed time: "+Duration.between(start, stop).getSeconds()+"s");
		img.save();
	}

    public void filterImage(Img i, double d0) {
		Complex[] F = fastFourierTransfrom(i);
		//Your code here
		int width = i.width;
		int height = i.height;
		//get the coordination center of the image, for frequency domain filter's calculation
		int Ycenter = width/2;
		int Xcenter = i.height/2;
		
		//apply the filter on the frequency domain
		double order = 2; //second-order butterworth low-pass filter
		for (int m=0; m<height; m++){
			for (int n=0; n<width; n++){
				double Distance = Math.sqrt(Math.pow(m-Xcenter, 2) + Math.pow(n-Ycenter, 2)); //filter's value according to distance to center
				double H = 1/ (1+Math.pow(Distance/d0, 2*order)); //the value of the filter
				F[m*width+n].mul(H); //the frequency domain's value x filter value
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
		new P1_2(Double.parseDouble(args[0]));
	}
}
