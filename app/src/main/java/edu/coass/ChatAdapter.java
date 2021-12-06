package edu.coass;

import static edu.coass.utils.ImageUtils.getImageFromAssetsFile;
import static edu.coass.utils.ImageUtils.zoomBitmap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private List<ChatBean> chatBeanList;//聊天数据
    private LayoutInflater layoutInflater;
    private AssetManager assetManager ;
    public ChatAdapter(List<ChatBean>chatBeanList, Context context){
        this.chatBeanList=chatBeanList;
        layoutInflater=LayoutInflater.from(context);
    }
    public void setAssetManager(AssetManager assetManager){
        this.assetManager = assetManager ;
    }
    @Override
    public int getCount() {
        return chatBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        //判断当前的消息是发送的消息还是接收到的消息，不同消息加载不同的view
        int state = chatBeanList.get(position).getState() ;
        switch(state){
            case ChatBean.RECEIVE:
                convertView=layoutInflater.inflate(R.layout.chatting_left_item,null);
                break;
            case ChatBean.SEND:
                convertView=layoutInflater.inflate(R.layout.chatting_right_item,null);
                break;
            case 3:
                convertView=layoutInflater.inflate(R.layout.chatting_left_image,null);
                String imageName = chatBeanList.get(position).getPhoto()+".png" ;
                System.out.println("---chatting_left_image");
                ImageView imageView = (ImageView)convertView.findViewById(R.id.leftProImages);

                holder.tv_chat_content=(TextView)convertView.findViewById(R.id.tv_image_content);
                holder.tv_chat_content.setText(chatBeanList.get(position).getMessage());

                Bitmap bitmap = getImageFromAssetsFile(assetManager,imageName) ;
                //Bitmap bitmapZoom = zoomBitmap(bitmap,3,5) ;
                imageView.setImageBitmap(bitmap);
                break ;
        }

//        if (chatBeanList.get(position).getState()==ChatBean.RECEIVE){
//            //加载左边布局，也就是机器人对应的布局信息
//            convertView=layoutInflater.inflate(R.layout.chatting_left_item,null);
//        }else{
//            //加载右边布局，也就是用户对应的布局信息
//
//        }
        if (state < 3){
            holder.tv_chat_content=(TextView)convertView.findViewById(R.id.tv_chat_content);
            holder.tv_chat_content.setText(chatBeanList.get(position).getMessage());
        }

        return convertView;
    }
    class Holder{
        public TextView tv_chat_content;//聊天内容
    }
}
