package co.charbox.core.utils;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import com.google.common.collect.Maps;

public final class HttpClientProvider {

	private static final Map<Long, HttpClient> clientMap = Maps.newConcurrentMap();
	
	private HttpClientProvider() { }
	
	public static HttpClient get() {
		long tid = Thread.currentThread().getId();
		if (!clientMap.containsKey(tid)) {
			HttpConnectionManagerParams cmparams = new HttpConnectionManagerParams();
		    cmparams.setSoTimeout(5000);
		    cmparams.setTcpNoDelay(true);
		    HttpConnectionManager manager = new SimpleHttpConnectionManager();
			HttpClientParams params = new HttpClientParams();
			params.setSoTimeout(5000);
			params.setConnectionManagerTimeout(5000);
			clientMap.put(tid, new HttpClient(params, manager));
		}
		return clientMap.get(tid);
	}
}
