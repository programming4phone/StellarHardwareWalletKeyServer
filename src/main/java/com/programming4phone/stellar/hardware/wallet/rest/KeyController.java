package com.programming4phone.stellar.hardware.wallet.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.programming4phone.stellar.hardware.wallet.dao.KeysRepository;
import com.programming4phone.stellar.hardware.wallet.entities.WalletKeys;
import com.programming4phone.stellar.hardware.wallet.error.InvalidKeyException;
import com.programming4phone.stellar.hardware.wallet.error.KeyNotFoundException;


@CrossOrigin
@RestController
@RequestMapping("/wallet/key")
public class KeyController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private KeysRepository keysRepository;
	
	/**
	 * Retrieve the account keys associated with a specific account number from the HSQLDB database.  
	 * If the account key is not found in the database, this web service will return HTTP status code  
	 * 404 (NOT_FOUND).
	 * @param accountNumber Stellar account number (hashed public key)
	 * @return <b>RequestResponseKeys</b> object containing hashed public key and encrypted private key
	 */
	@RequestMapping(value="/{accountNumber}", method=RequestMethod.GET, produces="application/json")
	public RequestResponseKeys getAccountKeys(@PathVariable String accountNumber) {
		logger.debug("accountNumber: " + accountNumber);
		WalletKeys walletKeys =  keysRepository.findByAccountNumber(accountNumber);
		Optional.ofNullable(walletKeys).orElseThrow(KeyNotFoundException::new);
		return new RequestResponseKeys()
				.setAccountNumber(walletKeys.getAccountNumber())
				.setSecretSeed(walletKeys.getSecretSeed());
	}

	/**
	 * Store the account keys associated with a specific account number into the HSQLDB database.  
	 * If either of the account keys is invalid, this web service will return HTTP status code  
	 * 400 (BAD_REQUEST).
	 * @param walletKeys Object containing hashed public key and encrypted private key
	 */
	@RequestMapping(method=RequestMethod.PUT, consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveAccount(@RequestBody RequestResponseKeys requestResponseKeys) {
		logger.debug("requestResponseKeys: " + requestResponseKeys.toString());
		Optional.ofNullable(requestResponseKeys.getAccountNumber()).orElseThrow(InvalidKeyException::new);
		Optional.ofNullable(requestResponseKeys.getSecretSeed()).orElseThrow(InvalidKeyException::new);
		WalletKeys walletKeys = new WalletKeys()
				.setAccountNumber(requestResponseKeys.getAccountNumber())
				.setSecretSeed(requestResponseKeys.getSecretSeed());
		keysRepository.save(walletKeys);
	}
	
	/**
	 * Remove the account keys associated with a specific account number from the HSQLDB database.  
	 * @param accountNumber Stellar account number (hashed public key)
	 */
	@RequestMapping(value="/delete/{accountNumber}", method=RequestMethod.DELETE)
	public void removeAccount(@PathVariable String accountNumber) {
		logger.debug("accountNumber: " + accountNumber);
		WalletKeys walletKeys = keysRepository.findByAccountNumber(accountNumber);
		Optional.ofNullable(walletKeys).ifPresent(w -> keysRepository.delete(w.getId()));
	}
	
	/**
	 * Exception handler that converts KeyNotFoundException to HTTP status 404 (NOT_FOUND)
	 */
	@ExceptionHandler(KeyNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void noAccountFound() {
	}
	
	/**
	 * Exception handler that converts InvalidKeyException to HTTP status 400 (BAD_REQUEST)
	 */
	@ExceptionHandler(InvalidKeyException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void invalidKey() {
	}
	
}
