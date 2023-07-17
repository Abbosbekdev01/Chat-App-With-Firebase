package uz.abbosbek.chatapp1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.abbosbek.chatapp1.databinding.ItemRvBinding
import uz.abbosbek.chatapp1.models.User


class UserAdapter(var list:ArrayList<User> = ArrayList()): RecyclerView.Adapter<UserAdapter.Vh>() {

    inner class Vh(val itemRvBinding: ItemRvBinding):RecyclerView.ViewHolder(itemRvBinding.root){

        fun onBind(user: User){
            itemRvBinding.itemText.text = user.name
            Picasso.get().load(user.imageLink).into(itemRvBinding.itemImage)
//            itemRvBinding.itemImage.
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }
}