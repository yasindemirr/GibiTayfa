package com.demir.gibitayfa.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.demir.gibitayfa.R
import com.demir.gibitayfa.databinding.RecordItemBinding
import com.demir.gibitayfa.model.gibiModel

class GibiAdapter():RecyclerView.Adapter<GibiAdapter.GibiHolder>() {
    class GibiHolder(val binding: RecordItemBinding,val context: Context):RecyclerView.ViewHolder(binding.root)
    var mediaPlayer: MediaPlayer? = null
    private var playingPosition = -1

    private val diffUtil=object :DiffUtil.ItemCallback<gibiModel>(){
        override fun areItemsTheSame(oldItem: gibiModel, newItem: gibiModel): Boolean {
          return  oldItem.user==newItem.user
        }

        override fun areContentsTheSame(oldItem: gibiModel, newItem: gibiModel): Boolean {
            return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GibiHolder {
        val view=RecordItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GibiHolder(view,parent.context)
    }

    override fun onBindViewHolder(holder: GibiHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.recorText.text = item.title
        if (holder.adapterPosition == playingPosition) {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    holder.binding.play.setImageResource(R.drawable.ic_baseline_pause_24)
                    holder.binding.item.setBackgroundResource(R.drawable.record_item_back_active)
                    holder.binding.play.setColorFilter(ContextCompat.getColor(holder.context,
                        R.color.active), PorterDuff.Mode.SRC_IN)
                    holder.binding.share.setColorFilter(ContextCompat.getColor(holder.context,
                        R.color.active), PorterDuff.Mode.SRC_IN)
                    holder.binding.recorText.setTextColor(ContextCompat.getColor(holder.context,
                        R.color.active))
                } else {
                    holder.binding.play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    holder.binding.item.setBackgroundResource(R.drawable.record_item_back)
                    holder.binding.play.setColorFilter(ContextCompat.getColor(holder.context,
                        R.color.black), PorterDuff.Mode.SRC_IN)
                    holder.binding.share.setColorFilter(ContextCompat.getColor(holder.context,
                        R.color.black), PorterDuff.Mode.SRC_IN)
                    holder.binding.recorText.setTextColor(ContextCompat.getColor(holder.context,
                        R.color.black))
                }
            }
        } else {
            holder.binding.play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            holder.binding.item.setBackgroundResource(R.drawable.record_item_back)
            holder.binding.play.setColorFilter(ContextCompat.getColor(holder.context,
                R.color.black), PorterDuff.Mode.SRC_IN)
            holder.binding.share.setColorFilter(ContextCompat.getColor(holder.context,
                R.color.black), PorterDuff.Mode.SRC_IN)
            holder.binding.recorText.setTextColor(ContextCompat.getColor(holder.context,
                R.color.black))
        }

        holder.binding.play.setOnClickListener {

            try {
                mediaPlayer?.stop()
                mediaPlayer = null

                if (holder.adapterPosition == playingPosition) {
                    playingPosition = -1
                    holder.binding.play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    holder.binding.item.setBackgroundResource(R.drawable.record_item_back)
                    holder.binding.play.setColorFilter(ContextCompat.getColor(holder.context, R.color.black), PorterDuff.Mode.SRC_IN)
                    holder.binding.share.setColorFilter(ContextCompat.getColor(holder.context, R.color.black), PorterDuff.Mode.SRC_IN)
                    holder.binding.recorText.setTextColor(ContextCompat.getColor(holder.context, R.color.black))
                    return@setOnClickListener
                }

                mediaPlayer = MediaPlayer().apply {
                    setDataSource(item.url)
                    prepare()
                    setOnPreparedListener {
                        start()
                        playingPosition = holder.adapterPosition
                        notifyDataSetChanged()
                    }
                    setOnCompletionListener {
                        playingPosition = -1
                        notifyDataSetChanged()
                    }
                }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        holder.binding.share.setOnClickListener {
            onlickShare?.let {
                it.invoke(item)
            }
        }
        }
    var onlickShare:((gibiModel)-> Unit)? = null
    var onClickPlay:((gibiModel)-> Unit)? = null


    override fun getItemCount(): Int {
       return differ.currentList.size
    }
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDetachedFromRecyclerView(recyclerView)
    }

}