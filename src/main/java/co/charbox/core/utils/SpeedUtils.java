package co.charbox.core.utils;


public abstract class SpeedUtils {

	// to kb 1024
	// to mb 1024
	// to seconds 1000
	// to bits 8
	// const = 8  * 1000 / 1024 / 1024 = 0.008
	//         bits sec    kb     mb
	private static final double CONST = 8.0 * 1000.0 / 1024.0 / 1024.0;
	
	public static double calcSpeed(double duration, double size) {
		double speed = (duration <= 0) 
				? -1
				: (size * CONST / duration);
		return speed;
	}
}
