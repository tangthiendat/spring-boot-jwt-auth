package com.ttdat.springbootjwt.mapper;

import com.ttdat.springbootjwt.dto.request.RegisterRequest;
import com.ttdat.springbootjwt.dto.response.RegisterResponse;
import com.ttdat.springbootjwt.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    User toUser(RegisterRequest registerRequest);
    RegisterResponse toRegisterResponse(User user);


}