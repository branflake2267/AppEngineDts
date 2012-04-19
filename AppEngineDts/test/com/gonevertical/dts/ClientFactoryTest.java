package com.gonevertical.dts;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gonevertical.dts.utils.AppEngineUtils;

public class ClientFactoryTest {
  
  private String configfilepath = "/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/options.properties";

  private AppEngineUtils utils;

  @Before
  public void setUp() throws Exception {
    PropertyConfigurator.configure("/Users/branflake2267/Documents/workspace/AppEngineDts/parameters/log4j.properties");

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testUse() {
    try {
      ClientFactory cf = new ClientFactory(new File(configfilepath));
    } catch (ConfigurationException e) {
      e.printStackTrace();
      fail();
    } catch (IOException e) {
      e.printStackTrace();
      fail();
    }
  }
  
  @Test
  public void testUse2() {
    ClientFactory cf = null;
    try {
      cf = new ClientFactory(new File(configfilepath));
    } catch (ConfigurationException e) {
      e.printStackTrace();
      fail();
    } catch (IOException e) {
      e.printStackTrace();
      fail();
    }
    
    
  }




}
