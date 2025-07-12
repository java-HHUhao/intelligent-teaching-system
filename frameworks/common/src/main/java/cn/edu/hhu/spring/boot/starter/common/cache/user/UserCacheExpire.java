package cn.edu.hhu.spring.boot.starter.common.cache.user;

import cn.edu.hhu.spring.boot.starter.common.cache.CacheExpire;

public interface UserCacheExpire extends CacheExpire {
    long USER_PERMISSION_EXPIRE = 60 * 60;
}
