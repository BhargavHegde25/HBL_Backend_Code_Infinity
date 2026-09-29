/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.constants;

/**
 * @author mrunalini.adepu
 */
public interface TradeLendingConstants {

    String DB_PARAM_EQUALS = " eq ";
    String PARAM_CREATED_BY = "created_by";
    String PARAM_UPDATED_BY = "updated_by";
    String PARAM_RECORD_ID = "record_id";
    String PARAM_LD_RECORDS_JSON = "record_data";
    String DB_PARAM_FILTER = "$filter";
    String PARAM_DBP_ERR_CODE = "dbpErrCode";
    String PARAM_DBP_ERR_MSG = "dbpErrMsg";
    String PARAM_MODULE_ID = "module_id";
    String SEPARATOR_USER_CUSTOMER_ID = "-";
    String PARAM_LD_RECORDS = "ld_records";
    String HTTP_HEADER_X_KONY_AUTHORIZATION = "X-Kony-Authorization";
    String HTTP_HEADER_X_KONY_REPORTING_PARAMS = "X-Kony-ReportingParams";
    String HTTP_HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    String HTTP_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    String HEADER_APPLICATION_PDF = "application/pdf";
    String INFINITY_LOGO = "DigitalBanking_logo.png";
    String PARAM_DOCUMENT_NAME = "documentName";
    String PARAM_DOCUMENT_REFERENCE = "documentReference";
    String PARAM_DOCUMENT_CATEGORY_KEY = "LD_Document_category";
    String PROPERTY_LD_BACKEND = "LD_BACKEND";
    String PARAM_LD_BACKEND_SRMS = "SRMS";
    String PARAM_LD_BACKEND_DBXDB = "DBXDB";
    String PARAM_ORDER_ID = "orderId";
    String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    String PARAM_PROPERTY = "OMSTradeLending";
    String PARAM_CREATED_DATE = "created_date";
    String PARAM_UPDATED_DATE = "updated_date";
}