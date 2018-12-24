package com.hit.dao;


import com.hit.dm.DataModel;

import java.io.*;
import java.util.Hashtable;


public class DaoFileImpl<T> implements IDao<java.lang.Long, DataModel<T>>
{
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String filePath;
    private Hashtable<Long, DataModel<T>> hashtable;


    public DaoFileImpl(String filePath) throws FileNotFoundException, IOException
    {
    	this.filePath = filePath;
    	this. hashtable = new Hashtable<> ();
    	this.openInputStream();
    	this.openOutputStream();
    }

    public DaoFileImpl(String filePath, int capacity) throws FileNotFoundException, IOException
    {
    	this.filePath = filePath;
    	this.hashtable = new Hashtable<> ();
    	this.openInputStream();
    	this.openOutputStream();
    }

    @Override
    public void delete(DataModel<T> entity)
    {
        openInputStream ();
        hashtable.remove (entity.getDataModelId ());
        openOutputStream ();

    }

	@Override
    public DataModel<T> find(Long id)
    {
        DataModel<T> resultModel = null;

        openInputStream ();

        resultModel = hashtable.get (id);

        openOutputStream ();
        closeStreams ();

        return resultModel;
    }


    @Override
    public void save(DataModel<T> entity)
    {
        openInputStream ();
        hashtable.put (entity.getDataModelId (),entity);

        openOutputStream ();
        closeStreams ();
    }

    @SuppressWarnings("unchecked")
	private void openInputStream()
    {
        try
        {
            this.inputStream = new ObjectInputStream (new FileInputStream (filePath));
            this.hashtable = (Hashtable<Long, DataModel<T>>)inputStream.readObject ();

        } catch (IOException e)
        {
            e.printStackTrace ();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace ();
        }


    }

    private void openOutputStream()
    {
        try
        {
            this.outputStream = new ObjectOutputStream (new FileOutputStream (filePath,false));
            this.outputStream.writeObject (hashtable);
        } catch (IOException e)
        {
            e.printStackTrace ();
        }


    }

    private void closeStreams()
    {
        try
        {
            outputStream.close ();
            inputStream.close ();
        } catch (IOException e)
        {
            e.printStackTrace ();
        }
    }

}
