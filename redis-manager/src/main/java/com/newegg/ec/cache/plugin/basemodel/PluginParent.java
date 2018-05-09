package com.newegg.ec.cache.plugin.basemodel;

import com.newegg.ec.cache.app.component.RedisManager;
import com.newegg.ec.cache.app.dao.IClusterDao;
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
    @Resource
    protected IClusterDao clusterDao;
    @Resource
    private RedisManager redisManager;

    public boolean installTemplate(PluginParent pluginParent, JSONObject reqParam){
        String ipListStr = reqParam.getString(IPLIST_NAME);
        Map<Map<String, String>, List<Map<String, String>>> ipMap = JedisUtil.getInstallNodeMap(ipListStr);
        Set<String> ipSet = JedisUtil.getIPList( ipListStr );
        boolean res = false;
        // 安装节点
        pluginParent.installNodeList(reqParam, ipSet);
        // 判断节点是否成功
        boolean checkRes = pluginParent.checkInstallResult( ipSet );
        // 成功就更新 cluster address
        if( checkRes ){
            res = true;
            // 建立集群
            pluginParent.buildRedisCluster( ipMap );
        }
        if( res ){ // 如果安装成功
            int clusterId = pluginParent.addCluster(reqParam);
            pluginParent.addNodeList(reqParam, clusterId);    
        }
        return res;
    }

    protected abstract void addNodeList(JSONObject reqParam, int clusterId);

    protected abstract void installNodeList(JSONObject reqParam, Set<String> ipSet);

    protected abstract int addCluster(JSONObject reqParam);

    protected void buildRedisCluster(Map<Map<String, String>, List<Map<String, String>>> ipMap){
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
