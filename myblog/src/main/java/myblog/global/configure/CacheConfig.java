package myblog.global.configure;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() throws Exception {
        // 인터페이스 제공 객체 생성
        CachingProvider cachingProvider = Caching.getCachingProvider();

        // 인터페이스에 사용할 구현체 주입 => 원하는 구현체를 갈아끼는 부분
        javax.cache.CacheManager jCacheManager = cachingProvider.getCacheManager(
                getClass().getResource("/ehcache.xml").toURI(),
                getClass().getClassLoader()
        );

        // 캐시 이름 출력 (디버깅용)
        System.out.println("Loaded caches: " + jCacheManager.getCacheNames());

        // Spring CacheManager로 JCache 연동
        return new JCacheCacheManager(jCacheManager);
    }
}
