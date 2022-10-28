package com.ly.core_model

import kotlinx.serialization.Serializable

@Serializable
data class RememberInfo(val account: String, val pwd: String)