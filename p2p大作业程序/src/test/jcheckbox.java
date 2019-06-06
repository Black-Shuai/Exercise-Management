package test;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.*; 
import javax.swing.*;
class jcheckbox extends JFrame{
    public static void main(String[] args){
        jcheckbox jbox = new jcheckbox();
        jbox.setListener();
    }
    JCheckBox jcbox1,jcbox2,jcbox3,jcbox4;
    JLabel jlb1,jlb2;
    //JRadioButton jrbtn1,jfbtn2;
    JTextField porttext;
    JPanel jp1,jp2,jp3;
    JButton jb1,jb2;
    public jcheckbox(){
    	this.setLocationRelativeTo(null);//窗体居中显示
        jcbox1=new JCheckBox("篮球");
        jcbox2=new JCheckBox("乒乓球");
        jcbox3=new JCheckBox("足球");
        jcbox4=new JCheckBox("排球"); 
        
        jlb1=new JLabel("你喜欢的运动");
        jlb2=new JLabel("请输入端口号");
        porttext=new JTextField(8);    
        jb1=new JButton("注册");
        jb2=new JButton("取消");
            //单选框
//        JRadioButton jrbtn1=new JRadioButton("男");
//        JRadioButton jrbtn2=new JRadioButton("女");
//        ButtonGroup bgroup=new ButtonGroup();
        //设置为流式布局
        this.setLayout(new GridLayout(3,1));
            
        jp1=new JPanel();
        jp1.add(jlb1);
        jp1.add(jcbox1);
        jp1.add(jcbox2);
        jp1.add(jcbox3);
        jp1.add(jcbox4);
        this.add(jp1);
        //第二行
        jp2=new JPanel();
        jp2.add(jlb2);
//        bgroup.add(jrbtn1);
//        bgroup.add(jrbtn2);
//        jp2.add(jrbtn1);
//        jp2.add(jrbtn2); 
        jp2.add(porttext);
        this.add(jp2);
        //第三行
        jp3=new JPanel();
        jp3.add(jb1);
        jp3.add(jb2);
        this.add(jp3);
             
        this.setTitle("选择你的兴趣");
        this.setSize(400,150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public void setListener(){
    	jb1.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e){
    			//porttext.setText("9988");
    			User user=new User();
    			List<String> list = new LinkedList<String>();
    			user.setUserid(porttext.getText());
    			if(jcbox1.isSelected()){
    				 list.add(jcbox1.getText());
    				 }
    			if(jcbox2.isSelected()){
   				 list.add(jcbox2.getText());
   				 }
    			if(jcbox3.isSelected()){
      				 list.add(jcbox3.getText());
      				 }
    			if(jcbox4.isSelected()){
      				 list.add(jcbox4.getText());
      				 }
    			String message=user.userid;
    			user.setHobby(list);
    			Iterator<String> it = user.hobby.iterator();
    			while (it.hasNext()) {
    				message=message+" "+it.next();
    			//System.out.println(it.next());
    			}
    			System.out.println(message);
    			try {
    				// 创建发送方的套接字，IP默认为本地，端口号随机  
    	            DatagramSocket sendSocket = new DatagramSocket(Integer.parseInt(user.userid));  
    	  
    	            // 确定要发送的消息：  
    	            String mes = "你好！接收方！";  
    	  
    	            // 由于数据报的数据是以字符数组传的形式存储的，所以传转数据  
    	            byte[] buf = message.getBytes();  
    	  
    	            // 确定发送方的IP地址及端口号，地址为本地机器地址  
    	            int port = 1010;  
    	            InetAddress ip = InetAddress.getLocalHost();  
    	  
    	            // 创建发送类型的数据报：  
    	            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ip,  
    	                    port);  
    	  
    	            // 通过套接字发送数据：  
    	            sendSocket.send(sendPacket); 
    	         // 关闭套接字  
    	            sendSocket.close();
    			}catch(Exception e1) {
    				System.out.println(e1.getMessage());
    			}
    			Client a=new Client();
                a.setUpUI();
    			a.initSocket(user);
    			a.setListener();
    			dispose();
    		}
    	});
}
}