/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.yeweihui.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-07-17 21:12
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource(name="redisTemplate")
    private ValueOperations<String, String> valueOperations;
    @Resource(name="redisTemplate")
    private HashOperations<String, String, Object> hashOperations;
    @Resource(name="redisTemplate")
    private ListOperations<String, Object> listOperations;
    @Resource(name="redisTemplate")
    private SetOperations<String, Object> setOperations;
    @Resource(name="redisTemplate")
    private ZSetOperations<String, Object> zSetOperations;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    public void set(String key, Object value, long expire){
        valueOperations.set(key, toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }


    /*************************************************************************
     *                           zset  排名
     *************************************************************************/

    /**
     * 1. 新增元素
     * 添加一个元素, zset与set最大的区别就是每个元素都有一个score，因此有个排序的辅助功能;  zadd
     * @param key
     * @param value
     * @param score
     */
    public void add(String key, Object value, double score) {
        zSetOperations.add(key, value, score);
    }

    /**
     * 2. 删除元素
     * 删除元素 zrem
     * @param key
     * @param value
     */
    public void remove(String key, Object value) {
        zSetOperations.remove(key, value);
    }

    /**
     * 3. 修改score
     * score的增加or减少 zincrby
     * @param key
     * @param value
     * @param score
     */
    public Double incrScore(String key, Object value, double score) {
        return zSetOperations.incrementScore(key, value, score);
    }

    /**
     * 4. 获取value对应的score
     * 查询value对应的score   zscore
     * @param key
     * @param value
     * @return
     */
    public Double score(String key, Object value) {
        return zSetOperations.score(key, value);
    }

    /**
     * 5. 获取value在集合中排名
     * 判断value在zset中的排名  zrank
     * @param key
     * @param value
     * @return
     */
    public Long rank(String key, Object value) {
        return zSetOperations.rank(key, value);
    }

    /**
     * 6. 集合大小
     * 返回集合的长度
     * @param key
     * @return
     */
    public Long size(String key) {
        return zSetOperations.zCard(key);
    }

    //7. 获取集合中数据
    //因为是有序，所以就可以获取指定范围的数据，下面有两种方式
    //
    //根据排序位置获取数据
    //根据score区间获取排序位置

    /**
     * 查询集合中指定顺序的值， 0 -1 表示获取全部的集合内容  zrange
     * 返回有序的集合，score小的在前面
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> range(String key, int start, int end) {
        return zSetOperations.range(key, start, end);
    }

    /**
     * 查询集合中指定顺序的值和score，0, -1 表示获取全部的集合内容
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScore(String key, int start, int end) {
        return zSetOperations.rangeWithScores(key, start, end);
    }

    /**
     * 查询集合中指定顺序的值  zrevrange
     * 返回有序的集合中，score大的在前面
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> revRange(String key, int start, int end) {
        return zSetOperations.reverseRange(key, start, end);
    }

    /**
     * 根据score的值，来获取满足条件的集合  zrangebyscore
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Object> sortRange(String key, int min, int max) {
        return zSetOperations.rangeByScore(key, min, max);
    }
}
