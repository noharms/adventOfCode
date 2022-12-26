package puzzles2022;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class Puzzle7_2 {


    public static final String ROOT_DIR = "/";
    public static final String PARENT_DIRECTORY = "..";
    public static final String READY_FOR_NEW_INPUT_CHARACTER = "$";
    public static final String PREFIX_CHILD_IS_DIRECTORY = "dir";
    public static final int REQUIRED_FREE_SPACE = 30_000_000;
    public static final int DISK_SPACE = 70_000_000;

    public static void main(String[] args) throws IOException {
        FileSystemNode root = readInput();

        List<FileNode> files = findFiles(root);
        List<DirectoryNode> directories = findDirectories(root);
        List<DirectoryNode> smaller100k = directories.stream().filter(dir -> dir.size() < 100_000).toList();
        System.out.println(smaller100k.stream().mapToInt(DirectoryNode::size).sum());

        int alreadyUnused = DISK_SPACE - root.size();
        int minRequiredToDelete = REQUIRED_FREE_SPACE - alreadyUnused;

        directories.sort(Comparator.comparingInt(DirectoryNode::size));
        int i = 0;
        while (directories.get(i).size() < minRequiredToDelete) {
            ++i;
        }
        System.out.println(directories.get(i).size());

    }

    private static List<DirectoryNode> findDirectories(FileSystemNode root) {
        LinkedList<DirectoryNode> directories = new LinkedList<>();
        findDirectories(root, directories);
        return directories;
    }

    private static void findDirectories(FileSystemNode node, LinkedList<DirectoryNode> directories) {
        if (node instanceof DirectoryNode directoryNode) {
            directories.addLast(directoryNode);
        }
        for (FileSystemNode child : node.children) {
            findDirectories(child, directories);
        }
    }

    private static List<FileNode> findFiles(FileSystemNode root) {
        LinkedList<FileNode> files = new LinkedList<>();
        findFiles(root, files);
        return files;
    }

    private static void findFiles(FileSystemNode node, LinkedList<FileNode> files) {
        if (node instanceof FileNode fileNode) {
            files.addLast(fileNode);
        }
        for (FileSystemNode child : node.children) {
            findFiles(child, files);
        }
    }

    private abstract static class FileSystemNode {

        final String name;
        final @Nullable DirectoryNode parent;
        final Set<FileSystemNode> children;

        protected FileSystemNode(String name, @Nullable DirectoryNode parent) {
            this.name = name;
            this.parent = parent;
            this.children = new HashSet<>();
        }

        abstract int size();

        @Override
        public String toString() {
            return name;
        }

    }

    private static class DirectoryNode extends FileSystemNode {

        private DirectoryNode(String name, DirectoryNode parent) {
            super(name, parent);
        }

        @Override
        int size() {
            return children.stream().mapToInt(FileSystemNode::size).sum();
        }

        void addChild(FileSystemNode child) {
            children.add(child);
        }

        Optional<FileSystemNode> getChild(String name) {
            return children.stream().filter(child -> child.name.equals(name)).findFirst();
        }


    }

    static class FileNode extends FileSystemNode {

        final int size;

        FileNode(String name, DirectoryNode parent, int size) {
            super(name, parent);
            this.size = size;
        }

        @Override
        int size() {
            return size;
        }

    }

    private enum Command {
        CD,
        LS;


    }

    private static FileSystemNode readInput() throws IOException {
        File inputFile = new File("resources\\2022\\input_puzzle7.txt");
        Path inputPath = Path.of(inputFile.getAbsolutePath());
        List<String> allLines = Files.readAllLines(inputPath);

        DirectoryNode root = new DirectoryNode(ROOT_DIR, null);
        int i = 0;
        DirectoryNode currentDirectory = root;
        while (i < allLines.size()) {
            String commandLine = allLines.get(i);
            String[] tokens = commandLine.split(" ");
            Command command = Command.valueOf(tokens[1].toUpperCase());

            if (command.equals(Command.CD)) {
                String targetDirectory = tokens[2];
                if (targetDirectory.equals(ROOT_DIR)) {
                    currentDirectory = root;
                } else if (targetDirectory.equals(PARENT_DIRECTORY)) {
                    currentDirectory = currentDirectory.parent;
                } else {
                    // assuming only relative paths are given in INPUT !
                    currentDirectory = (DirectoryNode) currentDirectory.children.stream().filter(child -> child.name.equals(targetDirectory)).findFirst().get();
                }
                ++i;
            } else if (command.equals(Command.LS)) {
                ++i;

                while (i < allLines.size() && !allLines.get(i).startsWith(READY_FOR_NEW_INPUT_CHARACTER)) {
                    String lsResultLine = allLines.get(i);
                    if (!lsResultLine.startsWith(PREFIX_CHILD_IS_DIRECTORY)) {
                        String[] splitted = lsResultLine.split(" ");
                        String fileName = splitted[1];
                        int fileSize = Integer.parseInt(splitted[0]);
                        FileNode newFileNode = new FileNode(fileName, currentDirectory, fileSize);
                        currentDirectory.addChild(newFileNode);
                    } else {
                        String[] splitted = lsResultLine.split(" ");
                        String directoryName = splitted[1];
                        DirectoryNode directoryNode = new DirectoryNode(directoryName, currentDirectory);
                        currentDirectory.addChild(directoryNode);
                    }
                    ++i;
                }

            }

        }

        return root;
    }

}
