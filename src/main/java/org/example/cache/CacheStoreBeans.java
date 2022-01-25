package org.example.cache;

import org.example.dto.response.wts.CouponCoreList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.common.cache.CacheStore;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheStoreBeans {
    @Bean
    public CacheStore<CouponCoreList> couponCoreListCacheStore() {
        return new CacheStore<CouponCoreList>(10, TimeUnit.SECONDS, 500);
    }
}
