//package com.agussuhardi.restapisimulator.entity;
//
//import com.agussuhardi.restapisimulator.config.ConvertUtil;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import java.sql.Types;
//import java.util.HashMap;
//import java.util.Map;
//
//@Converter
//public class JsonConverter implements AttributeConverter<Map<String, Object>, String> {
//
//  @Override
//  public String convertToDatabaseColumn(Map<String, Object> map) {
//    if (map == null) map = new HashMap<>();
//    return ConvertUtil.mapToJson(map);
//  }
//
//  @Override
//  public Map<String, Object> convertToEntityAttribute(String json) {
//
//    for (int i : new int[] {Types.JAVA_OBJECT}) {
//      //
//    }
//
//    if (json == null) json = "{}";
//    return ConvertUtil.jsonToMap(json);
//  }
//}
