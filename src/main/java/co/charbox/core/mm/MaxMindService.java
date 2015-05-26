package co.charbox.core.mm;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.mm.ConnectionInfoModel;

import com.google.api.client.util.DateTime;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.InsightsResponse;
import com.tpofof.core.utils.Config;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class MaxMindService {

	private final int USER_ID;
	private final String KEY;
	private final Cache<String, InsightsResponse> cache;
	
	private boolean lastRequestFailure = false;
	private DateTime lastHealthyTime;
	private DateTime lastFailedTime;
	private int remainingRequets = -1;
	
	private final WebServiceClient client;
	
	@Autowired
	public MaxMindService(Config config) {
		USER_ID = config.getInt("location.api.userId", 0);
		this.KEY = config.getString("location.api.key", "");
		this.client = new WebServiceClient.Builder(USER_ID, KEY).build();
		this.cache = CacheBuilder.newBuilder()
				.expireAfterAccess(config.getLong("location.api.cache.access", 60), TimeUnit.MINUTES)
				.expireAfterWrite(config.getLong("location.api.cache.write", 4320), TimeUnit.MINUTES)
				.recordStats()
				.build();
	}
	
	public InsightsResponse getInsights(String ip) {
		InsightsResponse response = cache.getIfPresent(ip);
		if (response != null) {
			return response;
		}
		try {
			InetAddress ipAddress = InetAddress.getByName(ip);
			InsightsResponse insights = client.insights(ipAddress);
			if (insights == null) {
				lastRequestFailure = true;
				lastFailedTime = new DateTime(new Date());
			} else {
				lastHealthyTime = new DateTime(new Date());
				lastRequestFailure = false;
				remainingRequets = insights.getMaxMind().getQueriesRemaining();
				cache.put(ip, insights);
			}
			return insights;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ConnectionInfoModel get(String ip) {
		InsightsResponse insights = getInsights(ip);
		return insights == null ? null : new ConnectionInfoModel(insights);
	}
	
	public boolean wasLastRequestFailure() {
		return lastRequestFailure;
	}
	
	public int getRemainingRequests() {
		return remainingRequets;
	}

	public DateTime getLastHealthyTime() {
		return lastHealthyTime;
	}

	public DateTime getLastFailedTime() {
		return lastFailedTime;
	}

	public Cache<String, InsightsResponse> getCache() {
		return cache;
	}
}
