package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.NoticeFragmentBinding

class NoticeFragment : Fragment() {

    companion object {
        fun newInstance() = NoticeFragment()
    }

    private lateinit var viewModel: NoticeViewModel

    private lateinit var binding : NoticeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.notice_fragment, container , false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoticeViewModel::class.java)

        binding.NoticeTextView.text = "<p>이 글은 공지 테스트용으로 작성되었습니다. 수정테스트</p><p><br></p><h1>헤딩1</h1><h2>헤딩2</h2><p><br></p><p><strong>BOLD</strong></p><p><em>ITALIC</em></p><p><u>UNDERLINE</u></p><p><s>STRIKE</s></p><p><br></p><blockquote>우리 아파트도 따듯해질수 있어요</blockquote><p><br></p><p><span style=\"color: rgb(255, 228, 2);\">메인</span> <span style=\"color: rgb(246, 139, 123);\">테마</span> <span style=\"color: rgb(255, 187, 1);\">색</span></p><p><br></p><ol><li>리스트</li><li>리스트2</li><li>리스트3</li></ol><p><br></p><p><a href=\"moduapt.com\" rel=\"noopener noreferrer\" target=\"_blank\">모두의아파트 링크</a></p><p><br></p><p>이미지 업로드 테스트</p><p><br></p><p><img src=\"https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/Notification%2Fme1zaurtxro5l7j6zg9fzf?alt=media&amp;token=00376e86-ccbb-40bc-bb53-7f43934074e0\"></p><p><br></p><p>비디오 링크 테스트</p><p><br></p><iframe class=\"ql-video\" frameborder=\"0\" allowfullscreen=\"true\" src=\"https://www.youtube.com/embed/g6F-hJ3K9RU?showinfo=0\"></iframe><p><br></p><p>공지끝~</p>"
    }

}