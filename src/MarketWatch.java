import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import org.json.JSONException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MarketWatch extends JFrame implements ActionListener {

    JButton button1,popStockButton,marketIndicesButton;
    JTextField stockname;
    public void displayMessage(String message) {
		JFrame f=new JFrame();
		JOptionPane.showMessageDialog(f,message);
	}
    public MarketWatch(){

        JLabel stocknameLabel = new JLabel("Stock Name");
        stocknameLabel.setBounds(100,100 - 50,140,25);
        stocknameLabel.setFont(new Font("Comic Sans",Font.ITALIC,15));
        stocknameLabel.setForeground(Color.WHITE);

        stockname = new JTextField();
        stockname.setBounds(100+90,100- 50,140,25);
        stockname.setPreferredSize(new Dimension(120,25));

        button1 = new JButton("Get Data");
        button1.setBounds(246+90,100- 50,120,25);
        button1.addActionListener(this);
        button1.setBorder(BorderFactory.createEtchedBorder());
        button1.setBackground(new Color(23,23,23));
        button1.setFont(new Font("Comic Sans",Font.ITALIC,15));
        button1.setForeground(Color.WHITE);
        button1.setFocusable(false);

        popStockButton = new JButton("Popular Stocks");
        popStockButton.setBounds(70+90,230 - 120,120,25);
        popStockButton.addActionListener(this);
        popStockButton.setBorder(BorderFactory.createEtchedBorder());
        popStockButton.setBackground(new Color(23,23,23));
        popStockButton.setFont(new Font("Comic Sans",Font.ITALIC,15));
        popStockButton.setForeground(Color.WHITE);
        popStockButton.setFocusable(false);

        marketIndicesButton = new JButton("Market Indices");
        marketIndicesButton.setBounds(210+90,230 - 120,120,25);
        marketIndicesButton.addActionListener(this);
        marketIndicesButton.setBorder(BorderFactory.createEtchedBorder());
        marketIndicesButton.setBackground(new Color(23,23,23));
        marketIndicesButton.setFont(new Font("Comic Sans",Font.ITALIC,15));
        marketIndicesButton.setForeground(Color.WHITE);
        marketIndicesButton.setFocusable(false);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,400);
        this.setLayout(null);
        this.getContentPane().setBackground(new Color(133,205,202));
        this.setVisible(true);

        this.add(button1);
        this.add(stocknameLabel);
        this.add(stockname);
        this.add(popStockButton);
        this.add(marketIndicesButton);


    }
    public void table(String[][] rows,String[] column) {
    	JFrame f=new JFrame();
    	JTable jt=new JTable(rows,column);    
        jt.setBounds(30,40,200,300);          
        jt.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        	public void valueChanged(ListSelectionEvent e) {
        		if(!e.getValueIsAdjusting()) {
        			String stock=jt.getValueAt(jt.getSelectedRow(), 0).toString();
//            		System.out.println(stock);
            		try {
            			drawGraph(stock);
            		} catch (JSONException x) {
            			// TODO Auto-generated catch block
            			x.printStackTrace();
            		}
        		}
        	}
        });
        JScrollPane sp=new JScrollPane(jt);    
        f.add(sp);          
        f.setSize(300,400);    
        f.setVisible(true); 
        displayMessage("Click the respective rows for graphs!");
	}
    public String[] forEachStock(Stock s) throws JSONException {

        LocalDateTime date = LocalDateTime.now().minusDays((long)1.0);
        String d = date.toString().substring(0,10);
        String day = date.getDayOfWeek().toString();
        String LatestDay ;
        String earlierDay =  date.minusDays((long) 7.0).toString().substring(0,10);
        if(day.equals("SUNDAY")){
            LatestDay = date.minusDays((long) 2.0).toString().substring(0,10);
            earlierDay = date.minusDays((long) 9.0).toString().substring(0,10);
        }
        else if(day.equals("SATURDAY")){
            LatestDay = date.minusDays((long) 1.0).toString().substring(0,10);
            earlierDay = date.minusDays((long) 8.0).toString().substring(0,10);
        }
        else{
            LatestDay = d;

        }
//        String price=String.valueOf(s.getClosePrice());  
        String p=String.format("%.3f", s.getClosePrice());
        System.out.println(String.format("%s | %s | %s",s.getSymbol(),p,s.StockTrend(LatestDay,earlierDay)));
        String data[]= {s.getSymbol(),p,s.StockTrend(LatestDay,earlierDay)};
        return data;

    }
    public void drawGraph(String s) throws JSONException {
    	List<Double> scores = new ArrayList<>();
        Stock stock=new Stock(s);
        scores=stock.lastSeven();
        Collections.reverse(scores);
        GraphPanel mainPanel = new GraphPanel(scores);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
    
    public void popularTechStocks() throws JSONException {

        System.out.println("*".repeat(50));
        System.out.println("Stock : Current price : Performance");
        Stock[] StockArr = {new Stock("GOOGL"), new Stock("TXN"),new Stock("AAPL") };
        String rows[][]=new String[3][3];
        int i=0;
        for(Stock A : StockArr){
            rows[i]=this.forEachStock(A);
            i++;
        }
        String col[]={"Stock","Current price","Performance"};
        table(rows, col);
        
        System.out.println("*".repeat(50));

    }
    public void marketIndices(){
    	String s="";
    	s+="\t\tMarket Indices";
    	s+="\n"+"-".repeat(50);
    	s+="\n"+"NYSE Composite index: $16,868.11";
    	s+="\n"+"Dow Jones Industrial Average: $35,515.38";
    	s+="\n"+"S&P 500 Index: $14,822.55";
    	s+="\n"+"Russell 2000 Index: $2,223.11";
    	s+="\n"+"Global Dow Realtime USD: $4,097.02";
    	s+="\n"+"Dow Jones U.S. Total Stock Market Index: $46,820.65";
    	s+="\n"+"NASDAQ 100 Index (NASDAQ Calculation): $15,136.68";
    	s+="\n"+"NYSE Composite Index: $16,868.11";
    	s+="\n"+"-".repeat(50);
    	System.out.println(s);
    	displayMessage(s);
    }
    public void completeStockData(String symbol) throws JSONException {
//        System.out.println("*".repeat(50));
        Stock stock = new Stock(symbol);
        String rows[][]=new String[1][3];
        rows[0]=forEachStock(stock);
//        forEachStock(stock);
        String col[]={"Stock","Current price","Performance"};
        table(rows, col);
        System.out.println("*".repeat(50));
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
//        if(e.getSource()==)
    	if(e.getSource()==button1){
            try {
                System.out.println("*".repeat(50));
                String s=stockname.getText();
                if(s.equals("")) {
                	displayMessage("Please enter a valid stock id");
                	return;
                }
                System.out.println("Stock : Current price : Performance");
                completeStockData(s);
//                forEachStock(new Stock(stockname.getText().toString()));
//                System.out.println("*".repeat(50));
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        if(e.getSource()==marketIndicesButton){
            this.marketIndices();
        }
        if(e.getSource()==popStockButton){
            try {
                this.popularTechStocks();
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws JSONException {
//         MarketWatch m = new MarketWatch();
    }
}