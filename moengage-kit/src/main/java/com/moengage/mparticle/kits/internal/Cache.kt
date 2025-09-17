package com.moengage.mparticle.kits.internal

import com.mparticle.MParticle.IdentityType

/**
 * Cache object for [com.moengage.mparticle.kits.MoEngageKit]
 * @author Abhishek Kumar
 */
internal object Cache {

    /**
     * Default identity mapping
     */
    private var identityMapping = mutableMapOf(
        IdentityType.Alias to "uid",
        IdentityType.CustomerId to "uid",
        IdentityType.Email to "u_em",
        IdentityType.MobileNumber to "u_mb"
    )

    /**
     * Update the identity mapping with the provided mapping
     */
    fun setIdentityMapping(mapping: Map<IdentityType, String>) {
        synchronized(this) {
            identityMapping.putAll(mapping)
        }
    }

    /**
     * Return the mapped value for the provided [IdentityType]
     */
    fun getIdentityForKey(key: IdentityType): String? {
        synchronized(this) {
            return identityMapping[key]
        }
    }
}