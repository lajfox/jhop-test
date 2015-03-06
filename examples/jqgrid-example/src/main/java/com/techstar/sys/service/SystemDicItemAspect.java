package com.techstar.sys.service;

import java.util.List;

import net.sf.ehcache.Cache;

import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import com.techstar.sys.entity.SystemDicItem;
import com.techstar.sys.utils.DicItemCacheUtils;

@Aspect
public class SystemDicItemAspect {

	@Autowired
	private Cache cache;

	@After("execution(* com.techstar.sys.service.SystemDicItemService.save(..) ) || execution(* com.techstar.sys.service.SystemDicItemService.savex(..))")
	public void save(JoinPoint jp) throws Throwable {
		Object args[] = jp.getArgs();
		if (ArrayUtils.isNotEmpty(args)) {
			for (Object obj : args) {
				if (obj instanceof SystemDicItem) {
					SystemDicItem item = (SystemDicItem) obj;
					String key = item.getSystemDicType().getSign();
					DicItemCacheUtils.put(cache, key, item);
					break;
				}
			}
		}
	}

	@After(" execution(* com.techstar.sys.service.SystemDicItemService.delete(..)) || execution(* com.techstar.sys.service.SystemDicItemService.deletex(..))")
	public void delete(JoinPoint jp) throws Throwable {
		Object args[] = jp.getArgs();
		if (ArrayUtils.isNotEmpty(args)) {
			for (Object obj : args) {
				if (obj instanceof SystemDicItem) {
					SystemDicItem item = (SystemDicItem) obj;
					String key = item.getSystemDicType().getSign();
					DicItemCacheUtils.remove(cache, key, item);
					break;
				}
			}
		}
	}
	
	@Around(" execution(* com.techstar.sys.service.SystemDicItemService.findBy(..))")
	public Object findBy(ProceedingJoinPoint pjp) throws Throwable {
		Object results = null;
		Object args[] = pjp.getArgs();
		if (ArrayUtils.isNotEmpty(args) && args.length == 2) {
			String key = args[1].toString();
			results = DicItemCacheUtils.get(cache,key );
			if(results == null){
				results = pjp.proceed();
				//DicItemCacheUtils.put(cache, key, (List<SystemDicItem>)results);
			}
		}else{
			results = pjp.proceed();
		}
		
		return results;
	}	
}
