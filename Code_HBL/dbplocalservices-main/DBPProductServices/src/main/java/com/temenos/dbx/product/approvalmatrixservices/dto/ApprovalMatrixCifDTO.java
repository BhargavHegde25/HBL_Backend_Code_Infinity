package com.temenos.dbx.product.approvalmatrixservices.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.dbp.core.api.DBPDTO;
import com.temenos.dbx.product.constants.Constants;

public class ApprovalMatrixCifDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cifId;
	private String cifName;
	private CifCommonApprovalMatrixDTO common;
	private List<ApprovalMatrixAccountDTO> accounts = new ArrayList<>();

	public ApprovalMatrixCifDTO() {
	}

	public ApprovalMatrixCifDTO(String cifId, String cifName, List<ApprovalMatrixAccountDTO> accounts) {
		this.cifId = cifId;
		this.cifName = cifName;
		this.accounts = accounts;
	}

	public ApprovalMatrixCifDTO(String cifId, String cifName, List<ApprovalMatrixAccountDTO> accounts,
			CifCommonApprovalMatrixDTO common) {
		this.cifId = cifId;
		this.cifName = cifName;
		this.accounts = accounts;
		this.common = common;
	}

	public String getCifId() {
		return cifId;
	}

	public void setCifId(String cifId) {
		this.cifId = cifId;
	}

	public String getCifName() {
		return cifName;
	}

	public void setCifName(String cifName) {
		this.cifName = cifName;
	}

	public List<ApprovalMatrixAccountDTO> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<ApprovalMatrixAccountDTO> accounts) {
		this.accounts = accounts;
	}

	public void add(ApprovalMatrixAccountDTO account) {
		this.accounts.add(account);
	}

	public CifCommonApprovalMatrixDTO getCommon() {
		return common;
	}

	public void setCommon(CifCommonApprovalMatrixDTO common) {
		this.common = common;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accounts == null) ? 0 : accounts.hashCode());
		result = prime * result + ((cifId == null) ? 0 : cifId.hashCode());
		result = prime * result + ((cifName == null) ? 0 : cifName.hashCode());
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
		ApprovalMatrixCifDTO other = (ApprovalMatrixCifDTO) obj;
		if (accounts == null) {
			if (other.accounts != null)
				return false;
		} else if (!accounts.equals(other.accounts))
			return false;
		if (cifId == null) {
			if (other.cifId != null)
				return false;
		} else if (!cifId.equals(other.cifId))
			return false;
		if (cifName == null) {
			if (other.cifName != null)
				return false;
		} else if (!cifName.equals(other.cifName))
			return false;
		return true;
	}

	public void addCommonApprovalMatrix(SortedSet<String> featureActions, HashMap<String, String> actionTypeMap) {
		Map<String, Map<String, ApprovalMatrixActionDTO>> actionsList = ApprovalMatrixActionDTO
				.getActionsList(featureActions,actionTypeMap);
		_fillCommonLimits(featureActions, actionsList, actionTypeMap);

		Collection<ApprovalMatrixActionDTO> values1 = actionsList.get(Constants.DAILY_LIMIT).values();
		Collection<ApprovalMatrixActionDTO> values2 = actionsList.get(Constants.MAX_TRANSACTION_LIMIT).values();
		Collection<ApprovalMatrixActionDTO> values3 = actionsList.get(Constants.WEEKLY_LIMIT).values();
		Collection<ApprovalMatrixActionDTO> values4 = actionsList.get(Constants.NON_MONETARY_LIMIT).values();
		ApprovalMatrixLimitTypeDTO dailyTransactionLimit = new ApprovalMatrixLimitTypeDTO(Constants.DAILY_LIMIT,
				new ArrayList<>(values1));
		ApprovalMatrixLimitTypeDTO maxTransactionLimit = new ApprovalMatrixLimitTypeDTO(Constants.MAX_TRANSACTION_LIMIT,
				new ArrayList<>(values2));
		ApprovalMatrixLimitTypeDTO weeklyTransactionLimit = new ApprovalMatrixLimitTypeDTO(Constants.WEEKLY_LIMIT,
				new ArrayList<>(values3));
		ApprovalMatrixLimitTypeDTO nonFinancial = new ApprovalMatrixLimitTypeDTO(Constants.NON_MONETARY_LIMIT, new ArrayList<>(values4));

		CifCommonApprovalMatrixDTO cifCommonApprovalMatrixDTO = new CifCommonApprovalMatrixDTO();
		cifCommonApprovalMatrixDTO.add(dailyTransactionLimit);
		cifCommonApprovalMatrixDTO.add(maxTransactionLimit);
		cifCommonApprovalMatrixDTO.add(weeklyTransactionLimit);
		cifCommonApprovalMatrixDTO.add(nonFinancial);

		this.setCommon(cifCommonApprovalMatrixDTO);

	}

	private void _fillCommonLimits(SortedSet<String> featureActions,
			Map<String, Map<String, ApprovalMatrixActionDTO>> actionsList, HashMap<String, String> actionTypeMap) {
		HashMap<String, List<ApprovalMatrixLimitDTO>> map = new HashMap<>();
		ApprovalMatrixActionDTO currentAction = null;

		for (ApprovalMatrixAccountDTO account : accounts) {
			List<ApprovalMatrixLimitTypeDTO> limitTypes = account.getLimitTypes();
			for (ApprovalMatrixLimitTypeDTO limitType : limitTypes) {
				List<ApprovalMatrixActionDTO> actions = limitType.getActions();
				for (ApprovalMatrixActionDTO action : actions) {
					String key = limitType.getLimitTypeId() + "_" + action.getActionId();
					currentAction = actionsList.get(limitType.getLimitTypeId()).get(action.getActionId());

					if (!map.containsKey(key)) {
						map.put(key, action.getLimits());

						currentAction.setFeatureId(action.getFeatureId());
						currentAction.setFeatureName(action.getFeatureName());
						currentAction.setActionName(action.getActionName());
						currentAction.setActionType(action.getActionType());
						currentAction.setActionDescription(action.getActionDescription());
						currentAction.setFeatureStatus(action.getFeatureStatus());
						currentAction.setMaxAmount(action.getMaxAmount());
						currentAction.setIsAccountLevel(action.getIsAccountLevel());
						currentAction.setLimits(action.getLimits());
					} else {
						List<ApprovalMatrixLimitDTO> limits1 = actionsList.get(limitType.getLimitTypeId())
								.get(action.getActionId()).getLimits();
						List<ApprovalMatrixLimitDTO> limits2 = action.getLimits();
						boolean same = ApprovalMatrixLimitDTO.sameLimits(limits1, limits2);
						if (!same) {
							currentAction.setLimits(action.getLimits());
						}
					}
				}
			}
		}
		Iterator<String> featureActionItr = featureActions.iterator();
		
		while (featureActionItr.hasNext()) {
			String featureAction = featureActionItr.next();
			
			
			for (ApprovalMatrixAccountDTO account : accounts) {
				List<ApprovalMatrixLimitTypeDTO> limitTypes = account.getLimitTypes();
				ApprovalMatrixLimitTypeDTO limitType = limitTypes.get(0);
				if (actionTypeMap.get(featureAction).equalsIgnoreCase("NON_MONETARY")) {
					for (int i = 0; i < limitTypes.size(); i++) {
						if (limitTypes.get(i).getLimitTypeId().equalsIgnoreCase(Constants.NON_MONETARY_LIMIT)) {
							limitType = limitTypes.get(i);
						}

					}
				}
				List<ApprovalMatrixActionDTO> actions = limitType.getActions();
				boolean found = false;
				for (ApprovalMatrixActionDTO action : actions) {
					if (action.getActionId().equals(featureAction)) {
						found = true;
					}
				}
				if (!found) {
					for (String limitTyp : actionsList.keySet()) {
						if(actionsList.get(limitTyp).get(featureAction)!= null && limitTyp.equalsIgnoreCase(Constants.NON_MONETARY_LIMIT)) {
							//actionsList.get(limitTyp).get(featureAction).clearLimits();
						}
					}
					break;
				}
			}
		
		}

	}

}
