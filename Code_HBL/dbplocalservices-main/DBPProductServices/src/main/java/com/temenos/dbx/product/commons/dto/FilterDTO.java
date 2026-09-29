package com.temenos.dbx.product.commons.dto;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.DBPDTO;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.constants.Constants;


@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterDTO implements DBPDTO{

	private static final long serialVersionUID = -962737810789392247L;
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@JsonAlias({"order","sortOrder"})
	private String _sortOrder;
	private String _queryType;
	@JsonAlias({"searchString"})
	private String _searchString;
	@JsonAlias({"sortBy","sortByParam"})
	private String _sortByParam;
	@JsonAlias({"limit","pageSize"})
	private String _pageSize;
	@JsonAlias({"offset","pageOffset"})
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

	public String get_filterByTransactionType() {
		return _filterByTransactionType;
	}

	public void set_filterByTransactionType(String _filterByTransactionType) {
		this._filterByTransactionType = _filterByTransactionType;
	}

	public FilterDTO() {
		super();
		this._sortOrder = "";
		this._queryType = "";
		this._searchString = "";
		this._sortByParam = "";
		this._pageSize = "";
		this._pageOffset = "";
		this._featureactionlist = "";
		this._filterByStatus = "";
		this._filterByTransactionType = "";
		this._filterByParam = "";
		this._filterByValue = "";
		this._timeParam = "";
		this._timeValue = "";
	}

	public FilterDTO(String _sortOrder, String _queryType, String _searchString, String _sortByParam, String _pageSize,
			String _pageOffset, String _featureactionlist, String _filterByParam, String _filterByValue,
			String _filterByStatus, String _filterByTransactionType, String _removeByParam, Set<String> _removeByValue, String _timeParam, String _timeValue) {
		super();
		this._sortOrder = _sortOrder;
		this._queryType = _queryType;
		this._searchString = _searchString;
		this._sortByParam = _sortByParam;
		this._pageSize = _pageSize;
		this._pageOffset = _pageOffset;
		this._featureactionlist = _featureactionlist;
		this._filterByParam = _filterByParam;
		this._filterByValue = _filterByValue;
		this._filterByStatus = _filterByStatus;
		this._filterByTransactionType = _filterByTransactionType;
		this._removeByParam = _removeByParam;
		this._removeByValue = _removeByValue;
		this._timeParam = _timeParam;
		this._timeValue = _timeValue;
	}

	public String get_filterByParam() {
		return _filterByParam;
	}

	public void set_filterByParam(String _filterByParam) {
		this._filterByParam = _filterByParam;
	}

	public String get_filterByValue() {
		return _filterByValue;
	}

	public void set_filterByValue(String _filterByValue) {
		this._filterByValue = _filterByValue;
	}

	public String get_filterByStatus() {
		return _filterByStatus;
	}

	public void set_filterByStatus(String _filterByStatus) {
		this._filterByStatus = _filterByStatus;
	}

	public String get_featureactionlist() {
		return _featureactionlist;
	}

	public void set_featureactionlist(String _featureactionlist) {
		this._featureactionlist = _featureactionlist;
	}

	public String get_sortOrder() {
		return _sortOrder;
	}

	public void set_sortOrder(String sortOrder) {
		this._sortOrder = sortOrder;
	}

	public String get_queryType() {
		return _queryType;
	}

	public void set_queryType(String queryType) {
		this._queryType = queryType;
	}

	public String get_searchString() {
		return _searchString;
	}

	public void set_searchString(String searchString) {
		this._searchString = searchString;
	}

	public String get_sortByParam() {
		return _sortByParam;
	}

	public void set_sortByParam(String sortByParam) {
		this._sortByParam = sortByParam;
	}

	public String get_pageSize() {
		return _pageSize;
	}

	public void set_pageSize(String pageSize) {
		this._pageSize = pageSize;
	}

	public String get_pageOffset() {
		return _pageOffset;
	}

	public void set_pageOffset(String pageOffset) {
		this._pageOffset = pageOffset;
	}
	
	public String get_removeByParam() {
		return _removeByParam;
	}

	public void set_removeByParam(String _removeByParam) {
		this._removeByParam = _removeByParam;
	}

	public Set<String> get_removeByValue() {
		return _removeByValue;
	}

	public void set_removeByValue(Set<String> _removeByValue) {
		this._removeByValue = _removeByValue;
	}
	
	public String get_timeParam() {
		return _timeParam;
	}

	public void set_timeParam(String _timeParam) {
		this._timeParam = _timeParam;
	}

	public String get_timeValue() {
		return _timeValue;
	}

	public void set_timeValue(String _timeValue) {
		this._timeValue = _timeValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_featureactionlist == null) ? 0 : _featureactionlist.hashCode());
		result = prime * result + ((_filterByParam == null) ? 0 : _filterByParam.hashCode());
		result = prime * result + ((_filterByStatus == null) ? 0 : _filterByStatus.hashCode());
		result = prime * result + ((_filterByTransactionType == null) ? 0 : _filterByTransactionType.hashCode());
		result = prime * result + ((_filterByValue == null) ? 0 : _filterByValue.hashCode());
		result = prime * result + ((_pageOffset == null) ? 0 : _pageOffset.hashCode());
		result = prime * result + ((_pageSize == null) ? 0 : _pageSize.hashCode());
		result = prime * result + ((_queryType == null) ? 0 : _queryType.hashCode());
		result = prime * result + ((_searchString == null) ? 0 : _searchString.hashCode());
		result = prime * result + ((_sortByParam == null) ? 0 : _sortByParam.hashCode());
		result = prime * result + ((_sortOrder == null) ? 0 : _sortOrder.hashCode());
		result = prime * result + ((_removeByParam == null) ? 0 : _removeByParam.hashCode());
		result = prime * result + ((_removeByValue == null) ? 0 : _removeByValue.hashCode());
		result = prime * result + ((_timeParam == null) ? 0 : _timeParam.hashCode());
		result = prime * result + ((_timeValue == null) ? 0 : _timeValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterDTO other = (FilterDTO) obj;
		if (_featureactionlist == null) {
			if (other._featureactionlist != null)
				return false;
		} else if (!_featureactionlist.equals(other._featureactionlist))
			return false;
		if (_filterByParam == null) {
			if (other._filterByParam != null)
				return false;
		} else if (!_filterByParam.equals(other._filterByParam))
			return false;
		if (_filterByStatus == null) {
			if (other._filterByStatus != null)
				return false;
		} else if (!_filterByStatus.equals(other._filterByStatus))
			return false;
		if (_filterByTransactionType == null) {
			if (other._filterByTransactionType != null)
				return false;
		} else if (!_filterByTransactionType.equals(other._filterByTransactionType))
			return false;
		if (_filterByValue == null) {
			if (other._filterByValue != null)
				return false;
		} else if (!_filterByValue.equals(other._filterByValue))
			return false;
		if (_pageOffset == null) {
			if (other._pageOffset != null)
				return false;
		} else if (!_pageOffset.equals(other._pageOffset))
			return false;
		if (_pageSize == null) {
			if (other._pageSize != null)
				return false;
		} else if (!_pageSize.equals(other._pageSize))
			return false;
		if (_queryType == null) {
			if (other._queryType != null)
				return false;
		} else if (!_queryType.equals(other._queryType))
			return false;
		if (_searchString == null) {
			if (other._searchString != null)
				return false;
		} else if (!_searchString.equals(other._searchString))
			return false;
		if (_sortByParam == null) {
			if (other._sortByParam != null)
				return false;
		} else if (!_sortByParam.equals(other._sortByParam))
			return false;
		if (_sortOrder == null) {
			if (other._sortOrder != null)
				return false;
		} else if (!_sortOrder.equals(other._sortOrder))
			return false;
		if (_timeParam == null) {
			if (other._timeParam != null)
				return false;
		} else if (!_timeParam.equals(other._timeParam))
			return false;
		if (_timeValue == null) {
			if (other._timeValue != null)
				return false;
		} else if (!_timeValue.equals(other._timeValue))
			return false;
		return true;
	}
	
	public <T> List<T> filter(List<T> inputlist) {
		
		if(inputlist==null || inputlist.size()<1) {
			alert.prepareError("Invalid data for filter.").log();
			return new ArrayList<T>();
		}
		
		if(StringUtils.isNotEmpty(_removeByParam) && CollectionUtils.isNotEmpty(_removeByValue)) {
			inputlist = removeBy(inputlist);
		}
		
		if(StringUtils.isNotEmpty(_filterByParam) && StringUtils.isNotEmpty(_filterByValue)) {
			inputlist = filterBy(inputlist, _filterByParam, _filterByValue);
		}
		
		if(StringUtils.isNotEmpty(_searchString)) {
			inputlist = searchBy(inputlist, _searchString);
		}
		
		if(StringUtils.isNotEmpty(_timeParam) && StringUtils.isNotEmpty(_timeValue)) {
			inputlist = filterByTimeFrame(inputlist);
		}
		
		if(StringUtils.isNotEmpty(_sortByParam) && StringUtils.isNotEmpty(_sortOrder)) {
			inputlist = sortBy(inputlist, _sortByParam, _sortOrder);
		}
		
		if(StringUtils.isNotEmpty(_pageSize) && StringUtils.isNotEmpty(_pageOffset)) {
			inputlist = paginateBy(inputlist, Integer.valueOf(_pageSize), Integer.valueOf(_pageOffset));
		}
		
		return inputlist;
	}
	
	private <T> List<T> removeBy(List<T> inputList){

		List<T> removedList = new ArrayList<T>();
		try {
			for(int i=0; i<inputList.size(); i++) {
				Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(inputList.get(i)).toString(), String.class, Object.class);
				if(objMap.get(_removeByParam) == null || !_removeByValue.contains(objMap.get(_removeByParam).toString())) {
					removedList.add(inputList.get(i));
				}
			}
		} catch (IOException e) {
			alert.prepareError("Failed on removing by params", e).log();
		}
		return removedList;
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> searchBy(List<T> inputlist, String searchString) {
		
		List<T> filteredList = new ArrayList<T>();
		try {
			for(Object ob : inputlist) {
				String obString = JSONUtils.stringify(ob); 
				if(StringUtils.isNotEmpty(obString) && obString.toUpperCase().contains(searchString.toUpperCase())) {
					filteredList.add((T)ob);
				}
			}
		} catch (IOException e) {
			alert.prepareError("Failed on filtering by search", e).log();
		}
		
		return filteredList;
	}
	
	private <T> List<T> filterBy(List<T> inputList, String filterByParam, String filterByValue) {
		List<T> filteredList = new ArrayList<T>();
		Map<String, Set<String>> filterParams = getFilterParamMap(filterByParam,filterByValue);
		List<T> temp;
		
		try {
			int i=0;
			for (String key : filterParams.keySet()) {
				temp = new ArrayList<T>();
				if(i==0) {
					for(T obj : inputList) {
						Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(obj).toString(), String.class, Object.class);
						String filterString = filterParams.get(key) != null ? filterParams.get(key).toString().toUpperCase() : null; 
						String dataString = objMap.get(key) != null ? objMap.get(key).toString().toUpperCase().trim() : null;
						if(filterString.contains(dataString)) {
							temp.add(obj);
						}
					}
				} else {
					for(T obj : filteredList) {
						Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(obj).toString(), String.class, Object.class);
						String filterString = filterParams.get(key) != null ? filterParams.get(key).toString().toUpperCase() : null; 
						String dataString = objMap.get(key) != null ? objMap.get(key).toString().toUpperCase().trim() : null;
						if(filterString.contains(dataString)) {
							temp.add(obj);
						}
					}
				}
				filteredList = temp;
				i++;
			}
		} catch(IOException e) {
			alert.prepareError("Failed on filtering by params", e).log();
		} 
		return filteredList;
	}
	
	private <T> List<T> filterByTimeFrame(List<T> inputList) {
		List<T> filteredList = new ArrayList<T>();
		String[] timeValues = _timeValue.split("\\,");
		if(timeValues.length<2 || !StringUtils.isNumeric(timeValues[0]) || StringUtils.isEmpty(timeValues[1])) {
			alert.prepareError("Failed on filtering by period due to improper param/values").log();
			return inputList;
		}
		int period = Integer.parseInt(timeValues[0]) * (-1);
		ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT);
		try {
			Date cutoffDate = dateFormat.parse(application.getServerTimeStamp());
			if(timeValues[1].toUpperCase().contains(Constants.DAY)) {
				cutoffDate = DateUtils.addDays(cutoffDate, period);
			} else if(timeValues[1].toUpperCase().contains(Constants.WEEK)) {
				cutoffDate = DateUtils.addWeeks(cutoffDate, period);
			}else if(timeValues[1].toUpperCase().contains(Constants.MONTH)) {
				cutoffDate = DateUtils.addMonths(cutoffDate, period);
			}else if(timeValues[1].toUpperCase().contains(Constants.YEAR)) {
				cutoffDate = DateUtils.addYears(cutoffDate, period);
			}
			for(T ob : inputList) {
				Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(ob).toString(), String.class, Object.class);
				if(objMap.containsKey(_timeParam) && objMap.get(_timeParam) != null && StringUtils.isNotEmpty(objMap.get(_timeParam).toString())) {
					Date recordDate = dateFormat.parse(objMap.get(_timeParam).toString());
					if(!recordDate.before(cutoffDate)) {
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
	
	private <T> List<T> sortBy(List<T> inputlist, String sortParam, String sortOrder) {
		
		Collections.sort(inputlist, new Comparator<T>() {

			@Override
			public int compare(T ob1, T ob2) {
				
				Map<String, Object> obj1Map;
				Map<String, Object> obj2Map;
				try {
					obj1Map = JSONUtils.parseAsMap(new JSONObject(ob1).toString(), String.class, Object.class);
					obj2Map = JSONUtils.parseAsMap(new JSONObject(ob2).toString(), String.class, Object.class);
					String ob1val = obj1Map.get(sortParam) == null ? null : obj1Map.get(sortParam).toString();
					String ob2val = obj2Map.get(sortParam) == null ? null : obj2Map.get(sortParam).toString();
					if(NumberUtils.isParsable(ob1val) && NumberUtils.isParsable(ob2val)) {
						if("DESC".equalsIgnoreCase(sortOrder)) {
							return ((Double)Double.parseDouble(ob2val)).compareTo((Double)Double.parseDouble(ob1val));
						} else {
							return ((Double)Double.parseDouble(ob1val)).compareTo((Double)Double.parseDouble(ob2val));
						}
					} else {
						if("DESC".equalsIgnoreCase(sortOrder)) {
							return ob2val.compareTo(ob1val);
						} else {
							return ob1val.compareTo(ob2val);
						}
					}
				} catch (Exception e) {
					alert.prepareError("Failed on sorting", e).log();
					return 0;
				}
			}
		});
		return inputlist;
	}
	
	private <T> List<T> paginateBy(List<T> inputlist, int pageSize, int pageOffset) {
		
		int fromIndex = pageOffset;
		if(pageSize<1 || inputlist == null || inputlist.size()==0 || inputlist.size()<=fromIndex) {
			alert.prepareError("Invalid filters for pagination.").log();
			return new ArrayList<T>();
		}
		int toIndex = pageOffset + pageSize;
		if(toIndex>inputlist.size()) {
			toIndex = inputlist.size();
		}
		return inputlist.subList(fromIndex, toIndex);
	}
	
	private Map<String, Set<String>> getFilterParamMap(String params, String values){
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		String key;
		String value;
		int i=0,j=0,temp;
		while(i < params.length()-1 && j < values.length()-1) {
			temp = params.indexOf((int)',', i);
			if(temp == -1) {
				temp=params.length();
			}
			key = params.substring(i,temp);
			i=temp+1;
			
			temp = values.indexOf((int)',', j);
			if(temp == -1) {
				temp=values.length();
			}
			value = values.substring(j,temp);
			j=temp+1;
			
			if(!map.containsKey(key)) {
				map.put(key, new HashSet<String>());
			}
			map.get(key.trim()).add(value.trim());
		}
		return map;
	}
	
	public <T, K> List<T> merge(List<T> primarylist, List<K> secondaryList, String condition, String exclude) {
		return merge(primarylist, secondaryList, condition, exclude, null);
	}
	
	@SuppressWarnings("unchecked")
	public <T, K> List<T> merge(List<T> primarylist, List<K> secondaryList, String condition, String exclude, String forceMapping) {
		List<T> mergedList = new ArrayList<T>();
		
		if(primarylist == null || primarylist.size() == 0 )
			return mergedList;
		
		if(secondaryList == null || secondaryList.size() == 0 )
			return primarylist;
		
		try {
			String[] fields = condition.split("\\=");
			
			List<String> excludeList = Arrays.asList(exclude.split("\\,"));
			Map<String, Map<String, Object>> secondaryListMap = new HashMap<>();
			
			if (fields.length == 2) {
				
				String primaryListKey=fields[0];
				String secondaryListKey=fields[1];
				
				List<String> forceMappingList = null;
				if(StringUtils.isNotEmpty(forceMapping)) {
					forceMappingList = Arrays.asList(forceMapping.split("\\,"));
				}
				
				for (K secondaryObj : secondaryList) {
					Map<String, Object> secondaryObject = JSONUtils.parseAsMap(new JSONObject(secondaryObj).toString(), String.class, Object.class);
					String key = secondaryObject.get(secondaryListKey) == null ? "" : secondaryObject.get(secondaryListKey).toString();
					secondaryListMap.put(key, secondaryObject);
				}
				
				for (T primaryObj : primarylist) {
					Map<String, Object> primaryObject = JSONUtils.parseAsMap(new JSONObject(primaryObj).toString(), String.class, Object.class);
					String key = primaryObject.get(primaryListKey) == null ? "" : primaryObject.get(primaryListKey).toString();
					
					Map<String, Object> secondaryObject = null;
					
					if (secondaryListMap.containsKey(key)) {
						
						secondaryObject = new HashMap<String, Object>();
						secondaryObject.putAll(secondaryListMap.get(key));
						
						for (String field : secondaryObject.keySet()) {
							
							if (excludeList.contains(field) && secondaryObject.get(field) != null ) {
								String val = String.valueOf(secondaryObject.get(field));

								if (!val.isEmpty() && !val.equals("0")) {
									primaryObject.put(field, secondaryObject.get(field));
								}
							}
						}
						secondaryObject.putAll(primaryObject);
					}
					if(secondaryObject == null) {
						secondaryObject = primaryObject;
					}
					Object finalObject = JSONUtils.parse(new JSONObject(secondaryObject).toString(), primaryObj.getClass());
					if(!CollectionUtils.isEmpty(forceMappingList)) {
						Map<String, Object> obj1Map = JSONUtils.parseAsMap(new JSONObject(finalObject).toString(), String.class, Object.class);
						for(String mapping : forceMappingList) {
							String[] mappingFields = mapping.split("\\=");
							if(secondaryObject.get(mappingFields[1]) != null) {
								obj1Map.put(mappingFields[0],secondaryObject.get(mappingFields[1]));
							}
						}
						finalObject = (T) JSONUtils.parse(new JSONObject(obj1Map).toString(), primaryObj.getClass());
					}
					mergedList.add((T)finalObject);						
				}
			} else {
				alert.prepareError("Invalid condition").log();
				return mergedList;
			}
		} catch (Exception e) {
			alert.prepareError("Failed in merging the lists", e).log();
			return mergedList;
		}
		return mergedList;
	}
	
	
	public boolean isValidFilter() {
		
		String pattern= "^[\\w\\s.,]*$";
		String numPattern = "^[0-9]*$";
		
		if(StringUtils.isNotEmpty(_searchString) && !Pattern.matches(pattern, _searchString)) {
			return false;
		}
		if(StringUtils.isNotEmpty(_sortOrder) && !Pattern.matches(pattern, _sortOrder)) {
			return false;
		}
		if(StringUtils.isNotEmpty(_sortByParam) && !Pattern.matches(pattern, _sortByParam)) {
			return false;
		}
		if(StringUtils.isNotEmpty(_pageSize) && !Pattern.matches(numPattern, _pageSize)) {
			return false;
		}
		if(StringUtils.isNotEmpty(_pageOffset) && !Pattern.matches(numPattern, _pageOffset)) {
			return false;
		}
		if(StringUtils.isNotEmpty(_filterByParam) && !Pattern.matches(pattern, _filterByParam)) {
			return false;
		}
		if(StringUtils.isNotEmpty(_filterByValue) && !Pattern.matches(pattern, _filterByValue)) {
			return false;
		}
		if(StringUtils.isNotEmpty(_filterByStatus) && !Pattern.matches(pattern, _filterByStatus)) {
			return false;
		}
		if(StringUtils.isNotEmpty(_filterByTransactionType) && !Pattern.matches(pattern, _filterByTransactionType)) {
			return false;
		}
		if(StringUtils.isNotEmpty(_timeParam) && !Pattern.matches(pattern, _timeParam)) {
			return false;
		}
		if(StringUtils.isNotEmpty(_timeValue) && !Pattern.matches(pattern, _timeValue)) {
			return false;
		}
		if(StringUtils.isNotEmpty(_featureactionlist) && !Pattern.matches(pattern, _featureactionlist)) {
			return false;
		}
		
		return true;
	}
}