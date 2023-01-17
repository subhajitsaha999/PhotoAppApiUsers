package com.appsdeveloperblog.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.photoapp.api.users.data.UserEntity;
import com.appsdeveloperblog.photoapp.api.users.data.UserRepository;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;

@Service
public class UserServiceImpl implements UsersService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder; 
	
	@Override
	public UserDto createUser(UserDto userdetails) {
		userdetails.setUserId(UUID.randomUUID().toString());
		userdetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userdetails.getPassword()));
		ModelMapper mapper =new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity= mapper.map(userdetails, UserEntity.class);
		userRepository.save(userEntity);
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity= userRepository.findByEmail(username);
		System.out.println("Userentity" + userEntity.getEmail() + "  "+ userEntity.getEncryptedPassword()) ;
		if(userEntity == null) throw new UsernameNotFoundException(username);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true,true,true,true,new ArrayList<>());
		}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity= userRepository.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		return new ModelMapper().map(userEntity,UserDto.class);
	}

}
