package com.alimahrus25.unity;

import android.app.Activity;

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
        description = "Unity Ads Extension",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = ""
)
@SimpleObject(external = true)
@UsesLibraries(libraries = "unity-ads-4.2.0.jar")
public class UnityAds extends AndroidNonvisibleComponent {

    private Activity activity;
    private BannerView bannerView;
    private boolean initialized = false;

    public UnityAds(ComponentContainer container) {
        super(container.$form());
        activity = container.$form();
    }

    // =========================================================
    // INITIALIZE
    // =========================================================

    @SimpleFunction(description = "Initialize Unity Ads")
    public void Initialize() {

        AdDebug("Initialize() dipanggil");

        if (com.unity3d.ads.UnityAds.isInitialized()) {
            initialized = true;
            AdDebug("Unity Ads sudah initialized");
            return;
        }

        com.unity3d.ads.UnityAds.initialize(
                activity,
                "800374528",
                true,
                new IUnityAdsInitializationListener() {

                    @Override
                    public void onInitializationComplete() {

                        initialized = true;

                        AdDebug("Unity Ads initialized");
                    }

                    @Override
                    public void onInitializationFailed(
                            UnityAdsInitializationError error,
                            String message) {

                        initialized = false;

                        AdDebug(
                                "Initialize failed: "
                                + error
                                + " - "
                                + message
                        );
                    }
                }
        );
    }

    // =========================================================
    // LOAD BANNER
    // =========================================================

    @SimpleFunction(description = "Load Unity Banner")
    public void LoadBanner() {

        if (!initialized &&
                !com.unity3d.ads.UnityAds.isInitialized()) {

            AdDebug("LoadBanner gagal: Unity Ads belum initialized");
            return;
        }

        initialized = true;

        AdDebug("Membuat BannerView");

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

                AdDebug(
                        "Banner failed: "
                        + errorInfo.errorMessage
                );
            }

            @Override
            public void onBannerClick(
                    BannerView bannerAdView) {

                AdDebug("Banner clicked");
            }

            @Override
            public void onBannerLeftApplication(
                    BannerView bannerAdView) {

                AdDebug("Banner left application");
            }
        });

        AdDebug("Memulai banner.load()");

        bannerView.load();

        AdDebug("Banner load started");
    }

    // =========================================================
    // DEBUG EVENT
    // =========================================================

    @SimpleEvent(description = "Debug message")
    public void AdDebug(String message) {

        EventDispatcher.dispatchEvent(
                this,
                "AdDebug",
                message
        );
    }
}
