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

package com.mparticle.kits.mocks

import com.mparticle.MParticle
import com.mparticle.UserAttributeListenerType
import com.mparticle.consent.ConsentState
import com.mparticle.identity.MParticleUser
import com.mparticle.kits.M_PARTICLE_ID

/**
 * Mock [MParticleUser] Class
 */
class MockMParticleUser(
    private val identities: Map<MParticle.IdentityType, String>
) : MParticleUser {

    override fun getId(): Long = M_PARTICLE_ID

    override fun getUserAttributes(): Map<String, Any> = mapOf()

    override fun getUserAttributes(p0: UserAttributeListenerType?): MutableMap<String, Any>? = null

    override fun setUserAttributes(map: Map<String, Any>): Boolean = false

    override fun getUserIdentities(): Map<MParticle.IdentityType, String> = identities

    override fun setUserAttribute(s: String, o: Any): Boolean = false

    override fun setUserAttributeList(s: String, o: Any): Boolean = false

    override fun incrementUserAttribute(p0: String, p1: Number?): Boolean = false

    override fun removeUserAttribute(s: String): Boolean = false

    override fun setUserTag(s: String): Boolean = false

    override fun getConsentState(): ConsentState = ConsentState.withConsentState("").build()

    override fun setConsentState(consentState: ConsentState?) {}

    override fun isLoggedIn(): Boolean = false

    override fun getFirstSeenTime(): Long = 0

    override fun getLastSeenTime(): Long = 0
}