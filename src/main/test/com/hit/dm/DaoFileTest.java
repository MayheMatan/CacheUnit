package com.hit.dm;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

//import static org.junit.jupiter.api.Assertions.assertEquals;
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

//@Nested
	//@DisplayName("Tests for Dao")

	public class DaoFileTest{
	private static final int CAPACITY = 10;
	public static IAlgoCache<Long, DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(CAPACITY);
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
			assertEquals("if inserted to the file it should find the current model", dataModels[i].getDataModelId(), dao.find(dataModels[i].getDataModelId()).getDataModelId());
		}
	}

	@Test
	@After
	public void afterAll() {
		for (int i = 0; i < CAPACITY; i++) {
			lru.removeElement(dataModels[i].getDataModelId());
			dao.delete(dataModels[i]);
			assertEquals("after we finished the test, we delete all models , if deleted successfully should return null" , null, dao.find(dataModels[i].getDataModelId()));

		}
	}


		@Test
		//@DisplayName("Test for Dao")
		public void testingDao() throws FileNotFoundException, IOException {
			DaoFileImpl<Integer> dao = new DaoFileImpl<>("DataSource.txt", CAPACITY);
			for (int i = 0; i < dataModels.length; i++) {
				dao.save(dataModels[i]);
				myModel = dao.find(dataModels[i].getDataModelId());
				assertEquals("checking if model was inserted",dataModels[i].getDataModelId(), myModel.getDataModelId());
			}
		}

		@Test
		//@DisplayName("Test for Dao delete")
		public void testingDaoDelete() {
			for (int i = 0; i < dataModels.length; i++) {
				dao.delete(dataModels[i]);
				myModel = dao.find(dataModels[i].getDataModelId());
				assertEquals("checking if model was removed", null, myModel);
			}
		}
}