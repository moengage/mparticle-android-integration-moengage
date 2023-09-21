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

plugins {
    `maven-publish`
    signing
}

val mavenSnapshotUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
val mavenCentralUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")

val libVersionName = project.findProperty("VERSION_NAME") as String
val group = project.findProperty("GROUP") as String
val artifactName = project.findProperty("ARTIFACT_NAME") as String

val pomName = project.findProperty("NAME") as String
val pomDescription = project.findProperty("DESCRIPTION") as String
val projectUrl = project.findProperty("POM_URL") as String

val licenseName = project.findProperty("LICENCE_NAME") as String
val licenseUrl = project.findProperty("LICENCE_URL") as String

val developerId = project.findProperty("DEVELOPER_ID") as String
val developerName = project.findProperty("DEVELOPER_NAME") as String

val scmConnection = project.findProperty("SCM_CONNECTION") as String
val scmDevConnection = project.findProperty("SCM_DEV_CONNECTION") as String

val repositoryUsername = project.findProperty("mavenCentralUsername") as? String ?: ""
val repositoryPassword = project.findProperty("mavenCentralPassword") as? String ?: ""

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = group
            artifactId = artifactName
            version = libVersionName
            afterEvaluate {
                from(components["release"])
            }
            pom {
                name.set(pomName)
                description.set(pomDescription)
                url.set(projectUrl)
                licenses {
                    license {
                        name.set(licenseName)
                        url.set(licenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(developerId)
                        name.set(developerName)
                    }
                }
                scm {
                    connection.set(scmConnection)
                    developerConnection.set(scmDevConnection)
                    url.set(projectUrl)
                }
            }
        }
        repositories {
            maven {
                credentials {
                    username = repositoryUsername
                    password = repositoryPassword
                }
                url = when {
                    libVersionName.endsWith("-SNAPSHOT") -> {
                        mavenSnapshotUrl
                    }
                    else -> {
                        mavenCentralUrl
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["release"])
}