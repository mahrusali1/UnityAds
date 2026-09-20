package com.alimahrus25.unity;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds.UnityAdsInitializationError;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.UnityBannerSize;

@DesignerComponent(
        version = 1,
        description = "Unity Ads Extension Test",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = ""
)
@SimpleObject(external = true)
@UsesLibraries(libraries = "unity-ads-4.2.0.jar")
public class UnityAds extends AndroidNonvisibleComponent {

    private Activity activity;
    private BannerView bannerView;

    public UnityAds(ComponentContainer container) {
        super(container.$form());
        activity = container.$form();
    }

    @SimpleFunction(description = "Initialize Unity Ads")
    public void Initialize() {

        com.unity3d.ads.UnityAds.initialize(
                activity,
                "800374528",
                true,
                new IUnityAdsInitializationListener() {

                    @Override
                    public void onInitializationComplete() {
                        AdDebug("Unity Ads initialized");
                    }

                    @Override
                    public void onInitializationFailed(
                            UnityAdsInitializationError error,
                            String message) {

                        AdDebug("Initialize failed: " + error + " - " + message);
                    }
                }
        );
    }

    @SimpleFunction(description = "Load Unity Banner")
    public void LoadBanner() {

        bannerView = new BannerView(
                activity,
                "BP_Banner_Android",
                new UnityBannerSize(320, 50)
        );

        bannerView.setListener(new BannerView.IListener() {

            @Override
            public void onBannerLoaded(BannerView bannerAdView) {
                AdDebug("Banner loaded");
            }

            @Override
            public void onBannerFailedToLoad(
                    BannerView bannerAdView,
                    BannerErrorInfo errorInfo) {

                AdDebug("Banner failed: " + errorInfo.errorMessage);
            }

            @Override
            public void onBannerClick(BannerView bannerAdView) {
                AdDebug("Banner clicked");
            }

            @Override
            public void onBannerLeftApplication(BannerView bannerAdView) {
                AdDebug("Banner left application");
            }
        });

        bannerView.load();
        AdDebug("Banner load started");
    }

    @SimpleEvent(description = "Debug message")
    public void AdDebug(String message) {
        EventDispatcher.dispatchEvent(this, "AdDebug", message);
    }
}
