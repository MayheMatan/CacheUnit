package com.hit.memory;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;

public class CacheUnitTest {

	@Test
	public void cacheUnitTest() throws ClassNotFoundException, IOException {
		IDao<Long, DataModel<String>> dao = new DaoFileImpl<>("out.txt");
		IAlgoCache<Long, DataModel<String>> algo = new LRUAlgoCacheImpl<>(4);

		CacheUnit<String> cacheUnit = new CacheUnit(algo, dao);
		DataModel<String> datamodel1 = new DataModel<String>(1L, "a");
		DataModel<String> datamodel2 = new DataModel<String>(2L, "b");
		DataModel<String> datamodel3 = new DataModel<String>(3L, "c");
		DataModel<String> datamodel4 = new DataModel<String>(4L, "d");
		DataModel<String> datamodel5 = new DataModel<String>(5L, "e");


		dao.save(datamodel1);
		dao.save(datamodel2);
		dao.save(datamodel3);
		dao.save(datamodel4);
		dao.save(datamodel5);
		DataModel<String> temp=dao.find(2L);
		System.out.println(temp.getDataModelId() + " " + temp.getContent());

		Long[] ids = { 5L, 3L , 1L};
        String[] answers= {"e","c","a"};
        int i=0;
        DataModel<String>[] resultArr = cacheUnit.getDataModels(ids);
    

		for (DataModel<String> item : resultArr) {
			System.out.println(item.getDataModelId() + " " + item.getContent());
			 assertEquals(item.getContent(), answers[i++]);
		}
		
	
	}
}
