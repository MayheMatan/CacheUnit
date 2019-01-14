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

	@SuppressWarnings("unchecked")
	synchronized public DataModel<T>[] getDataModels(Long[] ids) { 
		DataModel<T>[] models = new DataModel[ids.length];
		DataModel<DataModel<T>> dataModel = null;
		for (int i = 0; i < ids.length; i++) {
			dataModel = new DataModel<DataModel<T>>(ids[i], iAlgoCache.getElement(ids[i]));
			if (dataModel != null)
				models[i] = (DataModel<T>) dataModel;
		}
		return models;
	}

	synchronized public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) { 
		DataModel<T> myModel;
		int nullCounter = 0;
		for (int i = 0; i < datamodels.length; i++) {
			myModel = iAlgoCache.putElement(datamodels[i].getDataModelId(), datamodels[i]);
			if (myModel == null) 
				nullCounter++;
		}
		if (nullCounter == datamodels.length) { 
			return null;
		}
		return datamodels;
	}

	synchronized public void removeDataModels(Long[] ids) {
		DataModel<T> dataModel;
		for (int i = 0; i < ids.length; i++) {
			dataModel = iAlgoCache.getElement(ids[i]);
			iAlgoCache.removeElement(ids[i]);
		}
	}

	synchronized public void updateFile(DataModel<T> model) { 
		dao.save(model);
	}
}