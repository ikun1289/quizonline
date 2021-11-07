package com.tlcn.quizonline.services;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tlcn.quizonline.RandomString;
import com.tlcn.quizonline.models.VerifyToken;
import com.tlcn.quizonline.repositories.TokenRepository;


@Service
public class VerifyTokenService {

	@Autowired
	TokenRepository TokenRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public void saveNewVerifyToken(VerifyToken token)
	{
		VerifyToken checkToken = TokenRepository.findByUserId(token.getUserId());
		if(checkToken!=null)
		{
			token = checkToken;
			String randomCode = new RandomString().nextString();
			token.setToken(randomCode);
			token.setExpireAt(LocalDateTime.now().plusSeconds(28800));
		}
		TokenRepository.save(token);
	}
	public void removeToken(String token)
	{
		TokenRepository.deleteByToken(token);
	}
	
	public VerifyToken getVTokenByToken(String token) {
		return TokenRepository.findByToken(token);
	}
	
	public VerifyToken createNewToken(String userId) {
		VerifyToken token = new VerifyToken();
		String randomCode = new RandomString().nextString();
		token.setToken(randomCode);
		token.setUserId(userId);
		
		return token;
		
	}
}
