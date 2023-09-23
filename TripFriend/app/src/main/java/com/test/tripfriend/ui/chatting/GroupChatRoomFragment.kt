package com.test.tripfriend.ui.chatting

import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentGroupChatRoomBinding
import com.test.tripfriend.databinding.RowChatRoomUserBinding
import com.test.tripfriend.databinding.RowGroupChatRoomBinding
import com.test.tripfriend.dataclassmodel.GroupChatting
import com.test.tripfriend.dataclassmodel.PersonalChatting
import com.test.tripfriend.repository.GroupChatRepository
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.viewmodel.ChattingViewModel
import de.hdodenhof.circleimageview.CircleImageView
import java.util.Calendar

class GroupChatRoomFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentGroupChatRoomBinding: FragmentGroupChatRoomBinding
    lateinit var groupChatViewModel: ChattingViewModel
    lateinit var memberInfoMap: MutableList<MutableMap<String, String>>
    var groupChatRepository = GroupChatRepository()
    var memberList = mutableListOf<String>()
    var memberimage = mutableListOf<String>()

    lateinit var displayMetrics: DisplayMetrics


    lateinit var tripPostId: String
    lateinit var roomId: String
    lateinit var postTitle: String
    lateinit var roomOwnerEmail: String

    lateinit var MY_EMAIL: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        mainActivity = activity as MainActivity
        fragmentGroupChatRoomBinding = FragmentGroupChatRoomBinding.inflate(layoutInflater)
        MY_EMAIL = mainActivity.userClass.userEmail

        //그룹방 아이디와 해당 방의 동행글 정보 가져오기
        roomId = arguments?.getString("groupRoomId").toString()
        tripPostId = arguments?.getString("postId").toString()
        postTitle = arguments?.getString("postTitle").toString()
        roomOwnerEmail = arguments?.getString("roomOwnerEmail").toString()

        groupChatViewModel = ViewModelProvider(this)[ChattingViewModel::class.java]
        groupChatViewModel.run {
            groupChattingList.observe(viewLifecycleOwner) {
                (fragmentGroupChatRoomBinding.recyclerViewGroupChatRoom.adapter as GroupChatRoomAdapter).updateItemList(
                    it
                )
            }

            groupUserInfoMapList.observe(viewLifecycleOwner) {
                memberInfoMap = it
//                데이터 가져오기
                groupChatViewModel.groupChattingChangeListener(roomId)

                memberInfoMap[0].forEach { (key, value) ->
                    memberList.add(value)
                }
                memberInfoMap[1].forEach { (key, value) ->
                    memberimage.add(value)
                }

                (fragmentGroupChatRoomBinding.recyclerViewGroupChatRoomParticipants.adapter as ParticipantsAdapter).updateItemList(
                    memberList
                )
                //햄버거에서 내 사진
                fragmentGroupChatRoomBinding.imageViewGroupChatRoom
                if (memberInfoMap[1].get(mainActivity.userClass.userEmail) != "null") {
                    Glide.with(mainActivity).load(
                        memberInfoMap[1].get(mainActivity.userClass.userEmail)!!.toUri()
                    ).into(fragmentGroupChatRoomBinding.imageViewGroupChatRoom)
                } else {
                    fragmentGroupChatRoomBinding.imageViewGroupChatRoom.setImageResource(R.drawable.person_24px)
                }


            }
            if (::tripPostId.isInitialized) {
                getUserDataInGroupChat(tripPostId)
            }
            if (::postTitle.isInitialized) {
                fragmentGroupChatRoomBinding.textViewSidePostTitle.text = postTitle
            }
            if (::roomOwnerEmail.isInitialized) {
                if (mainActivity.userClass.userEmail == roomOwnerEmail) {
                    fragmentGroupChatRoomBinding.buttonGroupChatRoomExit.visibility = View.GONE
                }
            }


        }

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        // 기기의 화면 너비 구하기
        displayMetrics = resources.displayMetrics

        fragmentGroupChatRoomBinding.run {

            materialGroupChatRoomToolbar.run {
                // 백버튼
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.GROUP_CHAT_ROOM_FRAGMENT)
                }

                textViewGroupChatRoomToolbarTitle.text = postTitle

                // 메뉴 버튼
                setOnMenuItemClickListener {
                    if (it.itemId == R.id.item_chat_menu)
                        drawerLayoutGroupChatRoom.openDrawer(Gravity.RIGHT)
                    true
                }

                // 참가자 리사이클러 뷰
                recyclerViewGroupChatRoomParticipants.run {
                    adapter = ParticipantsAdapter()
                    layoutManager = LinearLayoutManager(mainActivity)
                }

                // 채팅 리사이클러 뷰
                recyclerViewGroupChatRoom.run {
                    adapter = GroupChatRoomAdapter()
                    val manager = LinearLayoutManager(mainActivity)
                    manager.stackFromEnd = true
                    layoutManager = manager
                }

                //햄버거에서 내 이름
                textViewGroupChattingUserName.text = "${mainActivity.userClass.userNickname}"


                // 나가기 버튼
                buttonGroupChatRoomExit.run {
                    setOnClickListener {
                        //다이얼로그 띄움
                        MaterialAlertDialogBuilder(mainActivity, R.style.DialogTheme).run {
                            setTitle("채팅방 나가기")
                            setMessage("나가기를 하면 대화내용이 모두 삭제되며 동행 신청이 취소되고 채팅 목록에서도 삭제됩니다.")
                            setNegativeButton("취소", null)
                            setPositiveButton("나가기") { dialogInterface: DialogInterface, i: Int ->
                                //여기서 메시지를 남기고 떠나야함.
                                val calendar = Calendar.getInstance()
                                val year = calendar.get(Calendar.YEAR)
                                val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 +1
                                val day = calendar.get(Calendar.DAY_OF_MONTH)
                                val calendar2 = Calendar.getInstance()
                                val hour = calendar2.get(Calendar.HOUR_OF_DAY) // 24시간 형식
                                val minute = calendar2.get(Calendar.MINUTE)
                                val second = calendar2.get(Calendar.SECOND)

                                //타임 스탬프
                                val groupChatSendTimeStamp =
                                    ("$year${month.toString().padStart(2, '0')}${
                                        day.toString().padStart(2, '0')
                                    }${hour.toString().padStart(2, '0')}${
                                        minute.toString().padStart(2, '0')
                                    }${second.toString().padStart(2, '0')}").toLong()

                                //저장할 데이터 생성
                                val outChatting = GroupChatting(
                                    MY_EMAIL,
                                    "${mainActivity.userClass.userNickname}",
                                    "",
                                    groupChatSendTimeStamp,
                                    true
                                )
                                if (roomId != null) {
                                    groupChatRepository.saveMyContentToDB(roomId, outChatting)
                                }

                                //동행글에서 멤버에 자기 자신 삭제하고 채팅방 자기자신 삭제
                                mainActivity.removeFragment(MainActivity.GROUP_CHAT_ROOM_FRAGMENT)
                                groupChatViewModel.outMemberFromChatRoom(
                                    mainActivity.userClass.userNickname,
                                    roomId,
                                    tripPostId
                                )

                            }
                            show()
                        }
                    }
                }
            }

            // 입력창의 최대 높이 설정 (기기 세로 사이즈의 1/3)
            val oneThirdScreenHeight = displayMetrics.heightPixels / 3
            textInputEditTextGroupChatRoomSearch.maxHeight = oneThirdScreenHeight

            buttonGroupChatRoomSend.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 +1
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val currentDate = "${year.toString().padStart(2, '0')}-${
                    month.toString().padStart(2, '0')
                }-${day.toString().padStart(2, '0')}"

                val calendar2 = Calendar.getInstance()
                val hour = calendar2.get(Calendar.HOUR_OF_DAY) // 24시간 형식
                val minute = calendar2.get(Calendar.MINUTE)
                val second = calendar2.get(Calendar.SECOND)

                val currentTime =
                    "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

                //채팅 내용
                val groupChatContent = textInputEditTextGroupChatRoomSearch.text.toString()

                //전송 시기
                val groupChatSendDateAndTime = "${currentDate} / ${currentTime}"

                //타임 스탬프
                val groupChatSendTimeStamp = ("$year${month.toString().padStart(2, '0')}${
                    day.toString().padStart(2, '0')
                }${hour.toString().padStart(2, '0')}${
                    minute.toString().padStart(2, '0')
                }${second.toString().padStart(2, '0')}").toLong()

                //저장할 데이터 생성
                val groupChatting = GroupChatting(
                    MY_EMAIL,
                    groupChatContent,
                    groupChatSendDateAndTime,
                    groupChatSendTimeStamp
                )

                //문서 아이디가 null이 아니라면 채팅 저장
                if (roomId != null) {
                    groupChatRepository.saveMyContentToDB(roomId, groupChatting)
                } else {
                }
                textInputEditTextGroupChatRoomSearch.setText("")
            }
        }

        return fragmentGroupChatRoomBinding.root
    }


    // GroupChatRoom 어댑터
    inner class GroupChatRoomAdapter :
        RecyclerView.Adapter<GroupChatRoomAdapter.GroupChatRoomViewHolder>() {

        //보여줄 데이터 정보
        private var itemList = mutableListOf<GroupChatting>()

        //데이터를 가져와서 업데이트하기 위한 메서드
        fun updateItemList(newList: MutableList<GroupChatting>) {
            //변경된 사항이 날아오므로 add를 수행
            this.itemList.addAll(newList)

            notifyDataSetChanged() // 갱신

            //업데이트시 스크롤 맨 아래로
            fragmentGroupChatRoomBinding.recyclerViewGroupChatRoom.scrollToPosition(itemList.size - 1)
        }


        inner class GroupChatRoomViewHolder(rowChatRoomUserBinding: RowChatRoomUserBinding) :
            RecyclerView.ViewHolder(rowChatRoomUserBinding.root) {
            val textViewRowChatRoomUser: TextView
            val textViewChatMoment: TextView

            val imageViewOpponent: CircleImageView
            val textViewOpponentName: TextView
            val textViewOpponentContent: TextView
            val textViewOpponentChatMoment: TextView
            val textViewOutNotification: TextView


            init {
                //내 채팅
                textViewRowChatRoomUser = rowChatRoomUserBinding.textViewRowChatRoomUser
                //내 채팅 시간대
                textViewChatMoment = rowChatRoomUserBinding.textViewMyChatMoment
                //상대방 이미지
                imageViewOpponent = rowChatRoomUserBinding.imageViewRowChatRoomOpponent
                //상대방 이름
                textViewOpponentName = rowChatRoomUserBinding.textViewRowChatRoomOpponentName
                //상대방 채팅
                textViewOpponentContent = rowChatRoomUserBinding.textViewRowChatRoomOpponent
                //상대방 채팅 시간대
                textViewOpponentChatMoment = rowChatRoomUserBinding.textViewOpponentChatMoment
                //상대방 나갔을 때 나오는 텍스트뷰
                textViewOutNotification = rowChatRoomUserBinding.textViewOutNotification

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChatRoomViewHolder {
            val rowChatRoomUserBinding = RowChatRoomUserBinding.inflate(layoutInflater)

            rowChatRoomUserBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return GroupChatRoomViewHolder(rowChatRoomUserBinding)
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(holder: GroupChatRoomViewHolder, position: Int) {

            // 리사이클러뷰 아이템의 최대 너비 설정 (기기 가로 사이즈의 절반)
            val halfScreenWidth = displayMetrics.widthPixels / 2
            // 최대 너비 설정
            holder.textViewRowChatRoomUser.maxWidth = halfScreenWidth
            holder.textViewOpponentContent.maxWidth = halfScreenWidth

            if (itemList[position].outToken) {
                holder.textViewOutNotification.visibility = View.VISIBLE
                holder.textViewRowChatRoomUser.visibility = View.GONE
                holder.textViewChatMoment.visibility = View.GONE
                holder.imageViewOpponent.visibility = View.GONE
                holder.textViewOpponentName.visibility = View.GONE
                holder.textViewOpponentContent.visibility = View.GONE
                holder.textViewOpponentChatMoment.visibility = View.GONE

                holder.textViewOutNotification.text =
                    itemList[position].groupChatContent + "님이 동행 멤버에서 나가셨습니다😭"
            } else {
                //받아온 데이터가 내가 보낸 게 아니라면
                if (itemList[position].groupChatWriterEmail != MY_EMAIL) {
                    holder.textViewRowChatRoomUser.visibility = View.GONE
                    holder.textViewChatMoment.visibility = View.GONE
                    holder.textViewOutNotification.visibility = View.GONE

                    holder.imageViewOpponent.visibility = View.VISIBLE
                    holder.textViewOpponentName.visibility = View.VISIBLE
                    holder.textViewOpponentContent.visibility = View.VISIBLE
                    holder.textViewOpponentChatMoment.visibility = View.VISIBLE
//
                    //이미지 설정
                    if (memberInfoMap[1].get(itemList[position].groupChatWriterEmail) != "null" && memberInfoMap[1].get(itemList[position].groupChatWriterEmail) != null && memberInfoMap[1].get(itemList[position].groupChatWriterEmail) != "") {
                        Glide.with(mainActivity).load(
                            memberInfoMap[1].get(itemList[position].groupChatWriterEmail)!!.toUri()
                        )
                            .into(holder.imageViewOpponent)
                    } else {
                        holder.imageViewOpponent.setImageResource(R.drawable.person_24px)
                    }
                    //이름
                    if(memberInfoMap[0].get(itemList[position].groupChatWriterEmail) !=null && memberInfoMap[0].get(itemList[position].groupChatWriterEmail) !="null" && memberInfoMap[0].get(itemList[position].groupChatWriterEmail) != ""){
                        holder.textViewOpponentName.text =
                            memberInfoMap[0].get(itemList[position].groupChatWriterEmail)
                    }else{
                        holder.textViewOpponentName.text = "알 수 없음"
                    }


                    holder.textViewOpponentContent.text = itemList[position].groupChatContent
                    holder.textViewOpponentChatMoment.text =
                        itemList[position].groupChatSendDateAndTime
                    //받아온 데이터가 내가 보낸 거라면
                } else {
                    holder.textViewRowChatRoomUser.visibility = View.VISIBLE
                    holder.textViewChatMoment.visibility = View.VISIBLE
                    holder.textViewRowChatRoomUser.text = itemList[position].groupChatContent
                    holder.textViewChatMoment.text = itemList[position].groupChatSendDateAndTime

                    holder.imageViewOpponent.visibility = View.GONE
                    holder.textViewOpponentName.visibility = View.GONE
                    holder.textViewOpponentContent.visibility = View.GONE
                    holder.textViewOpponentChatMoment.visibility = View.GONE
                    holder.textViewOutNotification.visibility = View.GONE
                }
            }
        }
    }

    inner class ParticipantsAdapter :
        RecyclerView.Adapter<ParticipantsAdapter.ParticipantsViewHolder>() {
        //보여줄 데이터 정보
        private var itemList = mutableListOf<String>()

        //데이터를 가져와서 업데이트하기 위한 메서드
        fun updateItemList(newList: MutableList<String>) {
            //변경된 사항이 날아오므로 add를 수행
            this.itemList = newList

            notifyDataSetChanged() // 갱신
        }

        inner class ParticipantsViewHolder(rowGroupChatRoomBinding: RowGroupChatRoomBinding) :
            RecyclerView.ViewHolder(rowGroupChatRoomBinding.root) {
            val imageViewGroupChatRoomImage: ImageView
            val textViewGroupChatRoomName: TextView

            init {
                imageViewGroupChatRoomImage = rowGroupChatRoomBinding.imageViewGroupChatRoomImage
                textViewGroupChatRoomName = rowGroupChatRoomBinding.textViewGroupChatRoomName
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsViewHolder {
            val rowGroupChatRoomBinding = RowGroupChatRoomBinding.inflate(layoutInflater)

            rowGroupChatRoomBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return ParticipantsViewHolder(rowGroupChatRoomBinding)
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(holder: ParticipantsViewHolder, position: Int) {
            if (mainActivity.userClass.userNickname == itemList[position]) {
                holder.textViewGroupChatRoomName.visibility = View.GONE
                holder.imageViewGroupChatRoomImage.visibility = View.GONE
            } else {
                holder.textViewGroupChatRoomName.text = itemList[position]
                //이미지가 없어서 이런느낌으로
                if (memberimage[position] != "null") {
                    Glide.with(mainActivity).load(memberimage[position].toUri())
                        .into(holder.imageViewGroupChatRoomImage)
                } else {
                    holder.imageViewGroupChatRoomImage.setImageResource(R.drawable.person_24px)
                }
            }

        }
    }


    override fun onStop() {
        super.onStop()
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE
    }

}