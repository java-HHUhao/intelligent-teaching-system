package cn.edu.hhu.its.resource.service;

import cn.edu.hhu.spring.boot.starter.store.utils.MinioUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static cn.edu.hhu.spring.boot.starter.store.utils.MinioUtils.uploadFile;

@SpringBootTest
class ItsResourceDOServiceApplicationTests {

    @Test
    void contextLoads() throws FileNotFoundException {
    }

}
