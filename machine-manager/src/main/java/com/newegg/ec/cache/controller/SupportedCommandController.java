package com.newegg.ec.cache.controller;

import com.alibaba.fastjson.JSONObject;
import com.newegg.ec.cache.model.entity.SupportedCommand;
import com.newegg.ec.cache.service.SupportedCommandService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jay.H.Zou
 * @date 2018/4/14
 */
@Controller
@RequestMapping("/rest/supportedCommand/*")
public class SupportedCommandController {

    @Autowired
    private SupportedCommandService supportedCommandService;

    @RequestMapping(value = "/addSupportedCommand", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addSupportedCommand(HttpServletRequest request) throws IOException {
        String command = IOUtils.toString(request.getInputStream(), "UTF-8");
        return supportedCommandService.addSupportedCommand(command);
    }

    @RequestMapping(value = "/getSupportedCommandList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getSupportedCommandList(HttpServletRequest request){
        List<SupportedCommand> supportedCommandList = supportedCommandService.getSupportedCommandList();

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        if (supportedCommandList != null && supportedCommandList.size() > 0){
            result.put("code", 1);
            result.put("supportedCommandList", supportedCommandList);
            System.out.println(result);
            }
        return result;
    }

    @RequestMapping(value = "/deleteSupportedCommandById", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteSupportedCommandById(HttpServletRequest request) throws IOException {
        String supportedCommandId = IOUtils.toString(request.getInputStream(), "UTF-8");
        JSONObject jsonObject = new JSONObject();
        int row = supportedCommandService.deleteSupportedCommandById(supportedCommandId);
        jsonObject.put("code", row);
        return jsonObject;
    }

}
