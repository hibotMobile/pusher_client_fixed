package com.github.chinloyal.pusher_client.pusher.listeners

import com.github.chinloyal.pusher_client.core.utils.Constants
import com.github.chinloyal.pusher_client.pusher.PusherService
import com.pusher.client.channel.PresenceChannelEventListener
import com.pusher.client.channel.PusherEvent
import com.pusher.client.channel.User
import java.lang.Exception
import android.util.Log

class FlutterPresenceChannelEventListener : FlutterBaseChannelEventListener(),
    PresenceChannelEventListener {
    companion object {
        val instance = FlutterPresenceChannelEventListener()
    }

    override fun onUsersInformationReceived(channelName: String, users: MutableSet<User>) {
        Log.d(
            "Pusher Client onUsersInformationReceived",
            "onUsersInformationReceived: channelName=$channelName, userId=${users}"
        )
        this.onEvent(
            PusherEvent(
                mapOf(
                    "event" to Constants.SUBSCRIPTION_SUCCEEDED.value,
                    "channel" to channelName,
                    "user_id" to null,
                    "data" to users.toString()
                )
            )
        )
    }

    override fun userUnsubscribed(channelName: String, user: User) {
        if (user != null) {
            Log.d(
                "Pusher Client userUnsubscribed",
                "userUnsubscribed: channelName=$channelName, userId=${user.id}"
            )
            if (user.id != null) {
                this.onEvent(
                    PusherEvent(
                        mapOf(
                            "event" to Constants.MEMBER_REMOVED.value,
                            "channel" to channelName,
                            "user_id" to user.id,
                            "data" to null
                        )
                    )
                )
            } else {
                Log.d("Error Pusher Client userUnsubscribed", "userUnsubscribed: user es nulo")
            }
        } else {
            Log.d("Error Pusher Client userUnsubscribed", "userUnsubscribed: user es nulo")
        }

    }

    override fun userSubscribed(channelName: String, user: User) {
        Log.d(
            "Pusher Client userSubscribed",
            "userSubscribed: channelName=$channelName, userId=${user}"
        )
        this.onEvent(
            PusherEvent(
                mapOf(
                    "event" to Constants.MEMBER_ADDED.value,
                    "channel" to channelName,
                    "user_id" to user.id,
                    "data" to null
                )
            )
        )
    }

    override fun onAuthenticationFailure(message: String, e: Exception) {
        Log.d(
            "Pusher Client onAuthenticationFailure",
            "onAuthenticationFailure: message=$message, e=${e}"
        )
        PusherService.errorLog(message)
        if (PusherService.enableLogging) e.printStackTrace()
    }

    override fun onSubscriptionSucceeded(channelName: String) {
        Log.d(
            "Pusher Client onSubscriptionSucceeded",
            "onSubscriptionSucceeded: channelName=$channelName"
        )
        PusherService.debugLog("[PRESENCE] Subscribed: $channelName")

        this.onEvent(
            PusherEvent(
                mapOf(
                    "event" to Constants.SUBSCRIPTION_SUCCEEDED.value,
                    "channel" to channelName,
                    "user_id" to null,
                    "data" to null
                )
            )
        )
    }
}