package com.anbang.qipai.daboluo.cqrs.q.dao.memcached;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.ListAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.MapAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.PukePaiMarkAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGameDbo;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.dml.shisanshui.pai.PukePaiMark;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedPukeGameDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	private Gson gson = new GsonBuilder().registerTypeAdapter(Map.class, new MapAdapter())
			.registerTypeAdapter(List.class, new ListAdapter())
			.registerTypeAdapter(PukePaiMark.class, new PukePaiMarkAdapter()).create();

	public PukeGameDbo findById(String id) throws Exception {
		String json = memcachedClient.get("gameinfo_" + id);
		if (json == null) {
			return null;
		}
		PukeGameDbo pukeGameDbo = gson.fromJson(json, PukeGameDbo.class);
		return pukeGameDbo;
	}

	public void save(PukeGameDbo pukeGameDbo) throws Exception {
		boolean operator = memcachedClient.set("gameinfo_" + pukeGameDbo.getId(), 0, gson.toJson(pukeGameDbo),
				24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public void updatePlayerOnlineState(String id, String playerId, GamePlayerOnlineState onlineState)
			throws Exception {
		String json = memcachedClient.get("gameinfo_" + id);
		if (json == null) {
			return;
		}
		PukeGameDbo pukeGameDbo = gson.fromJson(json, PukeGameDbo.class);
		pukeGameDbo.getPlayers().forEach((player) -> {
			if (player.getPlayerId().equals(playerId)) {
				player.setOnlineState(onlineState);
			}
		});
		boolean operator = memcachedClient.set("gameinfo_" + pukeGameDbo.getId(), 0, gson.toJson(pukeGameDbo),
				24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}
}
