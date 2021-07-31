package com.moa.moakotlin.recyclerview.review


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.Review
import com.moa.moakotlin.databinding.ItemReviewBinding
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class ReviewAdapter() : ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(diffUtil) {
    var defaultUrl = "https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/CONCIERGE_DEFAULT_200x200.png?alt=media&token=8ba33ee5-1b36-4d39-b400-6af648439187"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(ItemReviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.binding(currentList[position])
    }
    inner class ReviewViewHolder(var binding : ItemReviewBinding) : RecyclerView.ViewHolder(binding.root){

        fun binding(review : Review){
            CoroutineScope(Dispatchers.Main).launch {
                var repository  = UserRepository()

                var user = repository.getUserInfo(review.uid)
                val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일")

                binding.itemReviewContent.text = review.review
                binding.itemReviewDate.text = dateFormat.format(review.timeStamp.toDate())


                if(user==null){
                    binding.itemReviewAptName.text = "알수없음"
                    binding.itemReviewUserNickName.text = "알수없음"
                    Glide.with(binding.root).load(defaultUrl).into(binding.itemReviewUserProfile)
                }else{
                    binding.itemReviewAptName.text = user?.aptName
                    Glide.with(binding.root).load(user?.profileImage).into(binding.itemReviewUserProfile)
                    binding.itemReviewUserNickName.text = user?.nickName
                }

            }


        }

    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Review>(){
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

        }
    }


}
