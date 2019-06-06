package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.IOException;
import java.lang.String;
 
public class Client extends JFrame{
	private static final int DEFAULT_PORT=8899;
	private JLabel stateLB;
	private JTextArea centerTextArea;
	private JTextArea eastTextArea;
	private JPanel southPanel;
	private JTextArea inputTextArea;
	private JPanel bottomPanel;
	private JTextField ipTextField;
	private JTextField remotePortTF;
	private JButton sendBT;
	private JButton clearBT;
	private DatagramSocket datagramSoket;
	public void setUpUI(){
		setTitle("客户端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,400);
		setResizable(false);//窗口大小不可改变
		setLocationRelativeTo(null);//设置窗口相对于指定组件的位置
		stateLB=new JLabel("未监听");
		stateLB.setHorizontalAlignment(JLabel.RIGHT);
		centerTextArea=new JTextArea();
		centerTextArea.setEditable(false);
		centerTextArea.setBackground(new Color(211,211,211));
		eastTextArea=new JTextArea();
		southPanel=new JPanel(new BorderLayout());
		eastTextArea=new JTextArea(6,20);
		eastTextArea.setEditable(false);
		inputTextArea=new JTextArea(4,20);
		bottomPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
		ipTextField=new JTextField("127.0.0.1",8);
		remotePortTF=new JTextField(String.valueOf(DEFAULT_PORT),3);
		sendBT=new JButton("发送");
		clearBT=new JButton("获取推荐用户");
		bottomPanel.add(ipTextField);
		bottomPanel.add(remotePortTF);
		bottomPanel.add(sendBT);
		bottomPanel.add(clearBT);
		southPanel.add(new JScrollPane(inputTextArea),BorderLayout.CENTER);
		southPanel.add(bottomPanel,BorderLayout.SOUTH);
		add(stateLB,BorderLayout.NORTH);
		add(new JScrollPane(centerTextArea),BorderLayout.CENTER);
		southPanel.add(new JScrollPane(eastTextArea),BorderLayout.NORTH);
		add(southPanel,BorderLayout.SOUTH);
		setVisible(true);
	}
public void setListener(){
	sendBT.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			final String ipAddress=ipTextField.getText();
			final String remotePort=remotePortTF.getText();
			//final String remotePort="1010";
			if(ipAddress==null||ipAddress.trim().equals("")||remotePort==null||remotePort.trim().equals("")){
				JOptionPane.showMessageDialog(Client.this,"请输入IP地址和端口号");
				return;
			}
			if(datagramSoket==null||datagramSoket.isClosed()){
				JOptionPane.showMessageDialog(Client.this,"监听未成功");
				return;
			}
			String sendContent=inputTextArea.getText();
			byte[] buf=sendContent.getBytes();
			try{
				centerTextArea.append("我对"+ipAddress+":"+remotePort+"说:\n"+inputTextArea.getText()+"\n\n");
				centerTextArea.setCaretPosition(centerTextArea.getText().length());
				datagramSoket.send(new DatagramPacket(buf,buf.length,InetAddress.getByName(ipAddress),Integer.parseInt(remotePort)));
				inputTextArea.setText("");
			}catch(IOException e1){
				JOptionPane.showMessageDialog(Client.this, "出错了，发送不成功");
				e1.printStackTrace();
			}
		};
	});
	clearBT.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			final String ipAddress="127.0.0.1";//服务器IP地址
			final String remotePort="1010";//服务器端口号
			//final String remotePort="1010";
			if(ipAddress==null||ipAddress.trim().equals("")||remotePort==null||remotePort.trim().equals("")){
				JOptionPane.showMessageDialog(Client.this,"请输入IP地址和端口号");
				return;
			}
			if(datagramSoket==null||datagramSoket.isClosed()){
				JOptionPane.showMessageDialog(Client.this,"监听未成功");
				return;
			}
			String sendContent="获取";
			byte[] buf=sendContent.getBytes();
			try{
				datagramSoket.send(new DatagramPacket(buf,buf.length,InetAddress.getByName(ipAddress),Integer.parseInt(remotePort)));
			}catch(IOException e1){
				JOptionPane.showMessageDialog(Client.this, "出错了，发送不成功");
				e1.printStackTrace();
			}
		}
	});
}
public void initSocket(User user){
	int port=DEFAULT_PORT;
	while(true){
		try{
			if(datagramSoket!=null&&!datagramSoket.isConnected()){
				datagramSoket.close();
			}
			try{
				//port=Integer.parseInt(JOptionPane.showInputDialog(this,"请输入端口号","端口号",JOptionPane.QUESTION_MESSAGE));
				port=Integer.parseInt(user.userid);
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
					if(((InetSocketAddress)p.getSocketAddress()).getPort()==1111) {eastTextArea.setText(new String(p.getData(),0,p.getLength()));}
					else {
					centerTextArea.append(p.getAddress().getHostAddress()+":"+((InetSocketAddress)p.getSocketAddress()).getPort()+"对我说：\n"+new String(p.getData(),0,p.getLength())+"\n\n");
					centerTextArea.setCaretPosition(centerTextArea.getText().length());
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}.start();
}
		public static void main(String[] args) {
//			Client a=new Client();
//            a.setUpUI();
//			a.initSocket();
//			a.setListener();
		}
		
}