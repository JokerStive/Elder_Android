package lilun.com.pension.ui.chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseActivity;
import lilun.com.pension.module.adapter.ChatContentAdapter;
import lilun.com.pension.module.bean.Conversation;
import lilun.com.pension.widget.NormalTitleBar;

/**
 * 聊天V
 *
 * @author yk
 *         create at 2017/3/7 11:52
 *         email : yk_developer@163.com
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.rv_participant)
    RecyclerView rvParticipant;

    @Bind(R.id.rv_chat_container)
    RecyclerView rvChatContainer;

    @Bind(R.id.tv_chat_sent)
    TextView tvChatSent;

    @Bind(R.id.et_chat_input)
    EditText etChatInput;
    @Bind(R.id.btn_createGgroup)
    Button btnCreateGgroup;
    @Bind(R.id.btn_addGroupMembers)
    Button btnAddGroupMembers;
    @Bind(R.id.btn_removeGroupMembers)
    Button btnRemoveGroupMembers;
    @Bind(R.id.btn_exitGroup)
    Button btnExitGroup;
    private ChatContentAdapter chatContentAdapter;
    private long mGroupId=-1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initView() {
        rvChatContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tvChatSent.setOnClickListener(this);
        btnCreateGgroup.setOnClickListener(this);
        btnAddGroupMembers.setOnClickListener(this);
        btnRemoveGroupMembers.setOnClickListener(this);
        btnExitGroup.setOnClickListener(this);

        ininData();
    }

    private void ininData() {
        List<Conversation> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Conversation conversation = new Conversation();
            conversation.setContent("我是聊天我我是聊天我我是聊天我我是聊天我我是聊天我" + i);
            conversation.setOwn(i % 2 == 0);
            data.add(conversation);
        }

        chatContentAdapter = new ChatContentAdapter(data);
        rvChatContainer.setAdapter(chatContentAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_chat_sent:
//                sendChat();
                break;
            case R.id.btn_createGgroup:
//                createGroup();
                break;
            case R.id.btn_addGroupMembers:
//                addGroupMembers();
                break;
            case R.id.btn_removeGroupMembers:
//                removeGroupMembers();
                break;
            case R.id.btn_exitGroup:
//                exitGroup();
                break;
        }
    }

//    private void exitGroup() {
//        JMessageClient.createGroup("打麻将", "好玩", new CreateGroupCallback() {
//            @Override
//            public void gotResult(int responseCode, String responseMsg, long groupId) {
//                if (responseCode==-1){
//                    Logger.d("创建群组"+responseMsg+"---"+groupId);
//                    mGroupId = groupId;
//                }
//            }
//        });
//    }
//
//    private void removeGroupMembers() {
//        if (mGroupId!=-1){
//            List<String> userIdList = new ArrayList<>();
//            userIdList.add(User.getUserId());
//            JMessageClient.removeGroupMembers(mGroupId, userIdList, new CreateGroupCallback() {
//                @Override
//                public void gotResult(int responseCode, String responseMsg, long groupId) {
//                    if (responseCode==0){
//                        Logger.d("移除成员"+responseMsg+"---"+groupId);
//                    }
//                }
//            });
//        }
//    }
//
//    private void addGroupMembers() {
//        if (mGroupId!=-1){
//            List<String> userIdList = new ArrayList<>();
//            userIdList.add(User.getUserId());
//            JMessageClient.addGroupMembers(mGroupId, userIdList, new CreateGroupCallback() {
//                @Override
//                public void gotResult(int responseCode, String responseMsg, long groupId) {
//                    if (responseCode==0){
//                        Logger.d("添加成员"+responseMsg+"---"+groupId);
//                    }
//                }
//            });
//        }
//    }
//
//    private void createGroup() {
//        if (mGroupId!=-1){
//            JMessageClient.exitGroup(mGroupId, new CreateGroupCallback() {
//                @Override
//                public void gotResult(int responseCode, String responseMsg, long groupId) {
//                    if (responseCode==0){
//                        Logger.d("退出群"+responseMsg+"---"+groupId);
//                    }
//                }
//            });
//        }
//    }
//
//    private void sendChat() {
//        String text = etChatInput.getText().toString();
//        if (!TextUtils.isEmpty(text) && chatContentAdapter != null) {
//            Conversation conversation = new Conversation();
//            conversation.setOwn(true);
//            conversation.setContent(text);
//            chatContentAdapter.add(conversation);
//            rvChatContainer.smoothScrollToPosition(chatContentAdapter.getItemCount() - 1);
//            etChatInput.setText("");
//        }
//    }

}
