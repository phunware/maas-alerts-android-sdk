MaaSAlers Change Log
==========
Version 1.2.4 *(2013-11-08)*
----------------------------
 * Hotfix to temporarily re-register alerts on each application launch.

Version 1.2.3 *(2013-11-08)*
----------------------------
 * Updating subsriptions to use schema 1.2
 
Version 1.2.2 *(2013-10-23)*
----------------------------
 * Updating subsriptions to user schema 1.1
 
Version 1.2.1
----------------------------
 * Requires Core 1.3.1
 * Optimizing network calls.

Version 1.2.0 *(2013-10-17)*
----------------------------
Reducing interaction between SDK and GCM when an alert is received.
 * Removed `onMessageAlerts` and `onMessageAlertsError` methods.
 * Callback for alerts now come through `onMessage(context, extras)`.

Version 1.1.3 *(2013-09-24)*
----------------------------
 * Removed abstract method "onAlertMessageWithoutPayload" from PwAlertsIntentServices
 
Version 1.1.2 *(2013-09-23)*
----------------------------
 * Now handles push messages without associated S3 payload.
