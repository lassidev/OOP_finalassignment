package lassi.harjoitustyo;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Random;

public class BarGraph extends JPanel {
    private int barGraphHeight = 300;
    private int barWidth = 40;
    private int barGap = 15;

    private JPanel barPanel;
    private JPanel labelPanel;

    public ArrayList<Bar> bars = new ArrayList<Bar>();

    public BarGraph() { //constructor
        setBorder( new EmptyBorder(10, 10, 10, 10) );
        setLayout( new BorderLayout() );

        barPanel = new JPanel( new GridLayout(1, 0, barGap, 0) );
        Border outer = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Border inner = new EmptyBorder(10, 10, 0, 10);
        Border compound = new CompoundBorder(outer, inner);
        barPanel.setBorder( compound );

        labelPanel = new JPanel( new GridLayout(1, 0, barGap, 0) );
        labelPanel.setBorder( new EmptyBorder(5, 10, 0, 10) );

        JLabel summaryLabel = new JLabel("Summary for all expenses, divided by category");
        summaryLabel.setFont(new Font("Arial", 0, 30));

        add(summaryLabel, BorderLayout.NORTH);
        add(barPanel, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.PAGE_END);
    }

    public void addBarGraphColumn(String label, double value, Color color) { //Create new Bar object
        Bar bar = new Bar(label, value, color);
        bars.add( bar );
    }

    public void layoutBarGraph() { //Adding bars to JPanel
        barPanel.removeAll();
        labelPanel.removeAll();

        int maxValue = 0;

        for (Bar bar: bars)
            maxValue = Math.max(maxValue, (int)bar.getValue());

        for (Bar bar: bars) {
            JLabel label = new JLabel(bar.getValue() + "");
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.TOP);
            label.setVerticalAlignment(JLabel.BOTTOM);
            int barHeight = ((int)bar.getValue() * barGraphHeight) / maxValue;
            Icon icon = new ColorIcon(bar.getColor(), barWidth, barHeight);
            label.setIcon( icon );
            barPanel.add( label );

            JLabel barLabel = new JLabel( bar.getLabel() );
            barLabel.setHorizontalAlignment(JLabel.CENTER);
            labelPanel.add( barLabel );
        }
    }

    private class Bar {
        private String label;
        private double value;
        private Color color;

        public Bar(String label, double value, Color color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }

        public String getLabel() {
            return label;
        }

        public double getValue() {
            return value;
        }

        public void addValue(double value) {
            this.value += value;
        }

        public Color getColor() {
            return color;
        }
    }

    private class ColorIcon implements Icon {
        private int shadow = 3;

        private Color color;
        private int width;
        private int height;

        public ColorIcon(Color color, int width, int height) {
            this.color = color;
            this.width = width;
            this.height = height;
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, width - shadow, height);
            g.setColor(Color.GRAY);
            g.fillRect(x + width - shadow, y + shadow, shadow, height - shadow);
        }
    }

    public void createAndShowGraph(Database database) { //build bargraph from database of expenses
        BarGraph panel = new BarGraph();
        for (Expense e : database.db) {
            boolean existingCategory = false;
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);
            for (Bar bar : panel.bars) {
                if (bar.getLabel().equals(e.getCategory())) {
                    bar.addValue(e.getPrice());
                    existingCategory = true;
                    break;
                }
            }
            if(!existingCategory) {
                panel.addBarGraphColumn(e.getCategory(), e.getPrice(), randomColor);
            }
        }
        panel.layoutBarGraph();

        JFrame frame = new JFrame("Bar graph summary");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add( panel );
        frame.setLocationByPlatform( true );
        frame.pack();
        frame.setVisible( true );
    }

    public static void main(String[] args) {

    }
}
