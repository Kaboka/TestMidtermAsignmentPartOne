/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.javaanpr.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Kasper
 */
@RunWith(Parameterized.class)
public class RecognitionAll {
    private static final Logger logger = LoggerFactory.getLogger(RecognitionIT.class);
    
   private static List<String[]> readFile(BufferedReader br) throws IOException {
	    try {
                System.out.println("reading data");
                List<String[]> data = new ArrayList<>();
	        String line;
	        while ((line = br.readLine()) != null) {
                   data.add(line.split("="));
                   System.out.println(line);
	        }
	        return data;
	    } finally {
	        br.close();
	    }
	}
    
   @Parameters
   public static Collection<Object[]> data() throws FileNotFoundException, IOException{
       List<String[]> data = readFile(new BufferedReader(new FileReader("src/test/resources/results.properties")));
       Collection<Object[]> result = new ArrayList<>();
       for(String[] s : data){
           result.add(s);
       }
       return result;
   }

   private final String input;
   private final String expected;
   
   public RecognitionAll(String input, String expected){
       this.input = input;
       this.expected = expected;
   }
    
   @Test
    public void intelligenceSingleTest() throws IOException, ParserConfigurationException, SAXException {
        System.out.println("input=" + input + " expected=" + expected);
        CarSnapshot carSnap = new CarSnapshot("snapshots/"+input);
        assertThat(carSnap, notNullValue());
        assertThat(carSnap.getImage(), notNullValue());
        Intelligence intel = new Intelligence();
        assertThat(intel,notNullValue());
        String spz = intel.recognize(carSnap);
        assertThat(spz, notNullValue());
        assertThat(spz, is(expected));
        carSnap.close();
    }
}
