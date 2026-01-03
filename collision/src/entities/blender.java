package entities;//package entities;
//
//import javax.swing.*;
//import javax.swing.border.BevelBorder;
//import javax.swing.border.Border;
//import java.awt.*;
//
//import static sun.tools.jconsole.inspector.XDataViewer.dispose;
//
//public class blender {
//
//    public InstructionsDialog(Frame owner, String title, String message, String button1text, String button2text) {
//        super(owner, title, true);
//        setUndecorated(true);
//        setSize(400, 250);
//        setLocation(owner.getLocation().x+(owner.getWidth()/2) - 200 ,owner.getLocation().y+(owner.getHeight()/2) - 125) ;
////        setLocationRelativeTo(owner);
//
////        setLocation(bounds.getLocation().x+1520,bounds.getLocation().y+595);
//
//
//        JPanel panel = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                // Set a blackish transparent background
//                g.setColor(Color.WHITE);
//                g.fillRect(0, 0, getWidth(), getHeight());
//            }
//        };
//        panel.setLayout(new BorderLayout());
//        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
//
//        // Add the instructional message
//        JTextArea textArea = new JTextArea(message);
//        textArea.setEditable(false);
//        textArea.setForeground(Color.BLACK);
//        textArea.setBackground(new Color(0, 0, 0, 0));
//
//        textArea.setOpaque(false);
//        textArea.setLineWrap(true);
//        textArea.setWrapStyleWord(true);
//
//        textArea.setFont(new Font("Arial", Font.BOLD, 15));
//        textArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        textArea.setFocusable(false);
////        System.out.println("length of instruction : "+message.length());
//
//        // Add text area to panel
//        panel.add(textArea, BorderLayout.CENTER);
//
//        // Create a panel for the buttons
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setOpaque(false);
//        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
//
//        // Create OK button
//        JButton button1 = createStyledButton1(button1text);
//        valuebutton b1 = new valuebutton(button1,button1text,false);
//        button1.addActionListener(e -> actionperformed(b1));
//        buttons.add(b1);
//
//        // Create OK button
//        JButton button2 = createStyledButton1(button2text);
//        valuebutton b2 = new valuebutton(button2,button2text,false);
//        button2.addActionListener(e -> actionperformed(b2));
//        buttons.add(b2);
//
//        // Add buttons to the button panel
//        buttonPanel.add(button1);
//        buttonPanel.add(button2);
//
//        // Add button panel to the main panel
//        panel.add(buttonPanel, BorderLayout.SOUTH);
//
//        // Add panel to dialog
//        add(panel);
//
//        getRootPane().setDefaultButton(button1);
//        button1.requestFocusInWindow();
//    }
//
//    private JButton createStyledButton1(String buttonText) {
//        JButton button = new JButton(buttonText);
//        button.setFocusPainted(false);
//        button.setBackground(new Color(41, 119, 112)); // Set background to maroon
//        button.setFont(new Font("Arial", Font.BOLD, 15));
//        button.setForeground(Color.WHITE); // Set text color to white
//        button.setPreferredSize(new Dimension(150, 40)); // Adjust size to fit text
//
//        // Create a thicker black border
//        Border thickBlackBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
//
//        // Create multiple bevel borders to increase the bevel effect
//        Border bevelBorder1 = BorderFactory.createBevelBorder(BevelBorder.RAISED);
//        Border bevelBorder2 = BorderFactory.createBevelBorder(BevelBorder.RAISED);
//
//        // Combine the borders to create a compound border
//        Border compoundBorder = BorderFactory.createCompoundBorder(thickBlackBorder,
//                BorderFactory.createCompoundBorder(bevelBorder1, bevelBorder2));
//        // Set the combined border to the button
//        button.setBorder(compoundBorder);
//
//        return button;
//    }
//
//    public void actionperformed(valuebutton button){
//        button.value = true ;
//        dispose();
//    }
//
//    static class valuebutton{
//        public JButton button ;
//        public String name ;
//        public boolean value ;
//        public valuebutton(){
//
//        }
//        public valuebutton(JButton button){
//            this.button = button ;
//        }
//        public valuebutton(JButton button,String name){
//            this.button = button ;
//            this.name =name ;
//        }
//        public valuebutton(JButton button,String name,boolean value){
//            this.button = button ;
//            this.name = name ;
//            this.value = value ;
//        }
//    }
//}
