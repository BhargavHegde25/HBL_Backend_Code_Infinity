package com.kony.dbputilities.fileutil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;

public class CSVGenerator implements FileGenerator {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private String FIELD_SEPARATOR = ",";
    private String HEADER_SEPARATOR = "\t,";

    private String[] blockedheaders = new String[] {  "Reference Number", "Amount", "From Date" ,"To Date","Description"};
    private String[] blockedfields = new String[] { "transactionReference", "lockedAmount", "fromDate", "toDate", "lockReason" };

    private String dateFormat = "dd/MM/yyyy";
    private String statementOpeningBalance = "";
    private String statementClosingBalance = "";
    public String getFieldSeparator() {
        return FIELD_SEPARATOR;
    }

    public void setFieldSeparator(String fIELD_SEPARATOR) {
        FIELD_SEPARATOR = fIELD_SEPARATOR;
    }

    @Override
    public byte[] generateFile(JsonArray data, String title, String generatedBy, String startDate, String endDate,
            Map<String, String> fieldList, Map<String, Object> otherData, String filters,String downloadType,Map<String, String> inputParams) throws IOException {
    	
    	diagnostic.debug("Other data: ###"+ otherData.toString());
    	diagnostic.debug("startDate data: ###"+ startDate);
    	diagnostic.debug("endDate data: ###"+ endDate);
    	
    	// HBL: for month-titled statements the printed start date must be the 1st of that month
    	if (isMonthlyTitle(title)) {
    		startDate = normalizeSearchStartDate(startDate);
    		diagnostic.debug("normalized startDate data: ###" + startDate);
    	}
    	
    	String branch = null;

    	Object userDetailsObj = otherData.get("userDetails");
    	if (userDetailsObj != null) {
    	    JSONObject userDetailsJson = new JSONObject(userDetailsObj.toString());
    	    branch = userDetailsJson.optString("branch");
    	    
    	    if(!StringUtils.isNotBlank(branch))
    	    	branch = "-";
    	}
    	
    	//TODO: Move this to static block instead of repeating in all the methods
		try {
			this.dateFormat = EnvironmentConfigurationsHandler.getValue("DATE_FORMAT");
		} catch (Exception e1) {
			//if not able to read the server property use the default value
		}
    	
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
        	extractStatementBalances(data);
            createHeader(bos, title, generatedBy, startDate, endDate, fieldList,
                    (String) otherData.get("accountNumber"), branch);
            if (null != data && data.size() > 0) {
             //   for (int i = data.size() - 1; i >= 0; i--) {
            	  for (int i = 0; i < data.size(); i++) {
                    bos.write(getCsvRow(fieldList, data.get(i).getAsJsonObject(),this.dateFormat).getBytes());
               //     if (i > 0) {
                        bos.write(getBytes(System.lineSeparator()));
               //     }
                }
            }
            writeClosingBalanceRow(bos);
            return bos.toByteArray();
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    private String getCsvRow(Map<String, String> fieldList, JsonObject rowData,String paymentDateFormat) {
        StringBuilder csvRow = new StringBuilder();
        Iterator<String> itr = fieldList.keySet().iterator();
        String field = null;
        while (itr.hasNext()) {
            field = itr.next();
			diagnostic.prepareDebug("field ###"+ field).log();
			diagnostic.prepareDebug("rowData ###"+ rowData).log();
			diagnostic.prepareDebug("dateFormat ###"+ dateFormat).log();
			//if ("transactionDate".equals(field) || "fromDate".equals(field) || "toDate".equals(field)) {
			/** if ("fromDate".equals(field) || "toDate".equals(field)) {
				csvRow.append(HelperMethods.convertDateFormat(rowData.get(field).getAsString(), this.dateFormat));
			} else if ("Date".equals(field)) {
				csvRow.append(HelperMethods.convertDateFormat(rowData.get(field).getAsString(), paymentDateFormat));
			} else {
				csvRow.append((null == rowData.get(field)) ? "" : rowData.get(field).getAsString());
			} **/
			csvRow.append((null == rowData.get(field)) ? "" : rowData.get(field).getAsString());
			if (itr.hasNext()) {
				csvRow.append(FIELD_SEPARATOR);
			}
        }
        return csvRow.toString();
    }
    
    private String getCombinedStatementsCsvRow(Map<String, String> fieldList, JsonObject rowData) throws Exception{
        StringBuilder csvRow = new StringBuilder();
        Iterator<String> itr = fieldList.keySet().iterator();
        String field = null;
        while (itr.hasNext()) {
            field = itr.next();
			try {
				if ("transactionDate".equals(field)) {
					csvRow.append(HelperMethods.convertDateFormat(rowData.get(field).getAsString(), this.dateFormat));
				} else {
					csvRow.append((null == rowData.get(field)) ? "" : rowData.get(field).getAsString());
				}
				if (itr.hasNext()) {
					csvRow.append(FIELD_SEPARATOR);
				}
			} catch (Exception e) {
				alert.prepareError("Error in creating csv row",e).log();	
				throw e;
			}
        }
        return csvRow.toString();
    }

    private void createHeader(ByteArrayOutputStream bos, String title, String generatedBy, String startDate,
            String endDate, Map<String, String> fieldList, String accountNumber, String bankName) throws IOException {
        SimpleDateFormat dateFormatSD = new SimpleDateFormat(this.dateFormat+" hh:mm a");
        bos.write(getBytes("Title : " + title));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Report generated by : " + generatedBy));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Report generated on : " + dateFormatSD.format(new Date())));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Account Number : " + accountNumber));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Bank Name : " + "Himalayan Bank Ltd"));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Branch Name : " + bankName));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Start Date : " + (StringUtils.isBlank(startDate) ? "NA" : startDate)));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("End Date : " + (StringUtils.isBlank(endDate) ? "NA" : endDate)));
        bos.write(getBytes(System.lineSeparator()));
        if (StringUtils.isNotBlank(this.statementOpeningBalance)) {
            bos.write(getBytes("Opening Balance : "
                    + com.kony.dbputilities.util.CommonUtils.formatDecimal(this.statementOpeningBalance)));
            bos.write(getBytes(System.lineSeparator()));
        }
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes(System.lineSeparator()));
        
        StringBuilder csvRow = new StringBuilder();
        Iterator<String> itr = fieldList.keySet().iterator();
        while (itr.hasNext()) {
            csvRow.append(fieldList.get(itr.next()));
            if (itr.hasNext()) {
                csvRow.append(HEADER_SEPARATOR);
            }
        }
        bos.write(getBytes(csvRow.toString()));
        bos.write(getBytes(System.lineSeparator()));
    }
    
    /**
     * HBL: openingBalance / closingBalance are per BOOKING DATE, not per transaction
     * and not per statement. Every row of the same booking date carries the same pair.
     * (Enquiry AC.API.NOF.TRANSACTIONS.2.0.0, conversion routine HBL.CONV.TXN.BAL.)
     *
     * Statement Opening Balance = openingBalance of the OLDEST booking date in the array
     * Statement Closing Balance = closingBalance of the NEWEST booking date in the array
     */
    private void extractStatementBalances(JsonArray data) {
        this.statementOpeningBalance = "";
        this.statementClosingBalance = "";
        if (null == data || data.size() == 0) {
            return;
        }
        String minDate = null;
        String maxDate = null;
        for (int i = 0; i < data.size(); i++) {
            JsonObject rowData = data.get(i).getAsJsonObject();
            String bookingDate = "";
            if (rowData.has("postedDate") && !rowData.get("postedDate").isJsonNull()) {
                bookingDate = rowData.get("postedDate").getAsString();
            } else if (rowData.has("transactionDate") && !rowData.get("transactionDate").isJsonNull()) {
                bookingDate = rowData.get("transactionDate").getAsString();
            }
            if (StringUtils.isBlank(bookingDate)) {
                continue;
            }
            if (minDate == null || bookingDate.compareTo(minDate) < 0) {
                minDate = bookingDate;
                if (rowData.has("openingBalance") && !rowData.get("openingBalance").isJsonNull()) {
                    this.statementOpeningBalance = rowData.get("openingBalance").getAsString();
                }
            }
            if (maxDate == null || bookingDate.compareTo(maxDate) > 0) {
                maxDate = bookingDate;
                if (rowData.has("closingBalance") && !rowData.get("closingBalance").isJsonNull()) {
                    this.statementClosingBalance = rowData.get("closingBalance").getAsString();
                }
            }
        }
        diagnostic.prepareDebug("HBL## csv statement openingBalance=" + this.statementOpeningBalance
                + ", closingBalance=" + this.statementClosingBalance).log();
    }

    /** HBL: writes the statement closing balance as the last line of the file. */
    private void writeClosingBalanceRow(ByteArrayOutputStream bos) throws IOException {
        if (StringUtils.isBlank(this.statementClosingBalance)) {
            return;
        }
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Closing Balance : "
                + com.kony.dbputilities.util.CommonUtils.formatDecimal(this.statementClosingBalance)));
        bos.write(getBytes(System.lineSeparator()));
    }

    private byte[] getBytes(String str) {
        if (null == str) {
            str = "";
        }
        return str.getBytes();
    }

    @Override
    public String getContentType() {
        return "text/csv";
    }
    @Override
	public byte[] generateLoanFile(JsonArray data, String title, String generatedBy, 
			Map<String, String> fieldList, Map<String, Object> otherData, String filters,String installmentType,Map<String, String> summaryDetails,String paymentDateFormat) throws IOException {
		try {
			this.dateFormat = EnvironmentConfigurationsHandler.getValue("DATE_FORMAT");
		} catch (Exception e1) {
			//if not able to read the server property use the default value
		}
    	try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
			createLoanHeader(bos, title, generatedBy, fieldList,
                    (String) otherData.get("accountNumber"), (String) otherData.get("accountName"), summaryDetails);
			
			JsonArray paid = new JsonArray();
			JsonArray overdue = new JsonArray();
			JsonArray future = new JsonArray();
			
			if (null != data && data.size() > 0) {
				 for (int i = data.size() - 1; i >= 0; i--) {
					 String Type = data.get(i).getAsJsonObject().get("InstallmentType").getAsString();
					 if(Type.equalsIgnoreCase(DBPUtilitiesConstants.PAID)) {
						 paid.add(data.get(i).getAsJsonObject());
					 }
					 else if(Type.equalsIgnoreCase(DBPUtilitiesConstants.DUE)) {
						 overdue.add(data.get(i).getAsJsonObject());
					 }
					 else if(Type.equalsIgnoreCase(DBPUtilitiesConstants.FUTURE)) {
						 future.add(data.get(i).getAsJsonObject());
					 }	 
				 }
			}
			
			if (installmentType.equalsIgnoreCase("All")) {
				bos.write(getBytes(System.lineSeparator()));
				createInstallmentRows("OverDue",overdue,bos,fieldList,paymentDateFormat);
				bos.write(getBytes(System.lineSeparator()));
				createInstallmentRows("Paid", paid, bos, fieldList,paymentDateFormat);
				bos.write(getBytes(System.lineSeparator()));
				createInstallmentRows("Future",future,bos,fieldList,paymentDateFormat);
			}
			else if ( installmentType.equalsIgnoreCase("Paid")) {
				bos.write(getBytes(System.lineSeparator()));
				createInstallmentRows("Paid", paid, bos, fieldList,paymentDateFormat);
			}
			
			else if ( installmentType.equalsIgnoreCase("overdue")) {
				bos.write(getBytes(System.lineSeparator()));
				createInstallmentRows("OverDue",overdue,bos,fieldList,paymentDateFormat);
			}

			else if (installmentType.equalsIgnoreCase("future")) {
				bos.write(getBytes(System.lineSeparator()));
				createInstallmentRows("Future",future,bos,fieldList,paymentDateFormat);
			}
			

            return bos.toByteArray();
        } catch (IOException ioe) {
            alert.prepareError("Error in generation of csv",ioe).log();
            throw ioe;
        }
	}
    
    public byte[] generateCombinedStatementsFile(JsonArray dataObject, String title, String generatedBy, String startDate,
            String endDate, Map<String, String> fieldList, String bankName, String currencyCode, String paymentDateFormat,Map<String, Object> otherData) throws Exception {
		
		try {
			this.dateFormat = EnvironmentConfigurationsHandler.getValue("DATE_FORMAT");
		} catch (Exception e1) {
			//if not able to read the server property use the default value
		}
		
    	try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
			createStatementHeader(bos, title, generatedBy, startDate, endDate, fieldList, bankName);
			
			for(int i=0;i<dataObject.size();i++) {
	        	JsonElement accountElement = dataObject.get(i);
	        	JsonObject accountObject = accountElement.getAsJsonObject();
	        	String accountName = null;
	        	String accountNumber = null;
	        	JsonArray data = new JsonArray();
	        	if(StringUtils.isNotBlank(accountObject.get("accountName").toString()))
	        		accountName = accountObject.get("accountName").getAsString();
	        	if(StringUtils.isNotBlank(accountObject.get("accountNumber").toString()))
	        		accountNumber = accountObject.get("accountNumber").getAsString();
				if(StringUtils.isNotBlank(accountObject.get("transactionsList").toString()))
					data = accountObject.get("transactionsList").getAsJsonArray();
				JsonArray transactions = new JsonArray();
				if (null != data && data.size() > 0) {
					for (int j = data.size() - 1; j >= 0; j--) {
						transactions.add((JsonElement)data.get(j));
					}
				}
				bos.write(getBytes(System.lineSeparator()));
				createStatementRows(transactions, accountName, accountNumber, bos, fieldList, paymentDateFormat);
			}
            return bos.toByteArray();
        } catch (IOException ioe) {
            alert.prepareError("Error in generation of csv",ioe).log();
            throw ioe;
        } catch (Exception e) {
            alert.prepareError("Error while generating csv file",e).log();
            throw e;
        }
	}
	
	private void createInstallmentRows(String installemntType,JsonArray installmentObject,ByteArrayOutputStream bos,Map<String, String> fieldList,String paymentDateFormat) throws IOException {
		if (null != installmentObject && installmentObject.size() > 0) {
        	bos.write(getBytes(installemntType+" Installments"));
        	bos.write(getBytes(System.lineSeparator()));
        	createLoanSubHeader(bos, fieldList);
            for (int i = installmentObject.size() - 1; i >= 0; i--) {
                bos.write(getCsvRow(fieldList, installmentObject.get(i).getAsJsonObject(),paymentDateFormat).getBytes());
                if (i >= 0) {
                    bos.write(getBytes(System.lineSeparator()));
                }
            }
        }
	}
	
	private void createStatementRows(JsonArray statementObject, String accountName, String accountNumber, ByteArrayOutputStream bos,Map<String, String> fieldList,String paymentDateFormat) throws Exception {
		bos.write(getBytes("Account Name: "+accountName));
    	bos.write(getBytes(System.lineSeparator()));
    	bos.write(getBytes("Account Number: "+accountNumber));
    	bos.write(getBytes(System.lineSeparator()));
    	createLoanSubHeader(bos, fieldList);
		if (null != statementObject && statementObject.size() > 0) {
            for (int i = statementObject.size() - 1; i >= 0; i--) {
                bos.write(getCombinedStatementsCsvRow(fieldList, statementObject.get(i).getAsJsonObject()).getBytes());
                bos.write(getBytes(System.lineSeparator()));
            }
        }
	}
	
	private void createStatementHeader(ByteArrayOutputStream bos, String title, String generatedBy, String startDate,
            String endDate, Map<String, String> fieldList, String bankName) throws IOException {
        SimpleDateFormat reportGeneratedDateFormat = new SimpleDateFormat(this.dateFormat+" hh:mm a");
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
        bos.write(getBytes("Title : " + title));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Bank Name : " + bankName));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Report generated by : " + generatedBy));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Report generated on : " + reportGeneratedDateFormat.format(new Date())));
        bos.write(getBytes(System.lineSeparator()));
        try {
			startDate = HelperMethods.convertDateFormat(startDate, this.dateFormat);
			endDate = HelperMethods.convertDateFormat(endDate, this.dateFormat);
		} catch (ParseException e) {
			alert.prepareError("Error while parsing date" +e).log();
		}
        bos.write(getBytes("Start Date : " + (StringUtils.isBlank(startDate) ? "NA" : startDate)));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("End Date : " + (StringUtils.isBlank(endDate) ? "NA" : endDate)));
        bos.write(getBytes(System.lineSeparator()));
    }
	
	private void createLoanHeader(ByteArrayOutputStream bos, String title, String generatedBy, 
             Map<String, String> fieldList, String accountNumber, String accountName, Map<String, String> summary) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat+" hh:mm a");
        bos.write(getBytes("Title : " + title));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Account Name : " + accountName));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Account Number : " + accountNumber));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Report generated by : " + generatedBy));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes("Report generated on : " + dateFormat.format(new Date())));
        bos.write(getBytes(System.lineSeparator()));
        bos.write(getBytes(System.lineSeparator()));

		if (summary.size() > 0) {
			bos.write(getBytes("Summary"));
			bos.write(getBytes(System.lineSeparator()));
			if (summary.containsKey("OverDueInstallmentsCount")) {
				bos.write(getBytes("Overdue Payments:" +summary.get("OverDueInstallmentsCount") ));
				bos.write(getBytes(System.lineSeparator()));
			}
			if (summary.containsKey("PaidInstallmentsCount")) {
				bos.write(getBytes("Paid Installments:"+summary.get("PaidInstallmentsCount")));
				bos.write(getBytes(System.lineSeparator()));
			}
			if (summary.containsKey("FutureInstallmentsCount")) {
				bos.write(getBytes("Future Payments:"+summary.get("FutureInstallmentsCount")));
				bos.write(getBytes(System.lineSeparator()));
			}

		}
        bos.write(getBytes(System.lineSeparator()));


    }

	private void createLoanSubHeader(ByteArrayOutputStream bos,  Map<String, String> fieldList) throws IOException {
		
		StringBuilder csvRow = new StringBuilder();
        Iterator<String> itr = fieldList.keySet().iterator();
        while (itr.hasNext()) {
            csvRow.append(fieldList.get(itr.next()));
            if (itr.hasNext()) {
                csvRow.append(HEADER_SEPARATOR);
            }
        }
        bos.write(getBytes(csvRow.toString()));
        bos.write(getBytes(System.lineSeparator()));
		
	}
	
	@Override
	public byte[] generateBlockedFile(JsonArray data, String title, String generatedBy, String startDate, String endDate,
			Map<String, String> fieldList, Map<String, Object> otherData, String filters)
			throws IOException {
		
		try {
			this.dateFormat = EnvironmentConfigurationsHandler.getValue("DATE_FORMAT");
		} catch (Exception e1) {
			//if not able to read the server property use the default value
		}
		
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
            createHeader(bos, title, generatedBy, startDate, endDate, fieldList,
                    (String) otherData.get("accountNumber"), (String) otherData.get("bankName"));
            if (null != data && data.size() > 0) {
                for (int i = data.size() - 1; i >= 0; i--) {
                    bos.write(getCsvRow(fieldList, data.get(i).getAsJsonObject(),this.dateFormat).getBytes());
                    if (i > 0) {
                        bos.write(getBytes(System.lineSeparator()));
                    }
                }
            }
            return bos.toByteArray();
        } catch (IOException ioe) {
            throw ioe;
        }
    }
	
	/** HBL: true when the report title carries a month name, e.g. "June_2026", "Jun-2026". */
	private boolean isMonthlyTitle(String title) {
		if (StringUtils.isBlank(title)) {
			return false;
		}
		String normalizedTitle = StringUtils.lowerCase(StringUtils.trim(title));
		String[] monthNames = { "january", "february", "march", "april", "may", "june", "july",
				"august", "september", "october", "november", "december",
				"jan", "feb", "mar", "apr", "jun", "jul", "aug", "sept", "sep", "oct", "nov", "dec" };
		for (String monthName : monthNames) {
			if (StringUtils.contains(normalizedTitle, monthName)) {
				return true;
			}
		}
		return false;
	}

	/** HBL: forces the search start date to the 1st of its month, "2026-6-16" -> "2026-06-01". */
	private String normalizeSearchStartDate(String startDate) {
		if (StringUtils.isBlank(startDate)) {
			return startDate;
		}
		try {
			String value = StringUtils.trim(startDate);
			int year;
			int month;
			if (value.matches("\\d{8}")) {                     // yyyyMMdd
				year = Integer.parseInt(value.substring(0, 4));
				month = Integer.parseInt(value.substring(4, 6));
			} else {
				String[] dateParts = value.split("[-/.]");
				if (dateParts.length < 2) {
					return startDate;
				}
				year = Integer.parseInt(StringUtils.trim(dateParts[0]));
				month = Integer.parseInt(StringUtils.trim(dateParts[1]));
				if (month > 12 && dateParts.length > 2) {      // value came through as yyyy-dd-MM
					month = Integer.parseInt(StringUtils.trim(dateParts[2]));
				}
			}
			if (month < 1 || month > 12) {
				return startDate;
			}
			return year + "-" + StringUtils.leftPad(String.valueOf(month), 2, '0') + "-01";
		} catch (Exception e) {
			alert.prepareError("normalizeSearchStartDate failed for input=" + startDate
					+ " error=" + e.getMessage()).log();
			return startDate;
		}
	}
	
}
