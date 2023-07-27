package com.cloudacademy.bitcoin;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.NumberFormat;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConverterSvc {

    // calling API
    private final String BITCOIN_CURRENTPRICE_URL = "https://api.coindesk.com/v1/bpi/currentprice.json";
    // new HttpGet object
    private final HttpGet httpget = new HttpGet(BITCOIN_CURRENTPRICE_URL);


    // We need Http client to connect to Coindesk Bitcoin API
    private CloseableHttpClient httpclient;

    public ConverterSvc() {
        // initialize the Http client 
        this.httpclient = HttpClients.createDefault(); // at that point Unit test have become integration test (because they're making external connections to external dependency)
    }

    // second constructor for Mock purposes // pass mock http client
    public ConverterSvc(CloseableHttpClient httpClient) {
        this.httpclient = httpClient;
    }

    // currency
    public enum Currency {
        USD,
        GBP,
        EUR
    }
                                    // from enum
    public double getExchangeRate(Currency currency) {

        // if (currency.equals("USD")) {
        //     return 100;
        // } else if (currency.equals("GBP")) {
        //     return 200;
        // } else if (currency.equals("EUR")) {
        //     return 300;
        // } return 0;

        double rate = 0;

        try (CloseableHttpResponse response = this.httpclient.execute(httpget)) {
            // connecting to API

            // parsing response 
            InputStream inputstream = response.getEntity().getContent();
            var json = new BufferedReader(new InputStreamReader(inputstream));

            // extracting rate
            @SuppressWarnings("depracation")                    //currency is enum it have to be .toString();
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            String n = jsonObject.get("bpi").getAsJsonObject().get(currency.toString()).getAsJsonObject().get("rate").getAsString();
            NumberFormat nf = NumberFormat.getInstance();
            rate = nf.parse(n).doubleValue();


        } catch (Exception e) {
            rate = -1;
        }

        return rate;

    }

    public double convertBitcoins(Currency currency, double coins) {

        double dollars = 0;

        if (coins < 0) {
            throw new IllegalArgumentException("Number of coins must be not less then 0");
        }

        var exchangeRate = getExchangeRate(currency);

        if (exchangeRate >= 0) {
            dollars = exchangeRate *  coins;
        } else {
            dollars = -1;
        }

        return dollars;
    }
}