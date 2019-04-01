package com.anbang.qipai.daboluo.cqrs.q.dao;

import com.anbang.qipai.daboluo.cqrs.q.dbo.PanResultDbo;

public interface PanResultDboDao {

	void save(PanResultDbo panResultDbo);

	PanResultDbo findByGameIdAndPanNo(String gameId, int panNo);

}
