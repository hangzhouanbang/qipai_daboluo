package com.anbang.qipai.daboluo.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.daboluo.cqrs.q.dao.WatchRecordDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.mongodb.repository.WatchRecordRepoository;
import com.dml.mpgame.game.watch.WatchRecord;

/**
 * @Description:
 */
@Component
public class MongodbWatchRecordDao implements WatchRecordDao {

	@Autowired
	private WatchRecordRepoository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(WatchRecord watchRecord) {
		repository.save(watchRecord);
	}

	@Override
	public WatchRecord findByGameId(String gameId) {
		return repository.findOneByGameId(gameId);
	}

	@Override
	public WatchRecord findByPlayerId(String gameId, String playerId, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("gameId").is(gameId));
		query.addCriteria(Criteria.where("watchers.id").is(playerId));
		query.addCriteria(Criteria.where("watchers.state").is(state));
		return mongoTemplate.findOne(query, WatchRecord.class);
	}
}
