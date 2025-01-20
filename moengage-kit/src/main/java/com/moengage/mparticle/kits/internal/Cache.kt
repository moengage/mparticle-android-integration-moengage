package com.moengage.mparticle.kits.internal

import com.mparticle.MParticle

internal object Cache {

    private var identityMapping: Map<MParticle.IdentityType, String>? = null

    fun setIdentityMapping(mapping: Map<MParticle.IdentityType, String>?) {
        synchronized(this) {
            identityMapping = mapping
        }
    }

    fun getIdentityForKey(key: MParticle.IdentityType): String? {
        synchronized(this) {
            return identityMapping?.get(key)
        }
    }
}