package com.newegg.ec.cache.plugin.humpback;

import com.newegg.ec.cache.Application;
import com.newegg.ec.cache.plugin.basemodel.StartType;
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
public class HumpackOptionTest {

    @Autowired
    private HumpbackManager humpback;

    @Test
    public void testoptionContainer() {
        boolean result = humpback.optionContainer("172.16.35.219", "itemService_test_Log", StartType.start);
        System.out.println("result "+result);
    }


    @Test
    public void testDelContainer() {
        boolean stop = humpback.optionContainer("172.16.35.219", "itemService_test_Log", StartType.stop);
    }
}
