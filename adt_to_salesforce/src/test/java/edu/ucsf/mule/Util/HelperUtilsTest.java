package edu.ucsf.mule.Util;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.AssertTrue;

import org.junit.Assert;
import org.junit.Test;

public class HelperUtilsTest {

	@Test
	public void testDeepClone() {
		Map testMap = new HashMap<String, String>();
		testMap.put("key", "value");
		Map cloneMap = (Map) HelperUtils.deepClone(testMap);
		Assert.assertTrue(cloneMap.get("key").equals("value"));
		
	}

}
