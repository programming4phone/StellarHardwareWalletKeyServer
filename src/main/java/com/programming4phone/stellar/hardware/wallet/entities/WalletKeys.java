package com.programming4phone.stellar.hardware.wallet.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@Entity
@Table(name="KEYS")
public class WalletKeys implements Serializable{
	private static final long serialVersionUID = 9180069620267198950L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name="ID")
	Long id;
	   
	@Column(name="ACCOUNT_NUMBER")
    private String accountNumber;  
	
	@Column(name = "SECRET_SEED")
	private String secretSeed;

	public Long getId() {
		return id;
	}

	public WalletKeys setId(Long id) {
		this.id = id;
		return this;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public WalletKeys setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
		return this;
	}

	public String getSecretSeed() {
		return secretSeed;
	}

	public WalletKeys setSecretSeed(String secretSeed) {
		this.secretSeed = secretSeed;
		return this;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).
	        append(accountNumber).
	        append(secretSeed).
	        toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj.getClass() != getClass()) {
			return false;
		}
		WalletKeys rhs = (WalletKeys) obj;
		return new EqualsBuilder()
		    .appendSuper(super.equals(obj))
		    .append(accountNumber, rhs.accountNumber)
		    .append(secretSeed, rhs.secretSeed)
		    .isEquals();
	}

	@Override
	public String toString() {
		return "WalletKeys [id=" + id + ", accountNumber=" + accountNumber + ", secretSeed=" + secretSeed + "]";
	}
	
}
