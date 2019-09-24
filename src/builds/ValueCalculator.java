package builds;

import javax.swing.*;
import java.awt.*;

import core.Equation;

/**
 * Class ValueCalculator
 *
 * <p>A neat tool for easily calculating values</p>
 * <p>
 * Creates a window and interface that takes an equation and x value
 * Can calculate the value at the x point, the slope, and integral
 * </p>
 */
class ValueCalculator {

    private static final float VERSION = 1.0f;
    private JFrame frame;
    private JPanel panelInput;
    private JPanel panelOutput;
    private JTextField xVal, equation;
    private JLabel l_xVal, l_equation;
    private JButton calculate, integrate;
    private JTextField yVal, derivative, integral;
    private JLabel    l_yVal, l_derivative, l_integral;
    private int frame_width = 1000, frame_height = (int) (1000.0 * (1080.0/1920.0));
    private int component_height;
    private JFrame integral_frame;

    ValueCalculator(){

        initJFrame();

        initComponents();
        initJPanels();
        initComponentConfig();

    }

    private void initJFrame(){
        frame = new JFrame("Value Calculator v." + VERSION);
        frame.setBounds(0, 0, frame_width, frame_height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setFocusable(false);
    }

    private void initComponents(){
        equation = new JTextField();
        xVal = new JTextField();
        calculate = new JButton();
        integrate = new JButton();
        yVal = new JTextField();
        derivative = new JTextField();
        integral = new JTextField();
        l_xVal = new JLabel("X Value");
        l_equation = new JLabel("Equation");
        l_yVal = new JLabel("Y Value");
        l_derivative = new JLabel("Slope");
        l_integral = new JLabel("Integral");
    }

    private void initComponentConfig(){
        component_height = frame_height / 15;


        /*_________________Input_________________*/
        int input_position_ratio = frame_width / 9;
        int input_position_from_top = frame_height / 7;

        equation.setBounds(input_position_ratio, input_position_from_top,
                input_position_ratio * 3, component_height);


        xVal.setBounds(5 * input_position_ratio, input_position_from_top,
                input_position_ratio, component_height);


        calculate.setBounds(7 * input_position_ratio, input_position_from_top,
                input_position_ratio, component_height);
        calculate.setText("Calculate");
        calculate.addActionListener(e ->
                calculate("0", xVal.getText(), xVal.getText() ));

        integrate.setBounds(7 * input_position_ratio, input_position_from_top + component_height + 10,
                input_position_ratio, component_height);
        integrate.setText("Integrate...");
        integrate.addActionListener(e ->
                integrateFrom() );

        l_xVal.setBounds(xVal.getX(), xVal.getY() - component_height,
                xVal.getWidth(), xVal.getHeight());
        l_equation.setBounds(equation.getX(), equation.getY() - component_height,
                equation.getWidth(), equation.getHeight());

        /*_________________Output_________________*/
        int output_position_ratio = frame_width / 10;
        int output_position_from_top = frame_height / 7;


        yVal.setBounds(output_position_ratio, output_position_from_top,
                2 * output_position_ratio, component_height);
        yVal.setEditable(false);


        derivative.setBounds(4 * output_position_ratio, output_position_from_top,
                2 * output_position_ratio, component_height);

        derivative.setEditable(false);


        integral.setBounds(7 * output_position_ratio, output_position_from_top,
                2 * output_position_ratio, component_height);
        integral.setEditable(false);

        l_yVal.setBounds(yVal.getX(), yVal.getY() - component_height,
                yVal.getWidth(), yVal.getHeight());
        l_derivative.setBounds(derivative.getX(), derivative.getY() - component_height,
                derivative.getWidth(), derivative.getHeight());
        l_integral.setBounds(integral.getX(), integral.getY() - component_height,
                integral.getWidth(), integral.getHeight());
    }

    private void initJPanels(){
        JPanel panels = new JPanel(new BorderLayout());
        panels.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        frame.add(panels);


        panelInput = new JPanel(null);
        panelInput.setLayout(null);
        panelInput.setPreferredSize(new Dimension(frame_width, frame_height / 2 - 20));
        panelInput.setLocation(0, 0);
        panels.add(panelInput, BorderLayout.PAGE_START);
        panelInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Input"));
        panelInput.add(equation);
        panelInput.add(xVal);
        panelInput.add(calculate);
        panelInput.add(integrate);
        panelInput.add(l_xVal);
        panelInput.add(l_equation);

        panelOutput = new JPanel();
        panelOutput.setLayout(null);
        panelOutput.setPreferredSize(new Dimension(frame_width, frame_height / 2 - 20));

        panels.add(panelOutput, BorderLayout.PAGE_END);
        panelOutput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Output"));
        panelOutput.add(yVal);
        panelOutput.add(derivative);
        panelOutput.add(integral);
        panelOutput.add(l_yVal);
        panelOutput.add(l_derivative);
        panelOutput.add(l_integral);


    }

    private void calculate(String a, String b,  String x){
        double v_a;
        double v_b;
        double v_x;
        try{
            v_a = Double.parseDouble(a);
            v_b = Double.parseDouble(b);
            v_x = Double.parseDouble(x);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(frame, "Please enter valid input!",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }


        Equation equ = new Equation(equation.getText());
        yVal.setText(Double.toString(equ.calculate(v_x)));
        derivative.setText(Double.toString(equ.slopeAt(v_x)));
        l_integral.setText("Integral from " + a + " to " + b);
        integral.setText(Double.toString(equ.definiteIntegral(v_a, v_b)));
    }

    private void integrateFrom(){

        integral_frame = new JFrame("Integrate...");
        integral_frame.setBounds(0, 0,460, 200);
        integral_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        integral_frame.setLocationRelativeTo(null);
        integral_frame.setVisible(true);

        JPanel iPanel = new JPanel(null);
        iPanel.setLayout(null);

        JTextField a = new JTextField(), b = new JTextField();
        JButton cancel = new JButton(), iCalc = new JButton();
        JLabel from = new JLabel(), to = new JLabel();
        iPanel.add(a);
        iPanel.add(b);
        iPanel.add(cancel);
        iPanel.add(iCalc);
        iPanel.add(from);
        iPanel.add(to);
        integral_frame.add(iPanel);

        int indent = integral_frame.getWidth() / 9;

        from.setBounds(indent, indent / 2 - 5, 2 * indent , component_height);
        from.setText("From");

        to.setBounds(indent, indent / 2 + 5 +  component_height , 2 * indent, component_height);
        to.setText("to");

        a.setBounds(from.getX() + from.getWidth(), from.getY(), indent * 5, component_height);
        b.setBounds(to.getX() + to.getWidth(), to.getY(), indent * 5, component_height);

        cancel.setText("Cancel");
        cancel.addActionListener(e ->
                integral_frame.dispose() );
        cancel.setBounds(indent,
                integral_frame.getHeight() - 93,
                (integral_frame.getWidth() / 2) - indent - 10,
                component_height);

        iCalc.setText("Calculate");
        iCalc.setBounds(integral_frame.getWidth() / 2 + 10,
                integral_frame.getHeight() - 93,
                (integral_frame.getWidth() / 2) - indent - 10 ,
                component_height);
        iCalc.addActionListener(e ->
        {
            calculate(a.getText(), b.getText(), xVal.getText());
            integral_frame.dispose();
        });

    }



    public static void main(String[] args){
        new ValueCalculator();
    }



}



