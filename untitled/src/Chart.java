import org.jfree.ui.ApplicationFrame;
import java.awt.Color;
import java.awt.BasicStroke;
import java.util.ArrayList;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import javax.swing.*;

public class Chart extends ApplicationFrame {

    public Chart(String title, String chartTitle, ArrayList<Double> Y){
        super(title);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle ,
                "Seconds" ,
                "Degree" ,
                createDataset(Y, "degree") ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        final XYPlot plot = xylineChart.getXYPlot( );
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.BLUE );
        renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
        plot.setRenderer( renderer );
        setContentPane( chartPanel );

    }


    private XYDataset createDataset(ArrayList<Double> Y, String seriesTitle){
        final XYSeries ds = new XYSeries( seriesTitle );
        for (int i = 0; i < Y.size(); i++) {
            ds.add(i, Y.get(i));
        }
        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(ds);
        return dataset;
    }




}
