package com.example.requestmanager

import android.os.Parcelable
import com.example.requestmanager.APIConstants.ID
import com.example.requestmanager.APIConstants.TITLE
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class JsonTitleServer(
    @SerializedName(TITLE) val title: String?
): Parcelable

@Parcelize
data class JsonIdServer(
    @SerializedName(ID) val id: String?
): Parcelable
