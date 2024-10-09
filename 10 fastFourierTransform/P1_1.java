/**

In this task 1 of project 1 you will implement the fast Fourier transform and change the image to the Fourier spectrum in the method fourierSpectrum(). Your task is to implement the missing code in the method fastFourierTransform(). The implementation details of the FFT can be obtained in section 4.11 of our Textbook. 

Use the log transformation and ensure that all values are in the range 0 ... 255. 

You may use methods declared in the class Complex.java for your convenience and you may add new methods to that class if necessary. 

The solution files are provided for qualitative comparison. Output could be different because of differences in floating point arithmetic and differences in the way the rescaling is performed.

For your reference, we are able generate the Fourier spectrum of the file rectangle1024.png in < 1 seconds. 

**/

import java.time.Instant;
import java.time.Duration;
public class P1_1 {
	public P1_1() {
		Img img = new Img("rectangle1024.png");
		Instant start = Instant.now();
		fourierSpectrum(img);
		Instant stop = Instant.now();
		System.out.println("Elapsed time: "+Duration.between(start, stop).getSeconds()+"s");
		img.save();
	}
	//use FFT to transform the image and calculate the processing time

    public void fourierSpectrum(Img i) {
    	Complex[] F = fastFourierTransfrom(i);
		double max = Double.NEGATIVE_INFINITY;
		for (int x = 0; x < F.length; x++)
			max = Math.max(F[x].getNorm(), max);
		for (int x = 0; x < i.img.length; x++)
			i.img[x] = (byte)(255 / Math.log(256)*Math.log(255/max*F[x].getNorm()+1));
    }
	
	
	//My code here
	// need to define one more function before fastFouriertransform to split the complex[] iteratively
	public Complex[] FFTmath(Complex[] Input){
		//key math steps from DFT to FFT
		//principle1: W2n^2k = Wn^k
		//principle2: Wn^(k+n/2) = -Wn^k
		//take Cooley-Tukey algorithm as reference
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
		
		
		//2d FT, fft the row first, than fft the column again
		//row first
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
		
	public static void main(String[] args) {
		new P1_1();
	}
}
