package cn.edu.hhu.its.user.service.common.cache;

import cn.edu.hhu.spring.boot.starter.common.cache.CachePrefix;

public interface UserCachePrefix extends CachePrefix {
    //token String类型 token:access:{userId}
    String ACCESS_TOKEN="token:access:";
    //权限 list类型 auth:permission:{userId}
    String PERMISSIONS="auth:permission:";
}
