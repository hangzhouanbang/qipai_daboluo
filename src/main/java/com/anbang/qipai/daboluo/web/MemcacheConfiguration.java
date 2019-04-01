package com.anbang.qipai.daboluo.web;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.spy.memcached.MemcachedClient;

@Configuration
public class MemcacheConfiguration {

	@Value("${memcache.ip}")
	private String memcacheIP;

	@Value("${memcache.port}")
	private int memcachedPort;

	@Bean
	public MemcachedClient memCachedClient() {
		try {
			return new MemcachedClient(new InetSocketAddress(memcacheIP, memcachedPort));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
