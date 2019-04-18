package com.anbang.qipai.daboluo.cqrs.q.dao.memcached;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.ListAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.MapAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.PukePaiMarkAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PanResultDbo;
import com.dml.shisanshui.pai.PukePaiMark;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedPanResultDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	private Gson gson = new GsonBuilder().registerTypeAdapter(Map.class, new MapAdapter())
			.registerTypeAdapter(List.class, new ListAdapter())
			.registerTypeAdapter(PukePaiMark.class, new PukePaiMarkAdapter()).create();

	public void save(PanResultDbo panResultDbo) throws Exception {
		boolean operator = memcachedClient.set("panresult_" + panResultDbo.getPanNo() + "_" + panResultDbo.getGameId(),
				0, gson.toJson(panResultDbo), 24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public PanResultDbo findByGameIdAndPanNo(String gameId, int panNo) throws Exception {
		String json = memcachedClient.get("panresult_" + panNo + "_" + gameId);
		if (json == null) {
			return null;
		}
		PanResultDbo dbo = gson.fromJson(json, PanResultDbo.class);
		return dbo;
	}
}
