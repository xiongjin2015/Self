package com.xj.self.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xj.self.R;
import com.xj.self.entity.ChatMessage;
import com.xj.self.entity.ChatMessage.Type;

public class ChatMessageAdapter extends BaseAdapter {
    
    private LayoutInflater mInflater;
    private List<ChatMessage> datas;

    public ChatMessageAdapter(Context context, List<ChatMessage> datas) {
        this.mInflater = LayoutInflater.from(context);
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    /**
     * 注意的是，因为我们的ListView的Item有两种显示风格，所以比平时我们需要多重写两个方法：
     * getItemViewType根据当前Item的position决定返回不同的整型变量。然后在getView中，根据消息的类型去加载不同的Item布局即可
     * 接受到消息为1，发送消息为0
     */
    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = datas.get(position);
        return msg.getType() == Type.INPUT ? 1:0;
    }
    
    /**
     * 返回的就是种类数目
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = datas.get(position);
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            if(chatMessage.getType() == Type.INPUT){
                convertView = mInflater.inflate(R.layout.chat_from_msg,parent,false);
                viewHolder.createDate = (TextView)convertView.findViewById(R.id.chat_from_createDate);
                viewHolder.content = (TextView)convertView.findViewById(R.id.chat_from_content);
                convertView.setTag(viewHolder);
            }else{
                convertView = mInflater.inflate(R.layout.chat_send_msg, null);
                viewHolder.createDate = (TextView) convertView.findViewById(R.id.chat_send_createDate);
                viewHolder.content = (TextView) convertView.findViewById(R.id.chat_send_content);
                convertView.setTag(viewHolder);
            }
        }else{
            viewHolder =(ViewHolder)convertView.getTag();
        }
        
        viewHolder.content.setText(chatMessage.getMsg());
        viewHolder.createDate.setText(chatMessage.getDateStr());
        
        return convertView;
    }
    
    private class ViewHolder{
        public TextView createDate;
        public TextView name;
        public TextView content;
    }

}
