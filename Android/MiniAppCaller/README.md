# Programming a functionality provided by a native Super App

## Setting

- Point the local repository URL at [build.gradle:23](https://github.com/genexus-colab/gx-super-app/blob/main/Android/MiniAppCaller/build.gradle#L23) to the directory where it is located.

- Configure the Android SDK directory.

- Open the Android Studio project, compile and run.

## Introduction

This document briefly details how a functionality provided by a native Super App must be programmed so that it can be referenced from an External Object integrated in a Mini App build with GeneXus.


## Native implementation of the External Object in Android

### Module creation
 
The way to integrate a new functionality with the FlexibleClient (GeneXus application manager SDK) is through the GeneXusModule interface and the ExternalApi class.

It's necessary to declare two new classes: one that implements the GenexusModule and another one that extends the ExternalApi. Look at the following example:

```kotlin
class PaymentsApi(action: ApiAction?) : ExternalApi(action) {
    companion object {
        const val NAME = "Payments"
    }
}
```

```kotlin
class PaymentsModule : GenexusModule {
    override fun initialize(context: Context) {
        val def = ExternalApiDefinition(PaymentsApi.NAME, PaymentsApi::class.java, false)
        Services.SuperApps.Api = SuperAppApi(def)
    }
}
```

Note:
- The value of the variable NAME (“Payments”) will coincide with the name of the External Object in the GeneXus KB.

### Module registration

This new PaymentsModule must be referenced and registered in the FlexibleClient from the Application class used by the application:

```kotlin
class MainApplication : Application(), IEntityProvider {
    
    private val mApplicationHelper = ApplicationHelper(this, this)

    override fun onCreate() {
        super.onCreate()

        initializeModules()
        val genexusApplication = GenexusApplication()
        genexusApplication.name = "testprototyper"
        mApplicationHelper.onCreate(genexusApplication)
    }

    private fun initializeModules() {
        mApplicationHelper.registerModule(PaymentsModule())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mApplicationHelper.onConfigurationChanged(newConfig)
    }

    override fun getEntityServiceClass(): Class<out EntityService> {
        return AppEntityService::class.java
    }

    override fun getProvider(): EntityDataProvider {
        return AppEntityDataProvider()
    }
}
```

### Creation and registration of methods

Finally, the implementation of the methods must be provided by declaring new IMethodInvoker (when it's not necessary to call a new Activity to get a result) or IMethodInvokerWithActivityResult (when it is). These invokers are referenced and registered by the PaymentsApi itself in its constructor: 

```kotlin
class PaymentsApi(action: ApiAction?) : ExternalApi(action) {
    
    private val methodPayWithoutUI = IMethodInvoker { parameters: List<Any> -> 
        
    }

    private val methodPayWithUI: IMethodInvokerWithActivityResult = object : IMethodInvokerWithActivityResult {
        override fun invoke(parameters: List<Any>): ExternalApiResult {
            
        }

        override fun handleActivityResult(requestCode: Int, resultCode: Int, result: Intent?): ExternalApiResult {

        }
    }

    companion object {
        const val NAME = "Payments"
        private const val METHOD_PAY_NO_UI = "PayWithoutUI"
        private const val METHOD_PAY_UI = "PayWithUI"
    }

    init {
        addMethodHandler(METHOD_PAY_NO_UI, 1, methodPayWithoutUI)
        addMethodHandler(METHOD_PAY_UI, 1, methodPayWithUI)
    }
}
```

Note:
- The name of the methods (“PayWithUI” and “PayWithoutUI”) will coincide with those declared in the External Object in the GeneXus KB.
- The number of method parameters (1) will coincide with the one declared in the External Object in the GeneXus KB.

### Implementation of method functionality

Right now, the "skeleton" of our interface is ready. To achieve the final goal, it only remains to complete the invokers with the desired programming.

The first method is “PayWithoutUI”. As an example, an action that doesn't require user interaction is performed and a random value is returned as payment ID. In a real-life case, this could involve a  call to a service that performs the necessary payment processing.

```kotlin
private val methodPayWithoutUI = IMethodInvoker { parameters: List<Any> ->
    val amount = parameters[0].toString().toDouble()
    val paymentId = PaymentsService.pay(amount)
    ExternalApiResult.success(paymentId)
}
```

Note:
- The only parameter received by the method of the "parameters" variable, corresponding to the amount of the transaction, is obtained. 
- That amount is processed in a "pay" method that returns the payment ID. 
- An ExternalApiResult (success in this case) is returned, along with the resulting paymentId.

The second method is “PayWithUI”. As an example, a new Activity programmed by the native Super App is created, that shows the amount on the screen and has a "Pay" button, which ends that Activity with the payment ID as a result (in this case calling the same "Pay" method as in the previous case). 

In a real-life case, it could be possible to create a payment flow that guides the user by collecting their data to then return the payment ID, completing it. 

```kotlin
private val methodPayWithUI: IMethodInvokerWithActivityResult = object : IMethodInvokerWithActivityResult {
    override fun invoke(parameters: List<Any>): ExternalApiResult {
        val amount = parameters[0].toString().toDouble()
        startActivityForResult(PaymentActivity.newIntent(context, amount), PAYMENT_REQUEST_CODE)
        return ExternalApiResult.SUCCESS_WAIT
    }

    override fun handleActivityResult(requestCode: Int, resultCode: Int, result: Intent?): ExternalApiResult {
        if (resultCode == Activity.RESULT_OK && requestCode == PAYMENT_REQUEST_CODE && result != null) {
            val paymentId = result.getStringExtra(PaymentActivity.EXTRA_PAYMENT_ID)
            return ExternalApiResult.success(paymentId!!)
        }
        return ExternalApiResult.failure("An error occurred processing your payment.")
    }
}
```

Note:
- The invoke method IMethodInvokerWithActivityResult executes a startActivityForResult, launching the Activity of the first step of the payment flow and attaching the amount got from the parameters as an extra. 
- This same method returns ExternalApiResult.SUCCESS_WAIT, indicating that the execution is waiting for a result to continue (the one returned by the flow)
- The handleActivityResult method is also programmed to "catch" the result returned by the Activity when it executes its finish method. If the resultCode, requestCode and resulting Intent (result) are correct, the extra paymentId is retrieved from this method. As in the previous invoker, ExternalApiResult.success is returned along with the execution result. This line is the one that carries out the continuation of the execution of the “PayWithUI” method that was waiting for the result (for having returned SUCCESS_WAIT in the invoke)
- In case that the data is not what is expected, handleActivityResult returns ExternalApiResult.failure("Error description"), indicating that the execution failed together with a descriptive message of the error and canceling the GeneXus event. 
- Even though this is not the best, as at the moment it doesn't allow the Mini App programmer to handle errors, it's useful as an example for this scenario. To allow the execution to continue, a standard value to represent the error could be defined, returning it via ExternalApiResult.success(errorValue).

### Obtaining the caller's Mini App identifier

In certain scenarios, obtaining the current Mini App identifier is useful for discerning the caller of the Super App API method and displaying relevant information about the invoking Mini App.
This code retrieves the current Mini App identifier:

```kotlin
val miniAppId = Services.Application.miniApp?.id
```

## HowTo: Call a Super App API from a Mini App

To implement a communication interface between Mini Apps and Super Apps, visit the official documentation:

- For a Native mobile Mini App, please refer to: [HowTo: Call a Super App API from a Native mobile Mini App](https://wiki.genexus.com/commwiki/wiki?58185,HowTo%3A+Call+a+Super+App+API+from+a+Native+mobile+Mini+App#HowTo%3A+Call+a+non-GeneXus+Super+App+API)
- For a Web Mini App, please refer to: [HowTo: Call a Super App API from a Web Mini App](https://wiki.genexus.com/commwiki/wiki?57430,HowTo%3A+Call+a+Super+App+API+from+a+Web+Mini+App)

## Conclusion

Once the "Skeleton" of the ExternalApi is built, the iteration is reduced to creating new methods with the GeneXus External Object and their respective IMethodInvokers (or IMethodInvokerWithActivityResult) in the native implementation and then "registering" them through ExternalApi.addMethodHandler.
