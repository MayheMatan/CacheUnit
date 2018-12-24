package com.hit.memory;

import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;

import com.hit.dao.DaoFileImpl;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;

//@DisplayName("CacheUnit Test")
public class CacheUnitTest {

	private static final int CAPACITY = 10;
	public static final IAlgoCache<Long, DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(CAPACITY);
    public static DaoFileImpl<Integer> dao;{
    try{
    	this.dao = new DaoFileImpl<>("DataSource.txt", CAPACITY * 2);
    } catch (IOException e)
    {
        e.printStackTrace ();}}
	public static CacheUnit<Integer> cacheUnit = new CacheUnit(lru,dao);
	public static Long[] ids = new Long[CAPACITY];
	public static Integer[] nullArray = new Integer[CAPACITY];
	public static DataModel<Integer>[] dataModels = new DataModel[CAPACITY];
	public static DataModel<Integer> myModel = null;


	@Test
	@Before
	public void setUpWithData() {
		for (int i = 0; i < CAPACITY; i++) {
			ids[i] = Long.valueOf(i);
			myModel = new DataModel<Integer>(Long.valueOf(i), i);
			dataModels[i] = myModel;
			lru.putElement(Long.valueOf(i), myModel);
			dao.save(myModel);
			assertEquals("if inserted to the file it should find the current model",dataModels[i].getDataModelId(), dao.find(dataModels[i].getDataModelId()).getDataModelId());
		}
	}

	@Test
	@After
	 public void afterAll() {
		for (int i = 0; i < CAPACITY; i++) {
			lru.removeElement(dataModels[i].getDataModelId());
			dao.delete(dataModels[i]);
			assertEquals("after we finished the test, we delete all models , if deleted successfully should return null",null, dao.find(dataModels[i].getDataModelId()));

		}
	}

	//@DisplayName("Testing the Cache unit")
	class TestingCacheUnit {

		@Test
		//@DisplayName("Testing insertion")
		void InsertionTest() throws ClassNotFoundException, IOException {
			cacheUnit.putDataModels(dataModels); // testing putDataModels method
			DataModel<Integer>[] tempModels = new DataModel[CAPACITY];
			tempModels = cacheUnit.getDataModels(ids);
			for (int i = 0; i < tempModels.length; i++) {
				assertEquals("if put and get dataModels are working it should return the same models", String.valueOf(tempModels[i]), 
						String.valueOf(dataModels[i]));
			}
		}

		//@Nested
	//	@DisplayName("When Models inserted")
		class WhenModelsInsetred {

			@Test
			//@DisplayName("Deleting models using cacheUnit")
			void deleteModelsTest() {
				cacheUnit.removeDataModels(ids);
				for (int i = 0; i < ids.length; i++) {
					assertEquals("After we removed all models,getDataModels should return null",null, lru.getElement(ids[i]));
				}

			}
		}
	}
}