package com.anbang.qipai.daboluo.plan.dao;

import com.anbang.qipai.daboluo.plan.bean.MemberGoldBalance;

public interface MemberGoldBalanceDao {

	void save(MemberGoldBalance memberGoldBalance);

	MemberGoldBalance findByMemberId(String memberId);
}
