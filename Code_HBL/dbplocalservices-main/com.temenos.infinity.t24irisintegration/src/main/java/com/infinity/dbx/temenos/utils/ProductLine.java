package com.infinity.dbx.temenos.utils;

import com.infinity.dbx.temenos.accounts.AccountsConstants;

public enum ProductLine {

	DEPOSITS {
		@Override
		public String getNewActivity() {
			return AccountsConstants.DEPOSITS_NEW_ACTIVITY;
		}
	},
	ACCOUNTS {
		@Override
		public String getNewActivity() {
			return AccountsConstants.ACCOUNTS_NEW_ACTIVITY;
		}
	},
	LENDING {
		@Override
		public String getNewActivity() {
			return AccountsConstants.LENDING_NEW_ACTIVITY;
		}
	};

	public abstract String getNewActivity();
	
}
