package com.newegg.ec.cache.plugin.basemodel;

import com.newegg.ec.cache.app.dao.IClusterDao;
import com.newegg.ec.cache.app.util.JedisUtil;
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

    public boolean installTemplate(PluginParent pluginParent, JSONObject reqParam){
        String ipListStr = reqParam.getString( IPLIST_NAME );
        Map<Map<String, String>, List<Map<String, String>>> ipMap = JedisUtil.getInstallNodeMap( ipListStr );
        Set<String> ipSet = JedisUtil.getIPList( ipListStr );
        boolean res = true;
        int clusterId = pluginParent.addCluster(reqParam);
        // 安装节点
        pluginParent.installNodeList(reqParam, ipSet);
        // 判断节点是否成功
        boolean checkRes = pluginParent.checkInstallResult( ipSet );
        // 成功就更新 cluster address
        if( checkRes ){
            // 建立集群
            pluginParent.buildRedisCluster( ipMap );
        }else{ // 不成功就删除对应的 cluster
            res = false;
           clusterDao.removeCluster( clusterId );
        }
        return res;
    }

    protected abstract void buildRedisCluster(Map<Map<String, String>, List<Map<String, String>>> ipMap);

    protected abstract boolean checkInstallResult(Set<String> ipSet);

    protected abstract void installNodeList(JSONObject reqParam, Set<String> ipSet);

    protected abstract int addCluster(JSONObject reqParam);
}
