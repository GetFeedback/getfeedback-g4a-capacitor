import Foundation
import Capacitor
import Usabilla
import UIKit

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(GetFeedbackCapacitorPlugin)
public class GetFeedbackCapacitorPlugin: CAPPlugin {
    var passiveCallID: String?
    var campaignCallID: String?
    var standardEventsCallID: String?

    let getfeedback = Usabilla.self
    override public func load() {
        getfeedback.delegate = self
    }

    @objc weak var formNavigationController: UINavigationController?

    @objc func initialize(_ call: CAPPluginCall) {
        let appID = call.getString("appID") ?? ""
        getfeedback.initialize(appID: appID)
    }

    @objc func standardEvents(_ call: CAPPluginCall) {
        self.bridge?.saveCall(call)
        call.keepAlive = true
        standardEventsCallID = call.callbackId
    }

    @objc func loadFeedbackForm(_ call: CAPPluginCall) {
        let formID = call.getString("formID") ?? ""
        self.bridge?.saveCall(call)
        passiveCallID = call.callbackId
        getfeedback.loadFeedbackForm(formID)
    }

    @objc func loadFeedbackFormWithCurrentViewScreenshot(_ call: CAPPluginCall) {
        let formID = call.getString("formID") ?? ""
        self.bridge?.saveCall(call)
        passiveCallID = call.callbackId
        DispatchQueue.main.sync {
            if let rootVC = UIApplication.shared.windows.first?.rootViewController {
                let screenshot = self.takeScreenshot(view: rootVC.view)
                getfeedback.loadFeedbackForm(formID, screenshot: screenshot)
            }
        }
    }

    @objc func preloadFeedbackForms(_ call: CAPPluginCall) {
        guard let formIDs = call.options["formIDs"] as? [String] else {
            call.reject("Must provide formIDs")
            return
        }
        getfeedback.preloadFeedbackForms(withFormIDs: formIDs)
    }

    @objc func removeCachedForms(_ call: CAPPluginCall) {
        getfeedback.removeCachedForms()
    }

    @objc func sendEvent(_ call: CAPPluginCall) {
        let eventName = call.getString("eventName") ?? ""
        self.bridge?.saveCall(call)
        campaignCallID = call.callbackId
        getfeedback.sendEvent(event: eventName)
    }

    @objc func setCustomVariables(_ call: CAPPluginCall) {
        guard let variables = call.options["customVariables"] as? [String: String] else {
            call.reject("Must provide variables")
            print("ERROR : Expected customVariables as Dictionary of String [String : String]")
            return
        }
        getfeedback.customVariables = variables
    }

    @objc func setDebugEnabled(_ call: CAPPluginCall) {
        guard let debugEnabled = call.options["debugEnabled"] as? Bool else {
            call.reject("Must provide debugEnabled")
            return
        }
        getfeedback.debugEnabled = debugEnabled
    }

    @objc func loadLocalizedStringFile(_ call: CAPPluginCall) {
        let localizedStringFile = call.getString("localizedStringFile") ?? ""
        getfeedback.localizedStringFile = localizedStringFile
    }

    func takeScreenshot(view: UIView) -> UIImage {
        return getfeedback.takeScreenshot(view)!
    }

    @objc func resetCampaignData(_ call: CAPPluginCall) {
        getfeedback.resetCampaignData {
        }
    }

    @objc func dismiss(_ call: CAPPluginCall) {
        _ = getfeedback.dismiss()
    }

    @objc func getDefaultDataMasks(_ call: CAPPluginCall) {
        let str = getfeedback.defaultDataMasks
        call.resolve(["DEFAULT_DATA_MASKS": str])
    }

    @objc func setDataMasking(_ call: CAPPluginCall) {
        let masks = call.options["masks"] as? [String] ?? getfeedback.defaultDataMasks
        let maskChar = call.getString("character")?.first ?? "X"
        Usabilla.setDataMasking(masks: masks, maskCharacter: maskChar)
    }
}

extension GetFeedbackCapacitorPlugin: UsabillaDelegate {
    public func formDidLoad(form: UINavigationController) {
        formNavigationController = form
        if let rootVC = UIApplication.shared.keyWindow?.rootViewController {
            rootVC.present(formNavigationController!, animated: true, completion: nil)
        }
    }

    public func formDidFailLoading(error: UBError) {
        formNavigationController = nil
        if let callID = passiveCallID, let call = bridge?.savedCall(withID: callID) {
            call.reject("The form could not be loaded")
            bridge?.releaseCall(call)
        }
    }

    public func formDidClose(formID: String, withFeedbackResults results: [FeedbackResult], isRedirectToAppStoreEnabled: Bool) {
        var rnResults: [[String: Any]] = []
        for result in results {
            let dictionary: Dictionary = ["rating": result.rating ?? 0, "abandonedPageIndex": result.abandonedPageIndex ?? 0, "sent": result.sent] as [String: Any]
            rnResults.append(dictionary)
        }
        formNavigationController = nil

        if let callID = passiveCallID, let call = bridge?.savedCall(withID: callID) {
            call.resolve(["formId": formID, "results": rnResults, "isRedirectToAppStoreEnabled": isRedirectToAppStoreEnabled])
            bridge?.releaseCall(call)
        }
    }

    public func campaignDidClose(withFeedbackResult result: FeedbackResult, isRedirectToAppStoreEnabled: Bool) {
        let rnResult: [String: Any] = ["rating": result.rating ?? 0, "abandonedPageIndex": result.abandonedPageIndex ?? 0, "sent": result.sent] as [String: Any]
        formNavigationController = nil

        if let callID = campaignCallID, let call = bridge?.savedCall(withID: callID) {
            call.resolve(["result": rnResult, "isRedirectToAppStoreEnabled": isRedirectToAppStoreEnabled])
            bridge?.releaseCall(call)
            return
        }

        if let callEventID = standardEventsCallID, let callStandardEvent = bridge?.savedCall(withID: callEventID) {
            callStandardEvent.resolve(["result": rnResult, "isRedirectToAppStoreEnabled": isRedirectToAppStoreEnabled])
        }
    }
}
