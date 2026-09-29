/**
 * 
 */
package com.kony.adminconsole.multientity.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface MultiEntityResource extends Resource {
	/**
     * @description returns a Result object, containing a list of all company legal units.
     * @param dcRequest
     * @return Result
     *
     * @author Abhishek Jain
     */
	public Result getAllCompanyLegalUnits(DataControllerRequest dcRequest);

}
