package com.luochen.web.demo;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;




@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {


      // new Thread(new MailUtils("1329127421@qq.com",CodeUtils.generateUniqueCode()+CodeUtils.generateUniqueCode())).start();
    }

}
