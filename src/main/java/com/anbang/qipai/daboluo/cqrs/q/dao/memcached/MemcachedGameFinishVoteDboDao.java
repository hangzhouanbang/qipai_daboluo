package com.anbang.qipai.daboluo.cqrs.q.dao.memcached;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.MapAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize.SetAdapter;
import com.anbang.qipai.daboluo.cqrs.q.dbo.GameFinishVoteDbo;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedGameFinishVoteDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	private Gson gson = new GsonBuilder().registerTypeAdapter(Map.class, new MapAdapter())
			.registerTypeAdapter(Set.class, new SetAdapter()).create();

	public void save(GameFinishVoteDbo gameFinishVoteDbo) throws Exception {
		boolean operator = memcachedClient.set("vote_" + gameFinishVoteDbo.getGameId(), 24 * 60 * 60 * 1000,
				gson.toJson(gameFinishVoteDbo));
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public void update(String gameId, GameFinishVoteValueObject gameFinishVoteValueObject) throws Exception {
		GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
		gameFinishVoteDbo.setGameId(gameId);
		gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
		boolean operator = memcachedClient.set("vote_" + gameFinishVoteDbo.getGameId(), 24 * 60 * 60 * 1000,
				gson.toJson(gameFinishVoteDbo));
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public GameFinishVoteDbo findByGameId(String gameId) throws Exception {
		String json = memcachedClient.get("vote_" + gameId);
		if (json == null) {
			return null;
		}
		GameFinishVoteDbo gameFinishVoteDbo = gson.fromJson(json, GameFinishVoteDbo.class);
		return gameFinishVoteDbo;
	}

}
