package com.example.flixsterplus.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(
    val id: Int,
    val name: String,
    val profile_path: String?,
    val known_for_department: String?
) : Parcelable
