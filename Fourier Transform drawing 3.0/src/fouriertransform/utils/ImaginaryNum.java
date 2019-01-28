package fouriertransform.utils;

/**
 * @author Fabian
 */

public class ImaginaryNum {

	public double r;
	public double j;

	public ImaginaryNum(double r, double j) {
		this.r = r;
		this.j = j;
	}

	public ImaginaryNum add(ImaginaryNum num) {
		r += num.r;
		j += num.j;
		return this;
	}

	public ImaginaryNum sub(ImaginaryNum num) {
		r -= num.r;
		j -= num.j;
		return this;
	}

	public ImaginaryNum multi(ImaginaryNum num) {
		double rTemp = r;
		r = (r * num.r) - (j * num.j);
		j = (j * num.r) + (rTemp * num.j);
		return this;
	}

	public ImaginaryNum div(ImaginaryNum num) {
		double rTemp = r;
		r = (r * num.r + j * num.j) / (Math.pow(num.r, 2) + Math.pow(num.j, 2));
		j = (j * num.r - rTemp * num.j) / (Math.pow(num.r, 2) + Math.pow(num.j, 2));
		return this;
	}

	public ImaginaryNum clone() {
		return new ImaginaryNum(r, j);
	}
	
	public double getDistance(ImaginaryNum num) {
		return Math.sqrt(Math.pow(r - num.r, 2) + Math.pow(j - num.j, 2));
	}

	@Override
	public String toString() {
		if (j > 0) {
			return r + "+" + j + "i";
		} else if (j < 0) {
			return r + "" + j + "i";
		} else {
			return r + "";
		}
	}
}
