package com.anbang.qipai.daboluo.plan.dao;

import com.anbang.qipai.daboluo.plan.bean.PlayerInfo;

public interface PlayerInfoDao {

	PlayerInfo findById(String playerId);

	void save(PlayerInfo playerInfo);
}
