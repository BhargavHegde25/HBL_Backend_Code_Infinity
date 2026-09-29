package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LimitDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -8519707723695634950L;

    private String pre_approved_transaction_limit;
    private String auto_denied_transaction_limit;
    private String pre_approved_daily_limit;
    private String auto_denied_daily_limit;
    private String pre_approved_weekly_limit;
    private String auto_denied_weekly_limit;
    private String max_transaction_limit;
    private String weekly_limit;
    private String daily_limit;

    public String getMax_transaction_limit() {
        return max_transaction_limit;
    }

    public void setMax_transaction_limit(String max_transaction_limit) {
        this.max_transaction_limit = max_transaction_limit;
    }

    public String getWeekly_limit() {
        return weekly_limit;
    }

    public void setWeekly_limit(String weekly_limit) {
        this.weekly_limit = weekly_limit;
    }

    public String getDaily_limit() {
        return daily_limit;
    }

    public void setDaily_limit(String daily_limit) {
        this.daily_limit = daily_limit;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public LimitDTO() {
        super();
    }

    public LimitDTO(String pre_approved_transaction_limit, String auto_denied_transaction_limit,
            String pre_approved_daily_limit, String auto_denied_daily_limit, String pre_approved_weekly_limit,
            String auto_denied_weekly_limit, String maxTransactionLimit, String dailyLimit, String weeklyLimit) {
        super();
        this.pre_approved_transaction_limit = pre_approved_transaction_limit;
        this.auto_denied_transaction_limit = auto_denied_transaction_limit;
        this.pre_approved_daily_limit = pre_approved_daily_limit;
        this.auto_denied_daily_limit = auto_denied_daily_limit;
        this.pre_approved_weekly_limit = pre_approved_weekly_limit;
        this.auto_denied_weekly_limit = auto_denied_weekly_limit;
        this.max_transaction_limit = maxTransactionLimit;
        this.daily_limit = dailyLimit;
        this.weekly_limit = weeklyLimit;
    }

    @Override
    public String toString() {
        return "{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"" + this.pre_approved_transaction_limit
                + "\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"" + this.auto_denied_transaction_limit
                + "\", \"PRE_APPROVED_DAILY_LIMIT\":\""
                + this.pre_approved_daily_limit + "\", \"AUTO_DENIED_DAILY_LIMIT\":\"" + this.auto_denied_daily_limit
                + "\", \"PRE_APPROVED_WEEKLY_LIMIT\":\"" + this.pre_approved_weekly_limit
                + "\", \"AUTO_DENIED_WEEKLY_LIMIT\":\""
                + this.auto_denied_weekly_limit + "\"}";
    }

    public String getContractsLimitsString() {
        JsonArray contractactionlimits = new JsonArray();
        JsonObject limit = new JsonObject();
        limit.addProperty("id", "MAX_TRANSACTION_LIMIT");
        limit.addProperty("value", this.max_transaction_limit);
        contractactionlimits.add(limit);
        limit = new JsonObject();
        limit.addProperty("id", "DAILY_LIMIT");
        limit.addProperty("value", this.daily_limit);
        contractactionlimits.add(limit);
        limit = new JsonObject();
        limit.addProperty("id", "WEEKLY_LIMIT");
        limit.addProperty("value", this.weekly_limit);
        contractactionlimits.add(limit);
        return contractactionlimits.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((auto_denied_daily_limit == null) ? 0 : auto_denied_daily_limit.hashCode());
        result = prime * result
                + ((auto_denied_transaction_limit == null) ? 0 : auto_denied_transaction_limit.hashCode());
        result = prime * result + ((auto_denied_weekly_limit == null) ? 0 : auto_denied_weekly_limit.hashCode());
        result = prime * result + ((pre_approved_daily_limit == null) ? 0 : pre_approved_daily_limit.hashCode());
        result = prime * result
                + ((pre_approved_transaction_limit == null) ? 0 : pre_approved_transaction_limit.hashCode());
        result = prime * result + ((pre_approved_weekly_limit == null) ? 0 : pre_approved_weekly_limit.hashCode());
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
        LimitDTO other = (LimitDTO) obj;
        if (auto_denied_daily_limit == null) {
            if (other.auto_denied_daily_limit != null)
                return false;
        } else if (!auto_denied_daily_limit.equals(other.auto_denied_daily_limit))
            return false;
        if (auto_denied_transaction_limit == null) {
            if (other.auto_denied_transaction_limit != null)
                return false;
        } else if (!auto_denied_transaction_limit.equals(other.auto_denied_transaction_limit))
            return false;
        if (auto_denied_weekly_limit == null) {
            if (other.auto_denied_weekly_limit != null)
                return false;
        } else if (!auto_denied_weekly_limit.equals(other.auto_denied_weekly_limit))
            return false;
        if (pre_approved_daily_limit == null) {
            if (other.pre_approved_daily_limit != null)
                return false;
        } else if (!pre_approved_daily_limit.equals(other.pre_approved_daily_limit))
            return false;
        if (pre_approved_transaction_limit == null) {
            if (other.pre_approved_transaction_limit != null)
                return false;
        } else if (!pre_approved_transaction_limit.equals(other.pre_approved_transaction_limit))
            return false;
        if (pre_approved_weekly_limit == null) {
            if (other.pre_approved_weekly_limit != null)
                return false;
        } else if (!pre_approved_weekly_limit.equals(other.pre_approved_weekly_limit))
            return false;
        return true;
    }

    public String getPre_approved_transaction_limit() {
        return pre_approved_transaction_limit;
    }

    public void setPre_approved_transaction_limit(String pre_approved_transaction_limit) {
        this.pre_approved_transaction_limit = pre_approved_transaction_limit;
    }

    public String getAuto_denied_transaction_limit() {
        return auto_denied_transaction_limit;
    }

    public void setAuto_denied_transaction_limit(String auto_denied_transaction_limit) {
        this.auto_denied_transaction_limit = auto_denied_transaction_limit;
    }

    public String getPre_approved_daily_limit() {
        return pre_approved_daily_limit;
    }

    public void setPre_approved_daily_limit(String pre_approved_daily_limit) {
        this.pre_approved_daily_limit = pre_approved_daily_limit;
    }

    public String getAuto_denied_daily_limit() {
        return auto_denied_daily_limit;
    }

    public void setAuto_denied_daily_limit(String auto_denied_daily_limit) {
        this.auto_denied_daily_limit = auto_denied_daily_limit;
    }

    public String getPre_approved_weekly_limit() {
        return pre_approved_weekly_limit;
    }

    public void setPre_approved_weekly_limit(String pre_approved_weekly_limit) {
        this.pre_approved_weekly_limit = pre_approved_weekly_limit;
    }

    public String getAuto_denied_weekly_limit() {
        return auto_denied_weekly_limit;
    }

    public void setAuto_denied_weekly_limit(String auto_denied_weekly_limit) {
        this.auto_denied_weekly_limit = auto_denied_weekly_limit;
    }
}
