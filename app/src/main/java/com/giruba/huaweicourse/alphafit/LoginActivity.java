package com.giruba.huaweicourse.alphafit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.hihealth.HiHealthOptions;
import com.huawei.hms.hihealth.HuaweiHiHealth;
import com.huawei.hms.hihealth.SettingController;
import com.huawei.hms.hihealth.data.Scopes;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.api.entity.auth.Scope;
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.result.HuaweiIdAuthResult;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {


    // Request code for displaying the authorization screen using the startActivityForResult method. The value can be customized.
    private static final int REQUEST_SIGN_IN_LOGIN = 1002;
    private static final String TAG = "HihealthKitMainActivity";

    //  The scheme to display the Health authorization screen.
    private static final String HEALTH_APP_SETTING_DATA_SHARE_HEALTHKIT_ACTIVITY_SCHEME = "huaweischeme://healthapp/achievement?module=kit";
    //  Request code for displaying the Health authorization screen using the startActivityForResult method. The value can be customized.
    private static final int REQUEST_HEALTH_AUTH = 1003;
    private Context mContext;
    private SettingController mSettingController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signInSilently();
        setContentView(R.layout.activity_login);
        HuaweiIdAuthButton huaweiIdAuthButton = new HuaweiIdAuthButton(this);
        huaweiIdAuthButton = findViewById(R.id.huaweiIDLogin);
        
        //  Initialize SettingController.
        initService();

//
        huaweiIdAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });

    }

    private void signInSilently() {
        AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams();
        AccountAuthService service = AccountAuthManager.getService(LoginActivity.this, authParams);
        Task<AuthAccount> task = service.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                // Obtain the user's ID information.
                Log.i(TAG, "displayName:" + authAccount.getDisplayName());
                // Obtain the ID type (0: HUAWEI ID; 1: AppTouch ID).
                Log.i(TAG, "accountFlag:" + authAccount.getAccountFlag());
                // Switch Activity Only when success
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // The sign-in failed. Try to sign in explicitly using getSignInIntent().
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Log.i(TAG, "sign failed status:" + apiException.getStatusCode());
                }
            }
        });
    }

    private void initService() {
        mContext = this;
        HiHealthOptions fitnessOptions = HiHealthOptions.builder().build();
        AuthHuaweiId signInHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(fitnessOptions);
        mSettingController = HuaweiHiHealth.getSettingController(mContext, signInHuaweiId);
    }




    /**
     * Sign-in and authorization method. The authorization screen will display if the current account has not granted authorization.
     */
    private void signIn() {
        Log.i(TAG, "begin sign in");
        List<Scope> scopeList = new ArrayList<>();
        // Add scopes to apply for. The following only shows an example. You need to add scopes according to your specific needs.
        // View and store the step count in Health Kit.
        scopeList.add(new Scope(Scopes.HEALTHKIT_STEP_READ));
        scopeList.add(new Scope(Scopes.HEALTHKIT_STEP_WRITE));
        // View and store the height in Health Kit.
        scopeList.add(new Scope(Scopes.HEALTHKIT_HEIGHTWEIGHT_READ));
        scopeList.add(new Scope(Scopes.HEALTHKIT_HEIGHTWEIGHT_WRITE));
        // View and store the heart rate data in Health Kit.
        scopeList.add(new Scope(Scopes.HEALTHKIT_HEARTRATE_READ));
        scopeList.add(new Scope(Scopes.HEALTHKIT_HEARTRATE_WRITE));
        // Configure authorization parameters.
        HuaweiIdAuthParamsHelper authParamsHelper = new HuaweiIdAuthParamsHelper(
                HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM);
        HuaweiIdAuthParams authParams = authParamsHelper.setIdToken()
                .setAccessToken()
                .setScopeList(scopeList)
                .createParams();
        // Initialize the HuaweiIdAuthService object.
        final HuaweiIdAuthService authService = HuaweiIdAuthManager.getService(this.getApplicationContext(),
                authParams);
        // Silent sign-in. If authorization has been granted by the current account, the authorization screen will not display. This is an asynchronous method.
        Task<AuthHuaweiId> authHuaweiIdTask = authService.silentSignIn();
        // Add the callback for the call result.
        authHuaweiIdTask.addOnSuccessListener(new OnSuccessListener<AuthHuaweiId>() {
            @Override
            public void onSuccess(AuthHuaweiId huaweiId) {
                // The silent sign-in is successful.
                Log.i(TAG, "silentSignIn success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // The silent sign-in fails. This indicates that the authorization has not been granted by the current account.
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.i(TAG, "sign failed status:" + apiException.getStatusCode());
                    Log.i(TAG, "begin sign in by intent");
                    // Call the sign-in API using the getSignInIntent() method.
                    Intent signInIntent = authService.getSignInIntent();
                    // Display the authorization screen by using the startActivityForResult() method of the activity.
                    // You can change HihealthKitMainActivity to the actual activity.
                    LoginActivity.this.startActivityForResult(signInIntent, REQUEST_SIGN_IN_LOGIN);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the sign-in response.
        handleSignInResult(requestCode, data);
        //  Process the response returned from the Health authorization screen.
        handleHealthAuthResult(requestCode);
    }

    private void handleHealthAuthResult(int requestCode) {
        if (requestCode != REQUEST_HEALTH_AUTH) {
            return;
        }
        queryHealthAuthorization();
    }

//    Check whether the authorization is successful.
    private void queryHealthAuthorization() {
        Log.d(TAG, "begin to queryHealthAuthorization");
        Task<Boolean> queryTask = mSettingController.getHealthAppAuthorization();
        queryTask.addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (Boolean.TRUE.equals(result)) {
                    Log.i(TAG, "queryHealthAuthorization get result is authorized");
                } else {
                    Log.i(TAG, "queryHealthAuthorization get result is unauthorized");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e != null) {
                    Log.i(TAG, "queryHealthAuthorization has exception");
                }
            }
        });
    }

    /**
     * Method of handling authorization result responses
     *
     * @param requestCode Request code for displaying the authorization screen.
     * @param data        Authorization result response.
     */
    private void handleSignInResult(int requestCode, Intent data) {
        // Handle only the authorized responses
        if (requestCode != REQUEST_SIGN_IN_LOGIN) {
            return;
        }
        // Obtain the authorization response from the intent.
        HuaweiIdAuthResult result = HuaweiIdAuthAPIManager.HuaweiIdAuthAPIService.parseHuaweiIdFromIntent(data);
        Log.d(TAG, "handleSignInResult status = " + result.getStatus() + ", result = " + result.isSuccess());
        if (result.isSuccess()) {
            Log.d(TAG, "sign in is success");
            //  Obtain the authorization result.
            HuaweiIdAuthResult authResult = HuaweiIdAuthAPIManager.HuaweiIdAuthAPIService.parseHuaweiIdFromIntent(data);
            //  Check whether the HUAWEI Health app has been authorized to open data to Health Kit.
            checkOrAuthorizeHealth();
            // Switch Activity Only when success
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    //  Query the Health authorization and display the authorization screen when necessary.
    private void checkOrAuthorizeHealth() {
        Log.d(TAG, "begin to checkOrAuthorizeHealth");
        //  Check the Health authorization status. If the authorization has not been granted,
        //  display the authorization screen in the Health app.
        Task<Boolean> authTask = mSettingController.getHealthAppAuthorization();
        authTask.addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if(Boolean.TRUE.equals(result)) {
                    Log.i(TAG, "checkOrAuthorizeHealth get result success");
                } else {
                    //  If the authorization has not been granted, display the authorization screen in the Health app.
                    Uri healthKitSchemaUri = Uri.parse(HEALTH_APP_SETTING_DATA_SHARE_HEALTHKIT_ACTIVITY_SCHEME);
                    Intent intent = new Intent(Intent.ACTION_VIEW,healthKitSchemaUri);
                    //  Check whether the authorization screen of the Health app can be displayed.
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Display the authorization screen by using the startActivityForResult() method of the activity.
                        //  You can change HihealthKitMainActivity to the actual activity.
                        LoginActivity.this.startActivityForResult(intent, REQUEST_HEALTH_AUTH);
                    } else {
                        Log.w(TAG, "can not resolve HUAWEI Health Auth Activity");
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e != null) {
                    Log.i(TAG, "checkOrAuthorizeHealth has exception");
                }
            }
        });
    }
}