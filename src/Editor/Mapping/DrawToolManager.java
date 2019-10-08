package Editor.Mapping;

import Data.FileIO;
import Editor.DrawTools.DrawTool;
import Engine.SpecialText;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DrawToolManager implements KeyListener {

    private ToolButtonPair activeTool;
    private JPanel toolOptionsPanel;
    private SpecialText activeCharacter;
    private SingleTextRenderer renderer;
    private JLabel renderLabel;

    public DrawToolManager(JPanel toolOptionsPanel, SingleTextRenderer renderer, JLabel renderLabel){
        this.toolOptionsPanel = toolOptionsPanel;
        this.renderer = renderer;
        this.renderLabel = renderLabel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println("[DrawToolManager] Input caught!");
        activeCharacter = new SpecialText(e.getKeyChar(), Color.WHITE, Color.BLACK);
        renderer.specText = activeCharacter;
        renderLabel.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public DrawTool getActiveTool(){
        if (activeTool == null) return null;
        return activeTool.drawTool;
    }

    public SpecialText getActiveCharacter() {
        return activeCharacter;
    }

    public JButton generateToolButton(DrawTool tool, String iconPath, String name){
        FileIO io = new FileIO();
        Icon icon = new ImageIcon(io.getRootFilePath().concat(iconPath));
        JButton btn = new JButton(icon);
        btn.setToolTipText(name);
        btn.setMargin(new Insets(5, 5, 5, 5));
        ToolButtonPair pair = new ToolButtonPair(tool, btn);
        btn.addActionListener(e -> {
            //Deactivate previous tool
            if (activeTool != null) {
                activeTool.drawTool.onDeactivate(toolOptionsPanel);
                for (Component c : toolOptionsPanel.getComponents()) toolOptionsPanel.remove(c);
                activeTool.button.setEnabled(true);
            }
            //Activate new tool
            activeTool = pair;
            activeTool.drawTool.onActivate(toolOptionsPanel);
            toolOptionsPanel.validate();
            toolOptionsPanel.repaint();
            activeTool.button.setEnabled(false); //Disallows clicking a tool button twice and causing double-initialization, which can be troublesome.
        });
        return btn;
    }

    private class ToolButtonPair{
        private DrawTool drawTool;
        private JButton button;
        private ToolButtonPair(DrawTool drawTool, JButton button){
            this.drawTool = drawTool;
            this.button = button;
        }
    }
}
