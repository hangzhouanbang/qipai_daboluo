package com.anbang.qipai.daboluo.cqrs.q.dao;

import com.anbang.qipai.daboluo.cqrs.q.dbo.JuResultDbo;

public interface JuResultDboDao {

	void save(JuResultDbo juResultDbo);

	JuResultDbo findByGameId(String gameId);

}
