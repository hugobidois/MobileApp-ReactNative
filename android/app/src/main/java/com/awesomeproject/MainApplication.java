package com.awesomeproject;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.defaults.DefaultReactNativeHost;
import com.facebook.soloader.SoLoader;
import com.salesforce.marketingcloud.MarketingCloudConfig;
import com.salesforce.marketingcloud.notifications.NotificationCustomizationOptions;
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk;
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdkModuleConfig;

import java.util.List;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost =
      new DefaultReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }

        @Override
        protected boolean isNewArchEnabled() {
          return BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
        }

        @Override
        protected Boolean isHermesEnabled() {
          return BuildConfig.IS_HERMES_ENABLED;
        }
      };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();

      SFMCSdk.configure((Context) this, SFMCSdkModuleConfig.build(builder -> {
          builder.setPushModuleConfig(MarketingCloudConfig.builder()
                  .setApplicationId(BuildConfig.APPLICATION_ID)
                  .setAccessToken(BuildConfig.ACCESS_TOKEN)
                  .setSenderId(BuildConfig.SENDER_ID)
                  .setMarketingCloudServerUrl(BuildConfig.SFMC_URL)
                  .setNotificationCustomizationOptions(NotificationCustomizationOptions.create(R.drawable.ic_notification))
                  .setAnalyticsEnabled(true)
                  .build(this));

          return null;
      }), initializationStatus -> {
          Log.e("TAG", "STATUS "+initializationStatus);
          if (initializationStatus.getStatus() == 1) {
              Log.e("TAG", "STATUS SUCCESS");
          }
          return null;
      });

    SoLoader.init(this, /* native exopackage */ false);
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      DefaultNewArchitectureEntryPoint.load();
    }
    ReactNativeFlipper.initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
  }
}
