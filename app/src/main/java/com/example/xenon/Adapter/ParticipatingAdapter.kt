import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.xenon.DataClass.ParticipateIIITS
import com.example.xenon.R
import kotlin.random.Random

class ParticipatingAdapter(
    private val iiits: MutableList<ParticipateIIITS>
) : RecyclerView.Adapter<ParticipatingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_iiit, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return iiits.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val IIIT = iiits[position]

        holder.name.text = IIIT.Name
        Glide.with(holder.itemView.context)
            .load(IIIT.logo)
            .thumbnail(0.1f)
            .error(R.drawable.group)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.logo)
        holder.itemView.setOnClickListener { itemView ->
                itemView.isClickable = false // Disable click on the clicked item

                val rotationDirection = if (Random.nextBoolean()) 1 else -1
                itemView.animate().rotationYBy(rotationDirection * 360f).setDuration(1000)
                    .withEndAction {
                        itemView.isClickable = true // Enable click once animation is completed
                    }.start()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.teamName)
        val logo: ImageView = itemView.findViewById(R.id.teamLogo)
    }
}