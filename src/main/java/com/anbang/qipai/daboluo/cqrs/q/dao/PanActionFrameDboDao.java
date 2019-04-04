package com.anbang.qipai.daboluo.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.daboluo.cqrs.q.dbo.PanActionFrameDbo;

public interface PanActionFrameDboDao {

	void save(List<PanActionFrameDbo> list);

	List<PanActionFrameDbo> findByGameIdAndPanNo(String gameId, int panNo);

	PanActionFrameDbo findByGameIdAndPanNo(String gameId, int panNo, int actionNo);
}
