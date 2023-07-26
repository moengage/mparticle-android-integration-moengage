/*
 * Copyright (c) 2014-2023 MoEngage Inc.
 *
 * All rights reserved.
 *
 *  Use of source code or binaries contained within MoEngage SDK is permitted only to enable use of the MoEngage platform by customers of MoEngage.
 *  Modification of source code and inclusion in mobile apps is explicitly allowed provided that all other conditions are met.
 *  Neither the name of MoEngage nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *  Redistribution of source code or binaries is disallowed except with specific prior written permission. Any such redistribution must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.moengage.mparticle.kits

import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.Location
import com.moengage.core.LogLevel
import com.moengage.core.MoECoreHelper
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.disableDataTracking
import com.moengage.core.enableDataTracking
import com.moengage.core.exceptions.SdkNotInitializedException
import com.moengage.core.internal.INSTALL_REFERRER_EVENT
import com.moengage.core.internal.SdkInstanceManager
import com.moengage.core.internal.USER_ATTRIBUTE_USER_FIRST_NAME
import com.moengage.core.internal.USER_ATTRIBUTE_USER_GENDER
import com.moengage.core.internal.USER_ATTRIBUTE_USER_LAST_NAME
import com.moengage.core.internal.USER_ATTRIBUTE_USER_MOBILE
import com.moengage.core.internal.integrations.MoEIntegrationHelper
import com.moengage.core.internal.logger.Logger
import com.moengage.core.internal.model.IntegrationMeta
import com.moengage.core.internal.model.SdkInstance
import com.moengage.core.internal.utils.currentMillis
import com.moengage.core.model.IntegrationPartner
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.pushbase.MoEPushHelper
import com.mparticle.MPEvent
import com.mparticle.MParticle.IdentityType
import com.mparticle.MParticle.UserAttributes.FIRSTNAME
import com.mparticle.MParticle.UserAttributes.GENDER
import com.mparticle.MParticle.UserAttributes.LASTNAME
import com.mparticle.MParticle.UserAttributes.MOBILE_NUMBER
import com.mparticle.consent.ConsentState
import com.mparticle.identity.MParticleUser
import com.mparticle.kits.FilteredIdentityApiRequest
import com.mparticle.kits.FilteredMParticleUser
import com.mparticle.kits.KitIntegration
import com.mparticle.kits.KitIntegration.EventListener
import com.mparticle.kits.KitIntegration.IdentityListener
import com.mparticle.kits.KitIntegration.PushListener
import com.mparticle.kits.KitIntegration.UserAttributeListener
import com.mparticle.kits.KitUtils
import com.mparticle.kits.ReportingMessage
import kotlin.jvm.Throws

/**
 * MoEngage Kit to integrate MoEngage Android SDK with mParticle Android SDK
 */
open class MoEngageKit :
    KitIntegration(), IdentityListener, UserAttributeListener, EventListener, PushListener {

    private val tag = "MoEngageKit_${BuildConfig.MOENGAGE_KIT_VERSION}"

    private lateinit var integrationHelper: MoEIntegrationHelper
    private lateinit var appId: String
    private lateinit var sdkInstance: SdkInstance

    override fun getName(): String = KIT_NAME

    public override fun onKitCreate(
        settings: MutableMap<String, String?>,
        context: Context
    ): MutableList<ReportingMessage> {
        Logger.print { "$tag onKitCreate(): " }
        val appId = settings[MOE_APP_ID_KEY]
        require(appId != null && !KitUtils.isEmpty(appId)) { "MoEngage App Id can't be empty" }
        integrationHelper = MoEIntegrationHelper(context, IntegrationPartner.M_PARTICLE)
        integrationHelper.initialize(appId, context.applicationContext as Application)
        MoEIntegrationHelper.addIntegrationMeta(
            IntegrationMeta(
                INTEGRATION_META_TYPE,
                BuildConfig.MOENGAGE_KIT_VERSION
            ),
            appId
        )

        this.appId = appId
        this.sdkInstance = getSdkInstance(appId)
        Logger.print { "$tag onKitCreate(): mParticle Integration Initialised for $appId" }

        return mutableListOf(
            ReportingMessage(
                this,
                ReportingMessage.MessageType.APP_STATE_TRANSITION,
                currentMillis(),
                null
            )
        )
    }

    override fun onIdentifyCompleted(
        mParticleUser: MParticleUser,
        identityApiRequest: FilteredIdentityApiRequest?
    ) {
        try {
            sdkInstance.logger.log { "$tag onIdentifyCompleted(): " }
            updateUserIds(false, mParticleUser)
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag onIdentifyCompleted(): " }
        }
    }

    override fun onLoginCompleted(
        mParticleUser: MParticleUser,
        identityApiRequest: FilteredIdentityApiRequest?
    ) {
        try {
            sdkInstance.logger.log { "$tag onLoginCompleted(): " }
            updateUserIds(false, mParticleUser)
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag onLoginCompleted(): " }
        }
    }

    override fun onModifyCompleted(
        mParticleUser: MParticleUser,
        identityApiRequest: FilteredIdentityApiRequest?
    ) {
        try {
            sdkInstance.logger.log { "$tag onModifyCompleted(): " }
            updateUserIds(true, mParticleUser)
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag onModifyCompleted(): " }
        }
    }

    private fun updateUserIds(isUserModified: Boolean, mParticleUser: MParticleUser) {
        try {
            sdkInstance.logger.log { "$tag updateUserIds(): isUserModified = $isUserModified" }

            mParticleUser.userIdentities[IdentityType.Email]?.let { email ->
                MoEAnalyticsHelper.setEmailId(
                    context,
                    email,
                    appId
                )
            }

            mParticleUser.userIdentities[IdentityType.MobileNumber]?.let { mobileNumber ->
                MoEAnalyticsHelper.setMobileNumber(
                    context,
                    mobileNumber,
                    appId
                )
            }

            mParticleUser.userIdentities[IdentityType.CustomerId]?.let { id ->
                Logger.print { "$tag updateUserIds(): isUserModified-$isUserModified, UniqueId-$id" }
                if (isUserModified) {
                    MoEAnalyticsHelper.setAlias(context, id, appId)
                } else {
                    MoEAnalyticsHelper.setUniqueId(context, id, appId)
                }
            }
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag updateUserIds(): " }
        }
    }

    override fun onLogoutCompleted(
        mParticleUser: MParticleUser,
        identityApiRequest: FilteredIdentityApiRequest?
    ) {
        try {
            sdkInstance.logger.log { "$tag onLogoutCompleted(): " }
            MoECoreHelper.logoutUser(context, appId)
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag onLogoutCompleted(): " }
        }
    }

    override fun onUserIdentified(mParticleUser: MParticleUser) {
        try {
            sdkInstance.logger.log { "$tag onUserIdentified(): mParticle Id: ${mParticleUser.id}" }
            integrationHelper.trackAnonymousId(mParticleUser.id.toString(), appId)
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag onUserIdentified(): " }
        }
    }

    override fun setLocation(location: Location) {
        try {
            sdkInstance.logger.log { "$tag setLocation(): " }
            MoEAnalyticsHelper.setLocation(
                context,
                location.latitude,
                location.longitude,
                appId
            )
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag setLocation(): " }
        }
    }

    override fun setOptOut(optedOut: Boolean): MutableList<ReportingMessage> {
        try {
            sdkInstance.logger.log { "$tag setOptOut(): is tracking opted in-$optedOut" }
            if (optedOut) {
                disableDataTracking(context, appId)
            } else {
                enableDataTracking(context, appId)
            }

            return mutableListOf(
                ReportingMessage(
                    this,
                    ReportingMessage.MessageType.OPT_OUT,
                    currentMillis(),
                    null
                )
            )
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag setOptOut(): " }
        }

        return mutableListOf()
    }

    override fun setInstallReferrer(intent: Intent) {
        try {
            sdkInstance.logger.log { "$tag setInstallReferrer(): data = ${intent.dataString}" }
            val properties = Properties()
            properties.addAttribute(REFERRER_EXTRA, intent.dataString)
            properties.setNonInteractive()
            MoEAnalyticsHelper.trackEvent(
                context,
                INSTALL_REFERRER_EVENT,
                properties,
                appId
            )
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag setInstallReferrer(): " }
        }
    }

    override fun onIncrementUserAttribute(
        key: String,
        incrementedBy: Number,
        value: String,
        user: FilteredMParticleUser
    ) {
    }

    override fun onRemoveUserAttribute(key: String, user: FilteredMParticleUser) {}

    override fun onSetUserTag(key: String, user: FilteredMParticleUser) {}

    override fun onConsentStateUpdated(
        oldState: ConsentState,
        newState: ConsentState,
        user: FilteredMParticleUser
    ) {
    }

    override fun onSetUserAttribute(
        attributeKey: String,
        attributeValue: Any,
        user: FilteredMParticleUser
    ) {
        trackUserAttribute(attributeKey, attributeValue)
    }

    override fun onSetUserAttributeList(
        attributeKey: String,
        attributeValueList: List<String>,
        user: FilteredMParticleUser
    ) {
        trackUserAttribute(attributeKey, attributeValueList.toTypedArray())
    }

    override fun onSetAllUserAttributes(
        userAttributes: Map<String, String>,
        userAttributeLists: Map<String, List<String>>,
        user: FilteredMParticleUser
    ) {
        try {
            sdkInstance.logger.log { "$tag onSetAllUserAttributes(): " }
            if (!kitPreferences.getBoolean(PREF_KEY_HAS_SYNCED_ATTRIBUTES, false)) {
                for ((attributeKey, attributeValue) in userAttributes) {
                    trackUserAttribute(attributeKey, attributeValue)
                }
                for ((attributeKey, attributeValue) in userAttributeLists) {
                    trackUserAttribute(attributeKey, attributeValue.toTypedArray())
                }
                kitPreferences.edit().putBoolean(PREF_KEY_HAS_SYNCED_ATTRIBUTES, true).apply()
            }
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag onSetAllUserAttributes(): " }
        }
    }

    override fun supportsAttributeLists(): Boolean = true

    private fun trackUserAttribute(attributeKey: String, attributeValue: Any) {
        try {
            sdkInstance.logger.log { "$tag trackUserAttribute(): Key-$attributeKey, Value-$attributeValue" }
            var mappedKey = attributeKeyMap[attributeKey] ?: attributeKey

            // All the mParticle standard attribute starts with "$".
            // Removing the "$" from attribute as it's not required in MoEngage
            if (mappedKey.startsWith("$")) {
                mappedKey = mappedKey.substring(1)
            }
            MoEAnalyticsHelper.setUserAttribute(context, mappedKey, attributeValue, appId)
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag trackUserAttribute(): " }
        }
    }

    override fun leaveBreadcrumb(breadcrumb: String): List<ReportingMessage> = emptyList()

    override fun logError(
        message: String,
        errorAttributes: MutableMap<String, String>
    ): List<ReportingMessage> = emptyList()

    override fun logException(
        exception: Exception,
        exceptionAttributes: MutableMap<String, String>,
        message: String
    ): List<ReportingMessage> = emptyList()

    override fun logScreen(
        screenName: String,
        screenAttributes: MutableMap<String, String>
    ): List<ReportingMessage> = emptyList()

    override fun logEvent(event: MPEvent): MutableList<ReportingMessage> {
        try {
            sdkInstance.logger.log { "$tag logEvent(): " }
            if (event.eventName.isBlank()) {
                sdkInstance.logger.log(LogLevel.WARN) { "$tag logEvent(): Event name can't be empty" }
                return mutableListOf()
            }

            val properties = Properties()
            event.customAttributeStrings?.let { customAttributes ->
                for ((customAttributesKey, customAttributesValue) in customAttributes) {
                    properties.addAttribute(customAttributesKey, customAttributesValue)
                }
            }
            MoEAnalyticsHelper.trackEvent(context, event.eventName, properties, appId)

            return mutableListOf(ReportingMessage.fromEvent(this, event))
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag onKitCreate(): mParticle Integration Initialisation Failed" }
        }

        return mutableListOf()
    }

    override fun onPushRegistration(instanceId: String, senderId: String): Boolean {
        try {
            sdkInstance.logger.log { "$tag onPushRegistration(): instanceId = $instanceId " }
            MoEFireBaseHelper.getInstance().passPushToken(context, instanceId, appId)
            return true
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag onPushRegistration(): " }
        }
        return false
    }

    override fun willHandlePushMessage(intent: Intent): Boolean {
        try {
            sdkInstance.logger.log { "$tag willHandlePushMessage():" }
            return intent.extras?.let { bundle ->
                sdkInstance.logger.log { "$tag willHandlePushMessage(): checking if message is from MoEngage" }
                MoEPushHelper.getInstance().isFromMoEngagePlatform(bundle)
            } ?: false
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag willHandlePushMessage(): " }
        }
        return false
    }

    override fun onPushMessageReceived(context: Context, pushIntent: Intent) {
        try {
            sdkInstance.logger.log { "$tag onPushMessageReceived():" }
            pushIntent.extras?.let { bundle ->
                sdkInstance.logger.log { "$tag onPushMessageReceived(): Processing message" }
                MoEFireBaseHelper.getInstance().passPushPayload(context, bundle)
            }
        } catch (t: Throwable) {
            Logger.print(LogLevel.ERROR, t) { "$tag onPushMessageReceived(): " }
        }
    }

    @Throws(SdkNotInitializedException::class)
    open fun getSdkInstance(appId: String): SdkInstance {
        return SdkInstanceManager.getSdkInstance(appId)
            ?: throw SdkNotInitializedException()
    }

    companion object {

        private val attributeKeyMap: Map<String, String> = mapOf(
            MOBILE_NUMBER to USER_ATTRIBUTE_USER_MOBILE,
            GENDER to USER_ATTRIBUTE_USER_GENDER,
            FIRSTNAME to USER_ATTRIBUTE_USER_FIRST_NAME,
            LASTNAME to USER_ATTRIBUTE_USER_LAST_NAME
        )
    }
}