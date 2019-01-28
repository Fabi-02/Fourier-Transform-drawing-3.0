package fouriertransform.fourier;

import java.util.ArrayList;

import fouriertransform.utils.ImaginaryNum;

/**
 * @author Fabian
 */

public class Fourier {
	
	public static ArrayList<double[]> fourier(ArrayList<Double>[] data, double rotation) {
		int N = data[0].size();
		ArrayList<double[]> fourier = new ArrayList<double[]>();

		for (int k = 0; k < N; k++) {
			
			ImaginaryNum freq = new ImaginaryNum(0, 0);
			
			for (int n = 0; n < N; n++) {
				
				double rotAngle = -1 * (2 * Math.PI) * k * (n / (double) N);
				
				freq.add(new ImaginaryNum(
						Math.cos(rotAngle),
						Math.sin(rotAngle)).multi(new ImaginaryNum(data[0].get(n), data[1].get(n))));
			}
			
			double re = freq.r / N;
			double im = freq.j / N;
			fourier.add(new double[] {k, Math.sqrt(re * re + im * im), Math.atan2(im, re), rotation});
		}
		
		return fourier;
	}
}
