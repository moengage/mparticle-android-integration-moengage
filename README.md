![Logo](/.github/logo.png)

# MoEngage mParticle Android Kit

This repository contains the [MoEngage](https://www.moengage.com) integration for the [mParticle Android SDK](https://github.com/mParticle/mparticle-android-sdk).

## Repository Description

| Folder       | Description                                                            |
|--------------|------------------------------------------------------------------------|
| example      | Sample Application which integrates the MoEngage SDK and mParticle SDK |
| moengage-kit | MoEngage mParticle Android Kit                                         |

## SDK Installation

![MavenBadge](https://maven-badges.herokuapp.com/maven-central/com.moengage/mparticle-android-integration-moengage/badge.svg)

### mParticle Integration
Integrate [mParticle Android SDK](https://docs.mparticle.com/developers/sdk/android/initialization/)

### MoEngage Integration
Integrate [MoEngage Android SDK](https://developers.moengage.com/hc/en-us/sections/5338467172628-Basic-Integration)

### MoEngage Kit Integration
- Add the Required dependency in app level `build.gradle`
```groovy
implementation("com.mparticle:android-kit-base:5.51.3")
implementation("com.moengage:mparticle-android-integration-moengage:$kitVersion")
```
with `$kitVersion` replaced by the latest version of the mParticle MoEngage Kit.

- Initialize MoEngage SDK
```kotlin
val moEngage = MoEngage.Builder(this, "YOUR APP ID", [YOUR_DATA_CENTER])
    .enablePartnerIntegration(IntegrationPartner.M_PARTICLE)
    .build()

MoEngage.initialiseDefaultInstance(moEngage)
```

- Initialize the mParticle with MoEngage Kit
```kotlin
val options = MParticleOptions.builder(this)
    .credentials("YOU MPARTICLE API KEY", "YOUR MPARTICLE API SECRET")
    .configuration(KitOptions().addKit(MOENGAGE_KIT_ID, MoEngageKit::class.java))
    .build()

MParticle.start(options)
```

> Refer [**MoEngage Developer Document**](https://developers.moengage.com/hc/en-us/categories/360006147431-Android-SDK) to configure all MoEngage available features like Push Messaging, InApp, Cards.