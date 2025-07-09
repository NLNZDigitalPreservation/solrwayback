package dk.kb.netarchivesuite.solrwayback.parsers.warc;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;

import org.junit.Test;

import dk.kb.netarchivesuite.solrwayback.UnitTestUtils;
import dk.kb.netarchivesuite.solrwayback.facade.Facade;
import dk.kb.netarchivesuite.solrwayback.image.ImageUtils;
import dk.kb.netarchivesuite.solrwayback.normalise.Normalisation;
import dk.kb.netarchivesuite.solrwayback.service.dto.ArcEntry;


public class ArcParserTest extends UnitTestUtils{
       
    @Test
    public void testArcParser() throws Exception {
        
        File file = getFile("src/test/resources/example_arc/IAH-20080430204825-00000-blackbook.arc");
        
        ArcEntry arcEntry = Facade.getArcEntry(file.getCanonicalPath(), 136767); //Image entry
        assertEquals("image/jpeg", arcEntry.getContentType());
        assertEquals("hewlett.jpg", arcEntry.getFileName());
        assertEquals(7510, arcEntry.getContentLength());

        BufferedImage image = ImageUtils.getImageFromBinary(arcEntry.getBinaryDecoded());
        assertEquals(300,image.getWidth());
        assertEquals(116,image.getHeight());
        assertEquals(200,arcEntry.getStatus_code());        
        System.out.println(arcEntry.getUrl());
        System.out.println(arcEntry.getWaybackDate());;
        System.out.println(arcEntry.getCrawlDate());
        System.out.println(arcEntry.getRedirectUrl());
                     
    }


    @Test
    public void testArcParserRedirect() throws Exception {
      
      File file = getFile("src/test/resources/example_arc/IAH-20080430204825-00000-blackbook.arc");
      
      ArcEntry arcEntry = Facade.getArcEntry(file.getCanonicalPath(), 280750); //redirect

      
      String url=arcEntry.getUrl();
      System.out.println(url);
      String redirect = arcEntry.getRedirectUrl();
      
      String finalUrl = Normalisation.resolveRelative(url, redirect, false);

      System.out.println(finalUrl);
      //
  
                      
    }
   
    

    
    /*
    @Test
    public void testArcParser1() throws Exception {        
        File file = getFile("/netarkiv/0108/filedir/67238-102-20091209053237-00404-sb-prod-har-002.statsbiblioteket.dk.arc");        
        ArcEntry arcEntry = Facade.getArcEntry(file.getCanonicalPath(),  94839654 );         
        System.out.println(arcEntry.getContentType());       
    }
    */
     
  
}
