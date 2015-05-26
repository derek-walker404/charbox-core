package co.charbox.core.mm;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.MyLocation;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import com.tpofof.core.utils.Config;

@Component
public final class LocationProvider {

	private DatabaseReader reader;
	
	@Autowired
	public LocationProvider(Config config) {
		File databaseFile = new File(config.getString("location.db.filename", ""));
		try {
			if (databaseFile.exists()) {
				reader = new DatabaseReader.Builder(databaseFile).build();
			} else {
				System.err.println("location.db.filename Does NOT exist!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public MyLocation getLocation(String ip) {
		MyLocation loc = MyLocation.builder()
				.ip(ip)
				.build();
		try {
			InetAddress ipAddress = InetAddress.getByName(ip);
			CityResponse cityResponse = reader.city(ipAddress);
			Location location = cityResponse.getLocation();
			loc.setLat(location.getLatitude())
					.setLon(location.getLongitude());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			e.printStackTrace();
		}
		return loc;
	}
}