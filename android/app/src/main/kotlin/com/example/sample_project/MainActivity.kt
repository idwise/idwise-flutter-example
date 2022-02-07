package com.example.sample_project

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

import android.util.Log

import com.idwise.sdk.IDWiseSDKCallback
import com.idwise.sdk.data.models.IDWiseSDKError
import com.idwise.sdk.data.models.JourneyInfo
import com.idwise.sdk.IDWise

class MainActivity : FlutterActivity() {

    val CHANNEL = "com.idwise.fluttersampleproject/idwise"
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->

            when (call.method) {
                "initialize" -> {
                    IDWise.initialize("<YOUR_CLIENT_KEY>") { error ->
                        result.error("ERROR", error!!.message, null)
                    }
                }

                "startJourney" -> {
                    IDWise.startJourney(
                        this,
                        "<YOUR_JOURNEY_DEFINITION_ID>",
                        "",
                        "en",
                        callback = object : IDWiseSDKCallback {
                            override fun onJourneyStarted(journeyInfo: JourneyInfo) {
                                Log.d("IDWiseSDKCallback", "onJourneyStarted")
                            }

                            override fun onJourneyCompleted(
                                journeyInfo: JourneyInfo,
                                isSucceeded: Boolean
                            ) {
                                Log.d("IDWiseSDKCallback", "onJourneyCompleted")
                            }

                            override fun onJourneyCancelled(journeyInfo: JourneyInfo?) {
                                Log.d("IDWiseSDKCallback", "onJourneyCancelled")
                            }

                            override fun onError(error: IDWiseSDKError) {
                                Log.d(
                                    "IDWiseSDKCallback",
                                    "onError ${error.message}"
                                )
                            }
                        }
                    )

                }

                else -> result.error("NO_SUCH_METHOD", "NO SUCH METHOD", null)
            }
            // Note: this method is invoked on the main thread.
            // TODO
        }
    }
}
