package com.programming4phone.stellar.hardware.wallet.dao;

import org.springframework.data.repository.CrudRepository;

import com.programming4phone.stellar.hardware.wallet.entities.WalletKeys;

public interface KeysRepository extends CrudRepository<WalletKeys, Long>{
	WalletKeys findByAccountNumber(String accountNumber);
}
