# Deep Linking With Android

An example of how android deep linking works

To test it, do the follow:

1.Go to the test page of Google for Deeplinking
https://developers.google.com/app-indexing/webmasters/test

2.In the edit box, enter the follow text:

android-app://com.jiahaoliuliu.deeplinkingwithandroid/deeplinkingwithandroid/details

3.Use a Bar code scanner app to scan the QR Code. (e.g. Use Bar code scannner https://play.google.com/store/apps/details?id=com.google.zxing.client.android&hl=en)

4.It will open a browser showing the link. Click on the link. The app should appear showing the detail page and the source must be Deep Linking.

# Variation
On the step 2, it is possible to add background color as parameter.
Here is the example of the link to be used:
android-app://com.jiahaoliuliu.deeplinkingwithandroid/deeplinkingwithandroid/details?background=olive

The key must be background and the value could be any color accepted by Color.parseColor().
For more information, see here:
http://developer.android.com/reference/android/graphics/Color.html#parseColor(java.lang.String)

# Some comments

The guide from Google is not complete as it should be. It does not manage correctly the stack of the app. There are several situations where the stack matters:

1. If the user has the app already open, each time the user opens the app with deep linking by clicking on the link in the browser, a new instance of the app will be created. This will generate many instance of the app running at the same time.

2. If the app checks the intent data in onResume method of the main activity, when the user comes back from the details activity, the intent data will remains. So, it must be cleaned in order to show main activity correctly. Thus, getIntent().setData(null) is needed after the details activity has been started because the deep linking.

3. If the user has not the app open while he clicks on the link on the browser, at the first time, the app will open normally showing the detail page. If the user interacts with the app, for example, by coming back to the main page, when the user click on the link in the browser again, instead of generating a new intent, the same intent which has started the app originally will be used. Because we have removed the intent data in the step 2, the app won't start the details page.

To solve this three problems, the follow solution has been applied:

1. Modify the behaviour of the app when it is launched to singleTask. This is, by adding android:launchMode="singleTask" in the manifest of the main activity. This will prevent to have several instance of the same application.
2. Because of this new mode, a new intent is created. So, the main activity also checks the intent on the onNewIntent method.
3. If the user has opened the app with deep linking and he presses on the icon in the main menu of the app, normally the main activity will be show. To solve this, the app record the last screen and the background color if needed. This is not applicable to all the apps. So, as example, a new branch without this features is created here: https://github.com/jiahaoliuliu/deepLinkingWithAndroid/tree/affordableImpl

Any question or issue, send a mail to jiahaoliuliu@gmail.com

Happy coding!
