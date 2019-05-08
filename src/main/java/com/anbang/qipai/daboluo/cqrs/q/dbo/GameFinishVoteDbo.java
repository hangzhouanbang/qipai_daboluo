package com.anbang.qipai.daboluo.cqrs.q.dbo;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;

@Document
public class GameFinishVoteDbo {

	private String id;
	@Indexed(unique = false)
	private String gameId;
	private GameFinishVoteValueObject vote;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public GameFinishVoteValueObject getVote() {
		return vote;
	}

	public void setVote(GameFinishVoteValueObject vote) {
		this.vote = vote;
	}

}
