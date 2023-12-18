package io.flowing.retail.payment.rest;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Step1: Simply call REST Service
 */
@RestController
public class PaymentRestHacksControllerV1 {
  
  @Autowired
  private RestTemplate rest;
  private String stripeChargeUrl = "http://localhost:8099/charge";
  
  @RequestMapping(path = "/api/payment/v1", method = PUT)
  public String retrievePayment(String retrievePaymentPayload) throws Exception {
    String traceId = UUID.randomUUID().toString();
    String customerId = "0815"; // get somehow from retrievePaymentPayload
    long amount = 15; // get somehow from retrievePaymentPayload

    chargeCreditCard(customerId, amount);
    return "{\"status\":\"completed\", \"traceId\": \"" + traceId + "\"}";
  }

  public String chargeCreditCard(String customerId, long remainingAmount) {
    CreateChargeRequest request = new CreateChargeRequest();
    request.amount = remainingAmount;

    CreateChargeResponse response = rest.postForObject( //
            stripeChargeUrl, //
            request, //
            CreateChargeResponse.class);
    
    return response.transactionId;
  }
  
  public static class CreateChargeRequest {
    public long amount;
  }

  public static class CreateChargeResponse {
    public String transactionId;
  }

}