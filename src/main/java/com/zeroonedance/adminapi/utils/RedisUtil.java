package com.zeroonedance.adminapi.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 给指定的 key 设置过期时间。
     *
     * @param key  要设置过期时间的键。
     * @param time 过期时间（秒）。
     * @return 如果成功设置过期时间，则返回true；否则返回false。
     */
    public boolean expire(String key, long time) {
        // 使用 RedisTemplate 设置键的过期时间。
        return Boolean.TRUE.equals(redisTemplate.expire(key, time, TimeUnit.SECONDS));
    }

    /**
     * 根据给定的 key 获取过期时间。
     *
     * @param key 要获取过期时间的键。
     * @return 过期时间（秒）。
     */
    public long getTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 检查 Redis 数据库中是否存在指定的键。
     *
     * @param key 要检查的键。
     * @return 如果键存在，则返回true；否则返回false。
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 移除指定键的过期时间。
     *
     * @param key 要移除过期时间的键
     * @return 如果成功移除过期时间，则返回 true；否则返回 false
     */
    public boolean persist(String key) {
        return Boolean.TRUE.equals(redisTemplate.boundValueOps(key).persist());
    }





    public void setObject(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }



    //- - - - - - - - - - - - - - - - - - - - -  String类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 根据指定键获取值。
     *
     * @param key 要获取值的键
     * @return 获取到的值
     */
    public Object get(String key) {
        // 如果键为 null，则返回 null；否则使用 redisTemplate 获取值并返回
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 将值设置到缓存中。
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, String value) {
        // 使用 redisTemplate 将值设置到缓存中

        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 将值放入缓存并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) -1为无期限
     */
    public void set(String key, String value, long time) {
        // 使用过期时间将值存储在缓存中
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 批量添加 key (重复的键会覆盖)
     *
     * @param keyAndValue
     */
    public void batchSet(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSet(keyAndValue);
    }

    /**
     * 批量添加 key-value 只有在键不存在时,才添加
     * map 中只要有一个key存在,则全部不添加
     *
     * @param keyAndValue
     */
    public void batchSetIfAbsent(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是长整型 ,将报错
     *
     * @param key
     * @param number
     */
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是 纯数字 ,将报错
     *
     * @param key
     * @param number
     */
    public Double increment(String key, double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    //- - - - - - - - - - - - - - - - - - - - -  set类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 将数据放入集合缓存
     *
     * @param key   键
     * @param value 值
     */
    public void sSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 获取集合中的所有元素。
     *
     * @param key 键
     * @return 集合中的所有元素
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 从集合中随机获取指定个数的元素。
     *
     * @param key   键
     * @param count 指定的元素个数
     * @return 随机获取的元素集合
     */
    public Set<Object> randomMembers(String key, long count) {
        return new HashSet<>(redisTemplate.opsForSet().randomMembers(key, count));
    }


    /**
     * 从集合中随机获取一个元素。
     *
     * @param key 键
     * @return 随机获取的元素
     */
    public Object randomMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 弹出并移除集合中的一个元素。
     *
     * @param key 键
     * @return 弹出的元素
     */
    public Object pop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 获取集合的大小。
     *
     * @param key 键
     * @return 集合的大小
     */
    public long size(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 检查集合中是否存在指定的值。
     *
     * @param key   键
     * @param value 值
     * @return true 存在，false 不存在
     */
    public boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 检查集合中是否存在指定的元素。
     *
     * @param key 键
     * @param obj 元素对象
     * @return true 存在，false 不存在
     */
    public boolean isMember(String key, Object obj) {
        return redisTemplate.opsForSet().isMember(key, obj);
    }

    /**
     * 将集合中的元素移动到目标集合。
     *
     * @param key     源集合键
     * @param value   元素对象
     * @param destKey 目标集合键
     * @return true 移动成功，false 移动失败
     */
    public boolean move(String key, String value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 批量移除集合中的元素。
     *
     * @param key    键
     * @param values 要移除的元素
     */
    public void remove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 计算两个集合的差集。
     *
     * @param key     第一个集合键
     * @param destKey 第二个集合键
     * @return 两个集合的差集
     */
    public Set<Object> difference(String key, String destKey) {
        return redisTemplate.opsForSet().difference(key, destKey);
    }


    //- - - - - - - - - - - - - - - - - - - - -  hash类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 向指定的哈希表中添加多个键值对。
     *
     * @param key 键
     * @param map 键值对的映射
     */
    public void addToHash(String key, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取指定键的哈希表中的所有字段和值。
     *
     * @param key 键
     * @return 哈希表中的字段和值映射
     */
    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 验证指定哈希表中是否存在指定的字段。
     *
     * @param key     键
     * @param hashKey 哈希表中的字段
     * @return 如果字段存在，则返回true；否则返回false。
     */
    public boolean hashHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取指定哈希表中指定字段的字符串值。
     *
     * @param key     键
     * @param hashKey 哈希表中的字段
     * @return 字段的字符串值
     */
    public String getHashStringValue(String key, String hashKey) {
        Object value = redisTemplate.opsForHash().get(key, hashKey);
        return (value != null) ? value.toString() : null;
    }

    /**
     * 获取指定哈希表中指定字段的整数值。
     *
     * @param key     键
     * @param hashKey 哈希表中的字段
     * @return 字段的整数值
     */
    public Integer getHashIntValue(String key, String hashKey) {
        Object value = redisTemplate.opsForHash().get(key, hashKey);
        return (value != null && value instanceof Integer) ? (Integer) value : null;
    }

    /**
     * 弹出并删除指定集合的一个元素。
     *
     * @param key 键
     * @return 被弹出的元素的字符串值
     */
    public String popValue(String key) {
        Object value = redisTemplate.opsForSet().pop(key);
        return (value != null) ? value.toString() : null;
    }

    /**
     * 删除指定哈希表中的一个或多个字段。
     *
     * @param key      键
     * @param hashKeys 要删除的字段列表
     * @return 删除成功的字段数量
     */
    public Long deleteHashFields(String key, String... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 对指定哈希表中的字段进行增减操作。
     *
     * @param key     键
     * @param hashKey 哈希表中的字段
     * @param number  要增减的数值
     * @return 增减后的字段值
     */
    public Long incrementHashField(String key, String hashKey, long number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 对指定哈希表中的字段进行增减操作。
     *
     * @param key     键
     * @param hashKey 哈希表中的字段
     * @param number  要增减的数值
     * @return 增减后的字段值
     */
    public Double incrementHashField(String key, String hashKey, Double number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 获取指定哈希表中的所有字段。
     *
     * @param key 键
     * @return 哈希表中的所有字段
     */
    public Set<Object> getHashFields(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取指定哈希表中的字段数量。
     *
     * @param key 键
     * @return 哈希表中的字段数量
     */
    public Long getHashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    //- - - - - - - - - - - - - - - - - - - - -  list类型 - - - - - - - - - - - - - - - - - - - -


    /**
     * 在指定键的列表左侧添加一个元素。
     *
     * @param key   列表的键
     * @param value 要添加的元素
     */
    public void leftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 获取指定列表中指定位置的元素。
     *
     * @param key   列表的键
     * @param index 要获取的元素的位置
     * @return 位置上的元素
     */
    public Object index(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取指定列表中指定范围内的元素。
     *
     * @param key   列表的键
     * @param start 范围的起始位置
     * @param end   范围的结束位置
     * @return 元素列表
     */
    public List<Object> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 将一个值插入到指定列表的指定元素之前（如果元素存在的话）。
     *
     * @param key   列表的键
     * @param pivot 要查找的元素
     * @param value 要插入的值
     */
    public void leftPush(String key, String pivot, String value) {
        redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * 向列表的左侧批量添加元素。
     *
     * @param key    列表的键
     * @param values 要添加的元素数组
     */
    public void leftPushAll(String key, String... values) {
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 向列表的右侧添加一个元素。
     *
     * @param key   列表的键
     * @param value 要添加的元素
     */
    public void rightPush(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 向列表的右侧批量添加元素。
     *
     * @param key    列表的键
     * @param values 要添加的元素数组
     */
    public void rightPushAll(String key, String... values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 如果列表存在，向右侧添加一个元素。
     *
     * @param key   列表的键
     * @param value 要添加的元素
     */
    public void rightPushIfPresent(String key, Object value) {
        redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 获取列表的长度。
     *
     * @param key 列表的键
     * @return 列表的长度
     */
    public long getListLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 从列表的左侧移除并返回一个元素。
     *
     * @param key 列表的键
     */
    public void leftPop(String key) {
        redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从列表的左侧移除并返回一个元素，在指定的等待时间内等待元素可用。
     *
     * @param key     列表的键
     * @param timeout 等待的时间
     * @param unit    时间单位
     */
    public void leftPop(String key, long timeout, TimeUnit unit) {
        redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 从列表的右侧移除并返回一个元素。
     *
     * @param key 列表的键
     */
    public void rightPop(String key) {
        redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 从列表的右侧移除并返回一个元素，在指定的等待时间内等待元素可用。
     *
     * @param key     列表的键
     * @param timeout 等待的时间
     * @param unit    时间单位
     */
    public void rightPop(String key, long timeout, TimeUnit unit) {
        redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

}