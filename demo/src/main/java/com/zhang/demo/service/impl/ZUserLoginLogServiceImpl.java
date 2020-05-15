package com.zhang.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.demo.dao.ZUserLoginLogDao;
import com.zhang.demo.entity.ZUserLoginLogEntity;
import com.zhang.demo.service.ZUserLoginLogService;
import org.springframework.stereotype.Service;

@Service("zUserLoginLogService")
public class ZUserLoginLogServiceImpl extends ServiceImpl<ZUserLoginLogDao, ZUserLoginLogEntity> implements ZUserLoginLogService {



}
