package com.gonevertical.dts.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class ObjectUtils {
  
  private static final Logger log = LoggerFactory.getLogger(ObjectUtils.class);
  
  private static Gson gson;

  public static String convertObjectToString(Object o) {
    if (gson == null) {
      gson = new Gson();
    }
    String s = gson.toJson(o);
    return s;
  }

  public static <T> T stringToObject(Class<T> clazz, String json) {
    if (gson == null) {
      gson = new Gson();
    }
    T o = gson.fromJson(json, clazz);
    return o;
  }
  


} 



