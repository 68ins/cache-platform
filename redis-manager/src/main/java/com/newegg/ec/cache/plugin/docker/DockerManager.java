package com.newegg.ec.cache.plugin.docker;

import com.google.common.collect.Lists;
import com.newegg.ec.cache.app.util.HttpClientUtil;
import com.newegg.ec.cache.core.logger.CommonLogger;
import com.newegg.ec.cache.plugin.INodeOperate;
import com.newegg.ec.cache.plugin.INodeRequest;
import com.newegg.ec.cache.plugin.basemodel.Node;
import com.newegg.ec.cache.plugin.basemodel.StartType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by lzz on 2018/4/20.
 */
@Component
public class DockerManager implements INodeOperate,INodeRequest {

    private static CommonLogger logger = new CommonLogger( DockerManager.class );
    private static final String PROTOCOL = "http://";
    private static final String DOCKER_REPOSITORY_HOST = "10.16.46.170";
    private static final String DOCKER_REPOSITORY_PORT = "5000";
    private static final String DOCKER_RESTFUL_PORT = "2375";
    private static final String DOCKER_REPOSITORY_URL = PROTOCOL + DOCKER_REPOSITORY_HOST + ":" + DOCKER_REPOSITORY_PORT;
    @Value("${cache.docker.image}")
    private String images;

    public DockerManager(){
        //ignore
    }

    @Autowired
    IDockerNodeDao dockerNodeDao;

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
        return Lists.newArrayList( images.split(",") );
    }

    @Override
    public List<Node> getNodeList(int clusterId) {
        return dockerNodeDao.getDockerNodeList(clusterId);
    }

    @Override
    public String showInstall() {
        return "plugin/docker/dockerCreateCluster";
    }

    @Override
    public String showManager() {
        return "plugin/docker/dockerNodeManager";
    }

    /**
     * 获取container信息
     * 用于判断当前名称的容器是否存在
     * @param ip
     * @param containerId
     * @return
     */
    public JSONObject getContainerInfo(String ip, String containerId) {
        String res = null;
        try {
            String url = getContainerApi(ip);
            res = HttpClientUtil.getGetResponse(url, containerId + "/json");
        } catch (Exception e) {
            logger.error( "", e );
        }
        JSONObject result = JSONObject.fromObject(res);
        return result;
    }

    /**
     * todo 404
     * create  container 创建失败会返回一个空的json串
     * @param ip
     * @param param
     * @return
     */
    public JSONObject createContainer(String ip,JSONObject param){

        //返回的结果
        JSONObject result = new JSONObject();
        //获取image
        String strImage = DOCKER_REPOSITORY_HOST + ":" + DOCKER_REPOSITORY_PORT + "/" + param.getString("image");
        JSONObject installObject = generateInstallObject(param);
        try {
            //镜像会先拉去到指定的机器上
            String containerName = param.getString("container_name");
            String postUrl = getContainerApi(ip) + "create";
            String responseStr = HttpClientUtil.getPostResponse( postUrl, installObject );
            JSONObject dockerResult = JSONObject.fromObject( responseStr );
            String container_id = dockerResult.getString("Id");
            System.out.println( dockerResult );
            optionContainer(ip,container_id,StartType.start);
            result.put("result", true);
            result.put("container_id", container_id);
        } catch (IOException e) {
            result.put("result", false );
            logger.error("", e);
        }

        return result;
    }

    /**
     * container操作 : start stop restart
     * @param ip
     * @param containerName
     * @param startType
     * @return
     */
    public boolean optionContainer(String ip,String containerName, StartType startType) {
        try {
            HttpClientUtil.getPostResponse(getContainerApi(ip) + containerName + "/" + startType, new JSONObject());
        }catch (Exception e){
            logger.error("", e);
            return false;
        }
        return true;
    }

    /**
     * 构建生成容器所需要的参数
     * @param reqObject
     * @return
     */
    private JSONObject generateInstallObject(JSONObject reqObject){

        JSONObject req = new JSONObject();
        //image的完整路径
        String strImage = DOCKER_REPOSITORY_HOST + ":" + DOCKER_REPOSITORY_PORT + "/" + reqObject.getString("image");
        req.put("Image", strImage);
        req.put("Name", reqObject.getString("container_name"));
        JSONObject hostConfig = new JSONObject();
        hostConfig.put("NetworkMode", reqObject.getString("network_mode"));
        JSONObject restartPolicy = new JSONObject();
        restartPolicy.put("Name", reqObject.getString("restart_policy"));
        hostConfig.put("RestartPolicy", restartPolicy);
        if( !StringUtils.isBlank(reqObject.getString("machine_volume")) && !StringUtils.isBlank(reqObject.getString("container_volume"))){
            JSONArray binds = new JSONArray();
            String bindStr = reqObject.getString("machine_volume") + ":" + reqObject.getString("container_volume");
            binds.add( bindStr );
            hostConfig.put("Binds", binds);
        }
        req.put("HostConfig", hostConfig);
        String command = reqObject.getString("cmd");
        String[] cmds = command.split("\\s+");
        req.put("Cmd", cmds);

        return req;
    }

    private String getContainerApi(String ip){
        return getDockerRestfullApi(ip) + "/containers/";
    }
    private String getImageApi(String ip){
        return getDockerRestfullApi(ip) + "/images/";
    }
    private String getDockerRestfullApi(String ip){
        return PROTOCOL + ip + ":" + DOCKER_RESTFUL_PORT;
    }

    private String getRegistoryV2Url(){
        return DOCKER_REPOSITORY_URL + "/v2/";
    }
}
