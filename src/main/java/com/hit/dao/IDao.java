package com.hit.dao;

public interface IDao <ID extends java.io.Serializable,T> 
{
	void delete(T entity);
	
	T find(ID id);
	
	void save(T entity);

}
