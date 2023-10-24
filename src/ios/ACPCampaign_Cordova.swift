/*
 Copyright 2020 Adobe. All rights reserved.
 This file is licensed to you under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License. You may obtain a copy
 of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under
 the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 OF ANY KIND, either express or implied. See the License for the specific language
 governing permissions and limitations under the License.
 */

/********* cordova-acpcampaign.m Cordova Plugin Implementation *******/

//#import <Cordova/CDV.h>

//#import <ACPCampaign/ACPCampaign.h>
//#import <Cordova/CDVPluginResult.h>
//#import <ACPCore/ACPCore.h>
//#import <ACPCore/ACPIdentity.h>
//#import <ACPCore/ACPLifecycle.h>
//#import <ACPCore/ACPSignal.h>
//#import "ACPUserProfile.h"

import UserNotifications;


class ACPCampaign_Cordova : CDVPlugin {

    var typeId:String!

    func extensionVersion(command:CDVInvokedUrlCommand!) {
        self.commandDelegate.runInBackground({
            var pluginResult:CDVPluginResult! = nil
            let extensionVersion:String! = ACPCampaign.extensionVersion()

            if extensionVersion != nil && extensionVersion.length() > 0 {
                pluginResult = CDVPluginResult.resultWithStatus(CDVCommandStatus_OK, messageAsString:extensionVersion)
            } else {
                pluginResult = CDVPluginResult.resultWithStatus(CDVCommandStatus_ERROR)
            }

            self.commandDelegate.sendPluginResult(pluginResult, callbackId:command.callbackId)
        })
    }

    func setPushIdentifier(command:CDVInvokedUrlCommand!) {

        let center:UNUserNotificationCenter! = UNUserNotificationCenter.currentNotificationCenter()
          center.delegate = self
          center.requestAuthorizationWithOptions((UNAuthorizationOptionSound | UNAuthorizationOptionAlert | UNAuthorizationOptionBadge), completionHandler:{ (granted:Bool,error:NSError?) in

             typeId = NSBundle.mainBundle().infoDictionary().valueForKey("TypeId")

            if !error  {
                // required to get the app to do anything at all about push notifications
                dispatch_async(dispatch_get_main_queue(), {
                    let deviceToken:String! = command.arguments[0]
                    let valueTypeId:String! = command.arguments[1]

                    let data:NSData! = deviceToken.dataUsingEncoding(NSUTF8StringEncoding)

                    ACPCore.collectPii([typeId: valueTypeId])
                    UIApplication.sharedApplication().registerForRemoteNotifications()
                })

                NSLog("Push registration success.")

            } else {
                NSLog("Push registration FAILED")
                NSLog("ERROR: %@ - %@", error.localizedFailureReason, error.localizedDescription)
                NSLog("SUGGESTIONS: %@ - %@", error.localizedRecoveryOptions, error.localizedRecoverySuggestion)
            }
            })

        ACPCore.logLevel = ACPMobileLogLevelDebug

        let pluginResult:CDVPluginResult! = CDVPluginResult.resultWithStatus(CDVCommandStatus_OK)

        self.commandDelegate.sendPluginResult(pluginResult, callbackId:command.callbackId)
    }

    func getTypeId(command:CDVInvokedUrlCommand!) {
        self.commandDelegate.runInBackground({
             let pluginResult:CDVPluginResult! = CDVPluginResult.resultWithStatus(CDVCommandStatus_OK, messageAsString:typeId)
            self.commandDelegate.sendPluginResult(pluginResult, callbackId:command.callbackId)
        })
    }
}
