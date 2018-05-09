package com.newegg.ec.cache.plugin.basemodel;

import com.newegg.ec.cache.app.component.RedisManager;
import com.newegg.ec.cache.app.dao.IClusterDao;
import com.newegg.ec.cache.app.logic.ClusterLogic;
import com.newegg.ec.cache.app.model.RedisNode;
import com.newegg.ec.cache.app.util.JedisUtil;
import com.newegg.ec.cache.app.util.NetUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by gl49 on 2018/5/9.
 */
@Component
public abstract class PluginParent {
    public static final String IPLIST_NAME = "iplist";
    public static final String IMAGE_URL = "imageUrl";
    @Resource
    protected ClusterLogic clusterLogic;

    @Resource
    private RedisManager redisManager;

    public boolean installTemplate(PluginParent pluginParent, JSONObject reqParam){
        String ipListStr = reqParam.getString(IPLIST_NAME);
        Map<RedisNode, List<RedisNode>> ipMap = JedisUtil.getInstallNodeMap(ipListStr);
        Set<String> ipSet = JedisUtil.getIPList(ipListStr);
        List<RedisNode> nodelist = JedisUtil.getInstallNodeList(ipListStr);
        boolean res = false;
        // 安装节点
        pluginParent.installNodeList(reqParam, nodelist);
        // 判断节点是否成功
        boolean checkRes = pluginParent.checkInstallResult( ipSet );
        // 成功就更新 cluster address
        if( checkRes ){
            // 建立集群
            pluginParent.buildRedisCluster( ipMap );
            res = true;
        }
        if( res ){ // 如果安装成功
            int clusterId = pluginParent.addCluster(reqParam);
            if(clusterId != -1){
                pluginParent.addNodeList(reqParam, clusterId);
            }

        }
        return res;
    }

    protected abstract void addNodeList(JSONObject reqParam, int clusterId);

    protected abstract void installNodeList(JSONObject reqParam, List<RedisNode> nodelist);

    protected abstract int addCluster(JSONObject reqParam);

    protected void buildRedisCluster(Map<RedisNode, List<RedisNode>> ipMap){
        redisManager.buildCluster( ipMap );
    }

    protected boolean checkInstallResult(Set<String> ipSet){
        boolean res = false;
        for(String host : ipSet){
            res = NetUtil.checkHost( host );
            if( res ){
                break;
            }
        }
        return res;
    }
}
