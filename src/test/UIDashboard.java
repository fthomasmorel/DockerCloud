package test;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;


public class UIDashboard extends JFrame implements ActionListener, ChangeListener {

  private static final long serialVersionUID = 1L;
  private JFreeChart chart;
  private DynamicTimeSeriesCollection dataset;
  private int numberOfSeries;
  
  private DashboardDelegate delegate;
  
  private ChartPanel chartPanel;
  private JPanel mainPanel;
  
  public JButton initButton;
  public JButton startButton;
  public JButton stopButton;
  private JSlider frequenceValue;
   

  public UIDashboard(DashboardDelegate _delegate) {
        super("Dashboard");
        delegate = _delegate;
        initChart();
        initView();
		setVisible(true);
    } 
    
    private void initChart(){
    	
    	numberOfSeries = 0;
    	
    	Calendar calendar = new GregorianCalendar();
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        
        dataset = new DynamicTimeSeriesCollection(20, 200, new Second());
        dataset.setTimeBase(new Second(seconds, minutes, hours, date, month, year));

        chart = ChartFactory.createTimeSeriesChart("% Charge",  null, null, dataset, true, true, false);
        
        NumberAxis yaxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        yaxis.setRange(-5, 105);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        chartPanel.setBounds(0, 0, 500, 270);
        
    }
    
    private void initView(){

    	if(mainPanel != null) mainPanel.removeAll();
    	
        GridBagConstraints constraint = new GridBagConstraints();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new java.awt.Dimension(500, 40));
        
        initButton = new JButton("Init");
        initButton.addActionListener(this);
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        frequenceValue = new JSlider(0,100);
        frequenceValue.addChangeListener(this);
        
        buttonPanel.add(initButton);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(frequenceValue);
        
        buttonPanel.setBackground(Color.WHITE);
        
        mainPanel = new JPanel();
        
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridheight = 1;
        constraint.gridwidth = 1;
        mainPanel.add(chartPanel, constraint);
        
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.gridheight = 1;
        constraint.gridwidth = 1;
        mainPanel.add(buttonPanel, constraint);
        mainPanel.setBackground(Color.WHITE);
        
        setPreferredSize(new java.awt.Dimension(520, 350));
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        pack();
        repaint();
    }
    
    public void clearChart(){
    	initChart();
    	initView();
    }
    
    public void updateDataset(float val){
    	float [] values = {val};
    	dataset.advanceTime();
    	dataset.appendData(values);
    }
    
    public void addSerie(String name){
    	float[] data = {0};
    	dataset.addSeries(data, numberOfSeries, name);
        numberOfSeries++;
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(((JButton) arg0.getSource()).equals(startButton)){
			delegate.startSimulation();
		}else if(((JButton) arg0.getSource()).equals(stopButton)){
			delegate.stopSimulation();
		}else if(((JButton) arg0.getSource()).equals(initButton)){
			delegate.initSimulation();
		}
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		delegate.setFrequence(frequenceValue.getValue());
	}
    
} 