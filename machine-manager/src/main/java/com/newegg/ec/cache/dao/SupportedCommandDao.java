package com.newegg.ec.cache.dao;

import com.newegg.ec.cache.model.entity.SupportedCommand;

import java.util.List;

/**
 * @author Jay.H.Zou
 * @date 2018/4/13
 */
public interface SupportedCommandDao {

    int addSupportedCommand(SupportedCommand supportedCommand);

    List<SupportedCommand> selectSupportedCommandList();

    SupportedCommand selectSupportedCommandById(String supportedCommandId);

    SupportedCommand selectSupportedCommandByCommand(String command);

    int deleteSupportedCommandById(String supportedCommandId);
}
