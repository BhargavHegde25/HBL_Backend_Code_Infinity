package com.temenos.dbx.product.payeeservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import org.json.JSONObject;

public interface InternationalPayeeResource extends Resource {

    /**
     * This method creates an international payee
     *  @author KH2544
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to create a new international payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains newly created payeeId
     */
    public Result createPayee(String methodId, Object[] inputArray, DataControllerRequest request,
                                    DataControllerResponse response);


    /**
     *  This method is invoked to edit an International bank payee contract-cif linkage after the approval request is approved
     *  @author Subham Kumar Patel (21554)
     *
     *  @param request contains request handler
     *  @param inputObject contains the values (JSON Object of InterBankPayeeBackendDTO with contractCifMap indicating linked contract-cif map)
     *  @return Result object contains acknowledgement for edited payee
     */
    Result createPayeeAfterApproval( DataControllerRequest request, JSONObject inputObject, boolean isPending);



    /**
     * This method edits details of an existing international payee
     *  @author KH2544
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to edit a new international payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains edited payeeId
     */
    public Result editPayee(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);


    /**
     *  This method is invoked to edit an International bank payee optional fields after the approval request is approved
     *  @author Subham Kumar Patel (21554)
     *
     *  @param request contains request handler
     *  @param inputObject contains the values (JSON Object of InterBankPayeeBackendDTO with contractCifMap indicating which contract cif is linked and which is not)
     *                    with the edited optional fields which needs to be updated
     *  @return Result object contains acknowledgement for edited payee
     */
    Result editPayeeOptionalFieldsAfterApproval( DataControllerRequest request, JSONObject inputObject );

    /**
     *  This method is invoked to edit an International bank payee contract-cif linkage after the approval request is approved
     *  @author Subham Kumar Patel (21554)
     *
     *  @param request contains request handler
     *  @param inputObject contains the values (JSON Object of InterBankPayeeBackendDTO with contractCifMap indicating which contract cif is linked and which is not)
     *                    with the edited contract-cif linkage fields which needs to be updated
     *  @return Result object contains acknowledgement for edited payee
     */
    Result editPayeeLinkageAfterApproval( DataControllerRequest request, JSONObject inputObject );

    /**
     *  This method deletes an international payee
     *  @author KH2544
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to delete an existing international payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains acknowledgement for deleted payee
     */

    public Result deletePayee(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);


    /**
     *  This method deletes an International bank payee after the approval request is approved
     *  @author Subham Kumar Patel (21554)
     *
     *  @param request contains request handler
     *  @param deletePayeeObject contains the details of the InterBank Payee to be deleted
     *  @return Result object contains acknowledgement for deleted payee
     */
    Result deletePayeeAfterApproval( DataControllerRequest request, JSONObject deletePayeeObject );

    /**
     * This method fetches all international payees for a user or organization
     *  @author KH2544
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch all international payee
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains a list of all the payees
     */

    public Result fetchAllMyPayees(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response);


}
