package com.thed.zephyr.test;

import com.thed.zephyr.cloud.rest.client.ExecutionRestClient;
import com.thed.zephyr.cloud.rest.exception.BadRequestParamException;
import com.thed.zephyr.cloud.rest.exception.JobProgressException;
import com.thed.zephyr.cloud.rest.model.Defect;
import com.thed.zephyr.cloud.rest.model.Execution;
import com.thed.zephyr.cloud.rest.model.ExecutionStatus;
import com.thed.zephyr.cloud.rest.model.JobProgress;
import com.thed.zephyr.cloud.rest.model.enam.ExecutionFieldId;
import com.thed.zephyr.cloud.rest.model.enam.FromCycleFilter;
import com.thed.zephyr.cloud.rest.model.enam.SortOrder;
import com.thed.zephyr.cloud.rest.util.ZFJConnectResults;
import com.thed.zephyr.util.AbstractTest;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
//import org.junit.BeforeClass;
//import org.junit.Test;
import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;


/**
 * Created by aliakseimatsarski on 3/15/16.
 */
public class ExecutionUnitTest extends AbstractTest {

    private static ExecutionRestClient executionRestClient;
    final private Long projectId = 10001l;
    final private Long versionId = 10001l;
    final private Long issueId = 10024l;
    final private String cycleId = "-1";

    @BeforeClass
    public static void setUp() throws Exception{
        executionRestClient = client.getExecutionRestClient();
    }

   // @Test
    public String testCreateExecution(Long issueId) throws JSONException, HttpException, BadRequestParamException {
        Execution execution = new Execution();
        execution.issueId = issueId;
        execution.cycleId = cycleId;

        Execution responseExecution = executionRestClient.createExecution(execution);
       // Execution responseExecution1 = executionRestClient.createExecution(execution);
      //  assertEquals(responseExecution.id, responseExecution1.id);
        return responseExecution.id;
    }

  //  @Test
    public void testGetExecution() throws JSONException, HttpException, BadRequestParamException {
        String executionId = "0001496830897614-242ac112-0001";

        Execution responseExecution = executionRestClient.getExecution(projectId, issueId, executionId);

        log.info(responseExecution.toString());
        assertNotNull(responseExecution);
    }

  //  @Test
    public void testUpdateExecution(long issueId,String executionId) throws JSONException, HttpException, BadRequestParamException {
        Execution execution = new Execution();
        execution.id = executionId;
        execution.cycleId = cycleId;
        execution.issueId = issueId;
       // Defect defect = new Defect();
       // defect.id = 10300l;
     //   execution.defects = Arrays.<Defect>asList(defect);
        //execution.comment = "New Comment";

        ExecutionStatus executionStatus = new ExecutionStatus();
        executionStatus.id = 1;

        execution.status = executionStatus;

        Execution responseExecution = executionRestClient.updateExecution(execution);

        log.info(responseExecution.toString());
        assertNotNull(responseExecution);
     //   assertEquals(execution.comment, responseExecution.comment);
    }

    //@Test
    public void testDeleteExecution() throws JSONException, HttpException, BadRequestParamException {
        String executionId = "0001458978644495-d6eb16e347d4-0001";
        Long issueId = 10302l;
        Boolean response = executionRestClient.deleteExecution(issueId, executionId);

        log.info("Deleted execution: {}", response);
        assertTrue(response);
    }

   // @Test
    public void testGetExecutions() throws JSONException, HttpException, BadRequestParamException {
        int offset = 0;
        int size = 50;
        Long projectId = 10001l;
        Long issueId = 10024l;
        ZFJConnectResults<Execution> executionResults;
        List<String> ids = new ArrayList<String>();
        do{
            executionResults = executionRestClient.getExecutions(projectId, issueId, 2, 1);
            for (Execution execution:executionResults.resultList){
                ids.add(execution.id);
            }
            offset = offset + size;
        }while(executionResults.getTotalCount() > offset);
        log.info("Number founded executions:{}", ids.size());

        assertTrue(executionResults.totalCount > 0);
    }

   // @Test
    public void testGetExecutionsByCycle() throws JSONException, HttpException, BadRequestParamException {
        int offset = 0;
        int size = 10;
        String sortBy = ExecutionFieldId.STATUS.id;
        SortOrder sortOrder = SortOrder.ASC;
        ZFJConnectResults<Execution> searchResult = executionRestClient.getExecutionsByCycle(projectId, versionId, cycleId, offset, size, sortBy, sortOrder);

        List<Execution> executionList = searchResult.getResultList();
        for (Execution execution:executionList){
    //        log.info(execution.toString());
        }

        assertTrue(executionList.size() > 0);
    }

   // @Test
    public void testAddTestsToCycle() throws JobProgressException, HttpException, BadRequestParamException {
        Long issueId = 987l;
        Long projectId = 10000l;
        Long versionId = -1l;
        String cycleId = "yedhbkdn";

        List<Long>  issuesId = new ArrayList<Long>();
        issuesId.add(issueId);
        JobProgress jobProgress = executionRestClient.addTestsToCycle(projectId, versionId, cycleId, issuesId);
        log.info(jobProgress.toString());
        assertNotNull(jobProgress);
    }

 //   @Test
    public void testBulkUpdateStatus(List<String> list) throws JobProgressException, HttpException, BadRequestParamException {
        List<String> executionIds = new ArrayList();
        executionIds.add("0001459059333955-56459c344cdf-0001");
        Integer statusId = 1;
        Integer stepStatusId = 2;
        Boolean testStepStatusChangeFlag = true;
        Boolean clearDefectMappingFlag = false;

        JobProgress jobProgress = executionRestClient.bulkUpdateStatus(list, statusId, stepStatusId, testStepStatusChangeFlag);
        log.info(jobProgress.toString());
      //  assertNotNull(jobProgress);
    }

    //@Test
    public void testBulkDeleteExecutions() throws HttpException, JobProgressException, BadRequestParamException {
        List<String> executionIds = new ArrayList<String>();
        executionIds.add("0001461623027292-32cd60effffff460-0001");
        executionIds.add("0001461623027247-32cd60effffff460-0001");
        executionIds.add("0001461623027156-32cd60effffff460-0001");
        JobProgress jobProgress = executionRestClient.bulkDeleteExecutions(executionIds);

        log.info(jobProgress.toString());
    }
}
