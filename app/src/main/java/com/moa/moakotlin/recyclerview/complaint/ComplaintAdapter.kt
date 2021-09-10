package com.moa.moakotlin.recyclerview.complaint


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.ItemClaimBinding
import com.moa.moakotlin.databinding.ItemConciergeBinding
import com.moa.moakotlin.repository.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.view.View
import com.moa.moakotlin.R

class ComplaintAdapter : ListAdapter<Complaint, ComplaintAdapter.complaintViewHolder>(diffUtil){

    private var mListener : OnItemClickListener?=null

    var defaultUrl = "https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/CONCIERGE_DEFAULT_200x200.png?alt=media&token=8ba33ee5-1b36-4d39-b400-6af648439187"

    fun setOnItemClickListener(mListener: OnItemClickListener){
        this.mListener = mListener

    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintAdapter.complaintViewHolder {
        return complaintViewHolder(ItemClaimBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ComplaintAdapter.complaintViewHolder, position: Int) {
      holder.binding(currentList[position])
    }

    inner class complaintViewHolder(var binding : ItemClaimBinding ) : RecyclerView.ViewHolder(binding.root){

        fun binding(complaint: Complaint){
            binding.itemClaimTitle.text = complaint.title
            binding.itemClaimCategory.text = complaint.category

            if(complaint.status.equals("requested")){
                binding.itemClaimStatus.text = "요청중"
            }else if(complaint.status.equals("inProgress")){
                binding.itemClaimStatus.text = "진행중"
            }else{
                binding.itemClaimStatus.text = "완료"
            }

            if(complaint.uid == User.getInstance().uid){
                binding.itemClaimNickName.text = User.getInstance().nickName
            }else{
                var repository = FirebaseRepository<User>()
                CoroutineScope(Dispatchers.Main).launch {
                    var result = repository.getDocument<User>(
                            FirebaseFirestore.getInstance()
                                    .collection("User")
                                    .document(complaint.uid)
                                    .get()
                    )
                    if(result.size>0){
                        var otherUser  : User =result.get(0)
                        binding.itemClaimNickName.text = otherUser.nickName
                    }
                }

            }

            if(complaint.images.size!=0){
                Glide.with(binding.root).load(complaint.images[0]).into(binding.itemClaimImage)
            }
            
            
            binding.itemClaimLayout.setOnClickListener(ButtonClick())
        }
        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                when(v?.id){
                    R.id.itemClaimLayout ->{
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
    
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Complaint>(){
            override fun areItemsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

        }
    }


}