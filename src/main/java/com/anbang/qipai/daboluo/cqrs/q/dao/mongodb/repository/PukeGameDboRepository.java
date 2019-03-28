package com.anbang.qipai.daboluo.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGameDbo;

public interface PukeGameDboRepository extends MongoRepository<PukeGameDbo, String> {

}
