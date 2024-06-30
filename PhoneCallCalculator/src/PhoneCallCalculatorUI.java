import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class PhoneCallCalculatorUI extends JFrame {
    private JComboBox<String> destinationComboBox;
    private JRadioButton dayRadioButton, nightRadioButton;
    private JRadioButton weekdaysRadioButton, weekendsRadioButton;
    private JTextField durationTextField;
    private JButton calculateButton;
    private JTextArea outputTextArea;

    private final Map<String, Map<String, Map<Integer, Double>>> rates;
    private final Map<Integer, String> regions;

    public PhoneCallCalculatorUI() {
        setTitle("PLDT Call Calculator");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 10, 10));

        // Initialize maps for rates and regions
        rates = new HashMap<>();
        initializeRates();

        regions = new HashMap<>();
        initializeRegions();

        // Initialize GUI components
        initializeComponents();

        // Add components to the frame
        addComponents();

        // Add action listeners
        addActionListeners();

        // Set the frame visible
        setVisible(true);
    }

    private void initializeRates() {
        // Initialize rates based on Python script rates
        Map<String, Map<Integer, Double>> weekdaysRates = new HashMap<>();
        weekdaysRates.put("day", Map.of(1, 50.0 / 3, 2, 30.0 / 2, 3, 40.0 / 3, 4, 35.0 / 2));
        weekdaysRates.put("night", Map.of(1, 45.0 / 3, 2, 27.0 / 2, 3, 36.0 / 3, 4, 30.0 / 2));

        Map<String, Map<Integer, Double>> weekendRates = new HashMap<>();
        weekendRates.put("day", Map.of(1, 40.0 / 3, 2, 25.0 / 2, 3, 35.0 / 3, 4, 20.0 / 2));
        weekendRates.put("night", Map.of(1, 38.0 / 3, 2, 15.0 / 2, 3, 22.0 / 3, 4, 19.0 / 2));

        rates.put("weekdays", weekdaysRates);
        rates.put("weekend", weekendRates);
    }

    private void initializeRegions() {
        regions.put(1, "American Region");
        regions.put(2, "Asian Region");
        regions.put(3, "African Region");
        regions.put(4, "European Region");
    }

    private void initializeComponents() {
        destinationComboBox = new JComboBox<>(new String[]{"American", "Asian", "African", "European"});
        dayRadioButton = new JRadioButton("Day");
        nightRadioButton = new JRadioButton("Night");
        ButtonGroup timeGroup = new ButtonGroup();
        timeGroup.add(dayRadioButton);
        timeGroup.add(nightRadioButton);
        weekdaysRadioButton = new JRadioButton("Weekdays");
        weekendsRadioButton = new JRadioButton("Weekends");
        ButtonGroup dayGroup = new ButtonGroup();
        dayGroup.add(weekdaysRadioButton);
        dayGroup.add(weekendsRadioButton);
        durationTextField = new JTextField();
        calculateButton = new JButton("Calculate");
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
    }

    private void addComponents() {
        add(new JLabel("Destination Code:"));
        add(destinationComboBox);
        add(new JLabel("Time Code:"));
        JPanel timePanel = new JPanel();
        timePanel.add(dayRadioButton);
        timePanel.add(nightRadioButton);
        add(timePanel);
        add(new JLabel("Day Code:"));
        JPanel dayPanel = new JPanel();
        dayPanel.add(weekdaysRadioButton);
        dayPanel.add(weekendsRadioButton);
        add(dayPanel);
        add(new JLabel("Enter Duration [Minutes]:"));
        add(durationTextField);
        add(new JLabel()); // Placeholder for spacing
        add(calculateButton);
        add(new JLabel("Output:"));
        add(new JScrollPane(outputTextArea));
    }

    private void addActionListeners() {
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateCallCharges();
            }
        });
    }

    private void calculateCallCharges() {
        int destinationCode = destinationComboBox.getSelectedIndex() + 1; // Convert index to code (1-based)
        String dayType = weekdaysRadioButton.isSelected() ? "weekdays" : "weekend";
        String timeType = dayRadioButton.isSelected() ? "day" : "night";
        int duration = Integer.parseInt(durationTextField.getText());

        double rate = rates.get(dayType).get(timeType).get(destinationCode);
        double totalCharge = duration * rate;

        String output = "YOUR CALL INFORMATION\n";
        output += "Destination: " + regions.get(destinationCode) + "\n";
        output += "Type of Day: " + (dayType.equals("weekdays") ? "Weekday" : "Weekend") + "\n";
        output += "Time of Day: " + (timeType.equals("day") ? "Day" : "Night") + "\n";
        output += "Call Duration: " + duration + " minutes\n";
        output += "Total Charges: ₱" + String.format("%.2f", totalCharge) + "\n";

        outputTextArea.setText(output);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PhoneCallCalculatorUI();
            }
        });
    }
}
