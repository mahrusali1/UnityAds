package com.alimahrus25.unity;

import android.app.Activity;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.annotations.UsesLibraries;

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

    public UnityAds(ComponentContainer container) {
        super(container.$form());
        activity = container.$form();
    }

    @SimpleFunction(description = "Test BannerView")
    public void TestBanner() {

        BannerView testBanner = new BannerView(
                activity,
                "BP_Banner_Android",
                new UnityBannerSize(320, 50)
        );
    }
}
