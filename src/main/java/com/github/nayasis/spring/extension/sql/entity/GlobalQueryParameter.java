package com.github.nayasis.spring.extension.sql.entity;

import com.github.nayasis.basica.reflection.Reflector;
import com.github.nayasis.basica.thread.local.NThreadLocal;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@UtilityClass
@Slf4j
public class GlobalQueryParameter {

	private final String KEY_THREAD_LOCAL = String.format( "$s.SQL_PARAM", GlobalQueryParameter.class.getName() );

	private Map<String,Object> global = new HashMap<>();

    private Map<String,Object> threadlocal() {
		if( ! NThreadLocal.containsKey( KEY_THREAD_LOCAL ) ) {
			NThreadLocal.set( KEY_THREAD_LOCAL, new HashMap<>() );
		}
		return NThreadLocal.get( KEY_THREAD_LOCAL );
	}

	public Map<String,Object> global() {
		return global;
	}

	public void clear() {
		threadlocal().clear();
		global().clear();
	}

	public Map<String,Object> getAll() {
		return Reflector.merge( threadlocal(), global() );
	}

	public Object get( String key, Object defaultValue ) {
		return getOrDefault( key, null );
	}

	public Object getOrDefault( String key, Object defaultValue ) {
		return threadlocal().getOrDefault( key, global().getOrDefault(key, defaultValue) );
	}

	public void remove( String key ) {
		threadlocal().remove( key );
		global().remove( key );
	}

	public boolean containsKey( Object key ) {
		return threadlocal().containsKey( key ) || global().containsKey( key );
	}

	public Set<String> keySet() {
		Set<String> keys = threadlocal().keySet();
		keys.addAll( global().keySet() );
		return keys;
	}

}