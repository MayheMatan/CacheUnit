
package com.hit.services;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.hit.dm.DataModel;

public class CacheUnitController<T> extends java.lang.Object
{
    private CacheUnitService<T> unitService;

    public CacheUnitController() throws FileNotFoundException, IOException
    {
        unitService = new CacheUnitService<T>();
    }

    public boolean delete(com.hit.dm.DataModel<T>[] dataModels) throws ClassNotFoundException, IOException
    {
        return unitService.delete(dataModels);
    }

    public boolean update(com.hit.dm.DataModel<T>[] dataModels) throws ClassNotFoundException, IOException
    {
        return unitService.update(dataModels);
    }

    public com.hit.dm.DataModel<T>[] get(DataModel<T>[] dataModels) throws ClassNotFoundException, IOException
    {
        return unitService.get(dataModels);
    }
} 