package UTILS.BUTTONS;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RoundButton extends JButton {
    public RoundButton(ImageIcon image) {
        super(image);
        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setBackground(new Color(200,200,200));
        setFont(new Font("Segoe UI", Font.BOLD, 18));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Tạo hình tròn làm mặt nạ
        int diameter = Math.min(getWidth(), getHeight());
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;
        Ellipse2D circle = new Ellipse2D.Float(x, y, diameter, diameter);
        
        // Vẽ nền tròn
        g2.setColor(getBackground());
        g2.fill(circle);
        
        // Áp dụng clip hình tròn
        g2.setClip(circle);
        
        // Vẽ ảnh
        ImageIcon icon = (ImageIcon) getIcon();
        if (icon != null) {
            Image img = icon.getImage();
            g2.drawImage(img, x, y, diameter, diameter, null);
        }
        
        // Vẽ text nếu có
        if (getText() != null && !getText().isEmpty()) {
            g2.setColor(getForeground());
            g2.setFont(getFont());
            
            FontMetrics fm = g2.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(getText())) / 2;
            int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            
            g2.drawString(getText(), textX, textY);
        }
        
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(40, 40);
    }
}