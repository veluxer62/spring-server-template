package com.example.template

import io.kotest.core.config.AbstractProjectConfig

class KotestConfiguration : AbstractProjectConfig() {
    override val globalAssertSoftly: Boolean = true
}
