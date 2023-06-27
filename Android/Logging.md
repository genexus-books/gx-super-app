# Enable Mini App Logging for Android

The Super App may display Mini App logging messages. 
To do this, enable Mini-Apps login in the Super App, as shown in [Banking app example](/Android/MiniAppCaller/app/src/main/java/com/genexus/superapps/bankx/application/BankingApplication.kt#L39) 

```kotlin
Services.Log.setLevel(LogLevel.DEBUG)
```
