package com.programming4phone.stellar.hardware.wallet.rest;

public class RequestResponseKeys {
    private String accountNumber;  
	private String secretSeed;
	public String getAccountNumber() {
		return accountNumber;
	}
	public RequestResponseKeys setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
		return this;
	}
	public String getSecretSeed() {
		return secretSeed;
	}
	public RequestResponseKeys setSecretSeed(String secretSeed) {
		this.secretSeed = secretSeed;
		return this;
	}
	@Override
	public String toString() {
		return "RequestResponseKeys [accountNumber=" + accountNumber + ", secretSeed=" + secretSeed + "]";
	}
}
