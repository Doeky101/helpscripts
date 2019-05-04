package PolyDrawer;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

@Script.Manifest(name="PolyDrawer", description="Left click to add point, right click to remove point. Yellow is ghost polygon.",properties="author:Doeky; topic=999; client=4;")
public class GetCoords extends PollingScript<ClientContext> implements PaintListener,MouseListener,MouseMotionListener {
    Polygon polygon = new Polygon();
    Polygon ghost = new Polygon();
    ArrayList<Point> points =new ArrayList<Point>();
    ArrayList<Point> ghostPoints =new ArrayList<Point>();
    Point hoverLocation = new Point();

    public void poll() {
    }

    @Override
    public void stop() {
        System.out.println(getRectangleToString());
        System.out.println(getPolygonToString());
    }

    public void mouseClicked(MouseEvent e) {
        boolean left = SwingUtilities.isLeftMouseButton(e);
        //Left click adds the current mouse coordinate
        if(left) {
            Point toAdd = e.getPoint();
            points.add(toAdd);
            System.out.println(toAdd + " added");
        }
        //Right click removes the last mouse coordinate
        else {
            System.out.println(points.size());
            Point removed = points.remove(points.size()-1);
            System.out.println(removed + " removed");
            alterGhost();
        }
        alterPolygon();
    }

    public void mouseMoved(MouseEvent e) {
        hoverLocation = e.getPoint();
        alterGhost();
    }

    public void repaint(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;
        g.setStroke(new BasicStroke(3));
        g.setColor(Color.red);
        g.draw(polygon);

        g.setStroke(new BasicStroke(1));
        g.setColor(Color.yellow);
        g.draw(ghost);
    }

    public void alterPolygon() {
        polygon = new Polygon();
        for (Point p :
                points) {
            polygon.addPoint(p.x,p.y);
        }
    }
    public void alterGhost() {
        //double code... ohwell might clean it up later...
        ghostPoints = points;
        ghostPoints.add(hoverLocation);
        ghost = new Polygon();
        for (Point p :
                ghostPoints) {
            ghost.addPoint(p.x,p.y);
        }
        ghostPoints.remove(hoverLocation);
    }

    public String getPolygonToString() {
        //formatting for the copy paste polygon
        String xs = "";
        String ys = "";
        int nrPoints = points.size();

        for (int i = 0; i < points.size(); i++) {
            if (i != 0) {
                xs+=",";
                ys+=",";
            }
            xs+=points.get(i).x;
            ys+=points.get(i).y;
        }
        return String.format("Polygon polygon = new Polygon(new int[]{%s},new int[%s]),%d);",xs,ys,nrPoints);
    }

    public String getRectangleToString() {
        //formatting for the copy paste rectangle
        Rectangle rect = polygon.getBounds();
        return String.format("Rectangle rectangle = new Rectangle(%d,%d,%d,%d);",rect.x,rect.y,rect.width,rect.height);
    }

    public Point getRandomPointInRectangle(Rectangle r)
    {
        //how to get a random point within a rectangle
        Random random = new Random(); //should be declared static at fields of class
        int xPoint = random.nextInt(r.x,r.x+r.width);
        int yPoint = random.nextInt(r.y,r.y+r.height);
        return new Point(xPoint,yPoint);
    }

    public Point getRandomPointInPolygon(Polygon p)
    {
        Rectangle rect = p.getBounds();
        return getRandomPointInRectangle(rect);
    }

    public void click(Point p)
    {
        //how to click a point
        ctx.input.click(p,true);
    }


    public void mouseDragged(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {

    }

}
