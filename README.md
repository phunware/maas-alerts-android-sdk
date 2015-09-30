MaaS Alerts SDK for Android
==================

Version 1.2.9

This is Phunware's Android SDK for the Alerts & Notifications module. Visit http://maas.phunware.com/ for more details and to sign up.

Requirements
------------

* Android SDK 2.2+ (API level 8) or above
* Android Target 4.4.2
* Android Support v4 18.0.+


Documentation
------------

MaaS Alerts documentation is included in the Documents folder in the repository as both HTML and as a .jar. You can also find the latest documentation here: http://phunware.github.io/maas-alerts-android-sdk/



Overview
---------

The MaaS Alerts SDK provides push notification functionality.
Once installed, the SDK will automatically attempt to register for push notifications.
If unsuccessful, the attempt is made again the next time the app starts.



Server Setup
------------

*For detailed instructions, see our [GCM setup documentation](http://phunware.github.io/maas-alerts-android-sdk/how-to/Setup%20GCM%20Project.htm).*

Log into your [Google account's API console](https://code.google.com/apis/console). (You will need an email account with Google to have access to the console.)
Select "Services" and enable "Google Cloud Messaging for Android."
Once GCM is turned on, select "API Access" from the menu and look for the "API Key" under the section "Key for Android apps." Record the API key.

Once you have the API key, you will need the sender ID.
To get the sender ID, view the Google console's address bar and copy the value of the "project" key (i.e. https://code.google.com/apis/console/X/X/#project:111111111:access).

Once you have both the API key and sender ID, log into maas.phunware.com. Select the Alerts & Notifications tab from the menu, then select configure. Select an app you've created, otherwise create one first. Once you have an app, select the desired app and enter the token, which is your API key and sender ID. Select save to finish configuring your app.



Prerequisites
-------------

The MaaS Alerts SDK requires the latest `MaaS Core SDK` and Google Play services version 3.1 or higher.
Be sure to install the module in the `Application` `onCreate` method before registering keys. For example:
``` Java
@Override
public void onCreate() {
    super.onCreate();
    /* Other code */
    PwCoreSession.getInstance().installModules(PwAlertsModule.getInstance(), ...);
    /* Other code */
}
```



Integration
------------

### How do I receive push notifications?
There are a few steps to follow in order to be able to receive and handle push notifications.

#### Step 1: Update Permissions
Update the `AndroidManifest.xml` to include the following permissions.
These go outside of the `application` tag:

``` XML
<uses-permission android:name="android.permission.INTERNET" />

<!-- GCM requires a Google account. -->
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
<!-- Keeps the processor from sleeping when a message is received. -->
<uses-permission android:name="android.permission.WAKE_LOCK" />
<!--
    Creates a custom permission so only this app can receive its messages.
    NOTE: The permission must be called PACKAGE.permission.C2D_MESSAGE,
    where PACKAGE is the application's package name.
-->
<permission
    android:name="com.phunware.alerts.sample.permission.C2D_MESSAGE"
    android:protectionLevel="signature" />

<uses-permission android:name="com.phunware.alerts.sample.permission.C2D_MESSAGE" />
<!-- This app has permission to register and receive data messages. -->
<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
```

#### Step 2: Register Services
An `IntentService` runs to handle received messages.
Create a class called `GCMIntentService` that extends `PwAlertsIntentService`.
Then, register the service in the manifest.

``` Java
public class GCMIntentService extends PwAlertsIntentService {

    @Override
    public void onMessage(Context context, PwAlertExtras extras) {
    }
    
    @Override    
    public void onDelete(Context context, Intent intent) {
    }

    @Override
    public void onError(Context context, Intent intent) {
    }

}
```

The service should be registered with the correct path to the service in the manifest.
The `service` should be defined inside of the `application` tag.
If` GCMIntentService` is in the package root, follow the below example:
``` XML
<!--
    Application-specific subclass of PwAlertsIntentService that will
    handle received messages.
-->
<service android:name=".GCMIntentService" />
```

#### Step 3: Register Receivers
Register the GCM receiver in the manifest.
This will receive intents from GCM services, then forward them through to the `IntentService` defined above.
Be sure to replace the category tag with your own package name.
The `receiver` should be defined inside of the `application` tag.

``` XML
<!--
    BroadcastReceiver will receive intents from GCM
    services and handle them to the custom IntentService.

    The com.google.android.c2dm.permission.SEND permission is necessary
    so only GCM services can send data messages for the app.
-->
<receiver
    android:name="com.phunware.alerts.GCMBroadcastReceiver"
    android:permission="com.google.android.c2dm.permission.SEND" >
    <intent-filter>

        <!-- Receives the actual messages. -->
        <action android:name="com.google.android.c2dm.intent.RECEIVE" />
        <!-- Receives the registration ID. -->
        <action android:name="com.google.android.c2dm.intent.REGISTRATION" />
        <!-- [Your Package Name Here] -->
        <category android:name="com.your.package.name.here" />
    </intent-filter>
</receiver>
```

#### Step 4: Handle Received Alerts
In the `GCMIntentService`, there is a callback to receive a message and a callback to see when an error has occurred.
`onMessage` will provide a `PwAlertsExtras` object, which holds all of the parsed information from
the alert and provides convenient GET methods for them. For example:

``` Java
public void onMessage(Context context, PwAlertExtras extras) {
    String message = extras.getAlertMessage();
	/*
	 * Here is where the alert can be handled and built into a notification (or otherwise).
	 */
}
```

##### Get Extra Data
If extra data is expected in the alert, then forward the alert extras object to the method when handling the message: `getExtraData(Context, PwAlertExtras)`
```Java

    @Override
    public void onMessage(Context context, PwAlertExtras extras) {

        // Create a bundle to pass to notification creation.
        final Bundle bundle = new Bundle();
        // Add ‘alertExtras’ to the bundle.
        bundle.putParcelable("alertExtras", extras);

        try {
            // Delegate to getExtraData(context, extras).
            final JSONObject data = getExtraData(context, extras);
            try {
                // Process key/value pairs contained in the data and add them to the bundle. 
                bundle.putString(Utils.INTENT_ALERT_DATA, data.toString(2));
            } catch (JSONException e) {
                bundle.putString(Utils.INTENT_ALERT_DATA, data.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Create a notification using the extra data bundle.
    }
```

#### Analytic Event Trigger
When an alert reaches the device and is interacted with it is up to the responsibility of the developer to trigger a positive click. This should be called once the alert has been interacted with by a user. This increments the Alerts Opened counter.
```JAVA
// Trigger a positive click event for an alert by its PID.
PwAlertsRegister.sendPositiveClick(context, pid);
```


Verify Manifest
---------------

`PwAlertsModule` has a convenience method to check if the manifest for the Alerts SDK is set up properly.
This should only be used for development and testing, not in production.
Call the method with the line `PwAlertsModule.validateManifestAlertsSetup(context)`. The passed-in context should be the
application context. If there is an error, then an `IllegalStateException` will be thrown with an error message regarding what
couldn't be found.



Troubleshooting
---------------

### Why am I not getting alerts?
Make sure the SDK is properly configured:

1. Double-check that your manifest is correct and no errors are thrown when running `PwAlertsModule.validateManifestAlertsSetup(context)`.
2. Make sure that the `GCMIntentService` is in your root package. See [this post](http://stackoverflow.com/questions/16951216/gcmbaseintentservice-callback-only-in-root-package/16951296?noredirect=1#16951296) to see how to move the service to another location.
3. Check that your Google API keys are properly configured on the MaaS portal in the Alerts & Notifications tab.

### How do I dynamically unregister and register?
You can manually perform an unregister and register action simply by calling the static methods:
`PwAlertsRegister.unregister(Context context)` or `PwAlertsRegister.register(Context context)`

### How do I get a list of subscriptions?
To get the list of available subscriptions, call the line
`PwAlertsSubscriptions.getSubscriptionGroups(Context context)`.
This will return an `ArrayList` of `PwSubscription` objects.
Each object maintains an `id`, a `name`, an `isSubscribed` flag and an `ArrayList` of
children `PwSubscription` objects (referred to as sub-segments).
The server and the Alerts SDK maintain state for each subscription.

### How do I send an updated list of subscription preferences to the server?
Use the `saveSubscriptions()` method to save the subscription state on the server. **This method should be called asynchronously, or outside of the main UI thread.**
`PwAlertsSubscriptions.saveSubscriptions(Context context, List<PwSubscription> subscriptions)`.
This will use the `isSubscribed` flag in each of the models in the list.
**NOTE**: When the Alerts SDK is installed for the first time, or when it runs on the app’s first start,
a call is made to the backend in order to reset all the subscriptions to an unsubscribed state.**_

### How can I check whether the device is registered?
Use `PwAlertsRegister.gcmIsRegistered(Context)`. It will return true when the device is registered to GCM, false if not. Make sure the context you pass is not null, otherwise false will be returned.

### Can I retrieve the GCM device token? 
Yes, you can. Use `PwAlertsRegister.deviceGCMToken(Context)`. It will return GCM device token or null if one isn't available.
