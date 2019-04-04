package com.anbang.qipai.daboluo.cqrs.q.dao.memcached;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.daboluo.cqrs.q.dbo.PanActionFrameDbo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

@Component
public class MemcachedPanActionFrameDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	private Gson gson = new Gson();

	public void save(PanActionFrameDbo dbo) throws TimeoutException, InterruptedException, MemcachedException {
		memcachedClient.set(dbo.getGameId() + "_" + dbo.getActionNo(), 60 * 60 * 1000, dbo);
	}

	public List<PanActionFrameDbo> findByGameIdAndActionNo(String gameId, int lastestActionNo)
			throws TimeoutException, InterruptedException, MemcachedException {
		final List<String> keys = new ArrayList<>();
		for (int i = 0; i <= lastestActionNo; i++) {
			keys.add(gameId + "_" + i);
		}
		Type type = new TypeToken<ArrayList<PanActionFrameDbo>>() {
		}.getType();
		String json = gson.toJson(memcachedClient.get(keys));
		List<PanActionFrameDbo> frameList = gson.fromJson(json, type);
		return frameList;
	}
}
