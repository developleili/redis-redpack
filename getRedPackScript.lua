if redis.call('hexists', KEYS[3], KEYS[4]) ~= 0 then 
		return nil
else
	local redPack = redis.call('rpop', KEYS[1]);
	if redPack then 
		local x = cjson.decode(redPack);
		x['userId'] = KEYS[4];
		local re = cjson.encode(x);
		redis.call('hset', KEYS[3], KEYS[4], '1');
		redis.call('lpush', KEYS[2],re);
		return re;
	end
end	
return nil
