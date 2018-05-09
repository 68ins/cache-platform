package com.newegg.ec.cache.plugin.machine;

import com.newegg.ec.cache.plugin.INodeOperate;
import com.newegg.ec.cache.plugin.INodeRequest;
import com.newegg.ec.cache.plugin.basemodel.*;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lzz on 2018/4/20.
 */
@Component
public class MachineManager implements INodeOperate,INodeRequest {

    private int userId;
    public MachineManager(){
        //ignore
    }
    public MachineManager(int userId){
        this.userId = userId;
    }


    @Override
    public boolean pullImage(JSONObject pullParam) {
        return false;
    }

    @Override
    public boolean install(JSONObject installParam) {
        return false;
    }

    @Override
    public boolean start(JSONObject startParam) {
        return false;
    }

    @Override
    public boolean stop(JSONObject stopParam) {
        return false;
    }

    @Override
    public boolean restart(JSONObject restartParam) {
        return false;
    }

    @Override
    public boolean remove(JSONObject removePram) {
        return false;
    }


    @Override
    public List<String> getImageList() {
        return null;
    }

    @Override
    public List<Node> getNodeList(int clusterId) {
        return null;
    }

    @Override
    public String showInstall() {
        return "plugin/machine/machineCreateCluster";
    }

    @Override
    public String showManager() {
        return "plugin/machine/machineNodeManager";
    }
}
