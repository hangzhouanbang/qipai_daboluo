package com.anbang.qipai.daboluo.cqrs.q.dao.memcached;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.ListAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.MapAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.PukePaiMarkAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dbo.JuResultDbo;
import com.dml.shisanshui.pai.PukePaiMark;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedJuResultDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	private Gson gson = new GsonBuilder().registerTypeAdapter(Map.class, new MapAdapter())
			.registerTypeAdapter(List.class, new ListAdapter())
			.registerTypeAdapter(PukePaiMark.class, new PukePaiMarkAdapter()).create();

	public void save(JuResultDbo juResultDbo) throws Exception {
		boolean operator = memcachedClient.set("juresult" + juResultDbo.getGameId(), 0, gson.toJson(juResultDbo),
				24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public JuResultDbo findByGameId(String gameId) throws Exception {
		String json = memcachedClient.get("juresult" + gameId);
		if (json == null) {
			return null;
		}
		JuResultDbo dbo = gson.fromJson(json, JuResultDbo.class);
		return dbo;
	}
}
