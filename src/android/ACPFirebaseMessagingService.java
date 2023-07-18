package com.adobe.marketing.mobile.cordova;

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

    Map<String, String> data = remoteMessage.getData();
    String deliveryId = data.get("_dId");
    String messageId = data.get("_mId");
    String acsDeliveryTracking = data.get("_acsDeliveryTracking");

    if (acsDeliveryTracking == null) {
      acsDeliveryTracking = "on";
    }

    // Verifica se a notificação push contém os dados necessários para o rastreamento
    if (deliveryId != null && messageId != null && acsDeliveryTracking.equals("on")) {
      // Cria um HashMap para armazenar os dados de contexto
      HashMap<String, String> contextData = new HashMap<>();
      // Adiciona os dados necessários ao HashMap
      contextData.put("deliveryId", deliveryId);
      contextData.put("broadlogId", messageId);

      // Rastreia o evento de impressão da notificação push usando o Adobe Mobile SDK
      contextData.put("action", "7"); // 7 representa a impressão (impression)
      MobileCore.trackAction("push_impression", contextData);

      // Rastreia o evento de clique da notificação push usando o Adobe Mobile SDK
      contextData.put("action", "3"); // 3 representa o clique (click)
      MobileCore.trackAction("push_click", contextData);

      // Rastreia o evento de abertura da notificação push usando o Adobe Mobile SDK
      contextData.put("action", "2"); // 2 representa a abertura (open)
      MobileCore.trackAction("push_open", contextData);
    }

  }
}
