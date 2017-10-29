LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_PACKAGE_NAME := FileManager
LOCAL_CERTIFICATE := platform
LOCAL_PROGUARD_ENABLED := disabled
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_STATIC_JAVA_LIBRARIES := \
        android-support-annotations \
        android-support-v7-appcompat \
        android-support-v7-cardview \
        android-support-v7-recyclerview \
        android-support-v7-gridlayout \
        android-support-v4 \
        android-support-design \
        commons-compress \
        volley \
        junrar

LOCAL_RESOURCE_DIR := \
        $(LOCAL_PATH)/res \
        $(TOP)/frameworks/support/v7/appcompat/res \
        $(TOP)/frameworks/support/v7/cardview/res \
        $(TOP)/frameworks/support/v7/recyclerview/res \
        $(TOP)/frameworks/support/v7/gridlayout/res \
        $(TOP)/frameworks/support/design/res

LOCAL_AAPT_FLAGS := \
        --auto-add-overlay \
        --extra-packages android.support.v7.appcompat \
        --extra-packages android.support.v7.cardview \
        --extra-packages android.support.v7.recyclerview \
        --extra-packages android.support.v7.gridlayout \
        --extra-packages android.support.design

LOCAL_AAPT_INCLUDE_ALL_RESOURCES := true

include $(BUILD_PACKAGE)
include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
    commons-compress:libs/commons-compress/commons-compress-1.10.jar \
    junrar:libs/junrar/com.github.junrar-0.7.jar

include $(BUILD_MULTI_PREBUILT)
include $(call all-makefiles-under,$(LOCAL_PATH))

