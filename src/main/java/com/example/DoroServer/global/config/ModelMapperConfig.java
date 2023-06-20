package com.example.DoroServer.global.config;

import org.modelmapper.Condition;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setFieldMatchingEnabled(true)
                .setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }
}
