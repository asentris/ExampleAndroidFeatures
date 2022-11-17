package com.asentris.exampleandroidfeatures.core.presentation

class FragEntryVM {

    val features: List<FeatureList> = listOf(
        FeatureList.BroadcastReceiver,
        FeatureList.ContentProvider,
        FeatureList.Intent,
    )
}

sealed class FeatureList {
    object BroadcastReceiver : FeatureList()
    object ContentProvider : FeatureList()
    object Intent : FeatureList()

    override fun toString(): String = this::class.java.simpleName
}