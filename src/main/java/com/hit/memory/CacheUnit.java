package com.hit.memory;

import com.hit.dao.IDao;
import com.hit.dm.DataModel;

public class CacheUnit<T> extends java.lang.Object {
	
	//IDao & IAlgoCache as members
	public com.hit.algorithm.IAlgoCache<java.lang.Long, DataModel<T>> iAlgoCache;
	public IDao<java.io.Serializable,DataModel<T>> dao;
	
	public CacheUnit
	(com.hit.algorithm.IAlgoCache<java.lang.Long, DataModel<T>> algo,
            IDao<java.io.Serializable,DataModel<T>> dao)
	{
		this.iAlgoCache = algo;
		this.dao = dao;
	}
	
	// methods are synchronized for the situation if more then one client is trying
	// to enter the same section simultaneously
	
	@SuppressWarnings("unchecked")
	synchronized public DataModel<T>[] getDataModels(Long[] ids) { //
		DataModel<T>[] models = new DataModel[ids.length];
		DataModel<DataModel<T>> dataModel = null;
		for (int i = 0; i < ids.length; i++) {
			dataModel = new DataModel<DataModel<T>>(ids[i], iAlgoCache.getElement(ids[i]));
			if (dataModel != null)// if in cache
				models[i] = (DataModel<T>) dataModel;
		}
		return models;
	}

	synchronized public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) { // insertion of requested models inside the cache
		DataModel<T> myModel;
		int nullCounter = 0;
		for (int i = 0; i < datamodels.length; i++) {
			myModel = iAlgoCache.putElement(datamodels[i].getDataModelId(), datamodels[i]);
			if (myModel == null) // if already was inside the cache or cache is not full
				nullCounter++;
		}
		if (nullCounter == datamodels.length) { // if all of the models were already inside the cache or cache was never full
			return null;
		}
		return datamodels;
	}

	synchronized public void removeDataModels(Long[] ids) { // removing the requested models from the cache
		DataModel<T> dataModel;
		for (int i = 0; i < ids.length; i++) {
			dataModel = iAlgoCache.getElement(ids[i]);
			iAlgoCache.removeElement(ids[i]);
		}
	}

	synchronized public void updateFile(DataModel<T> model) { // we didn't asked to implement update but we chose to do so in order to supply Cache unit controller
		dao.save(model);
	}
}