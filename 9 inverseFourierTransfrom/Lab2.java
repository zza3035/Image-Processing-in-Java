/**

In this task you will implement the inverse Fourier transform and perform filtering in the frequency domain. You should reuse your implementation of the Fourier transform from the previous Lab 1. 

In the filterImage() method add one line of code that changes the Fourier transformed image such that it is 0 at the center.

Implement the inverse Fourier transform in the method inverseFourierTransfrom().

You may use methods declared in the class Complex.java for your convenience.

The solution files are provided for qualitative comparison. Output could be different because of differences in floating point arithmetic and differences in the way the rescaling is performed. 

**/

import java.time.Instant;
import java.time.Duration;
public class Lab2 {
	public Lab2() {
		Img img = new Img("ic128.png");
		Instant start = Instant.now();
		filterImage(img);
		Instant stop = Instant.now();
		System.out.println("Elapsed time: "+Duration.between(start, stop).getSeconds()+"s");
		img.save();
	}

    public void filterImage(Img i) {
		Complex[] F = fourierTransfrom(i);
		//Your code here
		int width = i.width;
        int height = i.height;
		F[(height/2) * width + (width/2)] = new Complex();
		inverseFourierTransfrom(F, i);
    }

    public Complex[] fourierTransfrom(Img i) {
    	//Change this to your code from Lab 1
    	int width = i.width;
        int height = i.height;
        Complex[] F = new Complex[width * height];

        // 遍历频域位置 (u, v)
        for (int u = 0; u < height; u++) {
			// System.out.println(u);
            for (int v = 0; v < width; v++) {
                F[u * width + v] = new Complex();

                // 遍历空域位置 (x, y)
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        
						double f = (double)(i.img[x*width+y] & 0xFF) * Math.pow(-1, x+y);
                        
                        // 计算相位角
                        double angle = -2 * Math.PI * ( (u*x)/(double)height + (v*y)/(double)width);
                        
                        // 累加到总和中
                        F[u*width + v].plus(new Complex(Math.cos(angle) * f, Math.sin(angle) * f));
                    }
                }
            }
        }

        return F;
	}

	private void inverseFourierTransfrom(Complex[] F, Img i) {
		//Your code here
		int width = i.width;
        int height = i.height;
		
		for (int x=0; x<height; x++){
			//System.out.println(x);
			for (int y=0; y<width; y++){
				Complex C = new Complex();
				for (int u=0; u<height; u++){
					for (int v=0; v<width; v++){
						double theta = 2 * Math.PI * ((u*x)/(double)height + (v*y)/(double)width);
						Complex D = new Complex(Math.cos(theta), Math.sin(theta));
						D.mul(F[u*width + v]);
						C.plus(D);
					}
				}
				C.div(height * width);
				C.mul(Math.pow(-1, x+y));
				if (C.r<0) C.r=0;
				i.img[x*width + y] = (byte)C.r;
			}
		}
	} 

	public static void main(String[] args) {
		new Lab2();
	}
}
