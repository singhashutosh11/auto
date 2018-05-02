package com.thed.zephyr.test;

import com.thed.zephyr.cloud.rest.client.CycleRestClient;
import com.thed.zephyr.cloud.rest.exception.BadRequestParamException;
import com.thed.zephyr.cloud.rest.exception.JobProgressException;
import com.thed.zephyr.cloud.rest.model.Cycle;
import com.thed.zephyr.cloud.rest.model.JobProgress;
import com.thed.zephyr.util.AbstractTest;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.codehaus.jettison.json.JSONException;
//import org.junit.BeforeClass;
//import org.junit.Test;
import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import static org.junit.Assert.*;


/**
 * Created by aliakseimatsarski on 3/15/16.
 */
public class CycleUnitTest extends AbstractTest {

    private static CycleRestClient cycleRestClient;
    final private Long projectId = 10001l;
    final private Long versionId = 10001l;
    final private Long issueId = 10000l;
	final private String cycleId = "0001459160518504-b82a729d7df-0001";
	final private String executionId = "0001459162174730-b82a729d7df-0001";

    @BeforeClass
    public static void setUp() throws Exception{
    	cycleRestClient = client.getCycleRestClient();
    }

	 // @Test
	public void testCreateCycle() throws JSONException, HttpException, ParseException, BadRequestParamException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Cycle cycle = new Cycle();
		cycle.projectId = projectId;
		cycle.versionId = versionId;
		cycle.name = "Create cycle with strat and end d dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
		cycle.startDate = dateFormatter.parse("1990-08-01");
		cycle.endDate = new Date();
		Cycle responseCycle = cycleRestClient.createCycle(cycle);
		assertNotNull(responseCycle);
	}

	//   @Test
	public void testGetCycle() throws JSONException, HttpException, BadRequestParamException {
		/*Cycle cycle = new Cycle();
		cycle.projectId = projectId;
		cycle.versionId = versionId;
		cycle.name = "Create cycle";

		Cycle responseCycle = cycleRestClient.createCycle(cycle);
		assertNotNull(responseCycle);

*/
		Cycle responseCycleAfterGetting = cycleRestClient.getCycle(projectId, versionId, null);
		assertNotNull(responseCycleAfterGetting);
		//assertEquals(responseCycle.id, responseCycleAfterGetting.id);

	}

	//   @Test
	public void testUpdateCycle() throws JSONException, HttpException, BadRequestParamException {
		Cycle cycle = new Cycle();
		cycle.projectId = projectId;
		cycle.versionId = versionId;
		cycle.name = "Cycle that is moved";

		Cycle responseCycle = cycleRestClient.createCycle(cycle);
		assertNotNull(responseCycle);

		responseCycle.versionId = 10000L;

		Cycle responseCycleAfterGetting = cycleRestClient.updateCycle(responseCycle.id, responseCycle);
		assertNotNull(responseCycleAfterGetting);
		assertEquals(responseCycle.id, responseCycleAfterGetting.id);
	}
//	  @Test
	public void testDeleteCycle() throws JSONException, HttpException, BadRequestParamException {
		Cycle cycle = new Cycle();
		cycle.projectId = projectId;
		cycle.versionId = versionId;
		cycle.name = "Create cycle";

		Cycle responseCycle = cycleRestClient.createCycle(cycle);
		assertNotNull(responseCycle);

		boolean flag = cycleRestClient.deleteCycle(responseCycle.projectId, responseCycle.versionId, responseCycle.id);
		assertTrue(flag);
	}

//	@Test
	public void testCloneCycle() throws JSONException, HttpException, BadRequestParamException {
		Cycle cycle = new Cycle();
		cycle.projectId = projectId;
		cycle.versionId = versionId;
		cycle.name = "Create cycle";

		Cycle createCycle = cycleRestClient.createCycle(cycle);

		//cycleRestClient.cloneCycle(createCycle.id, )

	}

	//@Test
	public void testGetCycles() throws HttpException, JSONException, BadRequestParamException {
		List<Cycle> cycles = cycleRestClient.getCycles(projectId, versionId);
		assertNotNull(cycles);
	}

}
