package lassi.harjoitustyo;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.border.*;
import java.util.Random;

public class BarGraph extends JPanel
{
    private int histogramHeight = 300;
    private int barWidth = 40;
    private int barGap = 15;

    private JPanel barPanel;
    private JPanel labelPanel;

    public ArrayList<Bar> bars = new ArrayList<Bar>();

    public BarGraph()
    {
        setBorder( new EmptyBorder(10, 10, 10, 10) );
        setLayout( new BorderLayout() );

        barPanel = new JPanel( new GridLayout(1, 0, barGap, 0) );
        Border outer = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Border inner = new EmptyBorder(10, 10, 0, 10);
        Border compound = new CompoundBorder(outer, inner);
        barPanel.setBorder( compound );

        labelPanel = new JPanel( new GridLayout(1, 0, barGap, 0) );
        labelPanel.setBorder( new EmptyBorder(5, 10, 0, 10) );

        add(barPanel, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.PAGE_END);
    }

    public void addHistogramColumn(String label, double value, Color color)
    {
        Bar bar = new Bar(label, value, color);
        bars.add( bar );
    }

    public void layoutHistogram()
    {
        barPanel.removeAll();
        labelPanel.removeAll();

        int maxValue = 0;

        for (Bar bar: bars)
            maxValue = Math.max(maxValue, (int)bar.getValue());

        for (Bar bar: bars)
        {
            JLabel label = new JLabel(bar.getValue() + "");
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.TOP);
            label.setVerticalAlignment(JLabel.BOTTOM);
            int barHeight = ((int)bar.getValue() * histogramHeight) / maxValue;
            Icon icon = new ColorIcon(bar.getColor(), barWidth, barHeight);
            label.setIcon( icon );
            barPanel.add( label );

            JLabel barLabel = new JLabel( bar.getLabel() );
            barLabel.setHorizontalAlignment(JLabel.CENTER);
            labelPanel.add( barLabel );
        }
    }

    private class Bar
    {
        private String label;
        private double value;
        private Color color;

        public Bar(String label, double value, Color color)
        {
            this.label = label;
            this.value = value;
            this.color = color;
        }

        public String getLabel()
        {
            return label;
        }

        public double getValue()
        {
            return value;
        }

        public void addValue(double value) {
            this.value += value;
        }

        public Color getColor()
        {
            return color;
        }
    }

    private class ColorIcon implements Icon
    {
        private int shadow = 3;

        private Color color;
        private int width;
        private int height;

        public ColorIcon(Color color, int width, int height)
        {
            this.color = color;
            this.width = width;
            this.height = height;
        }

        public int getIconWidth()
        {
            return width;
        }

        public int getIconHeight()
        {
            return height;
        }

        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            g.setColor(color);
            g.fillRect(x, y, width - shadow, height);
            g.setColor(Color.GRAY);
            g.fillRect(x + width - shadow, y + shadow, shadow, height - shadow);
        }
    }

    public void createAndShowGraph(Database database)
    {
        BarGraph panel = new BarGraph();
        //panel.addHistogramColumn("A", 350, Color.RED);
        //panel.addHistogramColumn("B", 690, Color.YELLOW);
        //panel.addHistogramColumn("C", 510, Color.BLUE);
        //String[] colors = {"BLACK", "BLUE", "CYAN", "DARK_GRAY", "GRAY", "GREEN", "LIGHT_GRAY"}
        //panel.addHistogramColumn("A", 350, Color.RED);
        System.out.println("Launched graph builder");
        for (Expense e : database.db) {
            boolean existingCategory = false;
            System.out.println("Now searching database");
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);
            //panel.addHistogramColumn(e.getCategory(), e.getPrice(), randomColor);
            for (Bar bar : panel.bars) {
                if (bar.getLabel().equals(e.getCategory())) {
                    bar.addValue(e.getPrice());
                    System.out.println("Not unique: " + bar.getLabel());
                    existingCategory = true;
                    break;
                }
            }
            if(!existingCategory) {
                panel.addHistogramColumn(e.getCategory(), e.getPrice(), randomColor);
                System.out.println("Unique: " + e.getCategory());
                System.out.println("How may bars: " + bars.size());
            }
        }
        System.out.println("How may bars: " + bars.size());
        panel.layoutHistogram();

        JFrame frame = new JFrame("Histogram Panel");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add( panel );
        frame.setLocationByPlatform( true );
        frame.pack();
        frame.setVisible( true );
    }

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                BarGraph bg = new BarGraph();
                Database db1 = new Database();
                //bg.addHistogramColumn("B", 222, Color.BLUE);
                //bg.addHistogramColumn("C", 222, Color.BLUE);
                //bg.addHistogramColumn("D", 222, Color.BLUE);
                db1.addExpense("2021-10-10", "A", 10, "comment1");
                db1.addExpense("2021-10-10", "B", 20, "comment2");
                db1.addExpense("2021-10-10", "B", 30, "comment3");
                db1.addExpense("2021-10-10", "C", 40, "comment4");
                bg.createAndShowGraph(db1);
            }
        });
    }
}
