import Foundation

@objc public class GetFeedbackCapacitor: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
