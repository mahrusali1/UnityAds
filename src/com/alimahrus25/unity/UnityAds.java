package com.alimahrus25.unity;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

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
public class UnityAds extends AndroidNonvisibleComponent {

    public UnityAds(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Test Unity Ads SDK")
    public void TestSDK() {
        boolean ready = com.unity3d.ads.UnityAds.isInitialized();
    }
}
