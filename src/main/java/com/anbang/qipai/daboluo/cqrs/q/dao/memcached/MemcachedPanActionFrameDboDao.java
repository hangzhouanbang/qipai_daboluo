package com.anbang.qipai.daboluo.cqrs.q.dao.memcached;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.ListAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.MapAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.PukePaiMarkAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PanActionFrameDbo;
import com.dml.shisanshui.pai.PukePaiMark;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedPanActionFrameDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	private Gson gson = new GsonBuilder().registerTypeAdapter(Map.class, new MapAdapter())
			.registerTypeAdapter(List.class, new ListAdapter())
			.registerTypeAdapter(PukePaiMark.class, new PukePaiMarkAdapter()).create();

	public void save(PanActionFrameDbo dbo) throws Exception {
		boolean operator = memcachedClient.set(dbo.getGameId() + "_" + dbo.getActionNo(), 24 * 60 * 60 * 1000,
				gson.toJson(dbo));
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public List<PanActionFrameDbo> findByGameIdAndActionNo(String gameId, int panNo, int lastestActionNo)
			throws Exception {
		final List<String> keys = new ArrayList<>();
		for (int i = 0; i <= lastestActionNo; i++) {
			keys.add(gameId + "_" + i);
		}
		Type type = new TypeToken<ArrayList<PanActionFrameDbo>>() {
		}.getType();
		String json = gson.toJson(memcachedClient.get(keys));
		if (json == null) {
			return null;
		}
		List<PanActionFrameDbo> frameList = gson.fromJson(json, type);
		return frameList;
	}

	public void removePanActionFrameDbo(String gameId, int panNo, int lastestActionNo) {
		for (int i = 0; i <= lastestActionNo; i++) {
			try {
				memcachedClient.delete(gameId + "_" + panNo + "_" + i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
