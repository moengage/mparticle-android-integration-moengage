package com.moengage.mparticle.kits

import android.app.Application
import com.moengage.mparticle.kits.internal.Cache
import com.mparticle.MParticle.IdentityType

/**
 * Helper class for setting up MoEngage specific configurations for mParticle<>MoEngage SDK.
 */
public object MoEMParticleHelper {

    /**
     * Set the readable [String] mapping for [IdentityType].
     *
     * Ensure this mapping set before identifying the user in the mParticle SDK. Possibly call this method in the onCreate() of  [Application] class.
     */
    public fun setMappingForIdentity(mapping: Map<IdentityType, String>?) {
        Cache.setIdentityMapping(mapping)
    }
}