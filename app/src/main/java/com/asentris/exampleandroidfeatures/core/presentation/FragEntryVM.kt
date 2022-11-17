package com.asentris.exampleandroidfeatures.core.presentation

class FragEntryVM {

    val features: List<FeatureList> = listOf(
        FeatureList.BroadcastReceiver,
    )
}

sealed class FeatureList {
    object BroadcastReceiver : FeatureList()

    override fun toString(): String = this::class.java.simpleName
}