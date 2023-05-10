# Enable Mini App Logging for Android

The Super App may display Mini App logging messages. 
To do this, enable Mini-Apps login in the Super App, as shown in [Banking app example](/Android/MiniAppCaller/app/src/main/java/com/genexus/superapps/bankx/application/BankingApplication.kt#L39) 

```kotlin
Services.Log.setLevel(LogLevel.DEBUG)
```

The Mini Apps itself must have [enabled the logging property](https://wiki.genexus.com/commwiki/servlet/wiki?37876,Enable+Logging+property).