package com.oup.cachedemo.sbcachedemo.util;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheEventLogger implements CacheEventListener<Object, Object> {@Override
    public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent) {
        // TODO Auto-generated method stub
        log.info("Key : "+
          cacheEvent.getKey()+
          "OLD_VAL : "+cacheEvent.getOldValue()+
          "NEW_VAL : "+cacheEvent.getNewValue());
        
    }
    
}
