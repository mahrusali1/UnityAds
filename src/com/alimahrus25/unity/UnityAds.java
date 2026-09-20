package com.alimahrus25.unity;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

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
}
