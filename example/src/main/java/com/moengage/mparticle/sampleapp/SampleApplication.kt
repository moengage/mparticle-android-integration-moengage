/*
 * Copyright (c) 2014-2024 MoEngage Inc.
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

package com.moengage.mparticle.sampleapp

import android.app.Application
import com.moengage.core.DataCenter
import com.moengage.core.LogLevel
import com.moengage.core.MoEngage
import com.moengage.core.config.LogConfig
import com.moengage.core.config.NotificationConfig
import com.moengage.core.model.IntegrationPartner
import com.moengage.mparticle.kits.MoEngageKit
import com.mparticle.MParticle
import com.mparticle.MParticleOptions
import com.mparticle.kits.KitOptions

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        configureMoEngageSdk()
        initializeMParticleSdk()
    }

    private fun initializeMParticleSdk() {
        val options = MParticleOptions.builder(this)
            .credentials(M_PARTICLE_API_KEY, M_PARTICLE_API_SECRET)
            .environment(MParticle.Environment.Development)
            .logLevel(MParticle.LogLevel.VERBOSE)
            .configuration(KitOptions().addKit(MOENGAGE_KIT_ID, MoEngageKit::class.java))
            .build()

        MParticle.start(options)

        // Register for push notifications
        // MParticle.getInstance()?.Messaging()?.enablePushNotifications(FCM_SENDER_ID)

        // disable push notifications
        // MParticle.getInstance()?.Messaging()?.disablePushNotifications()
    }

    private fun configureMoEngageSdk() {
        val moEngageBuilder = MoEngage.Builder(this, MOENGAGE_APP_ID, DataCenter.DATA_CENTER_1)
            .configureLogs(LogConfig(LogLevel.VERBOSE, true))
            .configureNotificationMetaData(
                NotificationConfig(
                    R.drawable.small_icon,
                    R.drawable.large_icon,
                    R.color.purple_200,
                    isMultipleNotificationInDrawerEnabled = true,
                    isBuildingBackStackEnabled = false,
                    isLargeIconDisplayEnabled = true
                )
            )

        // Configure the required configuration for MoEngage SDK
        MoEngage.configureForDefaultInstance(moEngageBuilder, IntegrationPartner.M_PARTICLE)
    }
}