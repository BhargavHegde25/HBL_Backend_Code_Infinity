/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.mapper;

import com.dbp.core.api.BusinessDelegate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author k.meiyazhagan
 */
@PrepareForTest
public class TradeSupplyFinanceBusinessDelegateMapperTest {

    TradeSupplyFinanceBusinessDelegateMapper mapper;
    @Mock
    HashMap<? extends BusinessDelegate, ? extends BusinessDelegate> mockedHashMap;

    @Before
    public void executedBeforeEach() {
        mapper = new TradeSupplyFinanceBusinessDelegateMapper();
    }

    @Test
    public void testGetAPIMappings() {
        for (Map.Entry<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> entry : mapper.getAPIMappings().entrySet()) {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
        }
    }

    @Test
    public void testGetParameterizedAPITypeClass() {
        assertEquals(BusinessDelegate.class, mapper.getParameterizedAPITypeClass());
    }
}