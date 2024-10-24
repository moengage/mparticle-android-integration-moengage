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

package com.mparticle.kits.mocks

import android.content.Context
import com.moengage.core.internal.initialisation.InitConfig
import com.moengage.core.internal.model.InstanceMeta
import com.moengage.core.internal.model.SdkInstance
import com.moengage.core.internal.remoteconfig.getDefaultRemoteConfig
import com.moengage.mparticle.kits.MoEngageKit
import com.mparticle.internal.CoreCallbacks
import com.mparticle.internal.ReportingManager
import com.mparticle.kits.KitConfiguration
import com.mparticle.kits.MoEngage_APP_ID
import org.mockito.Mockito

/**
 * Mock [MoEngageKit] Class
 */
class MockMoEngageKit : MoEngageKit() {

    init {
        kitManager = MockKitManagerImpl(
            Mockito.mock(Context::class.java),
            Mockito.mock(ReportingManager::class.java),
            Mockito.mock(CoreCallbacks::class.java)
        )
    }

    override fun getConfiguration(): KitConfiguration {
        return Mockito.mock(KitConfiguration::class.java)
    }

    override fun getSdkInstance(appId: String): SdkInstance {
        return SdkInstance(
            instanceMeta = InstanceMeta(MoEngage_APP_ID, true, true),
            initConfig = InitConfig(MoEngage_APP_ID),
            config = getDefaultRemoteConfig()
        )
    }
}