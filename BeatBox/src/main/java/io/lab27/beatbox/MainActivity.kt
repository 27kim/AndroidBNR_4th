package io.lab27.beatbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.lab27.beatbox.databinding.ActivityMainBinding
import io.lab27.beatbox.databinding.ListItemSoundBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var beatBox : BeatBox
//    val soundViewModel by lazy{
//        SoundViewModel()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        beatBox = BeatBox(this.assets)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = SoundAdapter(beatBox.sounds)
        }
    }

    private inner class SoundHolder(private val binding : ListItemSoundBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = SoundViewModel(beatBox)
        }

        fun onBind(sound: Sound) {
            binding.apply {
                viewModel?.sound = sound
                executePendingBindings()
            }
//            binding.viewModel = SoundViewModel()
//            binding.viewModel?.sound = sound
        }
    }

    private inner class SoundAdapter(private val sounds : List<Sound>) : RecyclerView.Adapter<SoundHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(layoutInflater, R.layout.list_item_sound, parent, false)
            binding.lifecycleOwner = this@MainActivity
            return SoundHolder(binding)
        }

        override fun getItemCount(): Int {
            return sounds.size
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            holder.onBind(sounds[position])
        }

    }

    override fun onDestroy() {
        beatBox.release()
        super.onDestroy()
    }
}