package com.newegg.ec.cache.service;

import com.newegg.ec.cache.model.entity.SupportedCommand;

import java.util.List;
import java.util.Map;

/**
 * @author Jay.H.Zou
 * @date 2018/4/14
 */
public interface SupportedCommandService {

    Map<String, Object> addSupportedCommand(String command);

    List<SupportedCommand> getSupportedCommandList();

    int deleteSupportedCommandById(String supportedCommandId);
}
