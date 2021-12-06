package edu.coass;

import static edu.coass.utils.ConstantUtli.BASE_URL;
import static edu.coass.utils.ConversationFlowUtils.Welcome;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;


import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.coass.bean.FriendBean;
import edu.coass.bean.SuggestionBean;
import edu.coass.bean.UserInfo;
import edu.coass.bean.witaiRes.WitaiRes;
import edu.coass.okhttp.OkHttpUtils;
import edu.coass.okhttp.RBResponse;
import edu.coass.okhttp.WebResponse;
import edu.coass.strategy.conversation.ConversationFlowManager;
import edu.coass.strategy.database.DatabaseResponseStrategyManager;
import edu.coass.strategy.witai.WitaiResponseStrategyManager;
import edu.coass.utils.ConversationFlowUtils;
import edu.coass.utils.FileUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements WebResponse,Runnable{

    Thread update_thread = new Thread(MainActivity.this);
    private ListView listView;
    private ChatAdapter adapter;
    private List<ChatBean> chatBeanList;//存放所有聊天数据的集合
    private EditText et_send_msg;
    private Button btn_send;
    //接口地址
    private static final String version  = "20211130";
    private static final String WEB_SITE="https://api.wit.ai/message?v="+version+"&q=";
    private WitaiResponseStrategyManager wrsm = new WitaiResponseStrategyManager() ;
    private DatabaseResponseStrategyManager drsm = new DatabaseResponseStrategyManager() ;
    //唯一key，该key的值是从官网注册账号获取的，注册地址为：http://www.tuling123.com/
    private static final String KEY="DKJTHB7FWSQOVYX3QOQVK5A3PXIADCRJ";
    private String sendMsg;//发送的信息
    private String welcome;//存储欢迎信息
    private String NotFoundFromWitai = "Sorry not find anything with wit.ai !" ;
    private String NotFoundFromDb = "Sorry not find anything with MySql !" ;
    private MHandler mHandler;
    private String name ;
    private UserInfo userInfo;
    public static final int MSG_OK=1;//获取数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatBeanList=new ArrayList<ChatBean>();
        mHandler=new MHandler();
        //获取内置的欢迎信息
        String userJson=getIntent().getStringExtra("userInfo");
        userInfo=new Gson().fromJson(userJson,UserInfo.class) ;
        name = userInfo.getName() ;
        welcome= Welcome;
        initView();//初始化界面控件

        if(this.update_thread.getState()== Thread.State.NEW) {
            this.update_thread.start();
        }
    }

    private void getDataFromFile(){
        AssetManager assetManager = getAssets() ;
        String fileName = "witai_json.txt" ;
        String content = FileUtils.readFileByLines(assetManager,fileName) ;
        paresData(content);
    }

    private void getKeyFromWitaiServer(){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request =new Request.Builder()
                .url(WEB_SITE+sendMsg)
                .addHeader("Authorization", "Bearer " + KEY)
                .build();
        Call call=okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                Message msg=new Message();
                msg.what=MSG_OK;
                msg.obj=res;
                //System.out.println(res);
                mHandler.sendMessage(msg);
            }
        });
    }
    private static String curState ="";
    private static String curContent ="";
    private static int isImage = 0 ;
    private WitaiRes witaiRes = null ;
    @Override
    public void onSuccessResponse(Call call, RBResponse resultBean, String requestCode) throws IOException {
        //Log.e("response",resultBean.getResponse())
        curState = requestCode ;
        System.out.println("onSuccessResponse :"+requestCode+ " " +resultBean.getResponse());
        //定时查找好友，若是空集合则直接返回
        if (resultBean.getResponse().equalsIgnoreCase("[]") && requestCode.equalsIgnoreCase("FriendsSearch")){
            return ;
        }
        if (requestCode.equalsIgnoreCase("FriendsInsert")){
            return ;
        }

        curContent = resultBean.getResponse()  ;
        if (requestCode.equalsIgnoreCase("courses_suggestions")){
            String dspValue = ConversationFlowManager.getAnswerUrl(curContent) ;
            showData(dspValue) ;
            return ;
        }
        String value = drsm.getResult(requestCode,resultBean.getResponse(),witaiRes.getValue()) ;
        if(requestCode.equalsIgnoreCase("professor_info")){
            isImage = 1;
        }
        if (value == null ){
            value = NotFoundFromDb ;
        }
        showData(value) ;
    }

    @Override
    public void onFailResponse(Call call, IOException e, String requestCode) {
        System.out.println("onFailResponse "+e.getMessage());
    }


    private void paresData(String content){  //Json解析


        Map<String,Object> witMap = new HashMap<>() ;
        witMap = new Gson().fromJson(content,Map.class) ;
        String type = null ;
        String value = null ;
        LinkedTreeMap<String,Object> entMap = (LinkedTreeMap)witMap.get("entities") ;

        for (String key:entMap.keySet()){

            List list = (ArrayList)entMap.get(key) ;
            //System.out.println(tmp);
            if (list.size()>0){
                Map<String,Object> map = (Map)list.get(0) ;
                type = (String)map.get("name") ;
                value = (String)map.get("value");
                witaiRes = new WitaiRes() ;
                witaiRes.setType(type);
                witaiRes.setValue(value);
            }
            break ;
        }


//        if (witaiRes.getType().equalsIgnoreCase("professor_image")){
//            isImage = 1 ;
//        }
//
//        if (isImage == 1){
//            System.out.println("-----------------");
//            showData(witaiRes.getValue());
//            return ;
//        }
        witaiRes.setType("courses_suggestions");
        witaiRes.setValue("Arielle Carr");
        if (witaiRes == null ){
            showData(NotFoundFromWitai);
            return ;
        }
        System.out.println(witaiRes.toString());
        //System.out.println(witaiRes.toString());
        if (witaiRes.getType().equalsIgnoreCase("courses_suggestions")){
            showData(ConversationFlowManager.getQuestion()) ;
            return ;
        }
        String url = wrsm.getResult(witaiRes,userInfo) ;
        if ( url== null){
            System.out.println("wrsm.getResult null");
            String tmp = witaiRes.getType() + " strategy is not configuration !";
            showData(tmp);
            return ;
        }
        System.out.println("wrsm.getResult "+url);
        OkHttpUtils.getInstance().doGet(MainActivity.this,url,witaiRes.getType());
    }


    private void showData(String message){
        ChatBean chatBean=new ChatBean();
        chatBean.setMessage(message);
        if (isImage == 1){
            AssetManager asset = getAssets() ;
            adapter.setAssetManager(asset);
            chatBean.setState(3);//机器人发送的消息
            chatBean.setPhoto(witaiRes.getValue());
        }else{
            chatBean.setState(ChatBean.RECEIVE);//机器人发送的消息
        }
        chatBeanList.add(chatBean);//将机器人发送的消息添加到chatBeanList集合中
        adapter.notifyDataSetChanged();
    }

    //事件捕获
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg){
            super.dispatchMessage(msg);
            switch(msg.what){
                case MSG_OK:
                    if(msg.obj!=null){
                        String vlResult=(String)msg.obj;
                        paresData(vlResult);
                    }
                    break;
            }
        }
    }

    public void initView(){
        listView=(ListView)findViewById(R.id.list);
        et_send_msg=(EditText)findViewById(R.id.et_send_msg);
        btn_send=(Button)findViewById(R.id.btn_send);
        adapter=new ChatAdapter(chatBeanList,this);
        listView.setAdapter(adapter);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();//点击发送按钮，发送信息
            }
        });
        et_send_msg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                if (keyCode==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==
                        KeyEvent.ACTION_DOWN){
                    sendData();//点击Enter键也可以发送信息
                }
                return false;
            }
        });
//        int position=(int)(Math.random()*welcome.length-1);//获取一个随机数
        showData(welcome);//用随机数获取机器人的首次聊天信息
//        String service_list = "#1 course\n#2 score\n Questions of course selection" ;
//        showData(service_list);
    }
    //
    private void sendData(){
        sendMsg=et_send_msg.getText().toString();//获取你输入的信息
        if(TextUtils.isEmpty(sendMsg)){//判断是否为空
            Toast.makeText(this,"You haven't entered any information",Toast.LENGTH_LONG).show();
            return;
        }
        et_send_msg.setText("");
        //替换空格和换行
        //sendMsg=sendMsg.replaceAll(" ","").replaceAll("\n","").trim();

        System.out.println("-----processing after ------   "+sendMsg);
        ChatBean chatBean=new ChatBean();
        chatBean.setMessage(sendMsg);
        chatBean.setState(chatBean.SEND);//SEND表示自己发送的信息
        chatBeanList.add(chatBean);      //将发送的信息添加到chatBeanList集合中
        adapter.notifyDataSetChanged();//更新ListView列表
        String url ="";
        int reqCode = 0 ;

        //删除多余的空格，只保留一个
        sendMsg = sendMsg.replaceAll("\\s{1,}", " ");

        //conversation flow 判断
        if (witaiRes != null && witaiRes.getType().equalsIgnoreCase("courses_suggestions")){
            int ret = ConversationFlowManager.getAnswer(sendMsg) ;
            System.out.println("-------ret  "+ret);
            if (ret <= 0 ){

                //getDataFromFile() ;
                getKeyFromWitaiServer() ;
                return ;
            }
            if (ret==2){
                String value = ConversationFlowManager.getQuestion() ;
                showData(value);
                return ;
            }
            if (ret==3){
                String value = Welcome ;
                showData(value);
                return ;
            }
            if (ConversationFlowManager.suggestion.getAction() == 1){
                System.out.println(ConversationFlowManager.suggestion.getAnswer());
                OkHttpUtils.getInstance().doGet(MainActivity.this,ConversationFlowManager.suggestion.getAnswer(),"courses_suggestions");
            }
            return ;
        }

        if (curState.equalsIgnoreCase("FriendsSearch") && sendMsg.equalsIgnoreCase("yes")){
            List<FriendBean> listFriend = new Gson().fromJson(curContent,new TypeToken<List<FriendBean>>() {}.getType()) ;
            for (FriendBean friend : listFriend){
                url = BASE_URL+"setFriends?name="+name+"&friend="+friend.getName()+"&email="+friend.getEmail();
                OkHttpUtils.getInstance().doGet(MainActivity.this,url,"FriendsInsert");
            }
        }else{
            getDataFromFile() ;
            //getKeyFromWitaiServer() ;
        }

        //getKeyFromWitaiServer() ;
    }
    public static int flag =0 ;
    @Override
    public void run() {

        while(true) {
            try{
                runOnUiThread(new Runnable() {
                    public void run() {
                        String interests = userInfo.getInterest() ;
                        if (flag >=1 && !curState.equalsIgnoreCase("FriendsSearch")){
                            if ( interests!= null ){
                                String url = BASE_URL+"interests?name="+name+"&interests="+interests.trim();
                                System.out.println(url);
                                OkHttpUtils.getInstance().doGet(MainActivity.this,url,"FriendsSearch");
                            }
                            System.out.println("runOnUiThread "+name+ " flag "+flag);
                        }
                    }
                });
                flag++ ;
                Thread.sleep(300*1000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap canvasTriangle(Bitmap bitmapimg, int direct) {
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);
        //设置默认背景颜色
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        //右边
        if (direct == 0) {
            canvas.drawRect(0, 0, bitmapimg.getWidth() - 15, bitmapimg.getHeight(), paint);
            Path path = new Path();
            path.moveTo(bitmapimg.getWidth() - 15, 10);
            path.lineTo(bitmapimg.getWidth(), 20);
            path.lineTo(bitmapimg.getWidth() - 15, 30);
            path.lineTo(bitmapimg.getWidth() - 15, 10);
            canvas.drawPath(path, paint);
        }
        //左边
        if (direct == 1) {
            canvas.drawRect(15, 0, bitmapimg.getWidth(), bitmapimg.getHeight(), paint);
            Path path = new Path();
            path.moveTo(15, 10);
            path.lineTo(0, 20);
            path.lineTo(15, 30);
            path.lineTo(15, 10);
            canvas.drawPath(path, paint);
        }
        //两层绘制交集。显示上层
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

}
