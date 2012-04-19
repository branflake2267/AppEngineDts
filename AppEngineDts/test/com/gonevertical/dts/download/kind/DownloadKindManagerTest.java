package com.gonevertical.dts.download.kind;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gonevertical.dts.ClientFactory;
import com.gonevertical.dts.data.EntityStat;
import com.gonevertical.dts.download.kind.DownloadKind;
import com.gonevertical.dts.utils.AppEngineUtils;

public class DownloadKindManagerTest {

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
  public void testDownloadCedars() {
    ClientFactory cf = null;
    try {
      cf = new ClientFactory(new File(configfilepath));
    } catch (ConfigurationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (cf == null) {
      fail("no client factory");
    }
    
    String kind = "CedarsJdo";
    

    DownloadKindManager dk = new DownloadKindManager(cf);
    dk.setKind(kind);
    
    dk.run();
  }
  
  

}
