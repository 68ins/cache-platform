package com.newegg.ec.cache.plugin.docker;

import com.newegg.ec.cache.Application;
import com.newegg.ec.cache.plugin.basemodel.StartType;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lf52 on 2018/5/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class DockerOptionTest {

    @Autowired
    private DockerManager docker;

    @Test
    public void testgetContainerInfo() {
        System.out.println(docker.getContainerInfo("10.16.46.170", "aaaaa_8512"));
    }

    @Test
    public void testcreateContainer() {
        JSONObject reqObject = new JSONObject();
        reqObject.put("image", "ssecbigdata02:5000/redis4.0.1");
        reqObject.put("container_name", "leoredistest");
        reqObject.put("container_volume", "/data/redis");
        reqObject.put("machine_volume", "/data/redis");
        reqObject.put("network_mode", "host");
        reqObject.put("restart_policy", "always");
        reqObject.put("cmd", "/redis/redis-4.0.1/start.sh 7001 10.16.46.170");
        System.out.println(docker.createContainer("10.16.46.170", reqObject));
    }
    @Test
    public void optionContainer() {
        System.out.println(docker.optionContainer("10.16.46.170", "leoredistest", StartType.stop));
    }

    @Test
    public void removeContainer() {
        System.out.println(docker.deleteContainer("10.16.46.170", "leoredistest"));
    }
}
