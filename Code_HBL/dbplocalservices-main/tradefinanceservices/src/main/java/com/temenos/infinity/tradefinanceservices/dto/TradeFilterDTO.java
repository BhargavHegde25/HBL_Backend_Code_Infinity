package com.temenos.infinity.tradefinanceservices.dto;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.constants.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author naveen.yerra
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeFilterDTO extends FilterDTO {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private static final long serialVersionUID = -962737810345391247L;
    @JsonAlias({"order", "sortOrder"})
    private String _sortOrder;
    private String _queryType;
    @JsonAlias({"searchString"})
    private String _searchString;
    @JsonAlias({"sortBy", "sortByParam"})
    private String _sortByParam;
    @JsonAlias({"limit", "pageSize"})
    private String _pageSize;
    @JsonAlias({"offset", "pageOffset"})
    private String _pageOffset;
    private String _featureactionlist;
    @JsonAlias({"filterByParam"})
    private String _filterByParam;
    @JsonAlias({"filterByValue"})
    private String _filterByValue;
    private String _filterByStatus;
    private String _filterByTransactionType;
    @JsonAlias({"removeByParam"})
    private String _removeByParam;
    @JsonAlias({"removeByValue"})
    private Set<String> _removeByValue;
    @JsonAlias({"timeParam"})
    private String _timeParam;
    @JsonAlias({"timeValue"})
    private String _timeValue;
    @JsonAlias({"dashboardTimeParam"})
    private String _dashboardTimeParam;
    @JsonAlias({"dashboardTimeValue"})
    private String _dashboardTimeValue;

    public String get_dashboardTimeParam() {
        return _dashboardTimeParam;
    }

    public void set_dashboardTimeParam(String _dashboardTimeParam) {
        this._dashboardTimeParam = _dashboardTimeParam;
    }

    public String get_dashboardTimeValue() {
        return _dashboardTimeValue;
    }

    public void set_dashboardTimeValue(String _dashboardTimeValue) {
        this._dashboardTimeValue = _dashboardTimeValue;
    }

    @Override
    public String get_sortOrder() {
        return _sortOrder;
    }

    @Override
    public void set_sortOrder(String _sortOrder) {
        this._sortOrder = _sortOrder;
    }

    @Override
    public String get_queryType() {
        return _queryType;
    }

    @Override
    public void set_queryType(String _queryType) {
        this._queryType = _queryType;
    }

    @Override
    public String get_searchString() {
        return _searchString;
    }

    @Override
    public void set_searchString(String _searchString) {
        this._searchString = _searchString;
    }

    @Override
    public String get_sortByParam() {
        return _sortByParam;
    }

    @Override
    public void set_sortByParam(String _sortByParam) {
        this._sortByParam = _sortByParam;
    }

    @Override
    public String get_pageSize() {
        return _pageSize;
    }

    @Override
    public void set_pageSize(String _pageSize) {
        this._pageSize = _pageSize;
    }

    @Override
    public String get_pageOffset() {
        return _pageOffset;
    }

    @Override
    public void set_pageOffset(String _pageOffset) {
        this._pageOffset = _pageOffset;
    }

    @Override
    public String get_featureactionlist() {
        return _featureactionlist;
    }

    @Override
    public void set_featureactionlist(String _featureactionlist) {
        this._featureactionlist = _featureactionlist;
    }

    @Override
    public String get_filterByParam() {
        return _filterByParam;
    }

    @Override
    public void set_filterByParam(String _filterByParam) {
        this._filterByParam = _filterByParam;
    }

    @Override
    public String get_filterByValue() {
        return _filterByValue;
    }

    @Override
    public void set_filterByValue(String _filterByValue) {
        this._filterByValue = _filterByValue;
    }

    @Override
    public String get_filterByStatus() {
        return _filterByStatus;
    }

    @Override
    public void set_filterByStatus(String _filterByStatus) {
        this._filterByStatus = _filterByStatus;
    }

    @Override
    public String get_filterByTransactionType() {
        return _filterByTransactionType;
    }

    @Override
    public void set_filterByTransactionType(String _filterByTransactionType) {
        this._filterByTransactionType = _filterByTransactionType;
    }

    @Override
    public String get_removeByParam() {
        return _removeByParam;
    }

    @Override
    public void set_removeByParam(String _removeByParam) {
        this._removeByParam = _removeByParam;
    }

    @Override
    public Set<String> get_removeByValue() {
        return _removeByValue;
    }

    @Override
    public void set_removeByValue(Set<String> _removeByValue) {
        this._removeByValue = _removeByValue;
    }

    @Override
    public String get_timeParam() {
        return _timeParam;
    }

    @Override
    public void set_timeParam(String _timeParam) {
        this._timeParam = _timeParam;
    }

    @Override
    public String get_timeValue() {
        return _timeValue;
    }

    @Override
    public void set_timeValue(String _timeValue) {
        this._timeValue = _timeValue;
    }

    @Override
    public <T> List<T> filter(List<T> inputlist) {
        if (inputlist == null || inputlist.size() < 1) {
            alert.prepareError("Invalid data for filter.").log();
            return new ArrayList<>();
        }

        if (StringUtils.isNotEmpty(_removeByParam) && CollectionUtils.isNotEmpty(_removeByValue)) {
            inputlist = removeBy(inputlist);
        }

        if (StringUtils.isNotEmpty(_filterByParam) && StringUtils.isNotEmpty(_filterByValue)) {
            inputlist = filterBy(inputlist, _filterByParam, _filterByValue);
        }

        if (StringUtils.isNotEmpty(_searchString)) {
            inputlist = searchBy(inputlist, _searchString);
        }

        if (StringUtils.isNotEmpty(_timeParam) && StringUtils.isNotEmpty(_timeValue)) {
            inputlist = filterByTimeFrame(inputlist);
        }

        if (StringUtils.isNotEmpty(_sortByParam) && StringUtils.isNotEmpty(_sortOrder)) {
            sortBy(inputlist, _sortByParam, _sortOrder);
        }

        if (StringUtils.isNotEmpty(_pageSize) && StringUtils.isNotEmpty(_pageOffset)) {
            inputlist = paginateBy(inputlist, Integer.parseInt(_pageSize), Integer.parseInt(_pageOffset));
        }

        if (StringUtils.isNotEmpty(_dashboardTimeParam) && StringUtils.isNotEmpty(_dashboardTimeValue)) {
            inputlist = filterByTimeFrameForDashboard(inputlist);
        }

        return inputlist;
    }

    private <T> List<T> filterByTimeFrameForDashboard(List<T> inputList) {
        List<T> filteredList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date currentDate = dateFormat.parse(_dashboardTimeValue);
            for (T ob : inputList) {
                Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(ob).toString(), String.class, Object.class);
                if (objMap.containsKey(_dashboardTimeParam) && objMap.get(_dashboardTimeParam) != null && StringUtils.isNotEmpty(objMap.get(_dashboardTimeParam).toString())) {
                    Date recordDate = dateFormat.parse(objMap.get(_dashboardTimeParam).toString());
                    String paymentStatus = objMap.get("paymentStatus").toString();
                    if (recordDate.equals(currentDate) || (recordDate.before(currentDate) && paymentStatus.equalsIgnoreCase("Overdue"))) {
                        filteredList.add(ob);
                    }
                }
            }
        } catch (ParseException e) {
            alert.prepareError("Failed on filtering by period due to date format", e).log();
        } catch (IOException e) {
            alert.prepareError("Failed on filtering by period while object parsing", e).log();
        }
        return filteredList;
    }

    private <T> List<T> removeBy(List<T> inputList) {

        List<T> removedList = new ArrayList<>();
        try {
            for (T t : inputList) {
                Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(t).toString(), String.class,
                        Object.class);
                if (objMap.get(_removeByParam) == null || !_removeByValue.contains(
                        objMap.get(_removeByParam).toString())) {
                    removedList.add(t);
                }
            }
        } catch (IOException e) {
            alert.prepareError("Failed on removing by params", e).log();
        }
        return removedList;
    }

    private <T> List<T> searchBy(List<T> inputList, String searchString) {

        List<T> filteredList = new ArrayList<T>();
        Map<String, Object> objMap;
        try {
            for (T obj : inputList) {
                objMap = JSONUtils.parseAsMap((new JSONObject(obj)).toString(), String.class, Object.class);
                String obString = objMap.values().toString();
                if (StringUtils.isNotEmpty(obString) && obString.toUpperCase().contains(searchString.toUpperCase())) {
                    filteredList.add(obj);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Failed on filtering by search", e).log();
        }

        return filteredList;
    }

    private <T> List<T> filterBy(List<T> inputList, String filterByParam, String filterByValue) {
        List<T> filteredList = new ArrayList<>();
        Map<String, Set<String>> filterParams = getFilterParamMap(filterByParam, filterByValue);
        List<T> temp;

        try {
            int i = 0;
            for (String key : filterParams.keySet()) {
                temp = new ArrayList<>();
                if (i == 0) {
                    for (T obj : inputList) {
                        Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(obj).toString(), String.class, Object.class);
                        String filterString = filterParams.get(key) != null ? filterParams.get(key).toString().toUpperCase() : null;
                        String dataString = objMap.get(key) != null ? objMap.get(key).toString().toUpperCase().trim() : null;
                        if (filterString != null && dataString != null && filterString.contains(dataString)) {
                            temp.add(obj);
                        }
                    }
                } else {
                    for (T obj : filteredList) {
                        Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(obj).toString(), String.class, Object.class);
                        String filterString = filterParams.get(key) != null ? filterParams.get(key).toString().toUpperCase() : null;
                        String dataString = objMap.get(key) != null ? objMap.get(key).toString().toUpperCase().trim() : null;
                        if (filterString != null && dataString != null && filterString.contains(dataString)) {
                            temp.add(obj);
                        }
                    }
                }
                filteredList = temp;
                i++;
            }
        } catch (IOException e) {
            alert.prepareError("Failed on filtering by params", e).log();
        }
        return filteredList;
    }

    private <T> List<T> filterByTimeFrame(List<T> inputList) {
        List<T> filteredList = new ArrayList<>();
        String[] timeValues = _timeValue.split(",");
        if (timeValues.length < 2 || !StringUtils.isNumeric(timeValues[0]) || StringUtils.isEmpty(timeValues[1])) {
            alert.prepareError("Failed on filtering by period due to improper param/values").log();
            return inputList;
        }
        int period = Integer.parseInt(timeValues[0]) * (-1);
        ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT);
        try {
            Date cutoffDate = dateFormat.parse(application.getServerTimeStamp());
            if (timeValues[1].toUpperCase().contains(Constants.DAY)) {
                cutoffDate = DateUtils.addDays(cutoffDate, period);
            } else if (timeValues[1].toUpperCase().contains(Constants.WEEK)) {
                cutoffDate = DateUtils.addWeeks(cutoffDate, period);
            } else if (timeValues[1].toUpperCase().contains(Constants.MONTH)) {
                cutoffDate = DateUtils.addMonths(cutoffDate, period);
            } else if (timeValues[1].toUpperCase().contains(Constants.YEAR)) {
                cutoffDate = DateUtils.addYears(cutoffDate, period);
            }
            for (T ob : inputList) {
                Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(ob).toString(), String.class, Object.class);
                if (objMap.containsKey(_timeParam) && objMap.get(_timeParam) != null && StringUtils.isNotEmpty(objMap.get(_timeParam).toString())) {
                    Date recordDate = dateFormat.parse(objMap.get(_timeParam).toString());
                    if (!recordDate.before(cutoffDate)) {
                        filteredList.add(ob);
                    }
                }
            }
        } catch (ParseException e) {
            alert.prepareError("Failed on filtering by period due to date format", e).log();
        } catch (IOException e) {
            alert.prepareError("Failed on filtering by period while object parsing", e).log();
        }
        return filteredList;
    }

    private <T> void sortBy(List<T> inputlist, String sortParam, String sortOrder) {

        inputlist.sort((ob1, ob2) -> {
            Map<String, Object> obj1Map;
            Map<String, Object> obj2Map;
            try {
                obj1Map = JSONUtils.parseAsMap(new JSONObject(ob1).toString(), String.class, Object.class);
                obj2Map = JSONUtils.parseAsMap(new JSONObject(ob2).toString(), String.class, Object.class);
                String ob1val = obj1Map.get(sortParam) == null ? null : obj1Map.get(sortParam).toString();
                String ob2val = obj2Map.get(sortParam) == null ? null : obj2Map.get(sortParam).toString();
                if (NumberUtils.isParsable(ob1val) && NumberUtils.isParsable(ob2val)) {
                    if ("DESC".equalsIgnoreCase(sortOrder)) {
                        return Double.compare(Double.parseDouble(ob2val), Double.parseDouble(ob1val));
                    } else {
                        return Double.compare(Double.parseDouble(ob1val), Double.parseDouble(ob2val));
                    }
                } else {
                    if (ob1val != null && ob2val != null && "DESC".equalsIgnoreCase(sortOrder)) {
                        return ob2val.compareTo(ob1val);
                    } else if (ob1val != null && ob2val != null) {
                        return ob1val.compareTo(ob2val);
                    }
                    return 0;
                }
            } catch (Exception e) {
                alert.prepareError("Failed on sorting", e).log();
                return 0;
            }
        });
    }

    private <T> List<T> paginateBy(List<T> inputlist, int pageSize, int pageOffset) {

        if (pageSize < 1 || inputlist == null || inputlist.size() == 0 || inputlist.size() <= pageOffset) {
            alert.prepareError("Invalid filters for pagination.").log();
            return new ArrayList<>();
        }
        int toIndex = pageOffset + pageSize;
        if (toIndex > inputlist.size()) {
            toIndex = inputlist.size();
        }
        return inputlist.subList(pageOffset, toIndex);
    }

    private Map<String, Set<String>> getFilterParamMap(String params, String values) {
        Map<String, Set<String>> map = new HashMap<>();
        String key;
        String value;
        int i = 0, j = 0, temp;
        while (i < params.length() - 1 && j < values.length() - 1) {
            temp = params.indexOf(',', i);
            if (temp == -1) {
                temp = params.length();
            }
            key = params.substring(i, temp);
            i = temp + 1;

            temp = values.indexOf(',', j);
            if (temp == -1) {
                temp = values.length();
            }
            value = values.substring(j, temp);
            j = temp + 1;

            if (!map.containsKey(key)) {
                map.put(key, new HashSet<>());
            }
            map.get(key.trim()).add(value.trim());
        }
        return map;
    }

    @Override
    public boolean isValidFilter() {
        return super.isValidFilter();
    }
}
