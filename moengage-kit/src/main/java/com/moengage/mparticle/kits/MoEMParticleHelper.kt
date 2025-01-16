package com.moengage.mparticle.kits

import com.moengage.mparticle.kits.internal.Cache
import com.mparticle.MParticle.IdentityType

public object MoEMParticleHelper {

    public fun setMappingForIdentity(mapping: Map<IdentityType, String>) {
        Cache.identityMapping = mapping
    }
}