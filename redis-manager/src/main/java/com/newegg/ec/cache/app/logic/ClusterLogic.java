package com.newegg.ec.cache.app.logic;

import com.newegg.ec.cache.app.component.RedisManager;
import com.newegg.ec.cache.app.dao.IClusterDao;
import com.newegg.ec.cache.app.dao.impl.NodeInfoDao;
import com.newegg.ec.cache.app.model.Cluster;
import com.newegg.ec.cache.app.model.Common;
import com.newegg.ec.cache.app.model.Host;
import com.newegg.ec.cache.app.model.RedisQueryParam;
import com.newegg.ec.cache.app.util.JedisUtil;
import com.newegg.ec.cache.app.util.NetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by gl49 on 2018/4/21.
 */
@Component
public class ClusterLogic {
    @Autowired
    private IClusterDao clusterDao;
    @Autowired
    private NodeInfoDao nodeInfoTable;
    @Resource
    private RedisManager redisManager;

    public Object query(RedisQueryParam redisQueryParam){
        return redisManager.query( redisQueryParam );
    }

    public Cluster getCluster(int id){
        return clusterDao.getCluster( id );
    }

    public List<Cluster> getClusterList(String group){
        return clusterDao.getClusterList( group );
    }

    public boolean removeCluster(int id){
        boolean res = false;
        try {
            clusterDao.removeCluster(id);
            String tableName = Common.NODE_INFO_TABLE_FORMAT + id;
            nodeInfoTable.dropTable( tableName );
            res = true;
        }catch (Exception e){

        }
        return res;
    }

    public boolean addCluster(Cluster cluster){
        boolean res = false;
        try {
            int row = clusterDao.addCluster(cluster);
            if (row > 0){
                cluster.setAddress(cluster.getAddress());
                cluster.setUserGroup(cluster.getUserGroup());
                cluster.setClusterName(cluster.getClusterName());
                nodeInfoTable.createTable(Common.NODE_INFO_TABLE_FORMAT + cluster.getId());
                res = true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public Map<String, String> getClusterInfo(String ip, int port){
        return redisManager.getClusterInfo(ip, port);
    }

    public Map<String, String> getClusterInfo(String address){
        List<Host> host = NetUtil.getHostByAddress( address );
        return redisManager.getClusterInfo(host.get(0).getIp(), host.get(0).getPort());
    }

    public Map<String, String> getNodeInfo(String address){
        Host host = NetUtil.getHostPassAddress( address );
        return redisManager.getMapInfo(host.getIp(), host.getPort());
    }

    public Map<String, String> getRedisConfig(String address){
        Host host = NetUtil.getHostPassAddress( address );
        return redisManager.getRedisConfig(host.getIp(), host.getPort() );
    }

    public List<Map<String, String>> nodeList(String address){
        Host host = NetUtil.getHostPassAddress( address );
        List<Map<String, String>> list = JedisUtil.nodeList( host.getIp(), host.getPort() );
        return list;
    }

    public Host getClusterHost(int id) {
        Cluster cluster = getCluster(id);
        String addressStr = cluster.getAddress();
        final String[] addressList = addressStr.split(",");
        // TODO: 多节点时，依次判断节点可用性，返回第一个可用节点
        String hostStr = addressList[0];
        String[] ipAndPort = hostStr.split(":");
        Host host = new Host();
        host.setIp(ipAndPort[0]);
        host.setPort(Integer.parseInt(ipAndPort[1]));
        return host;
    }

    public Map<String,Map> detailNodeList(String address) {
        Host host = NetUtil.getHostPassAddress( address );
        Map<String, Map> result = JedisUtil.getClusterNodes( host.getIp(), host.getPort() );
        return result;
    }
}