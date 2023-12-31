package com.cloudacademy;

import org.junit.Ignore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.Transient;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.sound.midi.SysexMessage;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;

import static org.mockito.Mockito.*;

import java.io.IOException;

public class ConverterSvcTest {
    //
    private CloseableHttpClient client;
    private CloseableHttpResponse response;
    private StatusLine statusLine;
    private HttpEntity entity;
    private InputStream stream;

    @BeforeEach
    public void setUP() {

        client = mock(CloseableHttpClient.class);
        response = mock(CloseableHttpResponse.class);
        statusLine = mock(StatusLine.class);
        entity = mock(HttpEntity.class);

        // payload as we make a real call to API (all values hard code and we always will be static)
        stream = new ByteArrayInputStream("{\"time\": {\"updated\": \"Oct 15, 2020 22:55:00 UTC\",\"updatedISO\": \"2020-10-15T22:55:00+00:00\",\"updateduk\": \"Oct 15, 2020 at 23:55 BST\"},\"chartName\": \"Bitcoin\",\"bpi\": {\"USD\": {\"code\": \"USD\",\"symbol\": \"&#36;\",\"rate\": \"11,486.5341\",\"description\": \"United States Dollar\",\"rate_float\": 11486.5341},\"GBP\": {\"code\": \"GBP\",\"symbol\": \"&pound;\",\"rate\": \"8,900.8693\",\"description\": \"British Pound Sterling\",\"rate_float\": 8900.8693},\"EUR\": {\"code\": \"EUR\",\"symbol\": \"&euro;\",\"rate\": \"9,809.3278\",\"description\": \"Euro\",\"rate_float\": 9809.3278}}}".getBytes());
    }


    @Test
    public void getExchangeRate_USD_ReturnsUSDExchangeRate() throws IOException {
        //Arange
        //ConverterSvc converterSvc = new ConverterSvc();
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        //Act // dependency injection on the constructor 
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.getExchangeRate(ConverterSvc.Currency.USD);

        //Assert
        double expected = 11486.5341;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getExchangeRate_GBP_ReturnsGBPExchangeRate() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.getExchangeRate(ConverterSvc.Currency.GBP);
    
        //assert
        double expected = 8900.8693;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getExchangeRate_EUR_ReturnsEURExchangeRate() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.getExchangeRate(ConverterSvc.Currency.EUR);
    
        //assert
        double expected = 9809.3278;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getExchangeRate_ExecuteThrowsIOException_ReturnsCorrectErrorCode() throws IOException {
        //arrange
        when(client.execute(any(HttpGet.class))).thenThrow(IOException.class);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.getExchangeRate(ConverterSvc.Currency.USD);
    
        //assert
        double expected = -1;
        Assertions.assertEquals(expected, actual);
    }

   
    @Test
    public void getExchangeRate_ClosingResponseThrowsIOException_ReturnsCorrectErrorCode() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        doThrow(IOException.class).when(response).close();

        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.getExchangeRate(ConverterSvc.Currency.USD);
    
        //assert
        double expected = -1;
        Assertions.assertEquals(expected, actual);
    }    

    @Test
    public void convertBitcoins_1BitCoinToUSD_ReturnsUSDDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.convertBitcoins(ConverterSvc.Currency.USD, 1);
    
        //assert
        double expected = 11486.5341;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void text() {
        System.out.println(ConverterSvc.Currency.USD);
        System.out.println(ConverterSvc.Currency.GBP);
        System.out.println(ConverterSvc.Currency.EUR);

    }
    
    @Test
    public void convertBitcoins_2BitCoinToUSD_ReturnsUSDDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.convertBitcoins(ConverterSvc.Currency.USD, 2);
    
        //assert
        double expected = 22973.0682;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void convertBitcoins_0BitCoinToUSD_ReturnsZeroUSDDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.convertBitcoins(ConverterSvc.Currency.USD, 0);
    
        //assert
        double expected = 0;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void convertBitcoins_1BitCoinToGBP_ReturnsGBPDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.convertBitcoins(ConverterSvc.Currency.GBP, 1);
    
        //assert
        double expected = 8900.8693;
        Assertions.assertEquals(expected, actual);
    }
    
    @Test
    public void convertBitcoins_2BitCoinToGBP_ReturnsGBPDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.convertBitcoins(ConverterSvc.Currency.GBP, 2);
    
        //assert
        double expected = 17801.7386;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void convertBitcoins_0BitCoinToGBP_ReturnsZeroGBPDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.convertBitcoins(ConverterSvc.Currency.GBP, 0);
    
        //assert
        double expected = 0;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void convertBitcoins_1BitCoinToEUR_ReturnsEURDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.convertBitcoins(ConverterSvc.Currency.EUR, 1);
    
        //assert
        double expected = 9809.3278;
        Assertions.assertEquals(expected, actual);
    }
    
    @Test
    public void convertBitcoins_2BitCoinToEUR_ReturnsEURDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.convertBitcoins(ConverterSvc.Currency.EUR, 2);
    
        //assert
        double expected = 19618.6556;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void convertBitcoins_0BitCoinToEUR_ReturnsZeroEURDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.convertBitcoins(ConverterSvc.Currency.EUR, 0);
    
        //assert
        double expected = 0;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void convertBitcoins_NegativeBitCoinsToUSD_ThrowsException() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        Assertions.assertThrows(IllegalArgumentException.class, () -> converterSvc.convertBitcoins(ConverterSvc.Currency.USD, -1));
    }

    
    @Test
    public void convertBitcoins_503ServiceUnavailable_ReturnsCorrectErrorCode() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(503);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
    
        //act
        ConverterSvc converterSvc = new ConverterSvc(client);
        var actual = converterSvc.convertBitcoins(ConverterSvc.Currency.USD, 2);
    
        //assert
        double expected = -1;
        Assertions.assertEquals(expected, actual);
    }


    // @Test
    // public void getExchangeRate_GBP_ReturnsUSDExchangeRate() {
    //     //Arange
    //     ConverterSvc converterSvc = new ConverterSvc();

    //     //Act
    //     var actual = converterSvc.getExchangeRate("GBP");

    //     //Assert
    //     double expected = 200;
    //     Assertions.assertEquals(expected, actual);
    // }
    // @Test
    // public void getExchangeRate_EUR_ReturnsUSDExchangeRate() {
    //     //Arange
    //     ConverterSvc converterSvc = new ConverterSvc();

    //     //Act
    //     var actual = converterSvc.getExchangeRate("EUR");

    //     //Assert
    //     double expected = 300;
    //     Assertions.assertEquals(expected, actual);
    // }

    // @Test
    // public void convertBitcoin_1BitcoinToUSD_ReturnUSDDollars() {
    //     //arrange
    //     ConverterSvc converterSvc = new ConverterSvc();
    //     //act
    //     var actual = converterSvc.convertBitcoins(ConverterSvc.Currency.USD, 1);
    //     //assert
    //     double expected = 100;
    //     Assertions.assertEquals(expected, actual);
    // }

    // @Test
    // public void convertBitcoin_1BitcoinToGBP_ReturnGBPDollars() {
    //     //arrange
    //     ConverterSvc converterSvc = new ConverterSvc();
    //     //act
    //     var actual = converterSvc.convertBitcoins("GBP", 1);
    //     //assert
    //     double expected = 200;
    //     Assertions.assertEquals(expected, actual);
    // }

    // @Test
    // public void convertBitcoin_1BitcoinToEUR_ReturnEURDollars() {
    //     //arrange
    //     ConverterSvc converterSvc = new ConverterSvc();
    //     //act
    //     var actual = converterSvc.convertBitcoins("EUR", 1);
    //     //assert
    //     double expected = 300;
    //     Assertions.assertEquals(expected, actual);
    // }

    // @Test
    // public void convertBitcoin_2BitcoinToUSD_ReturnUSDDollars() {
    //     //arrange
    //     ConverterSvc converterSvc = new ConverterSvc();
    //     //act
    //     var actual = converterSvc.convertBitcoins("USD", 2);
    //     //assert
    //     double expected = 200;
    //     Assertions.assertEquals(expected, actual);
    // }

    // @Test
    // public void convertBitcoin_2BitcoinToGBP_ReturnGBPDollars() {
    //     //arrange
    //     ConverterSvc converterSvc = new ConverterSvc();
    //     //act
    //     var actual = converterSvc.convertBitcoins("GBP", 2);
    //     //assert
    //     double expected = 400;
    //     Assertions.assertEquals(expected, actual);
    // }

    // @Test
    // public void convertBitcoin_2BitcoinToEUR_ReturnEURDollars() {
    //     //arrange
    //     ConverterSvc converterSvc = new ConverterSvc();
    //     //act
    //     var actual = converterSvc.convertBitcoins("EUR", 2);
    //     //assert
    //     double expected = 600;
    //     Assertions.assertEquals(expected, actual);
    // }
}