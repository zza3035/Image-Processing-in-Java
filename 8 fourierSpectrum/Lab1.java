/**

In this task you will implement the Fourier transform and change the image to the Fourier spectrum in the method fourierSpectrum(). Your task is to implement the missing code in the method fourierTransform(). 

Use the log transformation and ensure that all values are in the range 0 ... 255. 

You may use methods declared in the class Complex.java for your convenience.

The solution files are provided for qualitative comparison. Output could be different because of differences in floating point arithmetic and differences in the way the rescaling is performed. 

**/

import java.time.Instant;
import java.time.Duration;
public class Lab1 {
	public Lab1() {
		Img img = new Img("rectangle128.png");
		Instant start = Instant.now();
		fourierSpectrum(img);
		Instant stop = Instant.now();
		System.out.println("Elapsed time: "+Duration.between(start, stop).getSeconds()+"s");
		img.save();
	}

    public void fourierSpectrum(Img i) {
		// 寻找傅里叶变换结果的最大值
    	Complex[] F = fourierTransfrom(i);
		double max = Double.NEGATIVE_INFINITY;
		for (int x = 0; x < F.length; x++)
			max = Math.max(F[x].getNorm(), max);
		
		// 根据傅里叶变换结果的幅值进行对数变换和范围缩放，映射到（0，255）范围内
		for (int x = 0; x < i.img.length; x++)
			i.img[x] = (byte)(255 / Math.log(256)*Math.log(255/max*F[x].getNorm()+1));
    }

    public Complex[] fourierTransfrom(Img i) {
    	//Change this code
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

	
		
	public static void main(String[] args) {
		new Lab1();
	}
}
