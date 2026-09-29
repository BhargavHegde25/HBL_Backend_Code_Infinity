package com.temenos.infinity.product.bulkpaymentservices.javaservices;

import com.temenos.infinity.api.commons.constants.FabricConstants;
import  com.kony.dbputilities.util.ErrorCodeEnum;
import static org.junit.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.temenos.infinity.product.bulkpaymentservices.resource.api.BulkPaymentFileResource;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import com.kony.campaign.common.CampaignConstants;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

import org.powermock.core.classloader.annotations.PrepareForTest;


@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class })
public class FetchBulkPaymentUploadedFilesOperationTest {
    DBPAPIAbstractFactory dbpAPIAbstractFactory;
    ResourceFactory resourceFactory;
    JavaService2 javaService2;
    BulkPaymentFileResource resource;
    DataControllerRequest request;
    DataControllerResponse response;
    String methodID;
    Object[] inputArray;
    Result actualResult;
    Log4j2Configurator log4jConfigurator;

    private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
    private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;

    @BeforeClass
    public static void init() {
        mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
        mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);

    }

    @AfterClass
    public static void cleanup() {
        mockedStaticForDBPAPIFactoryImpl.close();
        mockedStaticLog4j2Configurator.close();
    }

    @Before
    public void executedBefore() throws Exception {


        methodID = "METHODID";
        inputArray = new Object[2];
        actualResult = new Result();

        request = mock(DataControllerRequest.class);
        response = mock(DataControllerResponse.class);
        resource = mock(BulkPaymentFileResource.class);

        dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
        log4jConfigurator = mock(Log4j2Configurator.class);
        resourceFactory = mock(ResourceFactory.class);

        when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
        when(dbpAPIAbstractFactory.getFactoryInstance(ResourceFactory.class)).thenReturn(resourceFactory);
        when(resourceFactory.getResource(BulkPaymentFileResource.class)).thenReturn(resource);
        when(Log4j2Configurator.getInstance()).thenReturn(log4jConfigurator);

        javaService2 = new FetchBulkPaymentUploadedFilesOperation();
    }
    @Test
    public void testInvoke() throws Exception {

        Result expectedResult = new Result();
        expectedResult.addParam(new Param("opstatus", "0", FabricConstants.INT));
        expectedResult.addParam(new Param("httpStatusCode", "200", FabricConstants.INT));

        Result result = new Result();
        //Mock should return expected results
        when(resource.fetchBulkPaymentUploadedFiles(methodID, inputArray, request, response)).thenReturn(result);

        //Actual call should return actualResults
        actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);

        //Compare expected and actual results. Here, Ideally we should check for success params
        assertEquals(actualResult.getParamByName("opstatus"), expectedResult.getParamByName("opstatus"));
        assertEquals(actualResult.getParamByName("httpStatusCode"), expectedResult.getParamByName("httpStatusCode"));
    }

    @Test
    public void testInvokeException() throws Exception {

        Result expectedResult = new Result();
        expectedResult.addParam(new Param(CampaignConstants.DBP_ERROR_CODE, ErrorCodeEnum.ERR_12000.getErrorCodeAsString()));
        expectedResult.addParam(new Param(CampaignConstants.DBP_ERROR_MESSAGE, ErrorCodeEnum.ERR_12000.getMessage()));

        when(resource.fetchBulkPaymentUploadedFiles(methodID, inputArray, request, response)).thenThrow(new RuntimeException());

        actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);

        assertEquals(actualResult.getParamByName("dbpErrCode").getValue(), expectedResult.getParamByName("dbpErrCode").getValue());
        assertEquals(actualResult.getParamByName("dbpErrMsg").getValue(), expectedResult.getParamByName("dbpErrMsg").getValue());
    }
}

