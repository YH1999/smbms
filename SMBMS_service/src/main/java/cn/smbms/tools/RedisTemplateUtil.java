package cn.smbms.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTemplateUtil {
	
	@Autowired
    private RedisTemplate<String, Object> rTemplate;  
	
	/**指定缓存失效时间
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key,long time) {    
        try {
            if (time > 0) {
                rTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }    
    }
    
    /**根据key获取过期时间
     * @param key
     * @return
     */
    public long getExpire(String key) {
        return rTemplate.getExpire(key, TimeUnit.SECONDS);
    }
        
    /**判断key是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        try {
            return rTemplate.hasKey(key);
        } catch (Exception e) {            
            e.printStackTrace();
            return false;
        }
    }
        
    /**删除缓存
     * @param key 可以传一个值或多个
     */
    public void del(String ... key) {
        if(key!=null&&key.length>0) {
            if(key.length==1) {
                rTemplate.delete(key[0]);
            }else {
                rTemplate.delete(Arrays.asList(key));
            }
        }
    }

//  ==========================String============================    
    /**普通缓存获取
     * @param key
     * @return
     */
    public Object get(String key) {
        try {
            Object object = rTemplate.opsForValue().get(key);        
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }        
    }
        
    /**普通缓存放入
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key,Object value) {
        try {
            rTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
        
    /**普通缓存放入并设置时间
     * @param key
     * @param value
     * @param time time如果小于等于0，将设置无限期
     * @return
     */
    public boolean set(String key,Object value,long time) {
        try {
            if(time>0) {
                rTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else {
                set(key,value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
        
    /**递增或递减
     * @param key
     * @param delta
     * @return
     */
    public long incre(String key,long delta) {        
        return rTemplate.opsForValue().increment(key, delta);
    }
    
 //==============================Map==================================
    /**HashMap或HashSet缓存获取具体的value
     * @param key
     * @param item
     * @return
     */
    public Object hget(String key,String item) {
        return rTemplate.opsForHash().get(key, item);
    }
    
    /**获取所有键值对
     * @param key
     * @return
     */
    public Map<Object,Object> hmget(String key) {
        return rTemplate.opsForHash().entries(key);
    }
    
    /**hashmap放入缓存
     * @param key
     * @param map
     * @return
     */
    public boolean hmset(String key,Map<Object,Object> map) {
        try {
            rTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**hashmap放入缓存并设置时间
     * @param key
     * @param map
     * @param time
     * @return
     */
    public boolean hmset(String key,Map<Object,Object> map,long time) {
        try {
            rTemplate.opsForHash().putAll(key, map);
            if(time>0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**向一张hash表中放入数据，如果不存在则创建
     * @param key
     * @param item
     * @param value
     * @return
     */
    public boolean hset(String key,String item,Object value) {
        try {
            rTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**向一张hash表中放入数据并设置时间，如果不存在则创建
     * @param key
     * @param item
     * @param value
     * @param time
     * @return
     */
    public boolean hset(String key,String item,Object value,long time) {
        try {
            rTemplate.opsForHash().put(key, item, value);
            if(time>0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**删除hash表中的值
     * @param key
     * @param item
     */
    public void hdel(String key,Object...item ) {
        rTemplate.opsForHash().delete(key, item);
    }
    
    /**判断hash表中是否有该项的值
     * @param key
     * @param item
     * @return
     */
    public boolean hHasKey(String key,String item) {
        return rTemplate.opsForHash().hasKey(key, item);
    }
    
//==========================List=================================
    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key,long start, long end){
        try {
            return rTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
     
    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetListSize(String key){
        try {
            return rTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
     
    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key,long index){
        try {
            return rTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
     
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            rTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            rTemplate.opsForList().rightPush(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            rTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            rTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     
    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index,Object value) {
        try {
            rTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     
    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key,long count,Object value) {
        try {
            Long remove = rTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
	
  
    /*public void set(String key, Object value) {  
        ValueOperations<String, Object> valueOperations = rTemplate.opsForValue();  
        valueOperations.set(key, value);  
    }  
  
    public Object get(String key) {  
        return rTemplate.opsForValue().get(key);  
    }  
  
    public void setList(String key, List<?> value) {  
        ListOperations<String, Object> listOperations = rTemplate.opsForList();  
        listOperations.leftPush(key, value);  
    }  
  
    public Object getList(String key) {  
        return rTemplate.opsForList().leftPop(key);  
    }  
  
    public void setSet(String key, Set<?> value) {  
        SetOperations<String, Object> setOperations = rTemplate.opsForSet();  
        setOperations.add(key, value);  
    }  
  
    public Object getSet(String key) {  
        return rTemplate.opsForSet().members(key);  
    }  
  
    public void setHash(String key, Map<String, ?> value) {  
        HashOperations<String, Object, Object> hashOperations = rTemplate.opsForHash();  
        hashOperations.putAll(key, value);  
    }  
  
    public Object getHash(String key) {  
        return rTemplate.opsForHash().entries(key);  
    }  
  
    public void delete(String key) {  
        rTemplate.delete(key);  
    }  	*/
}
