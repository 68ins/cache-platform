package com.newegg.ec.cache.app.controller;

import com.newegg.ec.cache.app.component.NodeManager;
import com.newegg.ec.cache.app.model.Common;
import com.newegg.ec.cache.app.model.Response;
import com.newegg.ec.cache.app.model.User;
import com.newegg.ec.cache.core.userapi.UserAccess;
import com.newegg.ec.cache.plugin.INodeOperate;
import com.newegg.ec.cache.plugin.basemodel.Node;
import com.newegg.ec.cache.plugin.basemodel.NodeRequestPram;
import com.newegg.ec.cache.plugin.basemodel.PluginType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lzz on 2018/4/20.
 */
@Controller
@RequestMapping("/node")
@UserAccess
public class NodeController {
    private INodeOperate nodeOperate;
    @Resource
    private NodeManager nodeManager;

    @RequestMapping("/selectClusterType")
    public String selectType(Model model) {
        return "selectClusterType";
    }

    @RequestMapping("/install")
    public String cluster(Model model,@SessionAttribute(Common.SESSION_USER_KEY) User user) {
        model.addAttribute("user", user);
        return "installNode";
    }

    @RequestMapping("/manager")
    public String manager(Model model, @SessionAttribute(Common.SESSION_USER_KEY) User user) {
        model.addAttribute("user", user);
        return "nodeManager";
    }

    @RequestMapping(value = "/getImageList", method = RequestMethod.GET)
    @ResponseBody
    public Response getImageList(@RequestParam PluginType pluginType){
        nodeOperate = nodeManager.factoryOperate(pluginType);
        List<String> imageList = nodeOperate.getImageList();
        return Response.Result(Response.DEFAULT, imageList);
    }

    @RequestMapping(value = "/getNodeList", method = RequestMethod.GET)
    @ResponseBody
    public Response getNodeList(@RequestParam PluginType pluginType,@RequestParam int clusterId){
        nodeOperate = nodeManager.factoryOperate(pluginType);
        List<Node> nodeList = nodeOperate.getNodeList(clusterId);
        return Response.Result(Response.DEFAULT, nodeList);
    }

    @RequestMapping(value = "/nodeStart", method = RequestMethod.POST)
    @ResponseBody
    public Response nodeStart(@RequestBody NodeRequestPram nodeRequestPram){
        nodeOperate = nodeManager.factoryOperate( nodeRequestPram.getPluginType() );
        boolean res = nodeOperate.start( nodeRequestPram.getReq() );
        if( res  ){
            return Response.Success();
        }
        return Response.Warn("start fail");
    }

    @RequestMapping(value = "/nodeStop", method = RequestMethod.POST)
    @ResponseBody
    public Response nodeStop(@RequestBody NodeRequestPram nodeRequestPram){
        nodeOperate = nodeManager.factoryOperate( nodeRequestPram.getPluginType() );
        boolean res = nodeOperate.stop( nodeRequestPram.getReq() );
        if( res  ){
            return Response.Success();
        }
        return Response.Warn("stop fail");
    }

    @RequestMapping(value = "/nodeRestart", method = RequestMethod.POST)
    @ResponseBody
    public Response nodeRestart(@RequestBody NodeRequestPram nodeRequestPram){
        nodeOperate = nodeManager.factoryOperate( nodeRequestPram.getPluginType() );
        boolean res = nodeOperate.restart( nodeRequestPram.getReq() );
        if( res  ){
            return Response.Success();
        }
        return Response.Warn("restart fail");
    }


    @RequestMapping(value = "/nodeRemove", method = RequestMethod.POST)
    @ResponseBody
    public Response nodeRemove(@RequestBody NodeRequestPram nodeRequestPram){
        nodeOperate = nodeManager.factoryOperate( nodeRequestPram.getPluginType() );
        boolean res = nodeOperate.remove( nodeRequestPram.getReq() );
        if( res  ){
            return Response.Success();
        }
        return Response.Warn("delete fail");
    }

    @RequestMapping(value = "/nodeInstall", method = RequestMethod.POST)
    @ResponseBody
    public Response nodeInstall(@RequestBody NodeRequestPram nodeRequestPram){
        nodeOperate = nodeManager.factoryOperate( nodeRequestPram.getPluginType() );
        boolean res = nodeOperate.install( nodeRequestPram.getReq() );
        if( res  ){
            return Response.Success();
        }
        return Response.Warn("install fail");
    }

    @RequestMapping(value = "/nodePullImage", method = RequestMethod.POST)
    @ResponseBody
    public Response nodePullImage(@RequestBody NodeRequestPram nodeRequestPram){
        nodeOperate = nodeManager.factoryOperate( nodeRequestPram.getPluginType() );
        boolean res = nodeOperate.pullImage( nodeRequestPram.getReq() );
        if( res ){
            return Response.Result(Response.DEFAULT, res);
        }
        return Response.Warn("pull image is fail");
    }
}
