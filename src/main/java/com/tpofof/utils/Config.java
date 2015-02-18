package com.tpofof.utils;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

public class Config extends CompositeConfiguration {

	private Config() {
		this.addConfiguration(new SystemConfiguration());
		try {
			this.addConfiguration(new PropertiesConfiguration("app.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private static final Config config = new Config();
	
	public static Config get() {
		return config;
	}
}
