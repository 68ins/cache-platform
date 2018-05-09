package com.newegg.ec.cache.plugin.humpback;

import com.google.common.collect.Lists;
import com.newegg.ec.cache.app.model.Cluster;
import com.newegg.ec.cache.app.model.RedisNode;
import com.newegg.ec.cache.app.model.User;
import com.newegg.ec.cache.app.util.*;
import com.newegg.ec.cache.core.logger.CommonLogger;
import com.newegg.ec.cache.plugin.INodeOperate;
import com.newegg.ec.cache.plugin.INodeRequest;
import com.newegg.ec.cache.plugin.basemodel.Node;
import com.newegg.ec.cache.plugin.basemodel.PluginParent;
import com.newegg.ec.cache.plugin.basemodel.StartType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by lzz on 2018/4/20.
 */
@Component
public class HumpbackManager extends PluginParent implements INodeOperate,INodeRequest {

    CommonLogger logger = new CommonLogger( HumpbackManager.class );

    static ExecutorService executorService = Executors.newFixedThreadPool(100);
    @Value("${cache.humpback.image}")
    private String humpbackImage;
    @Value("${cache.humpback.api.format}")
    private String humpbackApiFormat;

    private static final String CONTAINER_OPTION_API = "containers";
    private static final String IMAGE_OPTION_API = "images";

    @Autowired
    IHumpbackNodeDao humpbackNodeDao;

    public HumpbackManager(){

    }

    /**
     *  pull image
     * @param pullParam
     * @return
     */
    @Override
    public boolean pullImage(JSONObject pullParam) {

        String ipStr = pullParam.getString("iplist");
        Set<String> ipSet = JedisUtil.getIPList(ipStr);
        String imageUrl = pullParam.getString("imageUrl");

        List<Future<Boolean>> futureList = new ArrayList<>();
        for(String ip : ipSet){
            Future<Boolean> future = executorService.submit( new PullImageTask(ip, imageUrl) );
            futureList.add( future );
        }
        for(Future<Boolean> future : futureList){
            try {
                if(!future.get()){
                    //有一台机器pull image 失败返回true
                    return false;
                }
            } catch (Exception e) {
                logger.error("",e);
            }
        }
        return true;
    }

    /**
     * cluster install
     * @param installParam
     * @return
     */
    @Override
    public boolean install(JSONObject installParam) {
        return installTemplate(this, installParam);
    }

    /**
     * node start
     * @param startParam
     * @return
     */
    @Override
    public boolean start(JSONObject startParam) {
        String ip = startParam.getString("ip");
        String containerId = startParam.getString("containerId");
        return optionContainer(ip, containerId, StartType.start);
    }

    /**
     * node stop
     * @param stopParam
     * @return
     */
    @Override
    public boolean stop(JSONObject stopParam) {
        String ip = stopParam.getString("ip");
        String containerId = stopParam.getString("containerId");
        return optionContainer(ip, containerId, StartType.stop);
    }

    /**
     *  node restart
     * @param restartParam
     * @return
     */
    @Override
    public boolean restart(JSONObject restartParam) {
        stop(restartParam);
        start(restartParam);
        return true;
    }

    /**
     * node remove
     * @param removePram
     * @return
     */
    @Override
    public boolean remove(JSONObject removePram) {
        System.out.println(removePram);
       // logger.websocket( removePram.toString() );
        String ip = removePram.getString("ip");
        String containerId = removePram.getString("containerId");
        try {
            String url = getApiAddress( ip )+CONTAINER_OPTION_API;
            if( HttpClientUtil.getDeleteResponse(url, containerId ) == null) {
                return false;
            }
        } catch (IOException e) {
            logger.error("", e);
            return false;
        }
        return true;
    }

    /**
     * get image list from .yml
     * @return
     */
    @Override
    public List<String> getImageList() {
        User user  = RequestUtil.getUser();
        System.out.println(user);
        return Lists.newArrayList(humpbackImage.split(","));
    }

    /**
     * show node list table in wesite
     * @param clusterId
     * @return
     */
    @Override
    public List<Node> getNodeList(int clusterId) {
        List<Node> list = humpbackNodeDao.getHumbackNodeList(clusterId);
        return list;
    }

    @Override
    public String showInstall() {
        return "plugin/humpback/humpbackCreateCluster";
    }

    @Override
    public String showManager() {
        return "plugin/humpback/humpbackNodeManager";
    }

    public String getApiAddress(String ip){
        return String.format(humpbackApiFormat, ip);
    }

    /**
     * humpback 生成redis节点
     * @param reqParam
     * @param nodelist
     */
    @Override
    protected void installNodeList(JSONObject reqParam, List<RedisNode> nodelist) {
        String image = reqParam.getString("imageUrl");

        List<Future<Boolean>> futureList = new ArrayList<>();
        nodelist.forEach(node -> {
            String ip = String.valueOf(node.getIp());
            String port = String.valueOf(node.getPort());
            String name = "redis" + port;
            String command = ip + ":" + port;
            JSONObject reqObject = generateInstallObjerct(image, name, command);
            futureList.add(executorService.submit(new RedisInstallTask(ip, reqObject)));
        });

        for(Future<Boolean> future : futureList){
            try {
                future.get();
            } catch (Exception e) {
                logger.error("",e);
            }
        }
        logger.websocket("redis cluster node install success");
    }

    /**
     * 生成install node所需要的参数
     * @param image
     * @param name
     * @param command
     * @return
     */
    private JSONObject generateInstallObjerct(String image, String name, String command){

        JSONObject reqObject =  new JSONObject();

        reqObject.put("Image", image);
        JSONArray volumes = new JSONArray();
        JSONObject volumeObj = new JSONObject();
        volumeObj.put("ContainerVolume", "/data/redis");
        volumeObj.put("HostVolume", "/data/redis");
        volumes.add(volumeObj);
        reqObject.put("Volumes", volumes);
        reqObject.put("NetworkMode", "host");
        reqObject.put("RestartPolicy", "always");
        reqObject.put("CPUShares", 0);
        reqObject.put("Memory", 0);
        reqObject.put("Name", name);
        reqObject.put("Command", command);

        return reqObject;
    }

    /**
     * table cluster 写入数据
     * @param reqParam
     * @return 返回-1 写入数据失败
     */
    @Override
    protected int addCluster(JSONObject reqParam) {
        String ipListStr = reqParam.getString(IPLIST_NAME);
        List<RedisNode> nodelist = JedisUtil.getInstallNodeList(ipListStr);
        RedisNode node = new RedisNode();
        for(RedisNode redisNode : nodelist){
            if(NetUtil.checkIpAndPort(redisNode.getIp(),redisNode.getPort())){
                node = redisNode;
                break;
            }
        }
        if(StringUtils.isNotEmpty(node.getIp())){
            Cluster cluster = new Cluster();
            cluster.setAddress(node.getIp() + ":" + node.getPort());
            cluster.setUserGroup(reqParam.get("group").toString());
            cluster.setClusterType(reqParam.get("pluginType").toString());
            cluster.setClusterName(reqParam.get("clusterName").toString());
            if( clusterDao.addCluster(cluster) == 1){
                return cluster.getId();
            }
        }
        return -1;
    }

    /**
     * table humpback_node 写入数据
     * @param reqParam
     * @param clusterId
     */
    @Override
    protected void addNodeList(JSONObject reqParam, int clusterId) {
        String ipListStr = reqParam.getString(IPLIST_NAME);
        String image = reqParam.getString(IMAGE_URL);
        List<RedisNode> nodelist = JedisUtil.getInstallNodeList(ipListStr);
        nodelist.forEach(node -> {
            HumpbackNode humpbackNode = new HumpbackNode();
            humpbackNode.setClusterId(clusterId);
            humpbackNode.setContainerName("redis" + node.getPort());
            humpbackNode.setUserGroup(reqParam.get("group").toString());
            humpbackNode.setImage(image);
            humpbackNode.setIp(node.getIp());
            humpbackNode.setPort(node.getPort());
            humpbackNode.setAddTime(DateUtil.getTime());
            humpbackNodeDao.addHumbackNode(humpbackNode);
        });

    }

    /**
     * container option
     * @param ip
     * @param containerName
     * @param startType
     * @return
     */
    public boolean optionContainer(String ip,String containerName, StartType startType) {
        JSONObject object = new JSONObject();
        object.put("Action",startType);
        object.put("Container",containerName);
        try {
            String url = getApiAddress(ip) + CONTAINER_OPTION_API;
            if(HttpClientUtil.getPutResponse(url, object)==null){
                return false;
            }
        } catch (IOException e) {
            logger.error("", e);
            return false;
        }
        return true;
    }

    /**
     * create  container 创建失败会返回一个空的json串
     * @param ip
     * @param param
     * @return
     */
    public JSONObject createContainer(String ip, JSONObject param) {

        String response = null;
        try {
            String url = getApiAddress(ip) + CONTAINER_OPTION_API;
            response = HttpClientUtil.getPostResponse(url,param);
        } catch (IOException e) {
            logger.error("",e);
        }
        return JSONObject.fromObject(response);
    }

    /**
     * pull images task
     */
    class PullImageTask implements Callable<Boolean> {
        private String image;
        private String ip;
        public PullImageTask(String ip, String image){
            this.ip = ip;
            this.image = image;
        }

        @Override
        public Boolean call() throws Exception {
            boolean res = true;
            try {
                JSONObject reqObject = new JSONObject();
                reqObject.put("Image", this.image);
                String url = getApiAddress(ip)+IMAGE_OPTION_API;
                HttpClientUtil.getPostResponse(url, reqObject);
            }catch (Exception e){
                res = false;
            }
            return res;
        }
    }

    class RedisInstallTask implements Callable<Boolean> {

        private String ip;
        private JSONObject installObj ;
        public RedisInstallTask(String ip, JSONObject installObj){
            this.ip = ip;
            this.installObj = installObj;
        }
        @Override
        public Boolean call() throws Exception {
            if(createContainer(ip,installObj) != null){
                return true;
            }
            return false;
        }
    }
}
