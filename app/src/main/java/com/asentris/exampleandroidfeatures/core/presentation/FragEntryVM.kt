package com.asentris.exampleandroidfeatures.core.presentation

class FragEntryVM {

    val features: List<FeatureList> = listOf(
        FeatureList.BroadcastReceiver,
        FeatureList.ContentProvider,
    )
}

sealed class FeatureList {
    object BroadcastReceiver : FeatureList()
    object ContentProvider : FeatureList()

    override fun toString(): String = this::class.java.simpleName
}