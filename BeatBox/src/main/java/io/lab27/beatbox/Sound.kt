package io.lab27.beatbox

import androidx.lifecycle.MutableLiveData

private const val WAV = ".wav"

class Sound(val assetPath: String, var soundId: Int? = null) {
    val name  = assetPath.split("/").last().removeSuffix(WAV)
}