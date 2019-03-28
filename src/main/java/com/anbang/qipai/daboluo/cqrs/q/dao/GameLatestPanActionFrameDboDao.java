package com.anbang.qipai.daboluo.cqrs.q.dao;

import com.anbang.qipai.daboluo.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.dml.shisanshui.pan.PanActionFrame;

public interface GameLatestPanActionFrameDboDao {

	GameLatestPanActionFrameDbo findById(String id);

	void save(String id, PanActionFrame panActionFrame);

}
