package com.programming4phone.stellar.hardware.wallet.rest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.programming4phone.stellar.hardware.wallet.aes.AES;
import com.programming4phone.stellar.hardware.wallet.aes.AES.InvalidAESStreamException;
import com.programming4phone.stellar.hardware.wallet.aes.AES.InvalidKeyLengthException;
import com.programming4phone.stellar.hardware.wallet.aes.AES.InvalidPasswordException;
import com.programming4phone.stellar.hardware.wallet.aes.AES.StrongEncryptionNotAvailableException;
import com.programming4phone.stellar.hardware.wallet.dao.KeysRepository;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static String SECRET_SEED_1 = "SBKYUD4DRNFUVCYEBWCTTZOMV5P3OT7EXZUVYMHOSLQZQGLWLRT6YPA6"; 
	private final static String TEST_ACCOUNT_ID_1 = "GD5FTSQHKKXKD6MVO5KR7ZDIMRYZBS2YC2RJO7UBKTU2LPXESS2RPVTZ";
	
	private final static String PASS_PHRASE = "bad weather chicago good weather phoenix";
	
	private static final String BASE_URL = "/wallet/key";
	private static final String DELETE_URL = BASE_URL + "/delete/{accountNumber}";
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private KeysRepository keysRepository;
	
	private String encrypt(String passphrase, String secretSeed) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(secretSeed.getBytes());
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			AES.encrypt(128, passphrase.toCharArray(), inputStream, output);
		} catch (InvalidKeyLengthException | StrongEncryptionNotAvailableException | IOException e) {
			logger.error("Unable to encrypt secret seed", e);
			fail("Unable to encrypt secret seed");
		}
		byte[] encryptedBytes = output.toByteArray();
		return Base64.encodeBase64String(encryptedBytes);
	}
	
	private String decrypt(String passphrase, String cipherText) {
		byte[] decodedBytes = Base64.decodeBase64(cipherText.getBytes());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			AES.decrypt(passphrase.toCharArray(), inputStream, output);
		} catch (InvalidPasswordException | InvalidAESStreamException | IOException
				| StrongEncryptionNotAvailableException e) {
			fail("Unable to decrypt secret seed");
		}
		return new String(output.toByteArray());
	}
	
	@Test
	public void keysTest() {
		keysRepository.deleteAll();
		
		String hashedAccountNumber = DigestUtils.sha256Hex(TEST_ACCOUNT_ID_1);
		logger.info("hashedAccountNumber: " + hashedAccountNumber);
		String encryptedSecretSeed = encrypt(PASS_PHRASE, SECRET_SEED_1);
		logger.info("encryptedSecretSeed: " + encryptedSecretSeed);
		ResponseEntity<Void> voidResponseEntity;
		ResponseEntity<RequestResponseKeys> responseEntity;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<?> requestGet = new HttpEntity<>(headers);
		HttpEntity<RequestResponseKeys> requestUpdate;
		
		RequestResponseKeys createAccountKeys = new RequestResponseKeys().setAccountNumber(hashedAccountNumber).setSecretSeed(encryptedSecretSeed);
		
		requestUpdate = new HttpEntity<>(createAccountKeys, headers);
		logger.info("createAccountKeys: " + createAccountKeys.toString());
		voidResponseEntity = restTemplate.exchange(BASE_URL, HttpMethod.PUT, requestUpdate, Void.class);
		assertThat(voidResponseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
		
		responseEntity = restTemplate.exchange(BASE_URL + "/" + hashedAccountNumber, HttpMethod.GET, requestGet, RequestResponseKeys.class);
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
		RequestResponseKeys responseAccountKeys = responseEntity.getBody();
		logger.info("responseAccountKeys: " + responseAccountKeys.toString());
		assertThat(responseAccountKeys.getAccountNumber(),equalTo(hashedAccountNumber));
		String decryptedSecretSeed = decrypt(PASS_PHRASE, responseAccountKeys.getSecretSeed());
		logger.info("decryptedSecretSeed: " + decryptedSecretSeed);
		assertThat(decryptedSecretSeed ,equalTo(SECRET_SEED_1));		
		
 		voidResponseEntity = restTemplate.exchange(DELETE_URL, HttpMethod.DELETE, requestGet, Void.class, hashedAccountNumber);
 		assertThat(voidResponseEntity.getStatusCode(), equalTo(HttpStatus.OK));
		
 		responseEntity = restTemplate.exchange(BASE_URL + "/" + hashedAccountNumber, HttpMethod.GET, requestGet, RequestResponseKeys.class);
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));

		createAccountKeys = new RequestResponseKeys().setAccountNumber(hashedAccountNumber).setSecretSeed(null);
		requestUpdate = new HttpEntity<>(createAccountKeys, headers);
		logger.info("createAccountKeys: " + createAccountKeys.toString());
		voidResponseEntity = restTemplate.exchange(BASE_URL, HttpMethod.PUT, requestUpdate, Void.class);
		assertThat(voidResponseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
		
		createAccountKeys = new RequestResponseKeys().setAccountNumber(null).setSecretSeed(encryptedSecretSeed);
		requestUpdate = new HttpEntity<>(createAccountKeys, headers);
		logger.info("createAccountKeys: " + createAccountKeys.toString());
		voidResponseEntity = restTemplate.exchange(BASE_URL, HttpMethod.PUT, requestUpdate, Void.class);
		assertThat(voidResponseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
	}
}
