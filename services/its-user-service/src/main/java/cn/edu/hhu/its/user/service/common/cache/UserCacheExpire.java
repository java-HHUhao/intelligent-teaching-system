package cn.edu.hhu.its.user.service.common.cache;

import cn.edu.hhu.spring.boot.starter.common.cache.CacheExpire;

public interface UserCacheExpire extends CacheExpire {
    long USER_PERMISSION_EXPIRE = 60 * 60;
}
