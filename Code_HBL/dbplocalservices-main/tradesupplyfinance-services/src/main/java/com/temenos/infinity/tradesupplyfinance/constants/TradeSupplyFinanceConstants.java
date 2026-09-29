/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.constants;

/**
 * @author k.meiyazhagan
 */
public interface TradeSupplyFinanceConstants {
    String PARAM_PROPERTY = "OMSTradeSupplyFinance";
    String PARAM_CUSTOMER_ID = "customerId";
    String PARAM_ORDER_ID = "orderId";
    String PARAM_PARTY_ID = "partyId";
    String PARAM_TYPE = "type";
    String PARAM_subType = "subType";
    String PARAM_subtype = "subtype";
    String PARAM_PAGE_SIZE = "pageSize";
    String DEFAULT_PAGE_SIZE = "100000";
    String PARAM_REQUEST_BODY = "requestBody";
    String PARAM_SRMSID = "srmsId";
    String PARAM_SERVICE_REQUESTS = "serviceReqs";
    String PARAM_SERVICE_REQ_ID = "serviceReqId";
    String PARAM_SERVICE_REQUEST_ID = "serviceRequestId";
    String PARAM_SERVICE_REQUEST_IDS = "serviceRequestIds";
    String PARAM_SERVICE_REQ_REQUEST_IN = "serviceReqRequestIn";
    String PARAM_ORDER_PROCESSED_TIME = "serviceReqProcessedTime";
    String PARAM_LASTUPDATEDTIMESTAMP = "lastUpdatedTimeStamp";

    String PARAM_FILE_ID = "fileId";
    String PARAM_LOOP_COUNT = "loop_count";
    String PARAM_LOOP_SEPARATOR = "loop_separator";
    String SEPARATOR_BILLS_ORCHESTRATION = ",‽";
    String SEPARATOR_INVOICES_ORCHESTRATION = "~‽";
    String SEPARATOR_USER_CUSTOMER_ID = "-";

    String PREFIX_TRADESUPPLYFINANCE_DOCUMENT = "TSFD";

    String HTTP_HEADER_X_KONY_AUTHORIZATION = "X-Kony-Authorization";
    String HTTP_HEADER_X_KONY_REPORTING_PARAMS = "X-Kony-ReportingParams";
    String HTTP_HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    String HTTP_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    String UTC_DATE_FORMAT = "yyyy-MM-dd";
    String DISPLAY_DATE_FORMAT_INVOICE = "dd/MM/yyyy";

    String MESSAGE = "message";
    String BACKEND_MESSAGE = "backendMessage";
    String PARAM_DBP_ERR_CODE = "dbpErrCode";
    String PARAM_DBP_ERR_MSG = "dbpErrMsg";
    String PARAM_DOCUMENT_NAME = "documentName";
    String PARAM_DOCUMENT_REFERENCE = "documentReference";
    String PARAM_DOCUMENT_CATEGORY_KEY = "SCF_Document_category";
    String PARAM_INVOICE_APPROVAL_KEY = "SCF_INVOICE_APPROVAL";
    String PARAM_INVOICE_UPLOAD_KEY = "SCF_INVOICE_UPLOAD_CONFIG";
    String PARAM_OPTION_YES = "YES";
    String PARAM_ROLE = "role";
    String PARAM_ROLE_BUYER = "BUYER";
    String PARAM_ROLE_SUPPLIER = "SUPPLIER";
    String PARAM_ROLE_ANCHOR = "ANCHOR";
    String PARAM_ROLE_COUNTERPARTY = "COUNTERPARTY";
    String PARAM_CURRENCY = "CURRENCY";
    String PARAM_BILL_TYPE = "BILL_TYPE";
    String PARAM_KEY = "key";
    String PARAM_ALLOWED_DOC_TYPES = "allowedDocTypes";
    String PARAM_ALLOWED_DOC_CATEGORY = "allowedDocCategory";
    String PARAM_INVOICE_REFERENCE = "invoiceReference";
    String PARAM_INVOICE_REFERENCES = "invoiceReferences";
    String PARAM_INVOICES = "invoices";
    String PARAM_INVOICE_DOCUMENTS = "invoiceDocuments";
    String PARAM_FAILED_INVOICE_IDS = "failedInvoiceIds";
    String PARAM_MODULE_ID = "module_id";
    String PARAM_SCF_RECORDS = "scf_records";
    String PARAM_CREATED_DATE = "created_date";
    String PARAM_UPDATED_DATE = "updated_date";
    String PARAM_CREATED_BY = "created_by";
    String PARAM_UPDATED_BY = "updated_by";
    String PARAM_RECORD_ID = "record_id";
    String PARAM_SCF_RECORDS_JSON = "record_data";
    String DB_PARAM_FILTER = "$filter";
    String DB_PARAM_EQUALS = " eq ";
    String PROPERTY_SCF_BACKEND = "SCF_BACKEND";
    String PARAM_SCF_BACKEND_SRMS = "SRMS";
    String PARAM_SCF_BACKEND_DBXDB = "DBXDB";

    String[] BULK_XLSX_HEADER_DATA = new String[]{"Bill Reference", "Bill Type", "Supplier ID", "Buyer ID", "Issue Date", "Maturity Date", "Currency", "Amount"};
    String PAYLOAD_EMPTY_INVOICE = "{'billReference':'','billType':'','supplierId':'','supplierName':'','buyerId':'','buyerName':'','issueDate':'','maturityDate':'','invoiceCurrency':'','invoiceAmount':'','role':''}";
}