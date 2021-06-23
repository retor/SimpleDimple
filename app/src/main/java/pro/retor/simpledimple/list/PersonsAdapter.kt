package pro.retor.simpledimple.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pro.retor.simpledimple.R
import pro.retor.simpledimple.databinding.HeaderCardListBinding
import pro.retor.simpledimple.databinding.PersonCardListBinding
import pro.retor.simpledimple.items.HeaderItem
import pro.retor.simpledimple.items.Item
import pro.retor.simpledimple.items.PersonItem

class PersonsAdapter(val list: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val HEADER_TYPE = 1
    private val PERSON_TYPE = 0

    inner class HeaderHolder(val binding: HeaderCardListBinding) : RecyclerView.ViewHolder(binding.root)
    inner class PersonHolder(val binding: PersonCardListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) HEADER_TYPE else PERSON_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER_TYPE) {
            HeaderHolder(HeaderCardListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            PersonHolder(PersonCardListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)
        if (itemViewType == HEADER_TYPE) {
            with(holder as HeaderHolder) {
                with(list[position] as HeaderItem) {
                    binding.headerCount.text = itemView.resources.getString(R.string.header_list_count, count)
                }
            }
        } else {
            with(holder as PersonHolder) {
                with(list[position] as PersonItem) {
                    binding.personCity.text = city
                    binding.personName.text = title
                    binding.personDescription.text = about
                    Glide.with(binding.personPhoto).load(pic)
                        .circleCrop()
                        .placeholder(R.drawable.ic_photo_stub)
                        .into(binding.personPhoto)
                }
            }
        }
    }

    override fun getItemCount() = list.count()
}