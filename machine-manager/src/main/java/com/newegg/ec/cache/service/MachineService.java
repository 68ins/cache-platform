package com.newegg.ec.cache.service;

import com.newegg.ec.cache.model.entity.Machine;

import java.util.List;
import java.util.Map;

/**
 * Created by lf52 on 2018/3/16.
 */
public interface MachineService {

    public Machine getMachineByIp(String ip);

    public List<Machine> getMachineList(Map<String, Object > param);

    public boolean Add(Machine machine);

    public boolean Update(Machine machine);

    public boolean Delete(String ip);
}
