package org.zavod.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelCastCacheConfig {

    @Bean
    public Config authorConfig() {
        return cacheConfig("authorCache");
    }

    @Bean
    public Config mailConfig() {
        return cacheConfig("mailCache");
    }

    private Config cacheConfig(String cacheName) {
        return new Config().setInstanceName("hazelcast-instance")
                .addMapConfig(new MapConfig().setName(cacheName)
                        .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                        .setEvictionPolicy(EvictionPolicy.LRU).setTimeToLiveSeconds(600));
    }
}
