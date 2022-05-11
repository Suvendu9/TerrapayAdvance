package com.pms.beans;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "BankDetail")
public class BankDetail {
	@NotBlank(message = "bank name can not be NULL")
	private String bankName;
	@Id
	private String accountNumber;
	@Enumerated(EnumType.STRING)
	private BankType banktype;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BankType getBanktype() {
		return banktype;
	}

	public void setBanktype(BankType banktype) {
		this.banktype = banktype;
	}
}
