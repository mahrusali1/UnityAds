package com.alimahrus25.unity;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds.UnityAdsInitializationError;

import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;


@DesignerComponent(
        version = 1,
        description = "Unity Ads Banner Extension",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = ""
)
@SimpleObject(external = true)
@UsesPermissions(
        permissionNames =
                "android.permission.INTERNET,"
                + "android.permission.ACCESS_NETWORK_STATE"
)
public class UnityAds extends AndroidNonvisibleComponent {

    private final Activity activity;
    private final Form form;

    private String gameId = "";
    private String bannerAdUnitId = "";

    private boolean testMode = true;
    private boolean initialized = false;
    private boolean bannerLoaded = false;

    private BannerView bannerView;
    private FrameLayout bannerContainer;


    public UnityAds(ComponentContainer container) {
        super(container.$form());

        this.form = container.$form();
        this.activity = (Activity) container.$context();
    }


    @SimpleFunction(
            description = "Set the Unity Game ID."
    )
    public void GameId(String id) {

        if (id == null) {
            gameId = "";
        } else {
            gameId = id.trim();
        }
    }


    @SimpleFunction(
            description = "Set the Unity Banner Ad Unit / Placement ID."
    )
    public void BannerAdUnitId(String id) {

        if (id == null) {
            bannerAdUnitId = "";
        } else {
            bannerAdUnitId = id.trim();
        }
    }


    @SimpleFunction(
            description = "Enable or disable Unity Ads test mode."
    )
    public void TestMode(boolean enabled) {
        testMode = enabled;
    }


    @SimpleFunction(
            description = "Returns true when Unity Ads SDK has been initialized."
    )
    public boolean IsInitialized() {

        try {
            return initialized || com.unity3d.ads.UnityAds.isInitialized();
        } catch (Exception e) {
            return initialized;
        }
    }


    @SimpleFunction(
            description = "Returns the Unity Ads SDK version."
    )
    public String SDKVersion() {

        try {
            return com.unity3d.ads.UnityAds.getVersion();
        } catch (Exception e) {
            return "";
        }
    }


    @SimpleFunction(
            description = "Initialize Unity Ads using the Game ID."
    )
    public void Initialize() {

        if (gameId == null || gameId.length() == 0) {

            InitializationFailed(
                    "INVALID_GAME_ID",
                    "Unity Game ID is empty."
            );

            return;
        }

        activity.runOnUiThread(
                new Runnable() {

                    @Override
                    public void run() {

                        try {

                            com.unity3d.ads.UnityAds.initialize(
                                    activity,
                                    gameId,
                                    testMode,
                                    new IUnityAdsInitializationListener() {

                                        @Override
                                        public void onInitializationComplete() {

                                            initialized = true;

                                            Initialized();

                                            AdDebug(
                                                    "Unity Ads initialized successfully."
                                            );
                                        }


                                        @Override
                                        public void onInitializationFailed(
                                                UnityAdsInitializationError error,
                                                String message) {

                                            initialized = false;

                                            String errorText;

                                            if (error != null) {
                                                errorText = error.toString();
                                            } else {
                                                errorText = "UNKNOWN";
                                            }

                                            String errorMessage;

                                            if (message != null) {
                                                errorMessage = message;
                                            } else {
                                                errorMessage = "";
                                            }

                                            InitializationFailed(
                                                    errorText,
                                                    errorMessage
                                            );

                                            AdDebug(
                                                    "Unity Ads initialization failed: "
                                                    + errorText
                                                    + " - "
                                                    + errorMessage
                                            );
                                        }
                                    }
                            );

                        } catch (Exception e) {

                            initialized = false;

                            String message;

                            if (e.getMessage() != null) {
                                message = e.getMessage();
                            } else {
                                message = e.toString();
                            }

                            InitializationFailed(
                                    "EXCEPTION",
                                    message
                            );
                        }
                    }
                }
        );
    }


    @SimpleFunction(
            description = "Create and load a Unity banner."
    )
    public void LoadBanner() {

        if (!IsInitialized()) {

            BannerFailed(
                    "NOT_INITIALIZED",
                    "Unity Ads has not been initialized."
            );

            return;
        }

        if (bannerAdUnitId == null
                || bannerAdUnitId.length() == 0) {

            BannerFailed(
                    "INVALID_AD_UNIT_ID",
                    "Banner Ad Unit ID is empty."
            );

            return;
        }

        activity.runOnUiThread(
                new Runnable() {

                    @Override
                    public void run() {

                        try {

                            destroyBannerInternal();

                            UnityBannerSize bannerSize =
                                    new UnityBannerSize(320, 50);

                            bannerView =
                                    new BannerView(
                                            activity,
                                            bannerAdUnitId,
                                            bannerSize
                                    );

                            bannerView.setListener(
                                    new BannerView.IListener() {

                                        @Override
                                        public void onBannerLoaded(
                                                BannerView bannerAdView) {

                                            bannerLoaded = true;

                                            BannerLoaded();

                                            AdDebug(
                                                    "Unity banner loaded."
                                            );
                                        }


                                        @Override
                                        public void onBannerClick(
                                                BannerView bannerAdView) {

                                            BannerClicked();

                                            AdDebug(
                                                    "Unity banner clicked."
                                            );
                                        }


                                        @Override
                                        public void onBannerFailedToLoad(
                                                BannerView bannerAdView,
                                                BannerErrorInfo errorInfo) {

                                            bannerLoaded = false;

                                            String errorCode = "UNKNOWN";
                                            String errorMessage = "";

                                            if (errorInfo != null) {

                                                if (errorInfo.errorCode != null) {
                                                    errorCode =
                                                            errorInfo.errorCode.toString();
                                                }

                                                if (errorInfo.errorMessage != null) {
                                                    errorMessage =
                                                            errorInfo.errorMessage;
                                                }
                                            }

                                            BannerFailed(
                                                    errorCode,
                                                    errorMessage
                                            );

                                            AdDebug(
                                                    "Unity banner failed: "
                                                    + errorCode
                                                    + " - "
                                                    + errorMessage
                                            );
                                        }


                                        @Override
                                        public void onBannerLeftApplication(
                                                BannerView bannerAdView) {

                                            AdDebug(
                                                    "Unity banner left application."
                                            );
                                        }
                                    }
                            );

                            bannerView.setVisibility(
                                    View.GONE
                            );

                            bannerContainer =
                                    new FrameLayout(activity);

                            bannerContainer.setBackgroundColor(
                                    Color.TRANSPARENT
                            );

                            FrameLayout.LayoutParams bannerParams =
                                    new FrameLayout.LayoutParams(
                                            FrameLayout.LayoutParams.WRAP_CONTENT,
                                            FrameLayout.LayoutParams.WRAP_CONTENT
                                    );

                            bannerParams.gravity =
                                    Gravity.CENTER_HORIZONTAL
                                    | Gravity.BOTTOM;

                            bannerContainer.addView(
                                    bannerView,
                                    bannerParams
                            );

                            FrameLayout root =
                                    activity.findViewById(
                                            android.R.id.content
                                    );

                            if (root == null) {

                                BannerFailed(
                                        "ROOT_VIEW_ERROR",
                                        "Unable to find Android content view."
                                );

                                return;
                            }

                            root.addView(
                                    bannerContainer,
                                    new FrameLayout.LayoutParams(
                                            FrameLayout.LayoutParams.MATCH_PARENT,
                                            FrameLayout.LayoutParams.WRAP_CONTENT,
                                            Gravity.BOTTOM
                                    )
                            );

                            bannerView.load();

                            AdDebug(
                                    "Unity banner loading..."
                            );

                        } catch (Exception e) {

                            bannerLoaded = false;

                            String message;

                            if (e.getMessage() != null) {
                                message = e.getMessage();
                            } else {
                                message = e.toString();
                            }

                            BannerFailed(
                                    "EXCEPTION",
                                    message
                            );
                        }
                    }
                }
        );
    }


    @SimpleFunction(
            description = "Show the loaded Unity banner."
    )
    public void ShowBanner() {

        activity.runOnUiThread(
                new Runnable() {

                    @Override
                    public void run() {

                        try {

                            if (bannerView == null) {

                                BannerFailed(
                                        "NOT_CREATED",
                                        "Banner has not been created. Call LoadBanner first."
                                );

                                return;
                            }

                            if (!bannerLoaded) {

                                BannerFailed(
                                        "NOT_LOADED",
                                        "Banner is not loaded yet."
                                );

                                return;
                            }

                            bannerView.setVisibility(
                                    View.VISIBLE
                            );

                            if (bannerContainer != null) {

                                bannerContainer.setVisibility(
                                        View.VISIBLE
                                );
                            }

                            AdDebug(
                                    "Unity banner shown."
                            );

                        } catch (Exception e) {

                            String message;

                            if (e.getMessage() != null) {
                                message = e.getMessage();
                            } else {
                                message = e.toString();
                            }

                            BannerFailed(
                                    "EXCEPTION",
                                    message
                            );
                        }
                    }
                }
        );
    }


    @SimpleFunction(
            description = "Hide the Unity banner without destroying it."
    )
    public void HideBanner() {

        activity.runOnUiThread(
                new Runnable() {

                    @Override
                    public void run() {

                        try {

                            if (bannerView != null) {

                                bannerView.setVisibility(
                                        View.GONE
                                );
                            }

                            if (bannerContainer != null) {

                                bannerContainer.setVisibility(
                                        View.GONE
                                );
                            }

                            AdDebug(
                                    "Unity banner hidden."
                            );

                        } catch (Exception e) {

                            String message;

                            if (e.getMessage() != null) {
                                message = e.getMessage();
                            } else {
                                message = e.toString();
                            }

                            AdDebug(
                                    "HideBanner error: "
                                    + message
                            );
                        }
                    }
                }
        );
    }


    @SimpleFunction(
            description = "Destroy the Unity banner and release its resources."
    )
    public void DestroyBanner() {

        activity.runOnUiThread(
                new Runnable() {

                    @Override
                    public void run() {

                        try {

                            destroyBannerInternal();

                            AdDebug(
                                    "Unity banner destroyed."
                            );

                        } catch (Exception e) {

                            String message;

                            if (e.getMessage() != null) {
                                message = e.getMessage();
                            } else {
                                message = e.toString();
                            }

                            AdDebug(
                                    "DestroyBanner error: "
                                    + message
                            );
                        }
                    }
                }
        );
    }


    private void destroyBannerInternal() {

        bannerLoaded = false;

        if (bannerView != null) {

            try {
                bannerView.destroy();
            } catch (Exception ignored) {
            }

            bannerView = null;
        }

        if (bannerContainer != null) {

            try {

                FrameLayout root =
                        activity.findViewById(
                                android.R.id.content
                        );

                if (root != null) {

                    root.removeView(
                            bannerContainer
                    );
                }

            } catch (Exception ignored) {
            }

            bannerContainer = null;
        }
    }


    @SimpleEvent(
            description = "Raised when Unity Ads initialization succeeds."
    )
    public void Initialized() {

        EventDispatcher.dispatchEvent(
                this,
                "Initialized"
        );
    }


    @SimpleEvent(
            description = "Raised when Unity Ads initialization fails."
    )
    public void InitializationFailed(
            String error,
            String message) {

        EventDispatcher.dispatchEvent(
                this,
                "InitializationFailed",
                error,
                message
        );
    }


    @SimpleEvent(
            description = "Raised when the Unity banner is loaded."
    )
    public void BannerLoaded() {

        EventDispatcher.dispatchEvent(
                this,
                "BannerLoaded"
        );
    }


    @SimpleEvent(
            description = "Raised when the Unity banner fails to load."
    )
    public void BannerFailed(
            String error,
            String message) {

        EventDispatcher.dispatchEvent(
                this,
                "BannerFailed",
                error,
                message
        );
    }


    @SimpleEvent(
            description = "Raised when the Unity banner is clicked."
    )
    public void BannerClicked() {

        EventDispatcher.dispatchEvent(
                this,
                "BannerClicked"
        );
    }


    @SimpleEvent(
            description = "Debug information from the Unity Ads extension."
    )
    public void AdDebug(
            String message) {

        EventDispatcher.dispatchEvent(
                this,
                "AdDebug",
                message
        );
    }
}
