package co.charbox.core.utils;


public abstract class SpeedUtils {

	public static double calcSpeed(double duration, double size) {
		double speed = (duration <= 0) 
				? -1 
				: (size * 8.0 / 1048.576 / duration); // do I need to multiple by 8 here? math don't work now. comm bak laytur, da nomburs luk gud doh
		return speed;
	}
}
