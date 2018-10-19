package com.app.common.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private StringRedisTemplate redisTemplate;

	public void remove(String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	public void removePattern(String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0) {
			redisTemplate.delete(keys);
		}
	}

	public void remove(String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	public boolean exists(String key) {
		return redisTemplate.hasKey(key);
	}

	public String get(String key) {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		return operations.get(key);
	}

	public boolean set(String key, String value) {
		boolean result = false;
		try {
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void set(String key, String value, Long expireTime) {
		try {
			BoundValueOperations<String, String> valueOption = redisTemplate.boundValueOps(key);
			valueOption.set(value);
			if (null != expireTime)
				redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean flushExpireTime(String key, Long expireTime) {
		boolean result = false;
		try {
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Long increase(String key, String field) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		if (exists(key, field)) {
			return hashOps.increment(field, 1L);
		} else {
			hashOps.putIfAbsent(field, "1");
			return 1L;
		}
	}

	public Long decrease(String key, String field) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		if (exists(key, field)) {
			long count = hashOps.increment(field, -1L);
			if (count == 0) {
				deleteField(key, field);
				return 0L;
			} else {
				return count;
			}
		}
		return 0L;
	}

	public void deleteField(String key, String field) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		hashOps.delete(field);
	}

	public Set<String> getFields(String key) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		return hashOps.keys();
	}

	public boolean exists(String key, String field) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		return hashOps.hasKey(field);
	}

	public String getValueByKeyANdField(String key, String field) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		return hashOps.get(field);
	}

	public Map<String, String> getEntries(String key) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		return hashOps.entries();
	}

	public void put(String key, String field, String value) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		hashOps.put(field, value);
	}

	public void put(String key, String field, String value, Long expireTime) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		hashOps.put(field, value);
		if (null != expireTime) {
			hashOps.expire(expireTime, TimeUnit.SECONDS);
		}
	}

	public String hGet(String key, String field) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		String result = hashOps.get(field);
		if (result == null)
			return null;
		return result;
	}

	public void hSet(String key, Map<String, String> map, Long expireTime) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		if (hashOps == null) {
			return;
		}
		hashOps.putAll(map);
		if (null != expireTime) {
			hashOps.expire(expireTime, TimeUnit.SECONDS);
		}
	}

	public void hDel(String key, String field) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		if (hashOps == null) {
			return;
		}
		hashOps.delete(field);
	}

	public List<String> hMultiGet(String key, List<String> fileds) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		return hashOps.multiGet(fileds);
	}

	public Long geoPut(String key, Map<String, Point> location) {
		BoundGeoOperations<String, String> geoOps = redisTemplate.boundGeoOps(key);
		return geoOps.add(location);
	}

	public Long geoCover(String key, Map<String, Point> location, String id) {
		BoundGeoOperations<String, String> geoOps = redisTemplate.boundGeoOps(key);
		geoOps.remove(id);
		return geoOps.add(location);
	}

	public Long geoRemove(String key, String member) {
		BoundGeoOperations<String, String> geoOps = redisTemplate.boundGeoOps(key);
		return geoOps.remove(member);
	}

	public GeoResults<GeoLocation<String>> geoList(String key, Circle within, GeoRadiusCommandArgs args) {
		BoundGeoOperations<String, String> geoOps = redisTemplate.boundGeoOps(key);
		return geoOps.radius(within, args);

	}

	public void hIncri(String key, String filed) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		hashOps.increment(filed, 1);
	}

	public boolean lock(final String lockKey) {

		if (redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(lockKey.getBytes(), "1".getBytes());
			}
		})) {
			redisTemplate.expire(lockKey, 5, TimeUnit.SECONDS); // 设置超时时间，释放内存
			return true;
		}
		return false;
	}

	public boolean lock(final String lockKey, final Long timeout) {
		if (redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(lockKey.getBytes(), timeout.toString().getBytes());
			}
		})) {
			redisTemplate.expire(lockKey, timeout, TimeUnit.SECONDS); // 设置超时时间，释放内存
			return true;
		}
		return false;
	}

	public boolean lock(final String lockKey, final String str, final Long timeout) {
		if (redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(lockKey.getBytes(), str.toString().getBytes());
			}
		})) {
			redisTemplate.expire(lockKey, timeout, TimeUnit.SECONDS); // 设置超时时间，释放内存
			return true;
		}
		return false;
	}

	public Map<byte[], byte[]> hGetAll(final String key) {
		return redisTemplate.execute(new RedisCallback<Map<byte[], byte[]>>() {
			public Map<byte[], byte[]> doInRedis(RedisConnection connection) throws DataAccessException {

				Map<byte[], byte[]> resBytes = connection.hGetAll(key.getBytes());
				

				return resBytes;
			}
		});
	}

	public void hReduce(String key, String filed) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		hashOps.increment(filed, -1);
	}

	public void hReduce(String key, String filed, Long value, Long expire) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		hashOps.increment(filed, -value);

		if (expire != null) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
	}

	public void hReduce(String key, String filed, Double value) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		hashOps.increment(filed, -value);
	}

	public void hIncri(String key, String filed, Double value) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		hashOps.increment(filed, value);
	}

	public void hIncri(String key, String filed, Long value) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		hashOps.increment(filed, value);
	}

	public void hIncri(String key, String filed, Long value, Long expire) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
		hashOps.increment(filed, value);
		if (expire != null) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
	}

	public Set<String> sMembers(String key) {

		BoundSetOperations<String, String> setOps = redisTemplate.boundSetOps(key);

		return setOps.members();
	}

	public void sAdd(String key, String member) {

		BoundSetOperations<String, String> setOps = redisTemplate.boundSetOps(key);

		setOps.add(member);
	}

	public void zadd(String key, String value, Integer score) {
		BoundZSetOperations<String, String> setOps = redisTemplate.boundZSetOps(key);
		setOps.add(value, score);
	}

	public String rangeByLex(String key, String value) {

		BoundZSetOperations<String, String> setOps = redisTemplate.boundZSetOps(key);
		Set<String> member;
		member = setOps.rangeByScore(0, Integer.parseInt(value.toString()));
		Iterator<String> it = member.iterator();
		String rslt = "0";
		while (it.hasNext()) {
			rslt = it.next();
		}
		return rslt;
	}

	public List<String> rangeAll(String key) {

		BoundZSetOperations<String, String> setOps = redisTemplate.boundZSetOps(key);
		Set<String> member;
		member = setOps.rangeByScore(-1, 1000000);

		Iterator<String> it = member.iterator();

		List<String> restList = new ArrayList<String>();

		while (it.hasNext()) {
			restList.add(it.next());
		}
		return restList;
	}

	private static final double EARTH_RADIUS = 6378137;

	// 把经纬度转为度（°）
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public double getDistance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
}
