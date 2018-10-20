package com.mhlab.br.config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA에서 Boolean을 저장할 때 숫자 대신 Y / N 으로 컨버팅을 해주는 컨버터
 *
 * Created by MHLab on 19/10/2018..
 */

@Converter
public class BooleanToYesNoConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "Y".equals(dbData);
    }
}
