MaaSAlers Change Log
==========

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
