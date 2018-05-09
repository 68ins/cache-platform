package com.newegg.ec.cache.service.impl;

import com.newegg.ec.cache.dao.SupportedCommandDao;
import com.newegg.ec.cache.model.entity.SupportedCommand;
import com.newegg.ec.cache.service.SupportedCommandService;
import com.newegg.ec.cache.utils.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * supported command service
 * @author Jay.H.Zou
 * @date 2018/4/14
 */
@Service
public class SupportedCommandServiceImpl implements SupportedCommandService {

    @Autowired
    private SupportedCommandDao supportedCommandDao;

    @Override
    public Map<String, Object> addSupportedCommand(String command) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isNotBlank(command)){
            SupportedCommand existCommand = supportedCommandDao.selectSupportedCommandByCommand(command);
            SupportedCommand addSupportedCommand = null;
            if (existCommand != null){
                result.put("code", 2);
            } else {
                SupportedCommand supportedCommand = new SupportedCommand();
                supportedCommand.setSupportedCommandId(CommonUtils.getUuid());
                supportedCommand.setCommand(command);
                supportedCommand.setUpdateTime(CommonUtils.getTimestamp());
                int row = supportedCommandDao.addSupportedCommand(supportedCommand);
                result.put("code", row);
                if (row > 0){
                    addSupportedCommand = supportedCommandDao.selectSupportedCommandById(supportedCommand.getSupportedCommandId());
                    result.put("supportedCommand", addSupportedCommand);
                }
            }
        }
        return result;
    }

    @Override
    public List<SupportedCommand> getSupportedCommandList() {
        return supportedCommandDao.selectSupportedCommandList();
    }


    @Override
    public int deleteSupportedCommandById(String supportedCommandId) {
        int row = 0;
        if (StringUtils.isNotBlank(supportedCommandId)){
            row = supportedCommandDao.deleteSupportedCommandById(supportedCommandId);
        }
        return row;
    }
}
