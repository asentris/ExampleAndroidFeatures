package com.asentris.exampleandroidfeatures.core.presentation.main

class FragEntryVM {

    val features: List<FeatureList> = listOf(
        FeatureList.BroadcastReceiver,
        FeatureList.ContentProvider,
        FeatureList.Intent,
        FeatureList.Service,
    )
}

sealed class FeatureList {
    object BroadcastReceiver : FeatureList()
    object ContentProvider : FeatureList()
    object Intent : FeatureList()
    object Service : FeatureList()

    override fun toString(): String = this::class.java.simpleName
}