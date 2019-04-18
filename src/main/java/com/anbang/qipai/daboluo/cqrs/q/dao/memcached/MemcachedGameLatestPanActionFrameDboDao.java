package com.anbang.qipai.daboluo.cqrs.q.dao.memcached;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.ListAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.MapAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.PukePaiMarkAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.dml.shisanshui.pai.PukePaiMark;
import com.dml.shisanshui.pan.PanActionFrame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedGameLatestPanActionFrameDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	private Gson gson = new GsonBuilder().registerTypeAdapter(Map.class, new MapAdapter())
			.registerTypeAdapter(List.class, new ListAdapter())
			.registerTypeAdapter(PukePaiMark.class, new PukePaiMarkAdapter()).create();

	public GameLatestPanActionFrameDbo findById(String id) throws Exception {
		String json = memcachedClient.get("latest" + id);
		if (json == null) {
			return null;
		}
		GameLatestPanActionFrameDbo dbo = gson.fromJson(json, GameLatestPanActionFrameDbo.class);
		return dbo;
	}

	public void save(String id, PanActionFrame panActionFrame) throws Exception {
		GameLatestPanActionFrameDbo dbo = new GameLatestPanActionFrameDbo();
		dbo.setId(id);
		dbo.setPanActionFrame(panActionFrame);
		boolean operator = memcachedClient.set("latest" + id, 0, gson.toJson(dbo), 24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}
}
