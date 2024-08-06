package com.ttdat.springbootjwt.mapper;

import com.ttdat.springbootjwt.dto.RoleDTO;
import com.ttdat.springbootjwt.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleDTO roleDTO);
    RoleDTO toRoleDTO(Role role);
}
