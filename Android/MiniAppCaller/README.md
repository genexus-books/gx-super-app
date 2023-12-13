# Programming a functionality provided by a native SuperApp

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

```java
public class PaymentsApi extends ExternalApi {
    final static String NAME = "Payments";

    public PaymentsApi(ApiAction action) {
        super(action);
    }
}
```

```java
public class PaymentsModule implements GenexusModule {
    @Override
    public void initialize(Context context) {
        ExternalApiFactory.addApi(new ExternalApiDefinition(PaymentsApi.NAME, PaymentsApi.class, false));
    }
}
```

Note:
- The value of the variable NAME (“Payments”) will coincide with the name of the External Object in the GeneXus KB.

### Module registration

This new PaymentsModule must be referenced and registered in the FlexibleClient from the Application class used by the application:

```java
public class MainApplication extends Application implements IEntityProvider {
    
    ApplicationHelper mApplicationHelper = new ApplicationHelper(this, this);

    @Override
    public final void onCreate() {
        super.onCreate();
        
        /* GenexusApplication initialization */
        initializeModules();        
        
        GenexusApplication genexusApplication = new GenexusApplication();
        genexusApplication.setName("testprototyper"); //Use dummy application
        
        mApplicationHelper.onCreate(genexusApplication);
    }

    public void initializeModules() {
        /* Initialize other modules */
        mApplicationHelper.registerModule(new PaymentsModule());
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mApplicationHelper.onConfigurationChanged(newConfig);
    }
    
    @Override
    public Class<? extends EntityService> getEntityServiceClass() {
        return AppEntityService.class;
    }

    @Override
    public EntityDataProvider getProvider() {
        return new AppEntityDataProvider();
    }
}
```

### Creation and registration of methods

Finally, the implementation of the methods must be provided by declaring new IMethodInvoker (when it's not necessary to call a new Activity to get a result) or IMethodInvokerWithActivityResult (when it is). These invokers are referenced and registered by the PaymentsApi itself in its constructor: 

```java
public class PaymentsApi extends ExternalApi {

  final static String NAME = "Payments";
  private final static String METHOD_PAY_NO_UI = "PayWithoutUI";
  private final static String METHOD_PAY_UI = "PayWithUI";

  public PaymentsApi(ApiAction action) {
      super(action);
      addMethodHandler(METHOD_PAY_NO_UI, 1, methodPayWithoutUI);
      addMethodHandler(METHOD_PAY_UI, 1, methodPayWithUI);
  }

  private final IMethodInvoker methodPayWithoutUI = parameters -> {

  };

  private final IMethodInvokerWithActivityResult methodPayWithUI = new IMethodInvokerWithActivityResult() {
      @NonNull
      @Override
      public ExternalApiResult invoke(List<Object> parameters) {

      }

      @NonNull
      @Override
      public ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {

      }
  };
}
```

Note:
- The name of the methods (“PayWithUI” and “PayWithoutUI”) will coincide with those declared in the External Object in the GeneXus KB.
- The number of method parameters (1) will coincide with the one declared in the External Object in the GeneXus KB.

### Implementation of method functionality

Right now, the "skeleton" of our interface is ready. To achieve the final goal, it only remains to complete the invokers with the desired programming.

The first method is “PayWithoutUI”. As an example, an action that doesn't require user interaction is performed and a random value is returned as payment ID. In a real-life case, this could involve a  call to a service that performs the necessary payment processing.

```java
private final IMethodInvoker methodPayWithoutUI = parameters -> {
    int amount = Integer.parseInt(parameters.get(0).toString());
    String paymentId = pay(amount);
    return ExternalApiResult.success(paymentId);
};
```

Note:
- The only parameter received by the method of the "parameters" variable, corresponding to the amount of the transaction, is obtained. 
- That amount is processed in a "pay" method that returns the payment ID. 
- An ExternalApiResult (success in this case) is returned, along with the resulting paymentId.

The second method is “PayWithUI”. As an example, a new Activity programmed by the native Super App is created, that shows the amount on the screen and has a "Pay" button, which ends that Activity with the payment ID as a result (in this case calling the same "Pay" method as in the previous case). 

In a real-life case, it could be possible to create a payment flow that guides the user by collecting their data to then return the payment ID, completing it. 

```java
private final IMethodInvokerWithActivityResult methodPayWithUI = new IMethodInvokerWithActivityResult() {
    @NonNull
    @Override
    public ExternalApiResult invoke(List<Object> parameters) {
        int amount = Integer.parseInt(parameters.get(0).toString());
        startActivityForResult(PaymentActivity.newIntent(getContext(), amount), PAYMENT_REQUEST_CODE);
        return ExternalApiResult.SUCCESS_WAIT;
    }

    @NonNull
    @Override
    public ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
        if (resultCode == Activity.RESULT_OK && requestCode == PAYMENT_REQUEST_CODE && result != null) {
            String paymentId = result.getStringExtra(PaymentActivity.EXTRA_PAYMENT_ID);
            return ExternalApiResult.success(paymentId);
        }

        return ExternalApiResult.failure("An error occurred processing your payment.");
    }
};
```

Note:
- The invoke method IMethodInvokerWithActivityResult executes a startActivityForResult, launching the Activity of the first step of the payment flow and attaching the amount got from the parameters as an extra. 
- This same method returns ExternalApiResult.SUCCESS_WAIT, indicating that the execution is waiting for a result to continue (the one returned by the flow)
- The handleActivityResult method is also programmed to "catch" the result returned by the Activity when it executes its finish method. If the resultCode, requestCode and resulting Intent (result) are correct, the extra paymentId is retrieved from this method. As in the previous invoker, ExternalApiResult.success is returned along with the execution result. This line is the one that carries out the continuation of the execution of the “PayWithUI” method that was waiting for the result (for having returned SUCCESS_WAIT in the invoke)
- In case that the data is not what is expected, handleActivityResult returns ExternalApiResult.failure("Error description"), indicating that the execution failed together with a descriptive message of the error and canceling the GeneXus event. 
- Even though this is not the best, as at the moment it doesn't allow the Mini App programmer to handle errors, it's useful as an example for this scenario. To allow the execution to continue, a standard value to represent the error could be defined, returning it via ExternalApiResult.success(errorValue).

### Obtaining the caller's Mini App identifier

In certain scenarios, obtaining the current Mini App identifier is useful for discerning the caller of the Super App API method and displaying relevant information about the invoking Mini App.


## Interface in GeneXus KB (External Object)

### For testing purposes, create a new External Object named “Payments”

![image](https://user-images.githubusercontent.com/11620451/170534441-6a5d3788-6659-4b85-90ae-074f7e385300.png)

### Declare two new methods: “PayWithUI” and “PayWithoutUI”

![image](https://user-images.githubusercontent.com/11620451/170534961-d2536785-ff67-4265-903b-a7e02cb4896e.png)

Note the number of parameters received by each method (one of Numeric/Integer type), the type returned (VarChar/String) and the “Is Static” property with its value in “True”.

### Reference the new External Object from a button's event programmed in the Checkout Panel

![image](https://user-images.githubusercontent.com/11620451/128559420-2b140ad8-412b-48d9-a9de-c7c7ce95eb09.png)

![image](https://user-images.githubusercontent.com/11620451/170535263-874b14bf-51fc-45dc-a698-9f792f25ffd9.png)

As aforementioned and especially for this example, the amount of the transaction is passed to both methods as a parameter, whose processing returns the payment Id.

## Conclusion

Once the "Skeleton" of the ExternalApi is built, the iteration is reduced to creating new methods with the GeneXus External Object and their respective IMethodInvokers (or IMethodInvokerWithActivityResult) in the native implementation and then "registering" them through ExternalApi.addMethodHandler.
