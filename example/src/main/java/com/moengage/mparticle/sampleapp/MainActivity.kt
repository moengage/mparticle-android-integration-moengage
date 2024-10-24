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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.moengage.core.internal.utils.showToast
import com.moengage.core.isSdkInitialised
import com.moengage.inapp.MoEInAppHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<AppCompatButton>(R.id.loginButton).setOnClickListener {
            MParticleRequestHandler.logInUser()
        }
        findViewById<AppCompatButton>(R.id.enableDataTrackingButton).setOnClickListener {
            MParticleRequestHandler.enableDataTracking()
        }
        findViewById<AppCompatButton>(R.id.disableDataTrackingButton).setOnClickListener {
            MParticleRequestHandler.disableDataTracking()
        }
        findViewById<AppCompatButton>(R.id.locationButton).setOnClickListener {
            MParticleRequestHandler.trackUserLocation()
        }
        findViewById<AppCompatButton>(R.id.installReferrerButton).setOnClickListener {
            MParticleRequestHandler.trackInstallReferrerEvent()
        }
        findViewById<AppCompatButton>(R.id.customEventButton).setOnClickListener {
            MParticleRequestHandler.trackCustomEvent()
        }
        findViewById<AppCompatButton>(R.id.commerceEventButton).setOnClickListener {
            MParticleRequestHandler.trackCommerceEvent()
        }
        findViewById<AppCompatButton>(R.id.userAttributeButton).setOnClickListener {
            MParticleRequestHandler.trackUserAttributes()
        }
        findViewById<AppCompatButton>(R.id.modifyButton).setOnClickListener {
            MParticleRequestHandler.modifyUserAttributes()
        }
        findViewById<AppCompatButton>(R.id.logoutButton).setOnClickListener {
            MParticleRequestHandler.logoutUser()
        }
        findViewById<AppCompatButton>(R.id.showInApp).setOnClickListener {
            if (isSdkInitialised(MOENGAGE_APP_ID)) {
                MoEInAppHelper.getInstance().showInApp(this)
            } else {
                showToast(this, "MoEngage SDK is not initialised!")
            }
        }
    }
}