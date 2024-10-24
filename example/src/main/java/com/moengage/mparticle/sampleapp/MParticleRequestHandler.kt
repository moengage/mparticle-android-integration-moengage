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

import android.location.Location
import android.util.Log
import com.moengage.core.internal.utils.currentMillis
import com.mparticle.MPEvent
import com.mparticle.MParticle
import com.mparticle.MParticle.EventType
import com.mparticle.commerce.CommerceEvent
import com.mparticle.commerce.Product
import com.mparticle.commerce.TransactionAttributes
import com.mparticle.identity.IdentityApiRequest

internal object MParticleRequestHandler {

    private const val TAG = "MParticleRequestHandler"

    fun logInUser() {
        val identityRequest = IdentityApiRequest.withEmptyUser().run {
            email("moengage@test.com")
            customerId("loginUserId")
            userIdentity(MParticle.IdentityType.Other, "moengage-mparticle-other-id-1")
            build()
        }

        MParticle.getInstance()?.Identity()
            ?.login(identityRequest)
            ?.addSuccessListener { identityApiResult ->
                Log.d(TAG, "identifyTask Id: ${identityApiResult.user.id}")
            }?.addFailureListener { identityHttpResponse ->
                Log.d(TAG, "identifyTask Error: ${identityHttpResponse?.errors}")
            }
    }

    fun enableDataTracking() {
        MParticle.getInstance()?.optOut = false
    }

    fun disableDataTracking() {
        MParticle.getInstance()?.optOut = true
    }

    fun trackUserLocation() {
        val location = Location("dummy")
        location.latitude = 70.90
        location.longitude = 89.00
        MParticle.getInstance()?.setLocation(location)
    }

    fun trackInstallReferrerEvent() {
        MParticle.getInstance()?.installReferrer = "CustomInstallReferrerString"
    }

    fun trackCustomEvent() {
        // Event With Properties
        val customAttributes = mapOf(
            "category" to "Destination Intro",
            "title" to "Paris"
        )

        val eventWithProperties = MPEvent.Builder("Custom Event With Prop", EventType.Navigation)
            .customAttributes(customAttributes)
            .build()

        MParticle.getInstance()?.logEvent(eventWithProperties)

        // Event Without Properties
        val eventWithoutProperties = MPEvent.Builder("Custom Event", EventType.Navigation)
            .build()

        MParticle.getInstance()?.logEvent(eventWithoutProperties)
    }

    fun trackCommerceEvent() {
        val commerceEvent = CommerceEvent.Builder(
            Product.ADD_TO_CART,
            Product.Builder("productName", "productSKU", 2.0)
                .customAttributes(mapOf("customAttrKey" to "customAttrValue"))
                .category("productCategory")
                .couponCode("productCouponCode")
                .position(20)
                .unitPrice(2.0)
                .quantity(5.0)
                .brand("productBrand")
                .variant("productVariant")
                .build()
        ).transactionAttributes(TransactionAttributes("customTransactionId_${currentMillis()}"))
            .currency("inr")
            .build()

        MParticle.getInstance()?.logEvent(commerceEvent)
    }

    fun trackUserAttributes() {
        val currentUser = MParticle.getInstance()?.Identity()?.currentUser

        // Track Normal Attributes
        currentUser?.setUserAttribute("top_region", "Europe")

        // Track Standard Attributes
        currentUser?.setUserAttribute("Age", 99)
        currentUser?.setUserAttribute("FirstName", "First")
        currentUser?.setUserAttribute("LastName", "Last")
        currentUser?.setUserAttribute("Gender", "Male")
        currentUser?.setUserAttribute("Mobile", 1234567890)
        currentUser?.setUserAttribute("Address", "Koramangala")
        currentUser?.setUserAttribute("City", "Bengaluru")
        currentUser?.setUserAttribute("State", "Karnataka")
        currentUser?.setUserAttribute("Zip", 5600031)
        currentUser?.setUserAttribute("Country", "India")

        // Track In List
        val attributeList = ArrayList<String>()
        attributeList.add("Rome")
        attributeList.add("San Juan")
        attributeList.add("Denver")
        currentUser?.setUserAttributeList("destinations", attributeList)
    }

    fun modifyUserAttributes() {
        val modifyRequest = IdentityApiRequest.withUser(MParticle.getInstance()?.Identity()?.currentUser)
            .email("moengageChangedEmail@test.com")
            .build()
        MParticle.getInstance()?.Identity()?.modify(modifyRequest)
            ?.addSuccessListener { identityApiResult ->
                Log.d(TAG, "MoEngage Id Changed ${identityApiResult.user.userAttributes}")
            }?.addFailureListener { identityHttpResponse ->
                Log.d(TAG, "MoEngage Id Changed Error ${identityHttpResponse?.errors}")
            }
    }

    fun logoutUser() {
        MParticle.getInstance()?.Identity()
            ?.logout(IdentityApiRequest.withEmptyUser().build())
            ?.addSuccessListener {
                Log.d(TAG, "Logout Completed")
            }?.addFailureListener {
                Log.d(TAG, "Logout Failed Error")
            }
    }
}