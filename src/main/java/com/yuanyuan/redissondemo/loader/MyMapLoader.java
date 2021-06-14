package com.yuanyuan.redissondemo.loader;

import cn.hutool.core.util.StrUtil;
import org.redisson.api.map.MapLoader;

/**
 * @author FAYUAN.PENG
 * @version \$Id: MyMapLoader.java,  2021-05-21 17:29 FAYUAN.PENG Exp $$
 */
public class MyMapLoader implements MapLoader<String, Object> {

    @Override
    public Object load(String key) {
        if (StrUtil.equalsIgnoreCase("user", key)) {

        }
        return null;
    }

    @Override
    public Iterable loadAllKeys() {
        return null;
    }
}
