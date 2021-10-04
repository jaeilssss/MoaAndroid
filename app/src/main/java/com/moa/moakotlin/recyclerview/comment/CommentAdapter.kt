package com.moa.moakotlin.recyclerview.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Comment
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.ItemClaimCommentBinding
import com.moa.moakotlin.repository.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import android.view.View
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Partner

class CommentAdapter :ListAdapter<Comment ,CommentAdapter.CommentViewHolder>(diffUtil) {

    private var mListener : OnItemClickListener?=null

    fun setOnItemClickListener(mListener: OnItemClickListener){
        this.mListener = mListener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CommentViewHolder {

        return CommentViewHolder(ItemClaimCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    inner class CommentViewHolder(var binding: ItemClaimCommentBinding) : RecyclerView.ViewHolder(binding.root){

        fun binding(comment: Comment){

            CoroutineScope(Dispatchers.Main).launch {
                binding.itemCommentContent.text = comment.content
                binding.commentDelete.setOnClickListener(ButtonClick())
                val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm")
                binding.itemCommentDate.text = dateFormat.format(comment.timeStamp.toDate())
                var repository = FirebaseRepository<User>()
                if(comment.uid!=User.getInstance().uid){
                    if(comment.isAuthorPartner){
                        binding.commentDelete.isVisible = false
                        var result = repository.getDocumentList<Partner>(
                                FirebaseFirestore.getInstance()
                                        .collection("Partner")
                                        .whereEqualTo("aptCode",User.getInstance().aptCode)
                                        .get()
                        )
                        if(result.size!=0){
                            var commenter = result.get(0)
                            binding.itemClaimCommentNickName.text = commenter.nickName
                            Glide.with(binding.root).load(commenter.profileImage).into(binding.itemClaimCommentProfileImage)
                        }
                    }else{
                        binding.commentDelete.isVisible = false

                        var result = repository.getDocument<User>(
                                FirebaseFirestore.getInstance()
                                        .collection("User")
                                        .document(comment.uid)
                                        .get()
                        )
                        if(result.size!=0){
                            var commenter = result.get(0)
                            binding.itemClaimCommentNickName.text = commenter.nickName
                            Glide.with(binding.root).load(commenter.profileImage).into(binding.itemClaimCommentProfileImage)
                        }
                    }

                }else{
                    binding.itemClaimCommentNickName.text = User.getInstance().nickName
                    Glide.with(binding.root).load(User.getInstance().profileImage).into(binding.itemClaimCommentProfileImage)

                }

            }
        }

        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                when(v?.id){
                    R.id.commentDelete ->{
                        if(mListener!=null){
                            if(adapterPosition != RecyclerView.NO_POSITION){
                                mListener!!.onItemClick(v,adapterPosition)
                            }
                        }
                    }
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Comment>(){
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

        }
    }

}