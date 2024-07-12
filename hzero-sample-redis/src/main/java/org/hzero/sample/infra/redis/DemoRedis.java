package org.hzero.sample.infra.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.sample.api.dto.RoleDTO;
import org.hzero.sample.api.dto.UserCacheVO;
import org.hzero.sample.app.service.DemoCacheValueService;
import org.hzero.sample.infra.constant.RedisPrefixConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Redis 使用示例
 *
 * @author zifeng.ding@hand-china.com 2020/01/19 15:59
 */
@Component
public class DemoRedis {
    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private DemoCacheValueService demoCacheValueService;


    /**
     * RedisHelper关于string数据类型操作
     */
    public void stringOperating() {
        //设置缓存值
        redisHelper.strSet(RedisPrefixConstants.STRING_KEY + "str", "1");
        //设置缓存带过期时间
        redisHelper.strSet(RedisPrefixConstants.STRING_KEY + "str-expire", "1", 60, TimeUnit.SECONDS);
        //缓存值自增长,增长值可以为负数
        redisHelper.strIncrement(RedisPrefixConstants.STRING_KEY + "str", 10L);
        //如果值不存在则设置（原子操作）
        redisHelper.strSetIfAbsent(RedisPrefixConstants.STRING_KEY + "str", "0");
        //获取缓存值
        String string = redisHelper.strGet(RedisPrefixConstants.STRING_KEY + "str");
        //获取缓存值并设置超时时间
        string = redisHelper.strGet(RedisPrefixConstants.STRING_KEY + "str", 60, TimeUnit.SECONDS);
        //获取缓存值转化指定类型
        string = redisHelper.strGet(RedisPrefixConstants.STRING_KEY + "str", String.class);
        //删除缓存
      //  redisHelper.delKey(RedisPrefixConstants.STRING_KEY + "str");
    }

    /**
     * RedisHelper关于hash数据类型操作
     */
    public void hashOperating() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        //设置缓存值
        redisHelper.hshPut(RedisPrefixConstants.HASH_KEY + "hsh", "1", "1");
        //key是否存在
        Boolean hashKey = redisHelper.hshHasKey(RedisPrefixConstants.HASH_KEY + "hsh", "1");
        //批量插入缓存值
        redisHelper.hshPutAll(RedisPrefixConstants.HASH_KEY + "hsh", map);
        //删除缓存值
      //  redisHelper.hshDelete(RedisPrefixConstants.HASH_KEY + "hsh", "1", "2");
        String hshGet = redisHelper.hshGet(RedisPrefixConstants.HASH_KEY + "hsh", "3");
    }

    /**
     * RedisHelper关于list数据类型操作
     */
    public void listOperating() {
        //左侧插入缓存值
        redisHelper.lstLeftPush(RedisPrefixConstants.LIST_KEY + "lst", "lst-1");
        redisHelper.lstLeftPush(RedisPrefixConstants.LIST_KEY + "lst", "lst-2");
        //设置缓存值
        redisHelper.lstSet(RedisPrefixConstants.LIST_KEY + "lst", 0, "lst-3");
        //右侧插入缓存值
        redisHelper.lstRightPush(RedisPrefixConstants.LIST_KEY + "lst", "lst-4");
        redisHelper.lstRightPush(RedisPrefixConstants.LIST_KEY + "lst", "lst-5");
        //获取指定缓存值
        Object lstIndex = redisHelper.lstIndex(RedisPrefixConstants.LIST_KEY + "lst", 0);
        //获取全部缓存值
        List<String> list = redisHelper.lstAll(RedisPrefixConstants.LIST_KEY + "lst");
        //从左侧获取缓存值并删除该值
        String lstLeftPop = redisHelper.lstLeftPop(RedisPrefixConstants.LIST_KEY + "lst");
        //从右侧获取缓存值并删除该值
        String lstRightPop = redisHelper.lstRightPop(RedisPrefixConstants.LIST_KEY + "lst");
        //获取范围内缓存值
        List<String> lstRange = redisHelper.lstRange(RedisPrefixConstants.LIST_KEY + "lst", 0, 1);
        //移除列表中与value相等的元素，移除数量为index值，index大于0 从表头开始移除，否则从表尾开始
       // Long lstRemove = redisHelper.lstRemove(RedisPrefixConstants.LIST_KEY + "lst", 1, "lst-1");
    }

    /**
     * RedisHelper关于set数据类型操作
     */
    public void setOperating() {
        //插入缓存值
        redisHelper.setAdd(RedisPrefixConstants.SET_KEY + "set", new String[]{"1", "2", "3"});
        redisHelper.setAdd(RedisPrefixConstants.SET_KEY + "set1", new String[]{"3", "4", "5"});
        //获取set元素
        Set<String> members = redisHelper.setMembers(RedisPrefixConstants.SET_KEY + "set");
        //删除set元素
        //redisHelper.setDel(RedisPrefixConstants.SET_KEY + "set", "1");
        //获取差集
        Set<String> difference = redisHelper.setDifference(RedisPrefixConstants.SET_KEY + "set", RedisPrefixConstants.SET_KEY + "set1");
        //获取交集
        Set<String> setIntersect = redisHelper.setIntersect(RedisPrefixConstants.SET_KEY + "set", RedisPrefixConstants.SET_KEY + "set1");
        //获取并集
        Set<String> setUnion = redisHelper.setUnion(RedisPrefixConstants.SET_KEY + "set", RedisPrefixConstants.SET_KEY + "set1");
    }

    /**
     * RedisHelper关于zset数据类型操作
     */
    public void zSetOperating() {
        //zSet 是有序的set

        //设置缓存值
        redisHelper.zSetAdd(RedisPrefixConstants.Z_SET_KEY + "zSet", "1", 0.1);
        //返回指定元素在有序集合中的排名
        Long rank = redisHelper.zSetRank(RedisPrefixConstants.Z_SET_KEY + "zSet", "1");
        //返回有序集合中，指定元素的分值
        Double score = redisHelper.zSetScore(RedisPrefixConstants.Z_SET_KEY + "zSet", "1");
        //返回有序集合在升序排列元素的情况下，分值在 min和 max范围内的元素数量
        Long count = redisHelper.zSetCount(RedisPrefixConstants.Z_SET_KEY + "zSet", 0D, 1D);
    }

    /**
     * RedisHelper 动态切换DataBase
     */
    public void setDataBase() {
        //切换至db2
        redisHelper.setCurrentDatabase(2);
        //清除当前操作 database
     //   redisHelper.clearCurrentDatabase();
    }

    /**
     * redis CacheValue功能
     */
    public void cacheValue() {
        UserCacheVO userCacheVO = UserCacheVO.getDemoUserCache();
        //缓存中保存示例角色
        redisHelper.hshPut(RedisPrefixConstants.USER_KEY, userCacheVO.getUserId().toString(), redisHelper.toJson(userCacheVO));
        //获取角色使用CacheValue功能
        RoleDTO roleDTO = demoCacheValueService.getRoleDTO();
        //角色的创建者名称取自用户缓存,所以相等
        Assert.isTrue(roleDTO.getCreatedUserName().equals(userCacheVO.getUserName()), BaseConstants.ErrorCode.DATA_INVALID);
    }
}
