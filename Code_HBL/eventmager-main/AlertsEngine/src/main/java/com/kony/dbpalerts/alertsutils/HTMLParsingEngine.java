package com.kony.dbpalerts.alertsutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HTMLParsingEngine {

	private HTMLParsingEngine() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static void setTableRowsEqualToJson(JsonArray jarr, Elements rows) {
		if (jarr == null || rows == null || rows.size() - 1 == jarr.size())
			return;
		if (rows.size() - 1 < jarr.size())
			while (rows.size() - 1 != jarr.size()) {
				rows.add(rows.get(rows.size() - 1).clone());
			}
		else if (rows.size() - 1 > jarr.size()) {
			while (rows.size() - 1 != jarr.size()) {
				rows.remove(rows.get(rows.size() - 1));
			}
		}
	}

	public static void setBulkTemplates(Map<String, String> alertcontentfieldsfromeventdata,
			Map<String, String> alertcontentfieldsfromotherdata, Map<String, String> sessiondata,
			Map<String, JsonObject> communicationtemplate) {
		if (alertcontentfieldsfromeventdata.containsKey(AlertConstants.TRANSACTIONS)) {
			setBulkTempalte(alertcontentfieldsfromeventdata.get(AlertConstants.TRANSACTIONS), communicationtemplate);
		} else if (alertcontentfieldsfromotherdata.containsKey(AlertConstants.TRANSACTIONS)) {
			setBulkTempalte(alertcontentfieldsfromotherdata.get(AlertConstants.TRANSACTIONS), communicationtemplate);
		} else if (sessiondata.containsKey(AlertConstants.TRANSACTIONS)) {
			setBulkTempalte(sessiondata.get(AlertConstants.TRANSACTIONS), communicationtemplate);
		}
	}

	public static void setBulkTempalte(String transactionsarraystring, Map<String, JsonObject> communicationtemplate) {
		if (transactionsarraystring == null || transactionsarraystring.equals(""))
			return;
		if (communicationtemplate == null || communicationtemplate.isEmpty())
			return;
		try {
			JsonElement transactionsarray = new JsonParser().parse(transactionsarraystring);
			if (transactionsarray.isJsonArray()) {
				setCommunicationTemplateBulkBillPay(communicationtemplate, transactionsarray.getAsJsonArray());
			}
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
	}

	private static void removeTableAttributes(Element table) {
		List<String> attToRemove = new ArrayList<>();
		Attributes at = table.attributes();
		for (Attribute a : at) {
			attToRemove.add(a.getKey());
		}

		for (String att : attToRemove) {
			table.removeAttr(att);
		}
	}

	private static void setCommunicationTemplateBulkBillPay(Map<String, JsonObject> communicationtemplate,
			JsonArray jarr) {
		try {
			if (!communicationtemplate.containsKey(AlertConstants.CH_EMAIL)
					|| communicationtemplate.get(AlertConstants.CH_EMAIL) == null
					|| communicationtemplate.get(AlertConstants.CH_EMAIL).isJsonNull()
					|| !communicationtemplate.get(AlertConstants.CH_EMAIL).has(AlertConstants.TEXT)
					|| communicationtemplate.get(AlertConstants.CH_EMAIL).get(AlertConstants.TEXT) == null) {
				return;
			}
			String texttobemodified = communicationtemplate.get(AlertConstants.CH_EMAIL).get(AlertConstants.TEXT)
					.toString();
			Document document = Jsoup.parse(texttobemodified);
			Element table = document.select("table").get(0);
			removeTableAttributes(table);
			table.attr("border", "1");
			Elements rows = table.select("tr");
			setTableRowsEqualToJson(jarr, rows);
			for (int i = 0, j = 1; i < jarr.size(); i++, j++) {
				if (j < rows.size()) {
					Element row = rows.get(j);
					Elements cols = row.select("td");
					getReplacedCol(jarr.get(i).getAsJsonObject(), cols);
				}
			}
			table.empty();
			table.append(rows.toString());
			texttobemodified = document.body().toString();
			texttobemodified = texttobemodified.replace("\\&quot;", "");

			communicationtemplate.get(AlertConstants.CH_EMAIL).addProperty(AlertConstants.TEXT, texttobemodified);
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
	}

	private static void getReplacedCol(JsonObject obj, Elements cols) {

		for (Entry<String, JsonElement> entry : obj.entrySet()) {
			for (int i = 0; i < cols.size(); i++) {
				Element col = cols.get(i);
				String nameWithHash = "[#]" + entry.getKey() + "[/#]";
				if (nameWithHash.equals(col.text())) {
					col.empty();
					col.appendText(entry.getValue().getAsString());
				}
			}
		}
	}
}
