#Mobile as a Service

######Android MaaS Alerts Documentation
________________
##Overview
The MaaS Alerts SDK provides push notification functionality.
Once installed, the SDK will automatically attempt to register for push notifications.
If unsuccessful, the attempt is made again the next time the app starts. 

##Prerequisites
The MaaS Alerts SDK requires the `MaaS Core SDK`.
Be sure to install the module in the `Application` `onCreate` method before registering keys. For example:
``` Java
@Override
public void onCreate() {
    super.onCreate();
    /* Other Code */
    PwCoreSession.getInstance().installModules(PwAlertsModule.getInstance(), ...);
    /* Other code */
}
```

##How do I dynamically unregister and register?
You can manually perform an unregister and register action simply by calling the static methods
`PwAlertsRegister.unregister(Context context)` or `PwAlertsRegister.register(Context context)`

##How do I know when unregister or register operations are finished?
In your implementation of `GCMIntentService` (See below for implementation details) there are two methods that
will be called. `onRegistered(isSuccessful, errMessage)` and `onUnregistered(isSuccessful, errMessage)`.

`onRegistered` is called when the device has registered successfully or unsuccessfully.
The first parameter is a flag signifying this status.
The second parameter will provide an error message or `null` if the operation is successful.

`onUnregistered` is called when the device has unregistered successfully or unsuccessfully.
The first parameter is a flag signifying this status.
The second parameter will provide an error message or `null` if the operation is successful.

##How do I get a list of subscriptions
To get the list of available subscriptions, call the line
`PwAlertsSubscription.getSubscriptionGroups(Context context)`.
This will return an `ArrayList` of `PwSubscription` objects.
Each object maintains an `id`, a `name`, and an `isSubscribed` flag.
The server maintains a subscribed state for each subscription, however this information isn’t
passed back in this call. It is up to the developer to update and persist the saved state of each subscription. Using the saveSubscriptions() method, the subscription state can be saved on the server. PwAlertsSubscription.saveSubscriptions(Context context, List<Subscription> subscriptions). This will use the isSubscribedflag in each of the models in the list. When the Alerts SDK is installed for the first time, or when it runs on the app’s first start, a call is made to the back end in order to reset all the subscriptions to an unsubscribed state. _**This method should be called asynchronously, or outside of the main UI thread.**_

##How do I send an updated list of subscription preferences to the server?
Using the `saveSubscriptions()` method the subscription state can be saved on the server.
`PwAlertsSubscription.saveSubscriptions(Context context, List<Subscription> subscriptions)`.
This will use the `isSubscribed` flag in each of the models in the list.
_**When the Alerts SDK is installed for the first time, or when it runs on the app’s first start,
a call is made to the back end in order to reset all the subscriptions to an unsubscribed state.**_

##How do I receive Push Notifications?
There are a few steps to follow in order to be able to receive and handle push notifications.

###Step 1: Update Permissions
Update the `AndroidManifest.xml` with to include the following permissions.
These go outside of the `application` tag:

``` XML
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- GCM requires a Google account. -->
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
<!-- Keeps the processor from sleeping when a message is received. -->
<uses-permission android:name="android.permission.WAKE_LOCK" />
<!--
    Creates a custom permission so only this app can receive its messages.
    NOTE: the permission *must* be called PACKAGE.permission.C2D_MESSAGE,
    where PACKAGE is the application's package name.
-->
<permission
    android:name="com.phunware.alerts.sample.permission.C2D_MESSAGE"
    android:protectionLevel="signature" />

<uses-permission android:name="com.phunware.alerts.sample.permission.C2D_MESSAGE" />
<!-- This app has permission to register and receive data message. -->
<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
```

###Step 2: Register Services
An `IntentService` runs to handle received messages.
Create a class called `GCMIntentService` that extends `PwAlertsIntentService`.
Then register the service in the manifest.

``` Java
public class GCMIntentService extends PwAlertsIntentService {

    @Override
    public void onRegistered(boolean isSuccessful, String errMessage) {
	}

	@Override
	public void onUnregistered(boolean isSuccessful, String errMessage) {
	}
    
    @Override
    public void onMessageAlertsError(Context context, AlertExtras extras, Exception e) {
    }
        
    @Override
     public void onMessageAlerts(Context context, AlertExtras extras,JSONObject data) {
    }
}
```

In the manifest the service should be registered with the correct path to the service.
The `service` should be defined inside of the `application` tag.
If` GCMIntentService` is in the package root, then follow the below example:
``` XML
<!--
    Application-specific subclass of PwAlertsIntentService that will
    handle received messages.
-->
<service android:name=".GCMIntentService" />
```

###Step 3: Register Receivers
Register the GCM Receiver in the manifest.
This will receive intents from GCM services and then forward them through to the `IntentService` defined above.
Be sure to replace the category tag with your own package name.
The `receiver` should be defined inside of the `application` tag.

``` XML
<!--
    BroadcastReceiver that will receive intents from GCM
    services and handle them to the custom IntentService.

    The com.google.android.c2dm.permission.SEND permission is necessary
    so only GCM services can send data messages for the app
-->
<receiver
    android:name="com.google.android.gcm.GCMBroadcastReceiver"
    android:permission="com.google.android.c2dm.permission.SEND" >
    <intent-filter>

        <!-- Receives the actual messages. -->
        <action android:name="com.google.android.c2dm.intent.RECEIVE" />
        <!-- Receives the registration id. -->
        <action android:name="com.google.android.c2dm.intent.REGISTRATION" />
        <!-- Your Package Name Here -->
        <category android:name="com.your.package.name.here" />
    </intent-filter>
</receiver>
```

###Step 4: Handle Received Alerts
In the `GCMIntentService` there is a callback to receive a message and a callback to see when an error has occured.
`onMessageAlerts` will provide a `PwAlertsExtras` object which holds all of the parsed information from
the alert and provides convenient get methods for them. For example:

``` Java
public void onMessageAlerts(Context context, PwAlertExtras extras, JSONObject data) {
    String message = extras.getAlertMessage();
}
```

Here is where the alert can be handled and built into a notification or otherwise.

##Verify Manifest
`PwAlertsModule` has a convenience method to check if the manifest for the Alerts SDK is setup properlly.
This should only be used for development and testing, not in production.
Call the method with the line `PwAlertsModule.validateManifestAlertsSetup(context)`. The passed in context should be the
application context. If there is an error then an `IllegalStateException` will be thrown with an error message on what
couldn't be found.
