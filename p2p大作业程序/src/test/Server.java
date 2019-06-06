package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame{
	private static final int DEFAULT_PORT=8899;
	private JLabel stateLB;
	private JTextArea centerTextArea;
	private JPanel southPanel;
	private JTextArea inputTextArea;
	private JPanel bottomPanel;
	private JTextField ipTextField;
	private JTextField remotePortTF;
	private JButton sendBT;
	private JButton clearBT;
	private int i=0;
	private DatagramSocket datagramSoket;
	public String getusermes=null;
	public void setUpUI(){
		setTitle("服务端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,400);
		setResizable(false);//窗口大小不可改变
		setLocationRelativeTo(null);//设置窗口相对于指定组件的位置
		stateLB=new JLabel("weijianting");
		stateLB.setHorizontalAlignment(JLabel.RIGHT);
		centerTextArea=new JTextArea();
		centerTextArea.setEditable(false);
		centerTextArea.setBackground(new Color(211,211,211));
		southPanel=new JPanel(new BorderLayout());
		inputTextArea=new JTextArea(5,20);
		bottomPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
		southPanel.add(new JScrollPane(inputTextArea),BorderLayout.CENTER);
		southPanel.add(bottomPanel,BorderLayout.SOUTH);
		add(stateLB,BorderLayout.NORTH);
		add(new JScrollPane(centerTextArea),BorderLayout.CENTER);
		add(southPanel,BorderLayout.SOUTH);
		setVisible(true);
	}
public void initSocket(){
	int port=DEFAULT_PORT;
	while(true){
		try{
			if(datagramSoket!=null&&!datagramSoket.isConnected()){
				datagramSoket.close();
			}
			try{
				//port=Integer.parseInt(JOptionPane.showInputDialog(this,"请输入端口号","端口号",JOptionPane.QUESTION_MESSAGE));
				port=Integer.parseInt("1010");
				if(port<1||port>65535){
					throw new RuntimeException("端口号超出范围");
				}
			}catch(Exception e){
				JOptionPane.showMessageDialog(null,"你输入的端口不正确，请输入1~65535之间的数");
				continue;
			}
			datagramSoket=new DatagramSocket(port);
			startListen();
			stateLB.setText("已在"+port+"端口监听");
			break;
		}catch(SocketException e){
			JOptionPane.showMessageDialog(this, "端口号被占用，请重新设置端口");
			stateLB.setText("当前未启动监听");
		}
	}
}
public void startListen(){
	new Thread(){
		private DatagramPacket p;
		public void run(){
			byte[] buf=new byte[1024];
			p=new DatagramPacket(buf,buf.length);
			while(!datagramSoket.isConnected()){
						try{
							
							datagramSoket.receive(p);
							centerTextArea.append(p.getAddress().getHostAddress()+":"+((InetSocketAddress)p.getSocketAddress()).getPort()+"对我说：\n"+new String(p.getData(),0,p.getLength())+"\n\n");
							centerTextArea.setCaretPosition(centerTextArea.getText().length());
							if(new String(p.getData(),0,p.getLength()).equals("获取")) {
								DatagramSocket sendSocket = new DatagramSocket(Integer.parseInt("1111")); 
								// 确定发送方的IP地址及端口号，地址为本地机器地址  
			    	            int port = ((InetSocketAddress)p.getSocketAddress()).getPort();  
						    	  UserCF cf=new UserCF();
						    	  String re=inputTextArea.getText();
						    	  String useridh=String.valueOf(port);
						    	  System.out.println(useridh);
						    	  List<String> ls=cf.getUserCF(i, getusermes,useridh);
						    	  String getre=String.join("", ls);
			    	            // 确定要发送的消息：  
			    	            String mes = getre;  
			    	  
			    	            // 由于数据报的数据是以字符数组传的形式存储的，所以传转数据  
			    	            byte[] buf1 = mes.getBytes();  
			    	  
			    	            InetAddress ip = InetAddress.getLocalHost();  
			    	  
			    	            // 创建发送类型的数据报：  
			    	            DatagramPacket sendPacket = new DatagramPacket(buf1, buf1.length, ip,  
			    	                    port);  
			    	  
			    	            // 通过套接字发送数据：  
			    	            sendSocket.send(sendPacket); 
			    	         // 关闭套接字  
			    	            sendSocket.close();
							}else {
								inputTextArea.append(new String(p.getData(),0,p.getLength())+"\n");
								i++;
								System.out.println("当前已注册的用户数量"+i);
								if(i==1)getusermes=new String(p.getData(),0,p.getLength());
								else getusermes=getusermes+"\r\n"+new String(p.getData(),0,p.getLength());
							}
							
						}catch(IOException e){
							e.printStackTrace();
						}
			}
		}
	}.start();
}
		public static void main(String[] args) {
			Server a=new Server();
            a.setUpUI();
			a.initSocket();
		}
		
}
