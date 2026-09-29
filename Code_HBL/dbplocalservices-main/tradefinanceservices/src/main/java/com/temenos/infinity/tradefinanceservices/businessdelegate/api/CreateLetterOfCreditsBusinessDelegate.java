package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.*;

/**
 * The Interface CreateLetterOfCreditsBusinessDelegate.
 */
public interface CreateLetterOfCreditsBusinessDelegate extends BusinessDelegate {

    /**
     * Creates the letter of credits.
     *
     * @param letterOfCredit the letter of credit
     * @param request        the request
     * @return the letter of credits DTO
     * @throws ApplicationException the application exception
     */
    LetterOfCreditsDTO createLetterOfCredits(LetterOfCreditsDTO letterOfCredit, DataControllerRequest request)
            throws ApplicationException;

    /**
     * Execute letter of credits.
     *
     * @param letterOfCredit the letter of credit
     * @param request        the request
     * @return the letter of credits DTO
     * @throws ApplicationException the application exception
     */
    LetterOfCreditsActionDTO executeLetterOfCredits(LetterOfCreditsActionDTO letterOfCredit,
                                                    DataControllerRequest request) throws ApplicationException;

    /**
     * Validate for approvals.
     *
     * @param letterOfCredit the letter of credit
     * @param request        the request
     * @return the letter of credits DTO
     * @throws ApplicationException the application exception
     */
    LetterOfCreditStatusDTO validateForApprovals(LetterOfCreditStatusDTO letterOfCredit, DataControllerRequest request)
            throws ApplicationException;


    /**
     * Amend letter of credits.
     *
     * @param letterOfCredit the letter of credit
     * @param request        the request
     * @return the letter of credits DTO
     * @throws ApplicationException the application exception
     */
    LetterOfCreditsAmendmentDTO amendLetterOfCredit(LetterOfCreditsAmendmentDTO letterOfCredit, DataControllerRequest request) throws ApplicationException;

    /**
     * Fetches the request details
     *
     * @param requestId
     * @return
     */
    public BBRequestDTO getAccountId(String requestId);

    /**
     * Updates the approval queue with the backend Id
     *
     * @param requestId
     * @param transactionId
     * @param isSelfApproved
     * @param featureActionId
     * @param request
     * @return
     */
    public LetterOfCreditStatusDTO updateBackendIdInApprovalQueue(String requestId, String transactionId,
                                                                  boolean isSelfApproved, String featureActionId, DataControllerRequest request);

    /**
     * Deletes the cheque book request order from the approval queue
     *
     * @param requestId
     * @return
     */
    public boolean deleteLetterOfCreditFromApprovalQueue(String requestId);

    LetterOfCreditsAmendmentDTO updateAmendLC(LetterOfCreditsAmendmentDTO amendmentDto, DataControllerRequest request);

}