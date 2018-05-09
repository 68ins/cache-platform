package com.newegg.ec.cache.plugin.machine;

import com.newegg.ec.cache.app.model.RedisNode;
import com.newegg.ec.cache.plugin.INodeOperate;
import com.newegg.ec.cache.plugin.INodeRequest;
import com.newegg.ec.cache.plugin.basemodel.Node;
import com.newegg.ec.cache.plugin.basemodel.PluginParent;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lzz on 2018/4/20.
 */
@Component
public class MachineManager extends PluginParent implements INodeOperate,INodeRequest {
    @Value("${cache.machine.image}")
    private String machineImage;

    @Autowired
    IMachineNodeDao  machineNodeDao;

    public MachineManager(){
        //ignore
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
        return machineNodeDao.getMachineNodeList(clusterId);
    }

    @Override
    public String showInstall() {
        return "plugin/machine/machineCreateCluster";
    }

    @Override
    public String showManager() {
        return "plugin/machine/machineNodeManager";
    }

    @Override
    protected void addNodeList(JSONObject reqParam, int clusterId) {

    }

    @Override
    protected void installNodeList(JSONObject reqParam, List<RedisNode> nodelist) {

    }

    @Override
    protected int addCluster(JSONObject reqParam) {
        return 0;
    }
}
