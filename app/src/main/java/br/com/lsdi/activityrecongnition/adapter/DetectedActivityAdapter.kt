package br.com.lsdi.activityrecongnition.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.lsdi.activityrecongnition.R
import com.google.android.gms.location.DetectedActivity

class DetectedActivityAdapter (private val detecteActivities: List<DetectedActivity>) :
    RecyclerView.Adapter<DetectedActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detected_activity, parent, false);

        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtActivity.text = detecteActivities[position].toString()
            .replace("DetectedActivity","")
    }

    override fun getItemCount(): Int {
        return detecteActivities.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtActivity: TextView

        init {
            txtActivity = itemView.findViewById(R.id.txt_value)
        }
    }




}