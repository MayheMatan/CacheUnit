package com.hit.services;
/*
 * Gets from HandleRequest an action and invokes the right method,
 * the method then sends the action to DM and performs the it.
 * */

import java.io.FileNotFoundException;
import java.io.IOException;

import com.hit.dm.DataModel;

public class CacheUnitController<T> extends java.lang.Object
{
    @SuppressWarnings("rawtypes")
	CacheUnitService unitService;

    @SuppressWarnings("rawtypes")
	public CacheUnitController() throws FileNotFoundException, IOException
    {
        unitService = new CacheUnitService();
    }

    @SuppressWarnings("unchecked")
	public boolean delete(com.hit.dm.DataModel<T>[] dataModels) throws ClassNotFoundException, IOException
    {
        return unitService.delete(dataModels);
    }

    @SuppressWarnings("unchecked")
    public boolean update(com.hit.dm.DataModel<T>[] dataModels) throws ClassNotFoundException, IOException
    {
        return unitService.update(dataModels);
    }

    @SuppressWarnings("unchecked")
	public com.hit.dm.DataModel<T>[] get(DataModel<T>[] dataModels) throws ClassNotFoundException, IOException
    {
        return unitService.get(dataModels);
    }
}