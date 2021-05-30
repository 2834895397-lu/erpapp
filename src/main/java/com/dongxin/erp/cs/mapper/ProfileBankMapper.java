package com.dongxin.erp.cs.mapper;

import java.util.List;
import com.dongxin.erp.cs.entity.ProfileBank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 客商银行账户管理
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Repository
public interface ProfileBankMapper extends BaseMapper<ProfileBank> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ProfileBank> selectByMainId(@Param("mainId") String mainId);

	List<ProfileBank> queryBankByCsId(String cs_id);

	Long settleMaxId();

	void insertBankResume(@Param("profileBank") ProfileBank profileBank);

}
