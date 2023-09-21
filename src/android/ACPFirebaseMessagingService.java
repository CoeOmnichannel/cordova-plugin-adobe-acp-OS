package com.adobe.marketing.mobile.cordova;

import android.content.Intent;
import android.os.Bundle;
import com.adobe.marketing.mobile.MobileCore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.HashMap;
import java.util.Map;

public class ACPFirebaseMessagingService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    // Aqui você lida com o recebimento da notificação push
    // Extrai as informações necessárias do objeto remoteMessage, como deliveryId, messageId e acsDeliveryTracking

    System.out.println("onMessageReceived");

    Map<String, String> data = remoteMessage.getData();
    String deliveryId = data.get("_dId");
    String messageId = data.get("_mId");
    String acsDeliveryTracking = data.get("_acsDeliveryTracking");

    if (acsDeliveryTracking == null) {
      acsDeliveryTracking = "on";
    }

    System.out.println("deliveryId");
    System.out.println(deliveryId);

    System.out.println("messageId");
    System.out.println(messageId);

    System.out.println("acsDeliveryTracking");
    System.out.println(acsDeliveryTracking);

    // Verifica se a notificação push contém os dados necessários para o rastreamento
    if (deliveryId != null && messageId != null && acsDeliveryTracking.equals("on")) {
      // Cria um HashMap para armazenar os dados de contexto
      HashMap<String, String> contextData = new HashMap<>();
      // Adiciona os dados necessários ao HashMap
      contextData.put("deliveryId", deliveryId);
      contextData.put("broadlogId", messageId);

      // Rastreia o evento de impressão da notificação push usando o Adobe Mobile SDK
      contextData.put("action", "7"); // 7 representa a impressão (impression)
      MobileCore.trackAction("tracking", contextData);

      // Rastreia o evento de clique da notificação push usando o Adobe Mobile SDK
      contextData.put("action", "2"); // 2 representa o clique (click)
      MobileCore.trackAction("tracking", contextData);

      // Rastreia o evento de abertura da notificação push usando o Adobe Mobile SDK
      contextData.put("action", "1"); // 1 representa a abertura (open)
      MobileCore.trackAction("tracking", contextData);

       
    }

  }
 
}
