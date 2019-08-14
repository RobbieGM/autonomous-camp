package frc.robot;
import java.util.ArrayList;
import java.util.List;

public class PIDController {

    static class Point {
        double millis;
        double err;
    
        Point(double millis, double err) {
            this.millis = millis;
            this.err = err;
        }
    }

	public double P;
	public double I;
	public double D;
	List<Point> points = new ArrayList<Point>();
	
	public PIDController(final double p, final double i, final double d) {
		P = p;
		I = i;
		D = d;
	}
	
	public void reset() {
		points.clear();
	}
	
	public void input(double err) {
		if (latest().millis == System.currentTimeMillis()) return;
		if ((err > 0 && latest().err < 0) || (err < 0 && latest().err > 0)) {
			reset();
		}
		points.add(new Point(System.currentTimeMillis(), err));
	}
	
	public double getCorrection() {
		return getP() + getI();// + getD();
	}
	
	public double getP() {
		return P * latest().err;
	}
	
	public double getI() {
		if (points.size() < 2) {
			return 0;
		}
		double area = 0;
		for (int i = 1; i < points.size(); i++) {
			double rectWidth = points.get(i).millis - points.get(i - 1).millis;
			double rectHeight = (points.get(i - 1).err + points.get(i).err) / 2;
			area += rectHeight * rectWidth;
		}
		return I * area;
	}
	
	public double getD() {
		if (points.size() < 2) {
			return 0;
		}
		double derr = latest().err - prev().err;
		double dt = latest().millis - prev().millis;
		return D * derr / dt;
	}
	
	/**
	 * Checks if all the recent points (back to now - time) are within the threshold.
	 * @param threshold
	 * @param time The time to filter the points to count by
	 * @return
	 */
	public boolean isStable(double threshold, double time) {
		if (points.size() == 0) return false;
		if (System.currentTimeMillis() - points.get(0).millis < time) return false; // Not enough time has passed
		return points.stream().filter(point -> point.millis > System.currentTimeMillis() - time).allMatch(point -> point.err < threshold && point.err > -threshold);
	}
	
	public Point prev() {
		try {
			return points.get(points.size() - 2);
		} catch (ArrayIndexOutOfBoundsException e) {
			return new Point(System.currentTimeMillis(), 0);
		}
	}
	
	public Point latest() {
		try {
			return points.get(points.size() - 1);
		} catch (IndexOutOfBoundsException e) {
			return new Point(System.currentTimeMillis(), 0);
		}
	}
}