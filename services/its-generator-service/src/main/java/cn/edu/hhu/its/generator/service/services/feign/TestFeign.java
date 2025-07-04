package cn.edu.hhu.its.generator.service.services.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "its-user-service",path = "/user/common")
public interface TestFeign {

}
