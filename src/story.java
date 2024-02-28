
import java.io.FileWriter;
        import java.io.IOException;
        import java.io.PrintWriter;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Scanner;

public class story {
    private StoryNode root;
    private StoryNode cursor;
    private Scanner scanner;

    public story() {
        this.root = new StoryNode("Start of the story");
        this.cursor = root;
        this.scanner = new Scanner(System.in);
    }

    public void addBranch(String decision, String description) {
        StoryNode newNode = new StoryNode(description);
        cursor.addChild(decision, newNode);
    }

    public void deleteSubtree(StoryNode node) {
        node.clearChildren();
    }

    public void browseStoryEnds() {
        cursor = root;
        while (!cursor.getChildren().isEmpty()) {
            cursor = cursor.getChildren().get(0);
        }
    }

    public void exportToHTML() {
        try {
            FileWriter fileWriter = new FileWriter("story.html");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println("<html><body>");

            generateNodeHTML(root, printWriter, 0);

            printWriter.println("</body></html>");

            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateNodeHTML(StoryNode node, PrintWriter printWriter, int parentId) {
        int nodeId = parentId + 1;
        printWriter.println("<p id='" + nodeId + "'>" + node.getDescription() + "</p>");

        List<String> decisions = node.getDecisions();
        for (int i = 0; i < decisions.size(); i++) {
            printWriter.println("<a href='#" + (nodeId + i + 1) + "'>" + decisions.get(i) + "</a><br>");
        }

        List<StoryNode> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            printWriter.println("<hr>");
            generateNodeHTML(children.get(i), printWriter, nodeId + i);
        }
    }

    public StoryNode getRoot() {
        return root;
    }

    private static class StoryNode {
        private String description;
        private List<String> decisions;
        private List<StoryNode> children;

        public StoryNode(String description) {
            this.description = description;
            this.decisions = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        public void addChild(String decision, StoryNode child) {
            decisions.add(decision);
            children.add(child);
        }

        public void clearChildren() {
            children.clear();
        }

        public String getDescription() {
            return description;
        }

        public List<String> getDecisions() {
            return decisions;
        }

        public List<StoryNode> getChildren() {
            return children;
        }
    }

    public static void main(String[] args) {
        story editor = new story();

        // Test adding branches, editing, etc.
        editor.addBranch("Decision 1", "Description 1");
        editor.addBranch("Decision 2", "Description 2");
        editor.addBranch("Decision 3", "Description 3");

        // Test deleting subtree
        // editor.deleteSubtree(someNode);

        // Test browsing story ends
        // editor.browseStoryEnds();

        // Export to HTML
        editor.exportToHTML();
    }
}
