#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(GetFeedbackCapacitorPlugin, "GetFeedbackCapacitor",
           CAP_PLUGIN_METHOD(initialize, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(standardEvents, CAPPluginReturnCallback);
           CAP_PLUGIN_METHOD(loadFeedbackForm, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(sendEvent, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setDebugEnabled, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(loadLocalizedStringFile, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(setDataMasking, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(setCustomVariables, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(loadFeedbackFormWithCurrentViewScreenshot, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(preloadFeedbackForms, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(removeCachedForms, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(resetCampaignData, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(dismiss, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(getDefaultDataMasks, CAPPluginReturnPromise);
)
