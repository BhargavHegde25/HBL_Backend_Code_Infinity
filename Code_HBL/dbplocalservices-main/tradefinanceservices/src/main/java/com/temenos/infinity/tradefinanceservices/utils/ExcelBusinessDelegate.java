/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.utils;


import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;

import java.util.List;

public interface ExcelBusinessDelegate {

    <T> List<T> getList(DataControllerRequest request) throws ApplicationException;

}
