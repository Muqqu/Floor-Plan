import BasicIO.ASCIIPrompter;
import Media.Turtle;
import Media.TurtleDisplayer;
import java.awt.Color;
import java.util.Random;

public class FloorPlanGenerator {
    private FloorPlan floorPlan;
    private Turtle turtle;
    private TurtleDisplayer displayer;
    private Random random = new Random();

    public FloorPlanGenerator() {
        ASCIIPrompter prompter = new ASCIIPrompter();
        System.out.println("Enter floor width:");
        int width = prompter.readInt();

        System.out.println("Enter floor height:");
        int height = prompter.readInt();

        System.out.println("Enter division cutoff:");
        int depth = prompter.readInt();


        int square_width = 350;
        int canvas_height = 600;
        int canvas_width = 1000;
                this.floorPlan = new FloorPlan(width, height,depth);
      //  this.displayer = new TurtleDisplayer();
        this.turtle = new Turtle();
        displayer = new TurtleDisplayer(turtle, canvas_width, canvas_height);
    }

    public static void main(String[] args) {
        new FloorPlanGenerator().generateFloorPlan();
    }

    public void generateFloorPlan() {
        floorPlan.generate();
        floorPlan.draw(turtle);
    }

    private class FloorPlan {
        int width, height,depth;


        Room initialRoom;

        public FloorPlan(int width, int height, int depth) {
            this.width = width;
            this.height = height;
            this.depth = depth;
            this.initialRoom = new Room(0, 0, width, height, null, false);
        }

        public void generate() {
            initialRoom.drawRecursiveRectangles(-500,300,width, height,depth);
        }

        public void draw(Turtle turtle) {
           // initialRoom.draw(turtle, width,height );
        }
    }

    private class Room {
        int x, y, width, height;
        Room leftChild, rightChild;
        Integer doorPosition;
        boolean colored;

        public Room(int x, int y, int width, int height, Integer doorPosition, boolean colored) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.doorPosition = doorPosition;
            this.colored = Math.random() < 0.5; // 50% chance to color the room
        }
        public void drawRecursiveRectangles( int x,int y,int w, int h, int depth) {
            drawSquare(x,y,w,h/2);
            drawSquare(x,0,w,h/2);
            if (depth <= 0) {
                drawSquare(x,y,w,h);
            } else {
                // Randomly decide to split vertically or horizontally
                boolean splitVertically = random.nextBoolean();
                if (splitVertically) {
                    // Vertical split
                    int bnd = Math.abs((w - 20) + 10);
                    int splitX =Math.abs(x + random.nextInt(bnd)) ; // Ensure a minimum width

                    //drawRecursiveRectangles(x, y, splitX, h, depth - 1);
                    drawRecursiveRectangles(x + w, y, w, h, depth - 1);
                } else {
                    // Horizontal split
                    int bnd = Math.abs((h - 20) + 10);
                    int splitY = Math.abs(y + random.nextInt(bnd)); // Ensure a minimum height
                   // drawRecursiveRectangles(x, y, w, splitY, depth - 1);
                    drawRecursiveRectangles(x, y + h, w, h, depth - 1);
                }
            }
        }
        public void divide(int cutoff, double quitProbability) {
            System.out.println(Math.random() );
            if (width <= cutoff || height <= cutoff || Math.random() < quitProbability) {
                System.out.println("return");
                return;
            }

            if (width > height) {
                int bound = Math.abs(width - 2 * cutoff);
                int splitX = random.nextInt(bound) + cutoff;
                System.out.println("Split x"+ splitX);
                leftChild = new Room(x, y, splitX, height, null, false);
                rightChild = new Room(x + splitX, y, width - splitX, height, random.nextInt(height), false);
            } else {
                int bound = Math.abs(height - 2 * cutoff);
                int splitY = random.nextInt(bound) + cutoff;
                System.out.println("Split x"+ splitY);
                leftChild = new Room(x, y, width, splitY, null, false);
                rightChild = new Room(x, y + splitY, width, height - splitY, random.nextInt(width), false);
            }

            leftChild.divide(cutoff, quitProbability);
            rightChild.divide(cutoff, quitProbability);
        }

        public void draw(Turtle turtle, int offsetX, int offsetY) {

            drawSquare(-500,300,offsetX,offsetY);
            // Draw the door if present
            if (doorPosition != null) {
                if (leftChild == null) { // Vertical door
                    turtle.penUp();
                    turtle.moveTo(x + offsetX + doorPosition, y + offsetY);
                    turtle.penDown();
                    turtle.setPenColor(Color.ORANGE);
                    turtle.forward(10); // Simple door representation

                } else { // Horizontal door
                    turtle.penUp();
                    turtle.moveTo(x + offsetX, y + offsetY + doorPosition);
                    turtle.penDown();
                    turtle.setPenColor(Color.ORANGE);
                    turtle.forward(10); // Simple door representation

                }
            }

            // Recursively draw child rooms
            if (leftChild != null) leftChild.draw(turtle, offsetX, offsetY);
            if (rightChild != null) rightChild.draw(turtle, offsetX, offsetY);
        }
        private double rotation(int degrees) {
            return degrees * Math.PI / 180;
        }
        private void drawSquare(int x , int y,int size,int height) {
//            turtle.backward(size / 2);
//            turtle.left(rotation(90));
//            turtle.forward(size / 2);
//            turtle.right(rotation(90));
            System.out.println();
            turtle.penUp();
            turtle.moveTo(x, y);
            turtle.penDown();

            for (int i = 0; i < 2; i++) {
                turtle.forward(size);
                turtle.right(rotation(90));
                turtle.forward(size);
                turtle.right(rotation(90));
            // addRandomSpacing(); // Add random spacing between lines
            }

//            turtle.penUp();
//            turtle.moveTo(0, 0);
//            turtle.right(0 - turtle.getAngle());
        }

        private void addRandomSpacing() {
            int spacing = random.nextInt(20); // Random spacing between 0 and 20
            turtle.forward(spacing);
        }

    }
}
