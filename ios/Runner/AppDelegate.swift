import UIKit
import Flutter
import IDWise

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    
  let methodChannelName = "com.idwise.fluttersampleproject/idwise"
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    GeneratedPluginRegistrant.register(with: self)
      
     // Native code bridging Swift -> Dart , calling iOS SDK here
      let controller : FlutterViewController = window?.rootViewController as! FlutterViewController
      let channel = FlutterMethodChannel(name: methodChannelName,
                                                binaryMessenger: controller.binaryMessenger)
      channel.setMethodCallHandler({ [self]
          (call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in
          
          switch call.method {
          case "initialize":
              IDWise.initialize(clientKey: "<YOUR_CLIENT_KEY>") { error in
               result("got some error")
              }

          case "startJourney":
              IDWise.startJourney(journeyDefinitionId: "<YOUR_JOURNEY_DEFINITION_ID>",locale: "en", delegate: self)
              result("successfully started journey")
          default:
              result(FlutterMethodNotImplemented)
          }
          
      })
      
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}





extension  AppDelegate: IDWiseSDKDelegate {
    
    func JourneyStarted(journeyID: String) {
        print("Journey started with Id : \(journeyID)")
    }
    
    func JourneyFinished() {
        print("Journey Finished")
    }
    
    func JourneyCancelled() {
        print("Journey Cancelled")
    }
    
    func onError(error: IDWiseSDKError) {
        print(error.message)
    }
    
}
