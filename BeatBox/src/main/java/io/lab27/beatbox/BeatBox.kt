package io.lab27.beatbox

import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException
import java.lang.Exception

private const val TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"
private const val MAX_SOUNDS: Int = 5

class BeatBox(private val assets: AssetManager) {
    val sounds: List<Sound>

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_SOUNDS)
        .build()

    init {
        sounds = loadSounds()
    }

    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>

        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!
            Log.d(TAG, "Found ${soundNames.size} sounds")
        } catch (e: Exception) {
            Log.e(TAG, "Could not list assets", e)
            return emptyList()
        }

        val sounds = mutableListOf<Sound>()

        soundNames.forEach { fileName ->
            val assetPath = "$SOUNDS_FOLDER/$fileName"
            val sound = Sound(assetPath)
//            sounds.add(Sound(assetPath))
            try{
                load(sound)
                sounds.add(sound)
            }catch (ioe : IOException){
                Log.e(TAG, "Could not load sound $fileName", ioe)
            }
        }

        return sounds
    }

    private fun load(sound: Sound) {
        val afd = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd, 1)
        sound.soundId = soundId
    }

    fun play( sound : Sound){
        sound.soundId?.let{
            soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun release(){
        soundPool.release()
    }
}