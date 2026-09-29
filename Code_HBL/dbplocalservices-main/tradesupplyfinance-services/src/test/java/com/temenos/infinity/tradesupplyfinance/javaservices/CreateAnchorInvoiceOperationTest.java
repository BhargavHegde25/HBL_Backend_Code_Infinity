/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.javaservices;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;
import com.temenos.infinity.tradesupplyfinance.resource.api.AnchorInvoiceResource;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author k.meiyazhagan
 */
@PrepareForTest
public class CreateAnchorInvoiceOperationTest {
    private JavaService2 javaService;
    DataControllerRequest request;
    DataControllerResponse response;
    String methodID;
    Object[] inputArray;
    Result actualResult;
    AnchorInvoiceDTO inputDto;
    AnchorInvoiceResource anchorInvoiceResource;
    ResourceFactory resourceFactory;
    Log4j2Configurator log4j2Configurator;
    private static MockedStatic<Log4j2Configurator> log4j2ConfiguratorMockedStatic;
    private static MockedStatic<JSONUtils> JSONUtilsMockedStatic;
    private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;

    @BeforeClass
    public static void init() {
        mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
        log4j2ConfiguratorMockedStatic = Mockito.mockStatic(Log4j2Configurator.class);
        JSONUtilsMockedStatic = Mockito.mockStatic(JSONUtils.class);
    }

    @AfterClass
    public static void cleanup() {
        mockedStaticForDBPAPIFactoryImpl.close();
        log4j2ConfiguratorMockedStatic.close();
        JSONUtilsMockedStatic.close();
    }

    @Before
    public void executeBefore() {
        methodID = "MethodId";
        inputArray = new Object[10];
        javaService = new CreateAnchorInvoiceOperation();
        request = mock(DataControllerRequest.class);
        response = mock(DataControllerResponse.class);
        anchorInvoiceResource = mock(AnchorInvoiceResource.class);
        actualResult = new Result();
        inputDto = mock(AnchorInvoiceDTO.class);
        resourceFactory = mock(ResourceFactory.class);
    }

    // @Test
    public void testInvoke() throws Exception {
        Result result = mock(Result.class);
        log4j2ConfiguratorMockedStatic.when((MockedStatic.Verification) Log4j2Configurator.getInstance()).thenReturn(log4j2Configurator);
        JSONUtilsMockedStatic.when((MockedStatic.Verification)JSONUtils.parse(new JSONObject().toString(), AnchorInvoiceDTO.class)).thenReturn(inputDto);
        when(anchorInvoiceResource.createAnchorInvoice(inputDto, request)).thenReturn(result);
        assertEquals(0, actualResult.getAllParams().size());
    }

    @Test(expected = Exception.class)
    public void testInvoke_ThrowsException() throws Exception {
        log4j2ConfiguratorMockedStatic.when((MockedStatic.Verification) Log4j2Configurator.getInstance()).thenReturn(log4j2Configurator);
        JSONUtilsMockedStatic.when((MockedStatic.Verification) JSONUtils.parse(new JSONObject().toString(), AnchorInvoiceDTO.class)).thenReturn(inputDto);
        when(anchorInvoiceResource.createAnchorInvoice(inputDto, null)).thenThrow(Exception.class);
        assertThrows(Exception.class, () -> javaService.invoke(methodID, inputArray, request, response));
    }
}