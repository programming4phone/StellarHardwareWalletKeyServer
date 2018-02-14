package com.programming4phone.stellar.hardware.wallet.dao;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import com.programming4phone.stellar.hardware.wallet.entities.WalletKeys;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class KeysDaoTest {

	@Autowired
	private KeysRepository keysRepository;
	
	@Test
	public void test() {
		keysRepository.deleteAll();
		assertThat(keysRepository.count(), equalTo(0L));
		
		WalletKeys keys = new WalletKeys().setSecretSeed("12345").setAccountNumber("45678");
		keysRepository.save(keys);
		assertThat(keysRepository.count(), equalTo(1L));
		
		WalletKeys existingKeys = keysRepository.findByAccountNumber("45678");
		assertNotNull(existingKeys);
		
		WalletKeys nfKeys = keysRepository.findByAccountNumber("778899");
		assertNull(nfKeys);
		
		keysRepository.deleteAll();
		assertThat(keysRepository.count(), equalTo(0L));
	}

}
